package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.AssignedUser;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.helpers.Consts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jpetrakovich on 8/10/13.
 */
public class CardListAdapter extends ArrayAdapter<Card> {

    private int resource;
    private ViewHolder holder;
    private Context context;

    private HashMap<Integer, Integer> mAccentColorMap;
    private HashMap<String, Integer> mColorMap;
    private HashMap<String, String> mGravatarUrlMap;


    private BoardSettings mBoardSettings;

    public CardListAdapter(Context context, int resource, List<Card> cards) {

        super(context, resource, cards);

        this.resource = resource;
        this.context = context;

        mAccentColorMap = new HashMap<Integer, Integer>();
        mColorMap = new HashMap<String, Integer>();
        mGravatarUrlMap = new HashMap<String, String>();

    }

    public void setBoardSettings(BoardSettings settings){
        mBoardSettings = settings;
    }

    public void setColorMap(HashMap<String, Integer> map) {
        mColorMap = map;
    }

    public void setAccentColorMap(HashMap<Integer, Integer> map) {
        mAccentColorMap = map;
    }

    public void setGravatarUrlMap(HashMap<String, String> map) {
        mGravatarUrlMap = map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View cardView = convertView;

        if (cardView == null || cardView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cardView = inflater.inflate(resource, parent, false);

            holder = new ViewHolder(cardView);
            cardView.setTag(holder);

        } else {

            holder = (ViewHolder) cardView.getTag();
        }

        Card card = getItem(position);

        String colorHex = card.getColor();

        Integer color = getColor(colorHex);
        Integer accentColor;

        if (color == null) {

            //card type or class of service not set so the color sent from LeanKi won't be in the cache, let's add it.

            color = Color.parseColor(colorHex);

            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = hsv[2] * 0.5f;

            accentColor = Color.HSVToColor(hsv);

            putColor(colorHex, color);
            putAccentColor(color, accentColor);

        } else {

            accentColor = getAccentColor(color);
        }

        if (!mBoardSettings.isCardHeaderEnabled()
                || TextUtils.isEmpty(card.getExternalCardID())) {

            holder.cardIdFrame.setVisibility(View.GONE);

        } else {

            holder.cardIdFrame.setVisibility(View.VISIBLE);
            holder.cardIdFrameShape.setColor(accentColor);

            if (mBoardSettings.isCardIdPrefixEnabled()){

                holder.externalCardId.setText( mBoardSettings.getCardIdPrefix() + card.getExternalCardID() );

            } else {

                holder.externalCardId.setText(card.getExternalCardID());
            }
        }

        holder.cardShape.setColor(color);
        holder.cardTitle.setText(card.getTitle());

        int cardSize = card.getSize();

        if (cardSize > 0) {

            holder.cardSize.setVisibility(View.VISIBLE);
            holder.cardSizeBox.setColor(accentColor);
            holder.cardSize.setText(Integer.toString(cardSize));
        } else {

            holder.cardSize.setVisibility(View.GONE);

        }

        configureGravatars(card, holder);

        configureIcons(card, holder);

        return cardView;
    }



    private Integer getAccentColor(Integer color) {

        return mAccentColorMap.get(color);

    }

    private Integer getColor(String colorHex) {

        return mColorMap.get(colorHex);
    }

    private void putColor(String colorHex, Integer color) {

        mColorMap.put(colorHex, color);
    }

    private void putAccentColor(Integer color, Integer accentColor) {

        mAccentColorMap.put(color, accentColor);
    }

    private void configureIcons(Card card, ViewHolder holder) {

        String priority = card.getPriority();

        //No priority icon for normal priority

        if (!priority.equals(Consts.PRIORITY.NORMAL)) {

            holder.bottomIcon.setVisibility(View.VISIBLE);

            switch (priority) {

                case Consts.PRIORITY.LOW:

                    holder.bottomIcon.setImageDrawable(holder.priorityLowDrawable);
                    break;

                case Consts.PRIORITY.HIGH:

                    holder.bottomIcon.setImageDrawable(holder.priorityHighDrawable);
                    break;

                case Consts.PRIORITY.CRITICAL:

                    holder.bottomIcon.setImageDrawable(holder.priorityCriticalDrawable);
                    break;

                default:

                    holder.bottomIcon.setVisibility(View.INVISIBLE);
                    break;
            }

            if (card.isBlocked()) {

                holder.bottomIcon2.setVisibility(View.VISIBLE);
                holder.bottomIcon2.setImageDrawable(holder.blockedDrawable);
            } else {

                holder.bottomIcon2.setVisibility(View.GONE);
            }

        } else if (card.isBlocked()) {

            holder.bottomIcon.setVisibility(View.VISIBLE);
            holder.bottomIcon.setImageDrawable(holder.blockedDrawable);
        } else {

            holder.bottomIcon.setVisibility(View.GONE);
        }

    }


