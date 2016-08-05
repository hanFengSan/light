package com.yakami.light;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.squareup.leakcanary.LeakCanary;
import com.yakami.light.service.DiscRankService;
import com.yakami.light.service.NotificationService;
import com.yakami.light.service.SettingService;

public class AppManager extends Application {

    private static AppManager mApp;

    private DiscRankService mDiscRankService;
    private NotificationService mNotificationService;
    private SettingService mSettingService;

    private int mTabPos; //目前选择的position，用于读取前position

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //init leakCanary
        LeakCanary.install(this);
        //init service
        mDiscRankService = new DiscRankService();
        mNotificationService = new NotificationService();
        mSettingService = new SettingService();
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public static Resources getRes() {
        return mApp.getResources();
    }

    public static DiscRankService getDiscRankService() {
        return mApp.mDiscRankService;
    }

    public static NotificationService getNotificationService() {
        return mApp.mNotificationService;
    }

    public static SettingService getSettingService() {
        return mApp.mSettingService;
    }

    public static int getTabPos() {
        return mApp.mTabPos;
    }

    public static void setTabPos(int tabPos) {
        mApp.mTabPos = tabPos;
    }

    public static boolean isWatchedList() {
        if (mApp.mTabPos < mApp.getDiscRankService().getTimeRanksList().size())
            return false;
        return true;
    }
}
