package com.yakami.light.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Yakami, Created on 2016/5/1
 *         auto margin top with a status bar height, only available at api > 21
 */
public class MarginStatusBarLinearLayout extends LinearLayout {

    private boolean mMarginSet = false;

    public MarginStatusBarLinearLayout(Context context) {
        super(context);
    }

    public MarginStatusBarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarginStatusBarLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMargin();

    }

    /**
     * 动态设置margin_top
     */
    protected void setMargin() {
        if (!mMarginSet && getLayoutParams() instanceof ViewGroup.MarginLayoutParams && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin + getStatusBarHeight(), params.rightMargin, params.bottomMargin);
            this.setLayoutParams(params);
            mMarginSet = true;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
