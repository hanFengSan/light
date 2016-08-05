package com.yakami.light.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yakami.light.R;

/**
 * Created by Yakami on 2016/6/14, enjoying it!
 */

public class TagView extends RelativeLayout {

    private TextView tv_tag;
    private RightTriangle triangle;

    private String tag;
    private int textSize;
    private int bgColor;
    private int textColor;

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        textSize = ta.getDimensionPixelSize(R.styleable.TagView_textSize, textSize);
        tag = ta.getText(R.styleable.TagView_tagText).toString();
        bgColor = ta.getColor(R.styleable.TagView_bgColor, 0);
        textColor = ta.getColor(R.styleable.TagView_textColor, 0);
        setText(tag);
        if (textSize != 0)
            tv_tag.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setBgColor(bgColor);
        setTextColor(textColor);
        ta.recycle();
    }

    public TagView(Context context) {
        super(context);
        initView(context);
    }

    public void setBgColor(int color) {
        triangle.setColor(color);
    }

    public void setText(String text) {
        tv_tag.setText(text);
    }

    public void setTextColor(int color) {
        tv_tag.setTextColor(color);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_tag, this);
        tv_tag = (TextView) findViewById(R.id.tag);
        triangle = (RightTriangle) findViewById(R.id.triangle);
    }

}
