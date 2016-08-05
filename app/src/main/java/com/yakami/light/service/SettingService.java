package com.yakami.light.service;

import com.yakami.light.AppManager;
import com.yakami.light.bean.SettingProfile;
import com.yakami.light.model.CacheManager;
import com.yakami.light.service.base.BaseService;

import static com.yakami.light.bean.SettingProfile.CACHE_NAME;

/**
 * Created by Yakami on 2016/8/2, enjoying it!
 */

public class SettingService extends BaseService {

    private SettingProfile mProfile;

    public static final int VIBRATE = 0;
    public static final int SOUND = 1;
    public static final int LIGHT = 2;
    public static final int BLOCK = 3;
    public static final int NIGHT_MODE = 4;
    public static final int SINGLE = 5;
    public static final int NOT_DISTURB = 6;

    public SettingService() {
        if (CacheManager.isExist4DataCache(getContext(), CACHE_NAME)) {
            mProfile = CacheManager.readObject(getContext(), CACHE_NAME);
            if (mProfile == null)
                mProfile = new SettingProfile();
        } else
            mProfile = new SettingProfile();
    }

    public SettingProfile getProfile() {
        return mProfile;
    }

    public void save() {
        CacheManager.saveObject(AppManager.getContext(), mProfile, CACHE_NAME);
    }

    public void clear() {
        mProfile = new SettingProfile();
        save();
    }

    public void setItemValue(int item, boolean isChecked) {
        switch (item) {
            case VIBRATE:
                mProfile.setHasVibrate(isChecked);
                break;
            case SOUND:
                mProfile.setHasSound(isChecked);
                break;
            case LIGHT:
                mProfile.setHasLight(isChecked);
                break;
            case SINGLE:
                mProfile.setSingleNotification(isChecked);
                break;
            case BLOCK:
                mProfile.setBlocking(isChecked);
                break;
            case NIGHT_MODE:
                mProfile.setNightMode(isChecked);
                break;
            case NOT_DISTURB:
                mProfile.setNotDisturb(isChecked);
                break;
        }
        save();
    }
}
