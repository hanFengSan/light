package com.yakami.light.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yakami on 2016/6/14, enjoying it!
 */

public class RightTriangle extends View {

    private Paint mPaint;
    private int mColor = 0x3498db;

    public RightTriangle(Context context) {
        super(context);
        iniPaint();
    }

    public RightTriangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniPaint();
    }

    public RightTriangle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        iniPaint();
    }

    public void setColor(int color) {
        mColor = color;
    }
    protected void iniPaint() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawPaint(mPaint);

        mPaint.setStrokeWidth(4);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);


        Point a = new Point(0, 0);
        Point b = new Point(0, getMeasuredHeight());
        Point c = new Point(getMeasuredWidth(), 0);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, mPaint);
    }
}
