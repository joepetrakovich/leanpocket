package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.v2.Comment;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jpetrakovich on 9/8/13.
 */
public class CardCommentsAdapter extends ArrayAdapter<Comment> {


    private ViewHolder holder;

    public CardCommentsAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.comment_list_item, comments);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null || itemView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.comment_list_item, parent, false);

            holder = new ViewHolder(itemView);

        } else {

            holder = (ViewHolder) itemView.getTag();
        }


        Comment comment = getItem(position);

        holder.userName.setText(comment.createdBy.fullName);
        holder.commentDateTime.setText(comment.createdOn.toString());
        holder.commentText.setText(Html.fromHtml(comment.text.trim()));

        String avatarUrlWithoutSize = comment.createdBy.avatar.split("\\?")[0];
        String avatarUrl = avatarUrlWithoutSize + "?s=" + getContext().getResources().getInteger(R.integer.gravatar_assigned_user_list_size);

        Picasso.with(getContext())
                .load(avatarUrl)
                .placeholder(R.drawable.mysterymans)
                .into(holder.gravatar);

        return itemView;
    }

    private class ViewHolder {

        TextView userName;
        TextView commentDateTime;
        TextView commentText;

        ImageView gravatar;

        public ViewHolder(View v) {

            userName = (TextView) v.findViewById(R.id.tv_comment_user_name);
            commentDateTime = (TextView) v.findViewById(R.id.tv_comment_date_time);
            commentText = (TextView) v.findViewById(R.id.tv_comment_text);
            gravatar = (ImageView) v.findViewById(R.id.iv_comment_gravatar);

            v.setTag(this);

        }
    }

}
