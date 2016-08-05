package com.yakami.light.view.activity.base;

import android.os.Bundle;

import com.yakami.light.DeviceManager;

/**
 * @author Yakami, Created on 2016/4/10
 */
public abstract class BaseHoldBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> {
            //关闭可能存在的软键盘
            DeviceManager.hideSoftInput(mContext, getCurrentFocus());
            finish();
        });
    }
}
