package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.ArrayList;

/**
 * Created by Yakami on 2016/7/28, enjoying it!
 */

public class NotificationProfile extends Entity {

    public static final String CACHE_NAME = "NOTIFICATION_PROFILE";
    private int fishLine = 500; //杂鱼线
    private float flyScale = 0.25f; //爆上比例
    private float diveScale = 0.25f; //跳水比例
    private int top = 1; //登顶

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

    public int getFishLine() {
        return fishLine;
    }

    public void setFishLine(int fishLine) {
        this.fishLine = fishLine;
    }

    public float getFlyScale() {
        return flyScale;
    }

    public void setFlyScale(float flyScale) {
        this.flyScale = flyScale;
    }

    public float getDiveScale() {
        return diveScale;
    }

    public void setDiveScale(float diveScale) {
        this.diveScale = diveScale;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
