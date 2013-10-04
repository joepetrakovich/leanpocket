package com.appshroom.leanpocket.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.MyApplication;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitCallback;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardHistory;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;

/**
 * Created by jpetrakovich on 8/27/13.
 */
public class CardHistoryFragment extends Fragment {

    Card mCard;
    String mBoardId;

    ListView mHistoryList;
    ArrayAdapter<CardHistory> mHistoryAdapter;
    List<CardHistory> mCardHistory;

    RetroLeanKitApi mRetroLeanKitApi;

    public static CardHistoryFragment newInstance(Card card, String boardId) {

        CardHistoryFragment f = new CardHistoryFragment();

        Bundle args = new Bundle();
        args.putParcelable("card", card);
        args.putString("boardId", boardId);
        f.setArguments(args);

        return f;
    }

    public CardHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_history, container, false);

        mHistoryList = (ListView) v.findViewById(R.id.card_history_list);

        if (mCardHistory == null) {

            mCardHistory = new ArrayList<CardHistory>();
            mHistoryAdapter = new ArrayAdapter<CardHistory>(getActivity(), android.R.layout.simple_list_item_1, mCardHistory);
            mHistoryList.setAdapter(mHistoryAdapter);

            refreshCardHistoryList();
        } else {

            mHistoryAdapter = new ArrayAdapter<CardHistory>(getActivity(), android.R.layout.simple_list_item_1, mCardHistory);
            mHistoryList.setAdapter(mHistoryAdapter);

        }

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mCard = getArguments().getParcelable("card");
        mBoardId = getArguments().getString("boardId");

        mRetroLeanKitApi = ((MyApplication) getActivity().getApplication()).getRetroLeanKitApiInstance();


    }

    private void refreshCardHistoryList() {

        mRetroLeanKitApi.getCardHistory(mBoardId, mCard.getId(), new RetroLeanKitCallback<List<CardHistory>>() {

            @Override
            public void onSuccess(int replyCode, String replyText, List<List<CardHistory>> replyData) {

                mCardHistory = replyData.get(0);

                mHistoryAdapter.clear();
                mHistoryAdapter.addAll(mCardHistory);
                mHistoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<List<CardHistory>> replyData) {
                Crouton.makeText(getActivity(), replyText, Style.ALERT).show();
            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Crouton.makeText(getActivity(), retrofitError.getMessage(), Style.ALERT).show();
            }
        });

    }
}
