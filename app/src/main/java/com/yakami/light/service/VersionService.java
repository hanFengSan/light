package com.yakami.light.service;

import com.yakami.light.BuildConfig;
import com.yakami.light.ServerAPI;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.service.base.BaseService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yakami on 2016/8/5, enjoying it!
 * 版本更新判断
 */

public class VersionService extends BaseService {

    private static VersionService mInstance = new VersionService();

    private VersionService() {
    }

    public static VersionService getInstance() {
        return mInstance;
    }

    public void checkVersionUpdate() {
        ServerAPI.getLightAPI()
                .checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(version -> {
                    float latestVersion = Float.valueOf(version.getVersion());
                    float currentVersion = Float.valueOf(BuildConfig.VERSION_NAME);
                    if (latestVersion > currentVersion){
                        Event event = new Event();
                        event.type = Event.EventType.VERSION_DIALOG;
                        event.message = version;
                        RxBus.getInstance().send(event);
                    }
                }, Throwable::printStackTrace);
    }

}
