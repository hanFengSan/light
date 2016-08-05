package com.yakami.light.service.base;

import android.content.Context;
import android.content.res.Resources;

import com.yakami.light.AppManager;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 */

public class BaseService {

    public BaseService() {

    }

    public Context getContext() {
        return AppManager.getContext();
    }

    public Resources getRes() {
        return AppManager.getRes();
    }
}
