package com.yakami.light.view.fragment;

import com.yakami.light.adapter.DynamicBangumiRankAdapter;
import com.yakami.light.event.Event;

/**
 * Created by Yakami on 2016/5/27, enjoying it!
 */

public class NestedBangumiRankFragment extends BangumiRankFragment {

    //用于记录需要发送的事件类型，用于主页recyclerview的高度动态调整
    private Event.EventType mEventType;

    @Override
    public void afterCreateView() {
        mRecyclerView.setNestedScrollingEnabled(false);
        DynamicBangumiRankAdapter adapter = (DynamicBangumiRankAdapter) mAdapter;
        adapter.setReturnedEvent(mEventType);
        refresh();
    }

    @Override
    public void initAdapter() {
        mAdapter = new DynamicBangumiRankAdapter(mContext);
        if (mList != null) {
            mAdapter.addItem(mList);
        }
    }

    public void setReturnedEventType(Event.EventType type) {
        mEventType = type;
    }
}
