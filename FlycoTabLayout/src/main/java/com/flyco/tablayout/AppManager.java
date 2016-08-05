package com.flyco.tablayout;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppManager extends Application {

    public static Context context;
    public static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resources = getResources();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
