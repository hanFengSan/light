package com.yakami.light.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.yakami.light.R;
import com.yakami.light.utils.IntentHelper;
import com.yakami.light.utils.Tools;
import com.yakami.light.view.activity.base.BaseHoldBackTabActivity;
import com.yakami.light.view.fragment.LoadFragment;
import com.yakami.light.view.fragment.base.BaseFragment;

/**
 * Created by Yakami on 2016/5/21, enjoying it!
 */
public class SingleLoadFragmentActivity extends BaseHoldBackTabActivity {

    private BaseFragment mFragment;
    private LoadFragment mLoadFragment;
    private Class<BaseFragment> mFragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        IntentHelper helper = getIntentHelper();
        mFragmentClass = (Class<BaseFragment>) Tools.ClassForClearName(helper.getString("class"));
        mTitle.setText(helper.getString("title"));
        initTabs(savedInstanceBundle != null);
    }

    @Override
    protected void initTabs(boolean isRestarted) {
        super.initTabs(isRestarted);
        try {
            mFragment = mFragmentClass.newInstance();
            mLoadFragment = new LoadFragment();
            mLoadFragment.setFragment(mFragment);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_content, mLoadFragment, "SINGLE_LOAD_FRAGMENT").commit();
    }


    @Override
    protected int onBindLayout() {
        return R.layout.activity_single_fragment;
    }

}
