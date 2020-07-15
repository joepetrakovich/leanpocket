package com.appshroom.leanpocket.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.v2.ListBoardsBoard;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;

import java.util.List;

/**
 * Created by jpetrakovich on 8/30/13.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerListItem> implements StickyListHeadersAdapter {

    private List<DrawerListItem> mItems;
    private Context mContext;
    private LayoutInflater mInflater;


    public DrawerListAdapter(Context context, List<DrawerListItem> items) {
        super(context, R.layout.drawer_list_item, items);

        mContext = context;
        mItems = items;
        mInflater = LayoutInflater.from(context);
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

        String headerText = "";

        if (mItems.get(position).getType() == DrawerListItem.DrawerItemType.BOARD) {
            headerText = mContext.getResources().getString(R.string.boards);
        } else {
            headerText = mContext.getResources().getString(R.string.lanes);
        }

        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        if (mItems.get(position).getType() == DrawerListItem.DrawerItemType.BOARD) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean isEnabled(int position) {

        DrawerListItem item = mItems.get(position);

        if (item instanceof BoardSection) {

            return ((BoardSection) item).isReadyForUse();
        } else {

            return true;
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public DrawerListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);

            holder = new ViewHolder(convertView);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        DrawerListItem item = mItems.get(position);

        holder.text.setText(mItems.get(position).getName());
        holder.text.setTextColor(mContext.getResources().getColor(android.R.color.primary_text_light));

        if (item.getType() == DrawerListItem.DrawerItemType.SECTION) {

            buildSectionItem(holder, (BoardSection) item);

        } else {

            buildBoardItem(holder, (ListBoardsBoard) item);
        }

        return convertView;

    }

    private void buildBoardItem(ViewHolder holder, ListBoardsBoard board) {

        holder.icon.setVisibility(View.GONE);

        if (board.isActiveBoard()) {

            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.actionbar_blue));
            holder.text.setTextColor(mContext.getResources().getColor(android.R.color.white));

        } else {
            holder.layout.setBackgroundColor(Color.TRANSPARENT);
            holder.text.setTextColor(mContext.getResources().getColor(android.R.color.primary_text_light));
        }
    }

    private void buildSectionItem(ViewHolder holder, BoardSection section) {

        if (!section.isReadyForUse()) {
            holder.text.setTextColor(Color.LTGRAY);
        } else {
            holder.text.setTextColor(Color.BLACK);
        }

        holder.icon.setVisibility(View.VISIBLE);

        switch (section.getSectionType()) {

            case INFLIGHT:
                holder.icon.setImageDrawable(holder.homeIcon);
                break;

            case BACKLOG:
                holder.icon.setImageDrawable(holder.backlogIcon);
                break;

            case ARCHIVE:
                holder.icon.setImageDrawable(holder.archiveIcon);
                break;
        }

        if (section.isActive()) {

            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.leankit_yellow));
        } else {

            holder.layout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {

        TextView text;
        LinearLayout layout;
        ImageView icon;

        Drawable homeIcon;
        Drawable backlogIcon;
        Drawable archiveIcon;

        public ViewHolder(View v) {

            text = (TextView) v.findViewById(R.id.drawer_item_title);
            layout = (LinearLayout) v.findViewById(R.id.drawer_item_layout);
            icon = (ImageView) v.findViewById(R.id.drawer_item_icon);

            homeIcon = mContext.getResources().getDrawable(R.drawable.ic_action_home);
            backlogIcon = mContext.getResources().getDrawable(R.drawable.ic_av_add_to_queue);
            archiveIcon = mContext.getResources().getDrawable(R.drawable.ic_action_box);

            v.setTag(this);
        }

    }
}
