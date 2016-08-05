package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

/**
 * Created by Yakami on 2016/7/28, enjoying it!
 */

public class NotificationItem extends Entity {

    private int id;
    private String sName;

    private boolean isUpdate;
    private boolean isFly;
    private boolean isDive;
    private boolean isIgnoreFish;
    private boolean isTop;
    private boolean isWatched;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        this.isUpdate = update;
    }

    public boolean isFly() {
        return isFly;
    }

    public void setFly(boolean fly) {
        this.isFly = fly;
    }

    public boolean isDive() {
        return isDive;
    }

    public void setDive(boolean dive) {
        this.isDive = dive;
    }

    public boolean isIgnoreFish() {
        return isIgnoreFish;
    }

    public void setIgnoreFish(boolean ignoreFish) {
        this.isIgnoreFish = ignoreFish;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        this.isTop = top;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}
