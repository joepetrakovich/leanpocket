package com.appshroom.leanpocket.adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.helpers.Consts;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jpetrakovich on 8/13/13.
 */
public class AccountSpinnerAdapter extends ArrayAdapter<Account> {

    private int layoutResource;
    private Context mContext;
    private ViewHolder holder;
    private AccountManager mAccountManager;

    public AccountSpinnerAdapter(Context context, int resource, List<Account> accounts) {
        super(context, resource, accounts);

        this.layoutResource = resource;
        this.mContext = context;
        this.mAccountManager = AccountManager.get(context);
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
            itemView = inflater.inflate(layoutResource, parent, false);

            holder = new ViewHolder(itemView);

        } else {

            holder = (ViewHolder) itemView.getTag();
        }


        Account account = getItem(position);

        holder.accountUserName.setText(mAccountManager.getUserData(account, Consts.LEANKIT_USERDATA_EMAIL));
        holder.accountDescription.setText(mAccountManager.getUserData(account, Consts.LEANKIT_USERDATA_ORG_TEXT));

        String gravLink = mAccountManager.getUserData(account, Consts.LEANKIT_USERDATA_GRAVATAR);

        Picasso.with(mContext)
                .load(gravLink)
                .placeholder(R.drawable.mysterymans)
                .into(holder.accountGravatar);

        return itemView;
    }

    private class ViewHolder {

        TextView accountUserName;
        TextView accountDescription;
        ImageView accountGravatar;

        public ViewHolder(View v) {

            accountGravatar = (ImageView) v.findViewById(R.id.iv_account_spinner_gravatar);
            accountUserName = (TextView) v.findViewById(R.id.tv_account_username);
            accountDescription = (TextView) v.findViewById(R.id.tv_account_description);

            v.setTag(this);

        }
    }


}
