package com.yakami.light.adapter.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.yakami.light.bean.base.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakami, Created on 2016/4/19
 */
public abstract class BaseListAdapter<T extends Entity> extends RecyclerView.Adapter {

    protected Context mContext;
    private List<T> items;
    protected Resources mRes;
    protected LayoutInflater mInflater;

    protected OnItemClickListener mItemClickListener;
    protected OnItemLongClickListener mItemLongClickListener;

    public BaseListAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseListAdapter(Context context, List<T> items) {
        this.mContext = context;
        this.items = items;
        mRes = mContext.getResources();
        mInflater = LayoutInflater.from(mContext);
    }

    public final void addItem(T obj) {
        items.add(obj);
        notifyDataSetChanged();
    }

    public final void addItem(List<T> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public final T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onClick(int position, T itemData);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(int position, T itemData);
    }

}
