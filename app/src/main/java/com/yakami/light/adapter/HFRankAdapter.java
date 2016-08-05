package com.yakami.light.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.ActivityEvent;
import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.adapter.base.BaseHeaderFooterListAdapter;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.utils.Tools;
import com.yakami.light.view.activity.MainActivity;
import com.yakami.light.view.fragment.SlidingTabFragment;
import com.yakami.light.widget.TagView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Yakami on 2016/6/8, enjoying it!
 */

public class HFRankAdapter extends BaseHeaderFooterListAdapter<BangumiRank> {

    private MainActivity mActivityRef;

    public HFRankAdapter(MainActivity activity) {
        super(activity);
        mActivityRef = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new HFRankAdapter.BangumiRankViewHolder(mInflater.inflate(R.layout.item_rank, parent, false));
            case TYPE_HEADER:
                return new HFRankAdapter.HeaderHolder(mInflater.inflate(R.layout.view_main_recyclerview_header, parent, false));
            case TYPE_FOOTER:
                return new HFRankAdapter.FooterHolder(mInflater.inflate(R.layout.view_main_recyclerview_footer, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BangumiRankViewHolder) {
            BangumiRankViewHolder newHolder = (BangumiRankViewHolder) holder;

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

        } else if (holder instanceof HeaderHolder) {

        } else if (holder instanceof FooterHolder) {

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
                    mIsSwiping = false;
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
                    mIsSwiping = false;
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

    public class HeaderHolder extends RecyclerView.ViewHolder {

        public @Bind(R.id.banner_container) RelativeLayout mContainer;
        public @Bind(R.id.swipe_layout) SwipeLayout swipeLayout;
        public @Bind(R.id.spin_view) SpinKitView mSpinView;
        public @Bind(R.id.banner) ImageView mBanner;
        public @Bind(R.id.banner2) ImageView mBanner2;
        public @Bind(R.id.tab_layout) SlidingTabLayout mTabLayout;
        public @Bind(R.id.view_pager) ViewPager mViewPager;
        public @Bind(R.id.update_time) TextView updateTime;
        private PagerAdapter mPagerAdapter;
        private boolean mIsOpened;

        public HeaderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            RxBus.with(mActivityRef)
                    .setEvent(Event.EventType.INIT_TAB_LAYOUT)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> {
                        String[] titles = new String[AppManager.getDiscRankService().getTimeRanksList().size() + 1];
                        for (int i = 0; i < AppManager.getDiscRankService().getTimeRanksList().size(); i++) {
                            titles[i] = AppManager.getDiscRankService().getTimeRanksList().get(i).getName();
                        }
                        titles[titles.length - 1] = AppManager.getRes().getString(R.string.my_watch_list);
                        setTabLayout(titles);
                        if (!mIsOpened)
                            initUpdateTime();
                        mIsOpened = true;
                    }).create();

            //加载
            RxView.clicks(mBanner)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(aVoid -> {
                        setLoading();
                    });
            RxView.clicks(mBanner2)
                    .throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(aVoid -> {
                        setLoading();
                    });

            //swipeLayout
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, mBanner);

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
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
                }
            });
            //绑定Loading操作消息
            RxBus.with(mActivityRef)
                    .setEvent(Event.EventType.SHOW_LOADING)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> showLoading())
                    .create();
            RxBus.with(mActivityRef)
                    .setEvent(Event.EventType.REFRESH_COMPLETED)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> hideLoading())
                    .create();
        }

        public void setLoading() {
            showLoading();
            //刷新消息交互
            RxBus.getInstance().send(Event.EventType.REFRESH, null);
        }

        public void showLoading() {
            Sprite[] spriteArray = {
                    new DoubleBounce(),
                    new WanderingCubes(),
                    new Pulse(),
                    new ChasingDots(),
                    new ThreeBounce()
            };
            while (true) {
                Sprite sprite = spriteArray[new Random().nextInt(spriteArray.length)];
                if (sprite != mSpinView.getIndeterminateDrawable()) {
                    mSpinView.setIndeterminateDrawable(sprite);
                    break;
                }
            }
            mSpinView.setColor(mRes.getColor(R.color.PeterRiver));
            mSpinView.setVisibility(View.VISIBLE);
        }

        public void hideLoading() {
            mSpinView.setVisibility(View.GONE);
        }


        public void initUpdateTime() {
            RxBus.with(mActivityRef)
                    .setEvent(Event.EventType.SET_UPDATE_TIME)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> {
                        Observable.just("")
                                .delay(1000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tmp -> {
                                    if (AppManager.getTabPos() < AppManager.getDiscRankService().getTimeRanksList().size()) {
                                        updateTime.setText(mRes.getString(R.string.between_update_time,
                                                Tools.getTimeGap(AppManager.getDiscRankService().getTimeRanksList().get(AppManager.getTabPos()).getTime())));
                                    } else {
                                        //我的关注
                                        updateTime.setText(mRes.getString(R.string.between_update_time, ""));
                                    }
                                    RxBus.getInstance().send(Event.EventType.SET_UPDATE_TIME, null);
                                });
                    }).create();

            RxBus.getInstance().send(Event.EventType.SET_UPDATE_TIME, null);
        }

        private void setTabLayout(String[] titles) {
            mPagerAdapter = new PagerAdapter(mActivityRef.getSupportFragmentManager());
            for (String title : titles) {
                mPagerAdapter.addFragment(new SlidingTabFragment(), title);
            }
            mViewPager.setOffscreenPageLimit(titles.length);
            mPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mPagerAdapter);
            mPagerAdapter.notifyDataSetChanged();

            mTabLayout.setViewPager(mViewPager);
            mTabLayout.setTextsize(22.0f);
            mTabLayout.setTextSelectColor(mRes.getColor(R.color.black));
            mTabLayout.setTextUnselectColor(mRes.getColor(R.color.unselected_grey));
            mTabLayout.setTabMargin(0, 0, 15, 0);
            mTabLayout.setTabPadding(7);
            mTabLayout.setIndicatorWidth(30);
            mTabLayout.setIndicatorHeight(4);
            mTabLayout.setIndicatorLeft(true);
            mTabLayout.setIndicatorColor(mRes.getColor(R.color.PeterRiver));

            mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    Event event = new Event();
                    event.type = Event.EventType.TAB_SELECT;
                    event.message = position;
                    RxBus.getInstance().send(event);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            mTabLayout.setCurrentTab(AppManager.getTabPos());
        }


    }


    class FooterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.block) FrameLayout block;

        public FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            //为底部预先填充一个屏幕的高度，后期再动态调整
            ViewGroup.LayoutParams param = block.getLayoutParams();
            param.height = mActivityRef.getScreenSize().y;
            block.setLayoutParams(param);

            RxBus.with(mActivityRef)
                    .setEvent(Event.EventType.SET_ABOVE_NAVIGATION_BAR)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> {
                        ViewGroup.LayoutParams params = block.getLayoutParams();
                        params.height = event.getMessage();
                        block.setLayoutParams(params);
                    }).create();
        }
    }
}
