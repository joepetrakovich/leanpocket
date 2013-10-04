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

import java.util.List;

/**
 * Created by jpetrakovich on 8/26/13.
 */
public class LanesWithCardAmountsSpinnerAdapter extends ArrayAdapter<Lane> {


    private int layoutResource;

    private ViewHolder holder;
    private Holder hold;

    public LanesWithCardAmountsSpinnerAdapter(Context context, int resource, List<Lane> objects) {
        super(context, resource, objects);

        this.layoutResource = resource;

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


        Lane lane = getItem(position);

        hold.text.setText(lane.toString());

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


        Lane lane = getItem(position);

        holder.laneName.setText(lane.toString());

        int numCards = lane.getCards().size();
        String numCardsString = numCards > 0 ? Integer.toString(numCards) : "";
        holder.numCardsInLane.setText(numCardsString);

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
        TextView numCardsInLane;

        public ViewHolder(View v) {

            laneName = (TextView) v.findViewById(R.id.tv_lane_item_name);
            numCardsInLane = (TextView) v.findViewById(R.id.tv_lane_num_cards);

            v.setTag(this);

        }
    }
}
