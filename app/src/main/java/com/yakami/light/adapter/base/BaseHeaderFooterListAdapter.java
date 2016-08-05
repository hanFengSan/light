package com.yakami.light.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yakami.light.bean.base.Entity;

import java.util.List;

/**
 * Created by Yakami on 2016/6/8, enjoying it!
 */
public abstract class BaseHeaderFooterListAdapter<T extends Entity> extends BaseListAdapter<T> {

    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_FOOTER = 2;

    public BaseHeaderFooterListAdapter(Context context) {
        super(context);
    }

    public BaseHeaderFooterListAdapter(Context context, List<T> items) {
        super(context, items);
    }

    protected int getRealItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemCount() {
        return getRealItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        if (position == getRealItemCount() + 1)
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public int getRealAdapterPosition() {
            return super.getAdapterPosition() - 1;
        }

    }
}
