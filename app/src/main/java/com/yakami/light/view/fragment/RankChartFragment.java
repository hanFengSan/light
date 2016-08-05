package com.yakami.light.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.service.BangumiNameService;
import com.yakami.light.utils.Tools;
import com.yakami.light.utils.ViewUtils;
import com.yakami.light.view.fragment.base.BaseFragment;
import com.yakami.light.widget.LineAnnotationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Yakami on 2016/7/31, enjoying it!
 */

public class RankChartFragment extends BaseFragment {

    @Bind(R.id.chart_container) LinearLayout mChartContainer;
    @Bind(R.id.annotation_container) LinearLayout mAnnotationContainer;
    @Bind(R.id.root) LinearLayout mRoot;

    //chart 配置参数
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isCubic = false;  //是否是曲线
    private boolean isFilled = true; //是否显示area
    private boolean hasAxes = true;  //是否有坐标轴
    private boolean hasAxesNames = true; //是否有坐标轴名称
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean pointsHaveDifferentColor;

    private static final int COLOR_BLUE = Color.parseColor("#33B5E5");
    private static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
    private static final int COLOR_GREEN = Color.parseColor("#99CC00");
    private static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
    private static final int COLOR_RED = Color.parseColor("#FF4444");
    private static final int[] COLORS = new int[]{COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED};

    private LineChartView mLineChartView;
    private LineChartView mLinesChartView;
    private DiscRank mDiscRank;
    private List<DiscRank> mGroupList = new ArrayList<>();
    private List<DiscRank> mSuperFishList = new ArrayList<>(); //用于剔除完全无数据的super杂鱼
    private int mLineColor;

    private int mAnnotationPadding = 5;
    private int mAxisMinNum = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.fragment_rank_chart, container, false);
        ButterKnife.bind(this, view);

        //单独线图
        mLineChartView = getChartView();
        mLineChartView.setLineChartData(generateLineData());
        setViewPort(mLineChartView, getViewport(mLineChartView, getDiscRankValueList(mDiscRank), 0, 4, false));
        mLineChartView.setZoomEnabled(false);
        mLineChartView.disableTouch(true);
        //系列线图
        mLinesChartView = getChartView();
        mLinesChartView.setLineChartData(generateLinesData());
        setViewPort(mLinesChartView, getViewport(mLinesChartView, getDiscRankValueList(mGroupList), 0, 4, true));
        mLinesChartView.setZoomEnabled(false);
        mLinesChartView.disableTouch(true);

        //用于调整ViewPager高度
        mAnnotationContainer.post(() -> {
            Event event = new Event();
            event.type = Event.EventType.DETAIL_FRAGMENT_HEIGHT;
            event.message = ViewUtils.getVerticalLinearLayoutHeight(mRoot) + mGroupList.size() * mAnnotationPadding * 2;
            RxBus.getInstance().send(event);
        });
        return view;
    }

    public void setData(DiscRank discRank, List<DiscRank> group) {
        this.mDiscRank = discRank;
        this.mGroupList = group;
    }

    private List<Integer> getDiscRankValueList(DiscRank r) {
        List<Integer> result = new ArrayList<>();
        //数据全部取负数，使得图标的Y轴数值能够倒过来，然后在helloCharts最后的canvas draw的时候修复回来
        result.add(r.getPreRank5() * -1);
        result.add(r.getPreRank4() * -1);
        result.add(r.getPreRank3() * -1);
        result.add(r.getPreRank2() * -1);
        result.add(r.getPreRank1() * -1);
        return result;
    }

    private List<Integer> getDiscRankValueList(List<DiscRank> list) {
        List<Integer> result = new ArrayList<>();
        for (DiscRank rank : list) {
            result.addAll(getDiscRankValueList(rank));
        }
        return result;
    }


    private Viewport getViewport(LineChartView view, List<Integer> y, int left, int right, boolean removeFish) {
        final Viewport v = new Viewport(view.getMaximumViewport());
        v.bottom = Tools.getMinNum(y);
        if (v.bottom > -mAxisMinNum)
            v.bottom = -mAxisMinNum;
        int top = Tools.getMaxNum(y);
        if (top == 0) {
            if (removeFish)
                v.top = Tools.getMaxAndNoZeroNum(y);
            else
                v.top = 100;
        }
        v.left = left;
        v.right = right;
        return v;
    }

    private void setViewPort(LineChartView view, Viewport v) {
        view.setMaximumViewport(v);
        view.setCurrentViewport(v);
    }

    private LineChartView getChartView() {
        LineChartView chart = new LineChartView(AppManager.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, Tools.dp2px(250), 1.0f);
        chart.setLayoutParams(params);
        mChartContainer.addView(chart);
        return chart;
    }

    /**
     * 单独线图数据生成
     *
     * @return
     */
    private LineChartData generateLineData() {
        List<Integer> list = getDiscRankValueList(mDiscRank);
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<>();
        for (int j = 0; j < list.size(); ++j) {
            values.add(new PointValue(j, list.get(j)));
        }
        Line line = new Line(values);
        line.setColor(mLineColor != 0 ? mLineColor : COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        lines.add(line);
        LineChartData data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(mDiscRank.getName() + " " + mRes.getString(R.string.five_hours_situation));
                axisY.setName(mRes.getString(R.string.amazon_rank));
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        return data;
    }

    /**
     * 系列线图数据生成
     *
     * @return
     */
    private LineChartData generateLinesData() {
        List<Line> lines = new ArrayList<>();
        int i = 0;
        for (DiscRank rank : mGroupList) {
            List<Integer> list = getDiscRankValueList(rank);
            if (Tools.getMaxNum(list) == 0) {
                mSuperFishList.add(rank);
                addAnnotation(String.format("未显示：%s super杂鱼", rank.getName()), COLORS[i % COLORS.length]);
                continue;
            }
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < list.size(); ++j) {
                values.add(new PointValue(j, list.get(j)));
            }
            Line line = new Line(values);
            int color = COLORS[i % COLORS.length];
            line.setColor(color);
            line.setShape(shape);
            line.setCubic(true);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(true);
            lines.add(line);
            //添加line annotation
            addAnnotation(rank.getName(), color);
            i++;
        }
        LineChartData data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(BangumiNameService.newInstance(mGroupList.get(0).getName())
                        .getTrueName() + mRes.getString(R.string.series) + " " + mRes.getString(R.string.five_hours_situation));
                axisY.setName(mRes.getString(R.string.amazon_rank));
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        return data;
    }

    public void addAnnotation(String text, int color) {
        LineAnnotationView view = new LineAnnotationView(mContext);
        view.setText(text);
        view.setColor(color);
        view.setTextColor(mRes.getColor(R.color.text_translucent_dark));
        view.setTextSize(Tools.dp2px(12));
        mAnnotationContainer.addView(view);
        view.post(() -> view.setPadding(0, Tools.dp2px(mAnnotationPadding), 0, 0));
    }

    public void setLineColor(int color) {
        if (mLineChartView != null)
            mLineChartView.post(() -> {
                mLineChartView.getLineChartData().getLines().get(0).setColor(color);
                mLineChartView.invalidate();
            });
        else
            mLineColor = color;
    }
}
