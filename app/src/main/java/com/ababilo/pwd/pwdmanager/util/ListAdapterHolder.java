package com.ababilo.pwd.pwdmanager.util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ababilo.pwd.pwdmanager.R;

/**
 * Created by ababilo on 12.11.16.
 */

public class ListAdapterHolder extends RecyclerView.Adapter<ListAdapterHolder.ViewHolder> {

    private final Activity context;
    OnItemClickListener listener;

    public ListAdapterHolder(Activity context) {
        this.context = context;
        //createUserDetails();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.password_list_item, parent, false);
        return new ViewHolder(sView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
//        holder.vId.setText("ID: " + mUserDetails.get(position).getId());
//        holder.vName.setText("Name: " + mUserDetails.get(position).getName());
//        holder.vSex.setText("Sex: " + mUserDetails.get(position).getSex());
//        holder.vAge.setText("Age: " + mUserDetails.get(position).getAge());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vName, vSex, vId, vAge;
        OnItemClickListener listener;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
//            vId = (TextView) view.findViewById(R.id.list_id);
//            vName = (TextView) view.findViewById(R.id.list_name);
//            vSex = (TextView) view.findViewById(R.id.list_sex);
//            vAge = (TextView) view.findViewById(R.id.list_age);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getLayoutPosition());
            }
        }

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.listener = mItemClickListener;
    }
}
