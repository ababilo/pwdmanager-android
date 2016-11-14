package com.ababilo.pwd.pwdmanager.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ababilo on 12.11.16.
 */

public abstract class ListAdapterHolder<T, E extends ListAdapterHolder.ViewHolder> extends RecyclerView.Adapter<E> implements OnItemClickListener {

    protected List<T> list;
    private int itemView;

    public ListAdapterHolder(List<T> list, int itemView) {
        this.list = list;
        this.itemView = itemView;
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View view = mInflater.inflate(itemView, parent, false);
        return createViewHolder(view, this);
    }

    protected abstract E createViewHolder(View view, OnItemClickListener listener);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener listener;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getLayoutPosition());
            }
        }

    }
}
