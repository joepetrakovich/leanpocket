package com.appshroom.leanpocket.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.helpers.Consts;


/**
 * Created by jpetrakovich on 9/23/13.
 */
public class InfoDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View parent = inflater.inflate(R.layout.dialog_info, null);

        LinearLayout layout = (LinearLayout) parent.findViewById(R.id.dialog_info_linear_layout);

        String dlgTitle = getArguments().getString(Consts.DIALOG_TITLE_ARG);
        String[] titles = getArguments().getStringArray(Consts.TITLE_ARGS);
        String[] contents = getArguments().getStringArray(Consts.CONTENT_ARGS);

        for (int i = 0; i < titles.length; i++) {

            View itemView = inflater.inflate(R.layout.dialog_info_item, null);

            TextView title = (TextView) itemView.findViewById(R.id.dialog_info_item_title);
            TextView content = (TextView) itemView.findViewById(R.id.dialog_info_item_content);

            title.setText(titles[i]);

            content.setText(contents[i]);

            layout.addView(itemView);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(dlgTitle)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(parent);

        return builder.create();

    }


    public static InfoDialog newInstance(String dialogTitle, String[] titles, String[] content) {

        InfoDialog frag = new InfoDialog();
        Bundle args = new Bundle();
        args.putStringArray(Consts.TITLE_ARGS, titles);
        args.putStringArray(Consts.CONTENT_ARGS, content);
        args.putString(Consts.DIALOG_TITLE_ARG, dialogTitle);

        frag.setArguments(args);

        return frag;
    }

    public InfoDialog() {
    }
}