    private void configureGravatars(Card card, ViewHolder holder) {

        List<AssignedUser> assignedUsers = card.getAssignedUsers();
        int numAssignedUsers = assignedUsers.size();

        if (numAssignedUsers == 0) {
            holder.gravatar.setVisibility(View.GONE);
            holder.gravatar2.setVisibility(View.GONE);
        } else if (numAssignedUsers == 1) {

            holder.gravatar.setVisibility(View.VISIBLE);
            holder.gravatar2.setVisibility(View.GONE);

            String gravUrl = mGravatarUrlMap.get(assignedUsers.get(0).getId());

            Picasso.with(context)
                    .load(gravUrl)
                    .placeholder(holder.mysteryManPlaceholder)
                    .into(holder.gravatar);


        } else {

            holder.gravatar.setVisibility(View.VISIBLE);
            holder.gravatar2.setVisibility(View.VISIBLE);

            String gravUrl = mGravatarUrlMap.get(assignedUsers.get(0).getId());
            String gravUrl2 = mGravatarUrlMap.get(assignedUsers.get(1).getId());

            Picasso.with(context)
                    .load(gravUrl)
                    .placeholder(holder.mysteryManPlaceholder)
                    .into(holder.gravatar);

            Picasso.with(context)
                    .load(gravUrl2)
                    .placeholder(holder.mysteryManPlaceholder)
                    .into(holder.gravatar2);

        }
    }


    private class ViewHolder {
        FrameLayout cardIdFrame;
        GradientDrawable cardIdFrameShape;
        GradientDrawable cardShape;
        GradientDrawable cardSizeBox;
        TextView externalCardId;
        TextView cardTitle;
        TextView cardSize;
        ImageView gravatar;
        ImageView gravatar2;
        ImageView bottomIcon;
        ImageView bottomIcon2;
        Drawable mysteryManPlaceholder;
        Drawable blockedDrawable;
        Drawable priorityLowDrawable;
        Drawable priorityHighDrawable;
        Drawable priorityCriticalDrawable;
        StateListDrawable mStateList;

        int gravatarSize;

        public ViewHolder(View v) {

            mStateList = (StateListDrawable) v.findViewById(R.id.card_frame).getBackground();

            cardShape = (GradientDrawable) ((LayerDrawable) mStateList.getCurrent()).getDrawable(1);
            cardSizeBox = (GradientDrawable) v.findViewById(R.id.tv_card_size).getBackground();
            cardIdFrame = (FrameLayout) v.findViewById(R.id.card_id_frame);
            cardIdFrameShape = (GradientDrawable) cardIdFrame.getBackground();
            externalCardId = (TextView) v.findViewById(R.id.tv_ext_card_id);
            cardTitle = (TextView) v.findViewById(R.id.tv_card_title);
            cardSize = (TextView) v.findViewById(R.id.tv_card_size);
            gravatar = (ImageView) v.findViewById(R.id.assigned_user_gravatar);
            gravatar2 = (ImageView) v.findViewById(R.id.assigned_user_gravatar2);
            gravatarSize = context.getResources().getInteger(R.integer.gravatar_grid_size);
            mysteryManPlaceholder = context.getResources().getDrawable(R.drawable.mysterymans);
            bottomIcon = (ImageView) v.findViewById(R.id.bottom_right_icon);
            bottomIcon2 = (ImageView) v.findViewById(R.id.bottom_right_icon2);


            blockedDrawable = context.getResources().getDrawable(R.drawable.ic_blocked);
            priorityLowDrawable = context.getResources().getDrawable(R.drawable.ic_priority_low);
            priorityHighDrawable = context.getResources().getDrawable(R.drawable.ic_priority_high);
            priorityCriticalDrawable = context.getResources().getDrawable(R.drawable.ic_priority_critical);


            v.setTag(this);

        }


    }



}
