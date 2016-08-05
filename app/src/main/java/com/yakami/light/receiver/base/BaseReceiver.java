package com.yakami.light.receiver.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.res.Resources;

import com.yakami.light.AppManager;

/**
 * Created by Yakami on 2016/7/30, enjoying it!
 */

public abstract class BaseReceiver extends BroadcastReceiver {

    protected Resources mRes;
    protected Context mContext;

    public BaseReceiver() {
        mRes = AppManager.getRes();
        mContext = AppManager.getContext();
    }
}
