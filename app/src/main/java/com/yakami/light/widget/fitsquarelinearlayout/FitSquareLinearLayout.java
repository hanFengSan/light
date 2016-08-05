package com.yakami.light.widget.fitsquarelinearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Yakami, Created on 2016/4/15
 */
public class FitSquareLinearLayout extends LinearLayout {


    public FitSquareLinearLayout(Context context) {
        super(context);
    }

    public FitSquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitSquareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildCount();

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.width = width - height;
//        setLayoutParams(params);
        setMeasuredDimension(width - height, height);
        resizeChildren(this, width - height, height);

    }

    private void resizeChildren(ViewGroup parent, int newWidth, int newHeight) {
        // size is in pixels so make sure you have taken device display density into account
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View v = parent.getChildAt(i);
            if (v instanceof ViewGroup) {
                v.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
            }
        }
    }
}
