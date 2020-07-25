package com.appshroom.leanpocket.fragments;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.CardDetailActivity;
import com.appshroom.leanpocket.dto.AssignedUser;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by jpetrakovich on 8/27/13.
 */
public class DetailsFragment extends Fragment {

    Card mCard;
    BoardSettings mBoardSettings;

    public static DetailsFragment newInstance(Card card, BoardSettings settings) {

        DetailsFragment f = new DetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable("card", card);
        args.putParcelable("settings", settings);
        f.setArguments(args);

        return f;
    }

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_details, container, false);

        getActivity().getActionBar().setDisplayUseLogoEnabled(false);

        String title = "";

        if (mBoardSettings.isCardIdPrefixEnabled()){

            title = mBoardSettings.getCardIdPrefix();
        }

        getActivity().getActionBar().setTitle( title + mCard.getExternalCardID() );

        createTitle(v);

        createDescription(v);

        createBlocked(v);

        createPriority(v);

        createSize(v);

        createCardType(v);

        createClassOfService(v);

        createTags(v);

        createAssignedUsers(v);

        return v;
    }

    private void createAssignedUsers(View v) {

        LinearLayout assignedUsersLayout = (LinearLayout) v.findViewById(R.id.layout_assigned_users);

        if (mCard.getAssignedUsers().size() == 0) {

            assignedUsersLayout.setVisibility(View.GONE);
        } else {

            for (AssignedUser user : mCard.getAssignedUsers()) {

                View userLayout = getActivity().getLayoutInflater().inflate(R.layout.layout_assigned_user, null);

                ImageView gravView = (ImageView) userLayout.findViewById(R.id.iv_assigned_user_gravatar);
                TextView userName = (TextView) userLayout.findViewById(R.id.tv_assigned_user_name);

                userName.setText(user.getFullName());

                String gravUrl = GravatarHelpers.buildGravatarUrlAfterRemovingOldSize(user.getGravatarLink(),
                        getResources().getInteger(R.integer.gravatar_assigned_user_list_size));

                Picasso.with(getActivity())
                        .load(gravUrl)
                        .placeholder(R.drawable.mysterymans)
                        .into(gravView);

                assignedUsersLayout.addView(userLayout);

            }
        }
    }

    private void createCardType(View v) {
        TextView type = (TextView) v.findViewById(R.id.tv_card_detail_card_type);
        type.setText(mCard.getTypeName());
    }

    private void createClassOfService(View v) {

        View cosLayout = v.findViewById(R.id.layout_class_of_service);

        if (TextUtils.isEmpty(mCard.getClassOfServiceTitle())) {

            cosLayout.setVisibility(View.GONE);

        } else {

            TextView tvCOS = (TextView) cosLayout.findViewById(R.id.tv_card_detail_class_of_service);
            tvCOS.setText(mCard.getClassOfServiceTitle());
        }
    }

    private void createTags(View v) {
        View tagsLayout = v.findViewById(R.id.layout_tags);

        if (mCard.getTags() == null || mCard.getTags().size() == 0) {

            tagsLayout.setVisibility(View.GONE);
        } else {

            TextView tvTags = (TextView) tagsLayout.findViewById(R.id.tv_card_detail_tags);

            String tags = StringUtils.join(mCard.getTags(), ", ");

            tvTags.setText(tags); //TODO: eventually put these in chips
        }
    }

    private void createBlocked(View v) {

        View blockedLayout = v.findViewById(R.id.layout_detail_blocked);

        if (mCard.isBlocked()) {

            TextView blockReason = (TextView) blockedLayout.findViewById(R.id.tv_card_detail_blocked_reason);
            blockReason.setText(mCard.getBlockReason());

        } else {

            blockedLayout.setVisibility(View.GONE);
        }

    }

    private void createSize(View v) {

        View sizeSection = v.findViewById(R.id.detail_layout_size_section);

        if (mCard.getSize() < 1) {

            sizeSection.setVisibility(View.GONE);
        } else {

            TextView size = (TextView) sizeSection.findViewById(R.id.tv_card_detail_size);
            size.setText(Integer.toString(mCard.getSize()));
        }
    }

    private void createPriority(View v) {

        TextView priority = (TextView) v.findViewById(R.id.tv_card_detail_priority);

        String priorityText;
        int priorityTextColor;

        switch (mCard.getPriority()) {

            case Consts.PRIORITY.LOW:
                priorityText = getString(R.string.low_priority);
                priorityTextColor = getResources().getColor(android.R.color.holo_blue_dark);
                break;

            case Consts.PRIORITY.NORMAL:
                priorityText = getString(R.string.normal_priority);
                priorityTextColor = getResources().getColor(android.R.color.black);
                break;

            case Consts.PRIORITY.HIGH:
                priorityText = getString(R.string.high_priority);
                priorityTextColor = getResources().getColor(android.R.color.holo_red_dark);
                break;

            case Consts.PRIORITY.CRITICAL:
                priorityText = getString(R.string.critical_priority);
                priorityTextColor = getResources().getColor(android.R.color.holo_purple);
                break;

            default:
                priorityText = mCard.getPriority();
                priorityTextColor = getResources().getColor(android.R.color.black);

        }

        priority.setText(priorityText);
        priority.setTextColor(priorityTextColor);

    }

    private void createDescription(View v) {

        TextView descHeader = (TextView) v.findViewById(R.id.detail_desc_section_header);
        TextView desc = (TextView) v.findViewById(R.id.tv_card_detail_desc);

        if (TextUtils.isEmpty(mCard.getDescription())) {

            descHeader.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        } else {

            desc.setText(Html.fromHtml(mCard.getDescription().trim()));

        }

    }

    private void createTitle(View v) {

        View titleBackground = v.findViewById(R.id.layout_title_wrapper);
        titleBackground.setBackgroundColor(Color.parseColor(mCard.getColor()));

        TextView title = (TextView) v.findViewById(R.id.tv_card_detail_title);
        title.setText(mCard.getTitle());

        TextView dueDate = (TextView) v.findViewById(R.id.tv_card_detail_due_date);

        if (TextUtils.isEmpty(mCard.getDueDate())) {
            dueDate.setVisibility(View.GONE);
        } else {

            dueDate.setText(getString(R.string.due) + " " + mCard.getDueDate());
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        mCard = getArguments().getParcelable("card");
        mBoardSettings = getArguments().getParcelable("settings");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit:
                ((CardDetailActivity) getActivity()).startEditActivity();
                return true;

            case R.id.action_delete:
                ((CardDetailActivity) getActivity()).showDeleteCardsDialog(Arrays.asList(mCard));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detail_frag_menu, menu);
    }
}
