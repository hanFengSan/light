package com.yakami.light.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.github.ybq.android.spinkit.SpinKitView;
import com.trello.rxlifecycle.ActivityEvent;
import com.yakami.light.DeviceManager;
import com.yakami.light.R;
import com.yakami.light.adapter.PagerAdapter;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.service.BangumiInfoService;
import com.yakami.light.service.BangumiNameService;
import com.yakami.light.service.CopyService;
import com.yakami.light.utils.Tools;
import com.yakami.light.utils.ViewUtils;
import com.yakami.light.view.activity.base.BaseHoldBackTransparentTabActivity;
import com.yakami.light.view.fragment.BangumiInfoFragment;
import com.yakami.light.view.fragment.RankChartFragment;
import com.yakami.light.view.fragment.RankSeriesFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/7/31, enjoying it!
 */

public class DetailActivity extends BaseHoldBackTransparentTabActivity {

    @Bind(R.id.scroller) NestedScrollView mScroller;
    @Bind(R.id.blurView) BlurView blurView;
    @Bind(R.id.root) RelativeLayout mRootView;
    @Bind(R.id.info_container) LinearLayout mInfoContainer;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.info_text) TextView mInfoTextView;
    @Bind(R.id.cover) ImageView mCover;
    @Bind(R.id.cover_bg) ImageView mCoverBg;
    @Bind(R.id.loading_spin_view) SpinKitView mLoadingSpinView;
    @Bind(R.id.cover_spin_view) SpinKitView mCoverSpinView;
    @Bind(R.id.tv_log) TextView mInfoLog;
    @Bind(R.id.tv_cover_error) TextView mCoverErrorTextView;
    @Bind(R.id.view_pager_spin_view) SpinKitView mViewPagerSpinView;
    @Bind(R.id.view_pager_container) RelativeLayout mViewPagerContainer;

    private DiscRank mDiscRank;
    private PagerAdapter mPagerAdapter;
    private BangumiInfoService mInfoService;
    private List<DiscRank> mBangumiRankGroup = new ArrayList<>();
    private int mDominantColor;
    private List<OnGetDominantColorListener> mColorListenerList = new ArrayList<>();

    private RankChartFragment mChartFragment;
    private RankSeriesFragment mSeriesFragment;
    private BangumiInfoFragment mInfoFragment = new BangumiInfoFragment();

    private int mScrollCriticalVal;
    private List<Integer> mFragmentHeightList = new ArrayList<>();

    private int mToolbarAlpha = 220;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDiscRank = (DiscRank) getIntentHelper().getSerializable("data");
        mTitle.setText(mDiscRank.getsName());


        initBlurBg();
        //调整高度，似的内容在toolbar和status bar下
        initInfoLayout();

        initTabs(savedInstanceState != null);
        //封面、简介
        initInfo();

        initDominantColor();

        initToolbarColorAnimation();
    }

    protected void initTabs(boolean isRestarted) {
        super.initTabs(isRestarted);

        addOnGetDominantColorListener(color -> mViewPagerSpinView.setColor(color));

        //先获取番剧rank列表再初始化ViewPager
        Observable.create((Subscriber<? super List<DiscRank>> subscriber) -> {
            subscriber.onNext(BangumiNameService.newInstance(mDiscRank.getName()).getBangumiRankGroup());
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(groupList -> {
                    mBangumiRankGroup = groupList;

                    mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

                    mChartFragment = new RankChartFragment();
                    mChartFragment.setData(mDiscRank, groupList);

                    mSeriesFragment = new RankSeriesFragment();
                    mSeriesFragment.setData(groupList);

                    addOnGetDominantColorListener(color -> mChartFragment.setLineColor(color));

                    //由于内嵌于NestedScrollView的关系，ViewPager必须要指定高度
                    RxBus.with(this)
                            .setEvent(Event.EventType.DETAIL_FRAGMENT_HEIGHT)
                            .setEndEvent(ActivityEvent.DESTROY)
                            .onNext(event -> {
                                mFragmentHeightList.add(event.getMessage());
                                if (mFragmentHeightList.size() == 3)
                                    ViewUtils.setViewHeight(mViewPagerContainer, Tools.getMaxNum(mFragmentHeightList) + getNavigationBarHeight());
                            }).create();

                    mPagerAdapter.addFragment(mChartFragment, mRes.getString(R.string.rank_chart));
                    mPagerAdapter.addFragment(mSeriesFragment, mRes.getString(R.string.series_rank));
                    mPagerAdapter.addFragment(mInfoFragment, mRes.getString(R.string.detailed_info));

                    mViewPager.setOffscreenPageLimit(3);
                    mPagerAdapter.notifyDataSetChanged();
                    mViewPager.setAdapter(mPagerAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                    mPagerAdapter.notifyDataSetChanged();

                    mViewPagerSpinView.setVisibility(View.GONE);
                });
    }

    private void initBlurBg() {
        final float radius = 25;

        final View decorView = getWindow().getDecorView();
        final Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(mRootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(this, true))
                .blurRadius(radius);
    }

    private void initInfoLayout() {
        mToolbar.post(() -> {
            int statusBarHeight = getStatusBarHeight();
            int toolBarHeight = mToolbar.getMeasuredHeight();
            if (Build.VERSION.SDK_INT >= 21)
                ViewUtils.setViewMargin(mInfoContainer, ViewUtils.MARGIN_TOP, statusBarHeight + toolBarHeight);
            else
                ViewUtils.setViewMargin(mInfoContainer, ViewUtils.MARGIN_TOP, toolBarHeight);
            mInfoContainer.post(() -> mInfoContainer.setVisibility(View.VISIBLE));
        });
    }

    private void showInfo() {
        mLoadingSpinView.setVisibility(View.GONE);
        mInfoLog.setVisibility(View.GONE);
        mInfoTextView.setVisibility(View.VISIBLE);
        mCoverSpinView.setVisibility(View.VISIBLE);
    }

    private void showCover() {
        mCoverSpinView.setVisibility(View.GONE);
        mCover.setVisibility(View.VISIBLE);
    }

    private void showCoverLoadError() {
        mCoverSpinView.setVisibility(View.GONE);
        mCoverErrorTextView.setVisibility(View.VISIBLE);
    }

    private void initInfo() {
        mInfoService = BangumiInfoService.newInstance(mDiscRank.getName(), mDiscRank.getId());
        //简介信息
        RxBus.with(this)
                .setEvent(Event.EventType.BANGUMI_TEXT_INFO_GET)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext(event -> {
                    if (Tools.isAvailableStr(mInfoService.getcName()))
                    mInfoTextView.setText(String.format("译名：%s\n原名：%s\n简介：%s\n",
                            mInfoService.getcName(), mInfoService.getName(), mInfoService.getIntro()).trim().replace("　", ""));
                    else
                        mInfoTextView.setText(String.format("名称：%s\n简介：%s\n",
                                mInfoService.getName(), mInfoService.getIntro()).trim().replace("　", ""));
                    mInfoFragment.setData(mInfoService.getInfoBox());
                    showInfo();
                })
                .create();
        //图片
        RxBus.with(this)
                .setEvent(Event.EventType.BANGUMI_COVER_GET)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext(event -> {
                    mCover.setImageBitmap(mInfoService.getCover());
                    mCoverBg.setImageBitmap(mInfoService.getCover());
                    showCover();
                })
                .create();

        RxBus.with(this)
                .setEvent(Event.EventType.BANGUMI_COVER_FAIL)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext(event -> showCoverLoadError())
                .create();
        //加载log
        RxBus.with(this)
                .setEvent(Event.EventType.BANGUMI_PARSE_INFO)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext(event -> {
                    mInfoLog.setText(event.getMessage());
                })
                .create();
        RxBus.with(this)
                .setEvent(Event.EventType.BANGUMI_SEARCH_RESULT_FAIL)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext(event -> {
                    mInfoLog.setText(mRes.getString(R.string.no_available_search_result));
                    mLoadingSpinView.setVisibility(View.GONE);
                    mInfoFragment.showError();
                })
                .create();

        mInfoService.start();
    }

    private void initDominantColor() {
        if (mInfoService.getDominantColor() != 0) {
            mDominantColor = mInfoService.getDominantColor();
            afterInitDominantColor();
        } else {
            RxBus.with(this)
                    .setEvent(Event.EventType.BANGUMI_COLOR_GET)
                    .setEndEvent(ActivityEvent.DESTROY)
                    .onNext(event -> {
                        mDominantColor = mInfoService.getDominantColor();
                        afterInitDominantColor();
                    })
                    .create();
        }
    }

    private void afterInitDominantColor() {
        Stream.of(mColorListenerList)
                .forEach(item -> item.run(mDominantColor));
    }

    public interface OnGetDominantColorListener {
        void run(int color);
    }

    public void addOnGetDominantColorListener(OnGetDominantColorListener listener) {
        if (mDominantColor == 0)
            mColorListenerList.add(listener);
        else
            listener.run(mDominantColor);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_copy_series) {
            DeviceManager.copyToClipboard(CopyService.copyRank(mBangumiRankGroup));
            Tools.toast(mRes.getString(R.string.copy_completed));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbarColorAnimation() {
        mScrollCriticalVal = Tools.dp2px(173);
        addOnGetDominantColorListener((int color) -> {
            mToolbar.setBackgroundColor(color);
            mStatusBarBg.setBackgroundColor(color);
            mToolbar.getBackground().mutate().setAlpha(0);
            mStatusBarBg.getBackground().mutate().setAlpha(0);
        });
        mScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mScrollCriticalVal)
                    return;
                int alpha = (int) (scrollY * ((mToolbarAlpha * 1.0f) / mScrollCriticalVal));
                mToolbar.getBackground().mutate().setAlpha(alpha > mToolbarAlpha ? mToolbarAlpha : alpha);
                mStatusBarBg.getBackground().mutate().setAlpha(alpha > mToolbarAlpha ? mToolbarAlpha : alpha);
            }
        });
    }
}
