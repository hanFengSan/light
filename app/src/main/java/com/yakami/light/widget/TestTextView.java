package com.yakami.light.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yakami.light.R;

/**
 * Created by Yakami on 2016/6/11, enjoying it!
 */

public class TestTextView extends TextView {

    private Paint mPaint;
    private Paint mPaint1;
    private Paint mPaint2;
    private int mViewWidth;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate;

    public TestTextView(Context context) {
        super(context);
        iniPaint();
    }

    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniPaint();
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        iniPaint();
    }

    protected void iniPaint() {
        mPaint1 = new Paint();
        mPaint1.setColor(getResources().getColor(R.color.PeterRiver));
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2 = new Paint();
        mPaint2.setColor(Color.YELLOW);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(
                        0,
                        0,
                        mViewWidth,
                        0,
                        new int[]{Color.BLUE, 0xffffff, Color.BLUE},
                        null,
                        Shader.TileMode.CLAMP
                );
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint1);
//        canvas.drawRect(10, 10, getMeasuredWidth() - 10, getMeasuredHeight() - 10, mPaint2);
//        canvas.save();
//        canvas.translate(10, 0);
        super.onDraw(canvas);
        canvas.drawRect(0, 0, 0, 0, mPaint);

//        canvas.restore();
        if (mGradientMatrix != null) {
            mTranslate += mViewWidth / 5;
            if (mTranslate > 2* mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }
}
