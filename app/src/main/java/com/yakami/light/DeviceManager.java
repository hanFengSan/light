package com.yakami.light;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DeviceManager {

//    private static InputMethodManager mSoftInputManager;
    private static ConnectivityManager mCnnManager;

    public static InputMethodManager getSoftInputManager(Context context) {
//        if (mSoftInputManager == null)
//            mSoftInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static ConnectivityManager getCnnManager(Context context) {
        if (mCnnManager == null)
            mCnnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mCnnManager;
    }

    /**
     * 关闭软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        if (view == null) return;
        getSoftInputManager(context).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 检测是否有网络
     *
     * @return
     */
    public static boolean hasInternet(Context context) {
        return getCnnManager(context).getActiveNetworkInfo() != null && getCnnManager(context).getActiveNetworkInfo().isAvailable();
    }

    /**
     * 网络类型
     *
     * @return
     */
    public static int getInternetType(Context context) {
        return getCnnManager(context).getActiveNetworkInfo().getType();
    }

    public static void copyToClipboard(String str) {
        ClipboardManager clipboard = (ClipboardManager) AppManager.getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("排名", str);
        clipboard.setPrimaryClip(clip);
    }

}
