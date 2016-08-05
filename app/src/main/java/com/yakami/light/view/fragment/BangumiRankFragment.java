package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakami.light.R;
import com.yakami.light.adapter.BangumiRankAdapter;
import com.yakami.light.adapter.base.BaseListAdapter;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.view.fragment.base.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/5/23, enjoying it!
 */
public class BangumiRankFragment extends BaseFragment {

    @Bind(R.id.bangumi_rank_list) RecyclerView mRecyclerView;
    protected BangumiRankAdapter mAdapter;

    protected BaseListAdapter.OnItemClickListener mItemClickListener;
    protected BaseListAdapter.OnItemLongClickListener mItemLongClickListener;

    protected List<BangumiRank> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.fragment_bangumi_rank, container, false);
        ButterKnife.bind(this, view);

        initAdapter();
        initRecyclerView();

        afterCreateView();

        return view;
    }

    public void afterCreateView() {
    }

    protected void initAdapter() {
        mAdapter = new BangumiRankAdapter(mContext);
        if (mList != null) {
            mAdapter.addItem(mList);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setData(List<BangumiRank> list) {
        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.addItem(list);
        } else {
            mList = list;
        }
    }

    public void refresh() {
//        setRefreshState();
//        mRefreshListener.getRemoteData();
        mAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(BaseListAdapter.OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(BaseListAdapter.OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }



}
