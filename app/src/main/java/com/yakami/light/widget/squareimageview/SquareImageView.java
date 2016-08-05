package com.yakami.light.widget.squareimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.joooonho.SelectableRoundedImageView;

/**
 * @author Yakami, Created on 2016/4/15
 */
public class SquareImageView extends SelectableRoundedImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }
}
