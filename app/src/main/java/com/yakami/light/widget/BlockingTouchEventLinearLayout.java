package com.yakami.light.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Yakami on 2016/8/3, enjoying it!
 */

public class BlockingTouchEventLinearLayout extends LinearLayout {

    public BlockingTouchEventLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BlockingTouchEventLinearLayout(Context context) {
        super(context);
    }

    public BlockingTouchEventLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return true;
    }
}
