package com.yakami.light.view.activity.base;

import android.os.Handler;
import android.widget.Toast;

import com.yakami.light.R;

/**
 * Created by Yakami on 2016/7/28, enjoying it!
 * 提供一个双击后退键退出功能
 */

public abstract class BaseTransTabMainActivity extends BaseTransparentTabActivity {

    private boolean mIsBacking;
    private Toast mBackingToast;

    @Override
    public void onBackPressed() {
        if (mIsBacking) {
            if (mBackingToast != null)
                mBackingToast.cancel();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            mIsBacking = true;
            mBackingToast = Toast.makeText(this, mRes.getString(R.string.click_again_to_exit), Toast.LENGTH_LONG);
            mBackingToast.show();
            new Handler().postDelayed(() -> {
                mIsBacking = false;
                if (mBackingToast != null)
                    mBackingToast.cancel();
            }, 2000);
        }
    }

}
