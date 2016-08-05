package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.ArrayList;

/**
 * Created by Yakami on 2016/7/28, enjoying it!
 */

public class NotificationProfile extends Entity {

    public static final String CACHE_NAME = "NOTIFICATION_PROFILE";
    public static final int FISH_LINE = 500; //杂鱼线
    public static final float FLY_SCALE = 0.75f; //爆上比例
    public static final float DIVE_SCALE = 1.25f; //跳水比例
    public static final float TOP = 1; //登顶

    private ArrayList<NotificationItem> mList = new ArrayList<>();

    public static final int SWITCH_UPDATE = 0;
    public static final int SWITCH_FLY = 1;
    public static final int SWITCH_DIVE = 2;
    public static final int SWITCH_IGNORE_FISH = 3;
    public static final int SWITCH_TOP = 4;
    public static final int SWITCH_WATCH = 5;


    public ArrayList<NotificationItem> getList() {
        return mList;
    }

    public void setList(ArrayList<NotificationItem> list) {
        this.mList = list;
    }
}
