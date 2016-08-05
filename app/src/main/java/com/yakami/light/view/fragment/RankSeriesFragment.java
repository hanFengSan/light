package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.adapter.FRankAdapter;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.service.DiscRankService;
import com.yakami.light.utils.IntentHelper;
import com.yakami.light.utils.ViewUtils;
import com.yakami.light.view.activity.DetailActivity;
import com.yakami.light.view.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/2, enjoying it!
 */

public class RankSeriesFragment extends BaseFragment {

    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    @Bind(R.id.root) LinearLayout mRoot;

    private FRankAdapter mAdapter;
    private List<DiscRank> mRankList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_series, container, false);
        ButterKnife.bind(this, view);

        initList();

        //用于调整ViewPager高度
        mRoot.post(() -> {
            Event event = new Event();
            event.type = Event.EventType.DETAIL_FRAGMENT_HEIGHT;
            event.message = ViewUtils.getVerticalLinearLayoutHeight(mRoot);
            RxBus.getInstance().send(event);
        });
        return view;
    }

    public void setData(List<DiscRank> list) {
        mRankList.addAll(list);
    }

    private void initList() {
        mAdapter = new FRankAdapter(this);
        mAdapter.addItem(DiscRankService.toBangumiRank(mRankList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setNestedScrollingEnabled(false);
        //长按开启通知配置dialog
        mAdapter.setOnItemLongClickListener((pos, itemData) -> {
            RankDialogFragment dialog = new RankDialogFragment();
            dialog.show(getActivity().getSupportFragmentManager(), "RANK_DIALOG");
            dialog.setBangumiRankInfo(DiscRankService.toBangumiRank(mRankList.get(pos)));
        });
        //点击打开DetailActivity
        mAdapter.setOnItemClickListener((pos, itemData) -> {
            BangumiRank rank = (BangumiRank) itemData;
            startActivity(IntentHelper
                    .newInstance(getActivity(), DetailActivity.class)
                    .putSerializable("data", AppManager.getDiscRankService().toDicsRank(rank))
                    .toIntent());
        });
    }
}
