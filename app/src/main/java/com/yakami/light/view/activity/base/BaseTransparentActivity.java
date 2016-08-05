package com.yakami.light.view.activity.base;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yakami.light.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Yakami, Created on 2016/4/16
 */
public abstract class BaseTransparentActivity extends BaseActivity {

    @Bind(R.id.status_bar_bg) protected LinearLayout mStatusBarBg;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        ButterKnife.bind(this);

      //设置toolbar、status bar的背景都为透明
        mToolbar.getBackground().mutate().setAlpha(0);
        mStatusBarBg.getBackground().mutate().setAlpha(0);

        //设置status bar背景view的高度, 设置status bar为全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusBarBg.getLayoutParams();
            params.height = getStatusBarHeight();
            mStatusBarBg.setLayoutParams(params);
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN);
        }
    }
}
