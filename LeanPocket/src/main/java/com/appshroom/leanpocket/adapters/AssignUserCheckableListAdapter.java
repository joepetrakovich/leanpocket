package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.BoardUser;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jpetrakovich on 9/9/13.
 */
public class AssignUserCheckableListAdapter extends ArrayAdapter<BoardUser> {


    private ViewHolder holder;
    private int gravSize;

    public AssignUserCheckableListAdapter(Context context, List<BoardUser> objects) {
        super(context, R.layout.layout_assign_user_checkable_item, objects);

        this.gravSize = getContext().getResources().getInteger(R.integer.gravatar_assigned_user_list_size);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null || itemView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.layout_assign_user_checkable_item, parent, false);

            holder = new ViewHolder(itemView);

        } else {

            holder = (ViewHolder) itemView.getTag();
        }


        BoardUser user = getItem(position);

        holder.userName.setText(user.getFullName());

        String gravLink = GravatarHelpers.buildGravatarUrl(user.getGravatarLink(), gravSize);

        Picasso.with(getContext())
                .load(gravLink)
                .placeholder(R.drawable.mysterymans)
                .into(holder.gravatar);

        return itemView;
    }

    private class ViewHolder {

        CheckedTextView userName;
        ImageView gravatar;

        public ViewHolder(View v) {

            userName = (CheckedTextView) v.findViewById(R.id.tv_assigned_user_name);
            gravatar = (ImageView) v.findViewById(R.id.iv_assigned_user_gravatar);

            v.setTag(this);

        }
    }

}
