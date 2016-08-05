package com.yakami.light.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.yakami.light.R;
import com.yakami.light.adapter.base.BaseHeaderFooterListAdapter;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.view.fragment.base.BaseFragment;
import com.yakami.light.widget.TagView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yakami on 2016/8/2, enjoying it!
 */

public class FRankAdapter extends BaseHeaderFooterListAdapter<BangumiRank> {

    private BaseFragment mFragmentRef;

    public FRankAdapter(BaseFragment fragment) {
        super(fragment.getContext());
        mFragmentRef = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new FRankAdapter.BangumiRankViewHolder(mInflater.inflate(R.layout.item_rank, parent, false));
            case TYPE_HEADER:
                return new FRankAdapter.HeaderHolder(mInflater.inflate(R.layout.view_null, parent, false));
            case TYPE_FOOTER:
                return new FRankAdapter.FooterHolder(mInflater.inflate(R.layout.view_main_recyclerview_footer, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FRankAdapter.BangumiRankViewHolder) {
            FRankAdapter.BangumiRankViewHolder newHolder = (FRankAdapter.BangumiRankViewHolder) holder;

            BangumiRank rank = getItem(position - 1);
            newHolder.serialNum.setText(String.valueOf(rank.getSerialNum()));
            newHolder.name.setText(rank.getsName());
            newHolder.rank.setText(rank.getRank());
            int situation = Integer.valueOf(rank.getSituation());
            newHolder.situation.setText(Math.abs(situation) + "");
            if (situation < 0) {
                newHolder.situationImg.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                newHolder.situationImg.setColorFilter(mRes.getColor(R.color.soft_red));
            } else if (situation == 0) {
                newHolder.situationImg.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                newHolder.situationImg.setColorFilter(mRes.getColor(R.color.soft_yellow));
            } else if (situation > 0) {
                newHolder.situationImg.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                newHolder.situationImg.setColorFilter(mRes.getColor(R.color.PeterRiver));
            }
            //设置tag
            String name = rank.getName();
            if (!name.contains("尼限定") && !name.contains("[DVD]")) {
                newHolder.tagView.setVisibility(View.GONE);
            }
            if (name.contains("尼限定")) {
                newHolder.tagView.setVisibility(View.VISIBLE);
                newHolder.tagView.setText("尼限");
                newHolder.tagView.setBgColor(mRes.getColor(R.color.PeterRiver));
            }
            if (name.contains("[DVD]")) {
                newHolder.tagView.setVisibility(View.VISIBLE);
                newHolder.tagView.setText("DVD");
                newHolder.tagView.setBgColor(mRes.getColor(R.color.emerald));
            }
            if (name.contains("尼限定") && name.contains("[DVD]")) {
                newHolder.tagView.setVisibility(View.VISIBLE);
                newHolder.tagView.setText("尼DVD");
                newHolder.tagView.setBgColor(mRes.getColor(R.color.soft_purple));
            }
            //设置侧滑的详细信息
            newHolder.details.setText(String.format("累计PT: %s    距离上次更新：%s    nico预约: %s" +
                            "\nAmazon排名: %s    距离发售: %s    类型: %s", rank.getPT(), rank.getLastUpdate(),
                    rank.getNicoOrder(), rank.getAmazonRank(), rank.getSaleDay(), rank.getType()));

        } else if (holder instanceof FRankAdapter.HeaderHolder) {

        } else if (holder instanceof FRankAdapter.FooterHolder) {

        }
    }


    class BangumiRankViewHolder extends BaseHeaderFooterListAdapter.ViewHolder {

        @Bind(R.id.swipe_layout) SwipeLayout swipeLayout;
        @Bind(R.id.bottom_wrapper) LinearLayout bottomWrapper;
        @Bind(R.id.details) TextView details;
        @Bind(R.id.serial_num) TextView serialNum;
        @Bind(R.id.sName) TextView name;
        @Bind(R.id.rank) TextView rank;
        @Bind(R.id.situation) TextView situation;
        @Bind(R.id.situation_img) ImageView situationImg;
        @Bind(R.id.tag_view) TagView tagView;

        private boolean mIsSwiping;

        public BangumiRankViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, bottomWrapper);
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                    mIsSwiping = true;
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                    mIsSwiping = false;
                }
            });

            RxView.clicks(view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe((Void) -> {
                                if (mItemClickListener != null && !mIsSwiping) {
                                    int pos = getRealAdapterPosition();
                                    mItemClickListener.onClick(pos, getItem(pos));
                                }
                            }
                    );
            RxView.longClicks(view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe((Void) -> {
                        if (mItemLongClickListener != null && !mIsSwiping) {
                            int pos = getRealAdapterPosition();
                            mItemLongClickListener.onLongClick(pos, getItem(pos));
                        }
                    });
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View view) {
            super(view);
        }

    }


    class FooterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.block) FrameLayout block;

        public FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            //为底部预先填充一个屏幕的高度，后期再动态调整
            ViewGroup.LayoutParams param = block.getLayoutParams();
            param.height = 20;
            block.setLayoutParams(param);

//            RxBus.with(mFragmentRef)
//                    .setEvent(Event.EventType.SET_ABOVE_NAVIGATION_BAR)
//                    .setEndEvent(FragmentEvent.DESTROY)
//                    .onNext(event -> {
//                        ViewGroup.LayoutParams params = block.getLayoutParams();
//                        params.height = event.getMessage();
//                        block.setLayoutParams(params);
//                    }).create();
        }
    }


}
