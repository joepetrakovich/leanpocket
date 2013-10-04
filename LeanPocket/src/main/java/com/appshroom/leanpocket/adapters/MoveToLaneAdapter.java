package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.Lane;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;

import java.util.List;

/**
 * Created by jpetrakovich on 9/6/13.
 */
public class MoveToLaneAdapter extends ArrayAdapter<Lane> implements StickyListHeadersAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private int mResource;
    private ViewHolder holder;

    String archiveName;
    String inflightName;
    String backlogName;

    public MoveToLaneAdapter(Context context, List<Lane> items) {
        super(context, R.layout.move_to_lane_list_item, items);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = R.layout.move_to_lane_list_item;

        archiveName = mContext.getString(R.string.archive_lanes);
        inflightName = mContext.getString(R.string.in_flight_lanes);
        backlogName = mContext.getString(R.string.backlog_lanes);
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.drawer_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.tv_drawer_list_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Lane lane = getItem(position);

        switch (lane.getBoardSectionType()) {

            case ARCHIVE:
                holder.text.setText(archiveName);
                break;
            case BACKLOG:
                holder.text.setText(backlogName);
                break;
            case INFLIGHT:
                holder.text.setText(inflightName);
                break;
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        switch (getItem(position).getBoardSectionType()) {

            case ARCHIVE:
                return 0;

            case BACKLOG:
                return 1;

            case INFLIGHT:
                return 2;

            default:
                return 1;
        }

    }

    class HeaderViewHolder {

        TextView text;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;

        if (itemView == null || itemView.getTag() == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder(itemView);

        } else {

            holder = (ViewHolder) itemView.getTag();
        }


        Lane lane = getItem(position);

        holder.laneName.setText(lane.toString());

        return itemView;
    }


    private class ViewHolder {

        TextView laneName;

        public ViewHolder(View v) {

            laneName = (TextView) v.findViewById(R.id.tv_lane_item_name);

            v.setTag(this);

        }
    }
}
