package com.yakami.light.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.yakami.light.R;
import com.yakami.light.utils.IntentHelper;
import com.yakami.light.utils.Tools;
import com.yakami.light.view.activity.base.BaseHoldBackTabActivity;

/**
 * 使用例子：
 * Created by Yakami on 2016/5/21, enjoying it!
*                     startActivity(IntentHelper.newInstance(mContext, SingleLoadFragmentActivity.class)
                                .putString("class", UserInfoFragment.class.toString())
                                .putString("title", mRes.getString(R.string.user_info))
                                .toIntent());
 */
public class SingleFragmentActivity extends BaseHoldBackTabActivity {

    private Fragment mFragment;
    private Class<Fragment> mFragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        IntentHelper helper = getIntentHelper();
        mFragmentClass = (Class<Fragment>) Tools.ClassForClearName(helper.getString("class"));
        mTitle.setText(helper.getString("title"));
        initTabs(savedInstanceBundle != null);
    }

    @Override
    protected void initTabs(boolean isRestarted) {
        super.initTabs(isRestarted);
        try {
            mFragment = mFragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_content, mFragment, "SINGLE_FRAGMENT").commit();
    }


    @Override
    protected int onBindLayout() {
        return R.layout.activity_single_fragment;
    }
}
