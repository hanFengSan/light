package com.yakami.light.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakami on 2016/5/27, enjoying it!
 */

public class DynamicBangumiRankAdapter extends BangumiRankAdapter {

    private List<Integer> itemHeightList = new ArrayList<>();
    private Event.EventType mEventType;

    public DynamicBangumiRankAdapter(Context context) {
        super(context);
    }

    public void setReturnedEvent(Event.EventType event) {
        mEventType = event;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BangumiRankViewHolder newHolder = (BangumiRankViewHolder) holder;
        newHolder.itemView.post(() -> {
            int cellHeight = newHolder.itemView.getMeasuredHeight();
            itemHeightList.add(cellHeight);
            if (itemHeightList.size() == getItemCount()) {
                int result = 0;
                for (int item : itemHeightList) {
                    result += item;
                }
                //发送带true height的消息
                Event e = new Event();
                e.type = mEventType;
                e.message = result;
                RxBus.getInstance().send(e);
            }
        });
    }
}
