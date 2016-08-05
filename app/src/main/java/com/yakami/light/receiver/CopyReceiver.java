package com.yakami.light.receiver;

import android.content.Context;
import android.content.Intent;

import com.yakami.light.DeviceManager;
import com.yakami.light.R;
import com.yakami.light.receiver.base.BaseReceiver;
import com.yakami.light.utils.Tools;

/**
 * Created by Yakami on 2016/7/30, enjoying it!
 * 用于通知栏复制按钮使用
 */

public class CopyReceiver extends BaseReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DeviceManager.copyToClipboard(intent.getStringExtra("content"));
        Tools.toast(mRes.getString(R.string.copy_completed));
    }
}
