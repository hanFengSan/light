package com.yakami.light.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yakami.light.R;
import com.yakami.light.adapter.base.BaseListAdapter;
import com.yakami.light.bean.BangumiRank;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/5/23, enjoying it!
 */
public class BangumiRankAdapter extends BaseListAdapter<BangumiRank> {

    public BangumiRankAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BangumiRankViewHolder(mInflater.inflate(R.layout.item_rank, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BangumiRankViewHolder newHolder = (BangumiRankViewHolder) holder;

        BangumiRank rank = getItem(position);
        newHolder.serialNum.setText(String.valueOf(rank.getSerialNum()));
        newHolder.name.setText(rank.getsName());
        newHolder.rank.setText(rank.getRank());
        newHolder.situation.setText(rank.getSituation());
    }


    class BangumiRankViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.serial_num) TextView serialNum;
        @Bind(R.id.sName) TextView name;
        @Bind(R.id.rank) TextView rank;
        @Bind(R.id.situation) TextView situation;

        public BangumiRankViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            RxView.clicks(view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe((Void) -> {
                                if (mItemClickListener != null) {
                                    int pos = getAdapterPosition();
                                    mItemClickListener.onClick(pos, getItem(pos));
                                }
                            }
                    );
            RxView.longClicks(view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe((Void) -> {
                        if (mItemLongClickListener != null) {
                            int pos = getAdapterPosition();
                            mItemLongClickListener.onLongClick(pos, getItem(pos));
                        }
                    });
        }
    }
}
