package com.yakami.light.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Yakami on 2016/5/21, enjoying it!
 * 简化携带一些数据的activity跳转
 */
public class IntentHelper {

    private Intent mIntent;
    private Bundle mBundle = new Bundle();

    private IntentHelper(Context context, Class<?> cls) {
        mIntent = new Intent(context, cls);
    }

    private IntentHelper(Intent intent) {
        mIntent = intent;
        mBundle = intent.getBundleExtra("data");
    }

    public static IntentHelper newInstance(Context context, Class<?> cls) {
        return  new IntentHelper(context, cls);
    }

    public static IntentHelper newInstance(Intent intent) {
        return new IntentHelper(intent);
    }

    public IntentHelper putString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public String getString(String key) {
        return mBundle.getString(key);
    }

    public IntentHelper putInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public int getInt(String key, int defaultValue) {
        return mBundle.getInt(key, defaultValue);
    }

    public IntentHelper putBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        return mBundle.getBoolean(key);
    }

    public IntentHelper putSerializable(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public Serializable getSerializable(String key) {
        return mBundle.getSerializable(key);
    }

    public Intent toIntent() {
        mIntent.putExtra("data", mBundle);
        return mIntent;
    }


}
