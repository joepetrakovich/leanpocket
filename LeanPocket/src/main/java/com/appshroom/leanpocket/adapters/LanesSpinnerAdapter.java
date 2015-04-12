package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LaneDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpetrakovich on 9/9/13.
 */
public class LanesSpinnerAdapter extends ArrayAdapter<LaneDescription> {

    private Holder hold;
    private ViewHolder holder;
    private int layoutResource;

    public LanesSpinnerAdapter(Context context, List<LaneDescription> objects) {
        super(context, R.layout.into_lane_item, objects);

        this.layoutResource = R.layout.into_lane_item;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;

        if (itemView == null || itemView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

            hold = new Holder(itemView);


        } else {

            hold = (Holder) itemView.getTag();
        }


        LaneDescription lane = getItem(position);

        hold.text.setText(lane.getName());

        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null || itemView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(layoutResource, parent, false);

            holder = new ViewHolder(itemView);

        } else {

            holder = (ViewHolder) itemView.getTag();
        }


        LaneDescription lane = getItem(position);

        holder.laneName.setText(lane.getName());

        return itemView;
    }

    private class Holder {
        TextView text;

        public Holder(View v) {

            text = (TextView) v.findViewById(android.R.id.text1);
            text.setEllipsize(TextUtils.TruncateAt.START);

            v.setTag(this);
        }
    }

    private class ViewHolder {

        TextView laneName;


        public ViewHolder(View v) {

            laneName = (TextView) v.findViewById(R.id.tv_lane_item_name);

            v.setTag(this);

        }
    }
}
