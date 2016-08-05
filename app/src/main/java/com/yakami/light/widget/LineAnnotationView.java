package com.yakami.light.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yakami.light.R;
import com.yakami.light.widget.squareimageview.SquareImageView;

/**
 * Created by Yakami on 2016/8/2, enjoying it!
 */

public class LineAnnotationView extends LinearLayout {

    private SquareImageView mImageView;
    private TextView mTextView;
    private TextView mSpaceView;

    private String text;
    private int color;
    private int textSize;
    private int textColor;

    public LineAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public LineAnnotationView(Context context) {
        super(context);
        initView(context);
    }

    public LineAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineAnnotationView);
        textSize = ta.getDimensionPixelSize(R.styleable.LineAnnotationView_LATextSize, textSize);
        text = ta.getText(R.styleable.LineAnnotationView_text).toString();
        color = ta.getColor(R.styleable.LineAnnotationView_imageColor, 0);
        textColor = ta.getColor(R.styleable.LineAnnotationView_LATextColor, 0);

        setText(text);
        setTextSize(textSize);
        setColor(color);
        setTextColor(textColor);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_line_annotation, this);
        mImageView = (SquareImageView) findViewById(R.id.square_image_view);
        mTextView = (TextView) findViewById(R.id.text_view);
        mSpaceView = (TextView) findViewById(R.id.tv_space);
    }

    public SquareImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(SquareImageView mImageView) {
        this.mImageView = mImageView;
    }

    public TextView getmTextView() {
        return mTextView;
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        mTextView.setText(text);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        mImageView.setBackgroundColor(color);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        if (textSize != 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mSpaceView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mTextView.setTextColor(textColor);
    }
}
