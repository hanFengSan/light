package com.yakami.light.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yakami.light.AppManager;

/**
 * SharePreference管理类, oop管理众多的首选项文件
 *
 * Created by thanatos on 16/2/2.
 */
public class SharePreferenceManager {

    public static SharedPreferences getUser() {
        return AppManager.getContext().getSharedPreferences(User.FILE_NAME, Context.MODE_PRIVATE);
    }

    public static class User {

        public static final String FILE_NAME = "USER";

        public static final String KEY_USER_NAME = "KEY_USER_NAME";
        public static final String KEY_PASSWORD = "KEY_PASSWORD";
        public static final String KEY_USER_ID = "KEY_USER_ID";
        public static final String KEY_PORTRAIT_URL = "KEY_PORTRAIT_URL";
        public static final String KEY_COOKIES = "KEY_COOKIES";
    }

}
