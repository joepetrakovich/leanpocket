package com.appshroom.leanpocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.MyApplication;
import com.appshroom.leanpocket.adapters.MoveToLaneAdapter;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitCallback;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LaneDescription;
import com.appshroom.leanpocket.helpers.Consts;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;


/**
 * Created by jpetrakovich on 8/23/13.
 */
public class MoveCardDialog extends DialogFragment {


    public interface OnMoveCardDialogChoiceListener {

        public void onMoveCardDialogMoveSuccess(String message);

        public void onMoveCardDialogLeanKitException(int replyCode, String replyText);

        public void onMoveCardDialogRetrofitError(RetrofitError retrofitError);

        public void onMoveCardDialogCancel();
    }

    OnMoveCardDialogChoiceListener mChoiceListener;
    RetroLeanKitApi mRetroLeanKitApi;
    LaneDescription mSelectedLane;
    int mPosition;
    ProgressDialog pd;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mChoiceListener = (OnMoveCardDialogChoiceListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnMoveCardDialogChoiceListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        List<LaneDescription> lanes = getArguments().getParcelableArrayList(Consts.MOVE_CARD_DIALOG_ARGS_LANES);


        View customStickyView = ((Activity) mChoiceListener).getLayoutInflater().inflate(R.layout.move_to_dialog_sticky_list, null);

        StickyListHeadersListView lv = (StickyListHeadersListView) customStickyView.findViewById(R.id.sticky_move_to_list);
        lv.setAdapter(new MoveToLaneAdapter((Activity) mChoiceListener, lanes));
        lv.setAreHeadersSticky(false);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSelectedLane = (LaneDescription) parent.getItemAtPosition(position);
                moveCard();

            }
        });

        String position = PreferenceManager.getDefaultSharedPreferences((Activity) mChoiceListener)
                .getString(Consts.SHARED_PREFS_CARD_POSITION, getString(R.string.top));

        if (position.equals(getString(R.string.top))) {

            mPosition = 0;
        } else {
            mPosition = 999999999;
        }

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) mChoiceListener);


        builder.setTitle(R.string.dialog_move_to_title)

                .setView(customStickyView)

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mChoiceListener.onMoveCardDialogCancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();

        dismissProgressDialog();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication mApp = (MyApplication) ((Activity) mChoiceListener).getApplication();

        mRetroLeanKitApi = mApp.getRetroLeanKitApiInstance();
    }


    public static MoveCardDialog newInstance(String boardId, Card card, ArrayList<LaneDescription> lanes) {

        MoveCardDialog frag = new MoveCardDialog();
        Bundle args = new Bundle();
        args.putString(Consts.MOVE_CARD_DIALOG_ARGS_BOARD_ID, boardId);
        args.putParcelable(Consts.MOVE_CARD_DIALOG_ARGS_CARD, card);
        args.putParcelableArrayList(Consts.MOVE_CARD_DIALOG_ARGS_LANES, lanes);
        frag.setArguments(args);

        frag.setRetainInstance(true);

        return frag;
    }

    public MoveCardDialog() {
    }

    private void moveCard() {

        showProgressDialog();

        String boardId = getArguments().getString(Consts.MOVE_CARD_DIALOG_ARGS_BOARD_ID);
        Card cardToMove = getArguments().getParcelable(Consts.MOVE_CARD_DIALOG_ARGS_CARD);

        mRetroLeanKitApi.moveCard(boardId, cardToMove.getId(), mSelectedLane.getId(), mPosition, new RetroLeanKitCallback<JsonObject>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<JsonObject> replyData) {

                mChoiceListener.onMoveCardDialogMoveSuccess(((Activity) mChoiceListener).getString(R.string.move_card_success));
                dismiss();

            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<JsonObject> replyData) {

                mChoiceListener.onMoveCardDialogLeanKitException(replyCode, replyText);
                dismiss();

            }

            @Override
            public void onWIPOverrideCommentRequired() {

                mChoiceListener.onMoveCardDialogLeanKitException(99, ((Activity) mChoiceListener).getString(R.string.wip_not_supported));
                dismiss();

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mChoiceListener.onMoveCardDialogRetrofitError(retrofitError);
                dismiss();

            }
        });

    }

    private void showProgressDialog() {

        pd = new ProgressDialog((Activity) mChoiceListener);

        pd.setMessage(getString(R.string.moving_card));
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.show();

    }

    private void dismissProgressDialog() {

        if (pd != null) {

            pd.dismiss();
        }
    }


}
