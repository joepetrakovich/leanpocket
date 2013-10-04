package com.appshroom.leanpocket.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.Card;

import java.util.List;

/**
 * Created by jpetrakovich on 8/23/13.
 */
public class ConfirmDeleteCardDialog extends DialogFragment {

    public interface OnDeleteCardDialogChoiceListener {

        public void onDeleteDialogConfirm();

        public void onDeleteDialogCancel();
    }

    OnDeleteCardDialogChoiceListener mChoiceListener;
    int mNumCardsToDelete;
    List<Card> mCardsToDelete;
    String mBoardId;
    boolean mFromDetailView;


    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mChoiceListener = (OnDeleteCardDialogChoiceListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnDeleteCardDialogChoiceListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String message;

        if (mFromDetailView) {

            message = getString(R.string.you_will_not_be_able_to_recover);

        } else {

            message = getResources().getString(R.string.card_will_be_deleted);

            if (mNumCardsToDelete > 1) {
                message = mNumCardsToDelete + " " + getResources().getString(R.string.cards_will_be_deleted);
            }

        }

        builder.setTitle(R.string.dialog_delete_title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoiceListener.onDeleteDialogConfirm();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mChoiceListener.onDeleteDialogCancel();
                    }
                });

        return builder.create();

    }

    public ConfirmDeleteCardDialog(List<Card> cardsToDelete, String boardId, boolean fromDetailView) {

        setRetainInstance(true);

        mNumCardsToDelete = cardsToDelete.size();
        mCardsToDelete = cardsToDelete;
        mFromDetailView = fromDetailView;

        mBoardId = boardId;

    }


    public ConfirmDeleteCardDialog() {
    }

}
