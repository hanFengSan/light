package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yakami on 2016/6/6, enjoying it!
 */

public class TimeRankContainer extends Entity {

    private int id;
    private String sName; //简称
    private Date time;
    private String name;
    private List<DiscRank> discs;

    public TimeRankContainer() {
    }

    public TimeRankContainer(RawTimeRankContainer container) {
        id = container.getId();
        sName = container.getName();
        time = new Date(container.getTime());
        name = container.getTitle();
        discs = new ArrayList<>();
        for (RawDiscRank item : container.getDiscs()) {
            discs.add(new DiscRank(item));
        }
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DiscRank> getDiscs() {
        return discs;
    }

    public void setDiscs(List<DiscRank> discs) {
        this.discs = discs;
    }
}
