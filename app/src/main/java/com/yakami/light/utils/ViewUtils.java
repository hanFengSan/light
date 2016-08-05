package com.yakami.light.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Yakami on 2016/7/31, enjoying it!
 */

public class ViewUtils {

    //ViewMargin
    public static final int MARGIN_TOP = 0;
    public static final int MARGIN_BOTTOM = 1;
    public static final int MARGIN_LEFT = 2;
    public static final int MARGIN_RIGHT = 3;
    public static final int MARGIN_START = 4;
    public static final int MARGIN_END = 5;

    /**
     * 修改view的高度，height默认单位为dp
     *
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        view.post(() -> {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = height;
            view.setLayoutParams(params);
        });
    }

    /**
     * 修改view的margin，默认单位为px
     *
     * @param view
     * @param type  {@link #MARGIN_TOP}
     * @param value
     */
    public static void setViewMargin(View view, int type, int value) {
        view.post(() -> {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            switch (type) {
                case MARGIN_TOP:
                    params.topMargin = value;
                    break;
                case MARGIN_BOTTOM:
                    params.bottomMargin = value;
                    break;
                case MARGIN_LEFT:
                    params.leftMargin = value;
                    break;
                case MARGIN_RIGHT:
                    params.rightMargin = value;
                    break;
                case MARGIN_START:
                    params.setMarginStart(value);
                    break;
                case MARGIN_END:
                    params.setMarginEnd(value);
                    break;
            }
            view.setLayoutParams(params);
        });
    }

    /**
     * 得到vertical and wrap_content 的 LinearLayout的真实高度
     *
     * @param layout
     * @return
     */
    public static int getVerticalLinearLayoutHeight(LinearLayout layout) {
        int count = layout.getChildCount();
        int result = 0;
        View v;
        for (int i = 0; i < count; i++) {
            v = layout.getChildAt(i);
            result += v.getHeight() + v.getPaddingTop() + v.getPaddingBottom();
        }
        return result;
    }
}
