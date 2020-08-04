package com.appshroom.leanpocket.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.MyApplication;
import com.appshroom.leanpocket.adapters.CardCommentsAdapter;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2Callback;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitCallback;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.v2.ListCommentsResponse;
import com.appshroom.leanpocket.dto.v2.SaveCommentRequest;
import com.appshroom.leanpocket.dto.v2.Comment;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.SecurePreferences;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;

/**
 * Created by jpetrakovich on 8/27/13.
 */
public class CommentsFragment extends Fragment {

    Card mCard;
    String mBoardId;

    List<Comment> mComments;
    ListView mCommentsList;
    ArrayAdapter<Comment> mCommentsAdapter;
    View veil;
    ImageButton sendButton;
    RetroLeanKitApi mRetroLeanKitApi;
    RetroLeanKitApiV2 mRetroLeanKitApiV2;
    ProgressDialog pd;
    View mCommentsListWrapper;
    TextView mEmptyCommentsText;
    String commentsText;
    MenuItem mRefreshMenuItem;
    boolean mRefreshing;
    SharedPreferences mSharedPreferences;

    EditText mCommentsTextInput;
    private Activity mHostActivity;
    FragmentReceiver mReceiver;


    public static CommentsFragment newInstance(Card card, String boardId) {

        CommentsFragment f = new CommentsFragment();

        Bundle args = new Bundle();
        args.putParcelable("card", card);
        args.putString("boardId", boardId);
        f.setArguments(args);

        return f;
    }

    public CommentsFragment() {
    }

    public class FragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_comments, container, false);

        mCommentsListWrapper = v.findViewById(R.id.layout_comments_list_wrapper);

        View emptyView = mCommentsListWrapper.findViewById(android.R.id.empty);
        mEmptyCommentsText = (TextView) emptyView.findViewById(R.id.tv_empty_comments);
        mEmptyCommentsText.setText(commentsText);

        mCommentsList = (ListView) mCommentsListWrapper.findViewById(R.id.card_comments_list);
        mCommentsList.setEmptyView(emptyView);

        if (mComments == null) {

            mComments = new ArrayList<Comment>();
            mCommentsAdapter = new CardCommentsAdapter(getActivity(), mComments);
            mCommentsList.setAdapter(mCommentsAdapter);

            refreshCommentList();
        } else {

            mCommentsAdapter = new CardCommentsAdapter(getActivity(), mComments);
            mCommentsList.setAdapter(mCommentsAdapter);
        }

        mCommentsTextInput = (EditText) v.findViewById(R.id.et_comment);


        sendButton = (ImageButton) v.findViewById(R.id.btn_comment_send);
        sendButton.setOnClickListener(new OnSendButtonClickListener());

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(true);
        pd.setIndeterminate(true);

        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mHostActivity = activity;

        mReceiver = new FragmentReceiver();

        mHostActivity.registerReceiver(mReceiver, new IntentFilter(Consts.INTENT_FILTER_PREM_PURCHASE));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mHostActivity.unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        mCard = getArguments().getParcelable("card");
        mBoardId = getArguments().getString("boardId");
        mSharedPreferences = new SecurePreferences(getActivity());

        mRetroLeanKitApi = ((MyApplication) getActivity().getApplication()).getRetroLeanKitApiInstance();
        mRetroLeanKitApiV2 = ((MyApplication) getActivity().getApplication()).getRetroLeanKitApiV2Instance();
    }


    private void refreshCommentList() {

        showProgress();

        mRetroLeanKitApiV2.listComments(mCard.getId(), new RetroLeanKitApiV2Callback<ListCommentsResponse>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<ListCommentsResponse> replyData) {
                commentsText = mHostActivity.getString(R.string.no_comments_yet);
                mEmptyCommentsText.setText(commentsText);

                mComments = replyData.get(0).comments;
                mCommentsAdapter.clear();
                mCommentsAdapter.addAll(mComments);
                mCommentsAdapter.notifyDataSetChanged();

                hideProgress();
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<ListCommentsResponse> replyData) {
                hideProgress();

                commentsText = mHostActivity.getString(R.string.comments_failed_to_load);
                mEmptyCommentsText.setText(commentsText);
                Crouton.makeText(getActivity(), replyText, Style.ALERT, R.id.layout_comments_fragment).show(); //TODO: risky showing reply text here
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
                commentsText = mHostActivity.getString(R.string.comments_failed_to_load);
                mEmptyCommentsText.setText(commentsText);
                handleRetrofitError(error, mHostActivity.getString(R.string.no_network_signal_comments));
            }
        });
    }

    private void showProgress() {

        mRefreshing = true;
        //If the fragment isn't shown, the options menu isn't created yet.
        if (mRefreshMenuItem != null) {

            mRefreshMenuItem.setActionView(R.layout.refresh_spinner);
        }
    }

    private void hideProgress() {

        mRefreshing = false;

        if (mRefreshMenuItem != null) {
            mRefreshMenuItem.setActionView(null);
        }
    }

    private void showProgressDialog(String message) {

        if (pd != null) {
            pd.setMessage(message);
            pd.show();
        }

    }

    private void dismissProgressDialog() {

        if (pd != null) {

            if (pd.isShowing()) {

                pd.dismiss();
            }
        }
    }

    private void dismissSoftKeybeard() {

        InputMethodManager imm = (InputMethodManager) mHostActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCommentsTextInput.getWindowToken(), 0);
    }

    private class OnSendButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String comment = mCommentsTextInput.getText().toString();

            if (TextUtils.isEmpty(comment.trim()) == false) {

                showProgressDialog(getString(R.string.sending_comment));

                SaveCommentRequest request = new SaveCommentRequest();
                request.text = Html.toHtml(mCommentsTextInput.getText());

                mRetroLeanKitApiV2.saveComment(mCard.getId(), request, new RetroLeanKitApiV2Callback<Comment>() {
                    @Override
                    public void onSuccess(int replyCode, String replyText, List<Comment> replyData) {
                        dismissProgressDialog();
                        dismissSoftKeybeard();
                        mCommentsTextInput.setText("");
                        refreshCommentList();
                    }

                    @Override
                    public void onLeanKitException(int replyCode, String replyText, List<Comment> replyData) {
                        dismissProgressDialog();
                        dismissSoftKeybeard();
                        Crouton.makeText(getActivity(), replyText, Style.ALERT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissProgressDialog();
                        dismissSoftKeybeard();
                        handleRetrofitError(error, mHostActivity.getString(R.string.no_network_signal_save_comment));
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {

        dismissProgressDialog();

        super.onDestroy();

    }

    private void handleRetrofitError(RetrofitError retrofitError, String msg) {

        if (retrofitError.getResponse() == null) {

            Crouton.makeText(mHostActivity, msg, Style.ALERT).show();


        } else {

            switch (retrofitError.getResponse().getStatus()) {

                case HttpStatus.SC_UNAUTHORIZED:

                    Crouton.makeText(getActivity(), mHostActivity.getString(R.string.an_error_occurred_try_again), Style.ALERT).show();

                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.comments_menu, menu);

        mRefreshMenuItem = menu.findItem(R.id.action_refresh);

        if (mRefreshing) {
            showProgress();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh:
                refreshCommentList();
                return true;
        }

        return false;

    }
}
