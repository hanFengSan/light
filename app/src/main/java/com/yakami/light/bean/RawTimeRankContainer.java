package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.List;

/**
 * Created by Yakami on 2016/6/6, enjoying it!
 */

public class RawTimeRankContainer extends Entity {

    private String name;
    private int id;
    private long time;
    private String title;
    private List<RawDiscRank> discs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RawDiscRank> getDiscs() {
        return discs;
    }

    public void setDiscs(List<RawDiscRank> discs) {
        this.discs = discs;
    }
}
