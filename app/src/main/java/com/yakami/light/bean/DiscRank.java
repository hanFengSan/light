package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.Date;

/**
 * Created by Yakami on 2016/6/6, enjoying it!
 */

public class DiscRank extends Entity {

    private int id;
    private String name;
    private String sName;
    private Date updateDate;
    private int discType;
    private Date releaseDate;
    private int amazonRank;
    private int currentRank;
    private int preRank;
    private int preRank1;
    private int preRank2;
    private int preRank3;
    private int preRank4;
    private int preRank5;
    private int PT;
    private Date latestPullDate;
    private int nicoOrder; //nico预约
    private int saleDay; //距离销售天数

    public DiscRank(RawDiscRank rank) {
        id = rank.getId();
        name = rank.getTitle();
        sName = rank.getsName();
        updateDate = new Date(rank.getStot());
        discType = rank.getType();
        releaseDate = new Date(rank.getRelease());
        amazonRank = rank.getArnk();
        currentRank = rank.getCurk();
        preRank = rank.getPrrk();
        preRank1 = rank.getRank1();
        preRank2 = rank.getRank2();
        preRank3 = rank.getRank3();
        preRank4 = rank.getRank4();
        preRank5 = rank.getRank5();
        PT = rank.getCupt();
        latestPullDate = new Date(rank.getAtot());
        nicoOrder = rank.getCubk();
        saleDay = rank.getSday();
    }

    public DiscRank() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getDiscType() {
        return discType;
    }

    public void setDiscType(int discType) {
        this.discType = discType;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getAmazonRank() {
        return amazonRank;
    }

    public void setAmazonRank(int amazonRank) {
        this.amazonRank = amazonRank;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public int getPreRank() {
        return preRank;
    }

    public void setPreRank(int preRank) {
        this.preRank = preRank;
    }

    public int getPreRank1() {
        return preRank1;
    }

    public void setPreRank1(int preRank1) {
        this.preRank1 = preRank1;
    }

    public int getPreRank2() {
        return preRank2;
    }

    public void setPreRank2(int preRank2) {
        this.preRank2 = preRank2;
    }

    public int getPreRank3() {
        return preRank3;
    }

    public void setPreRank3(int preRank3) {
        this.preRank3 = preRank3;
    }

    public int getPreRank4() {
        return preRank4;
    }

    public void setPreRank4(int preRank4) {
        this.preRank4 = preRank4;
    }

    public int getPreRank5() {
        return preRank5;
    }

    public void setPreRank5(int preRank5) {
        this.preRank5 = preRank5;
    }

    public int getPT() {
        return PT;
    }

    public void setPT(int PT) {
        this.PT = PT;
    }

    public Date getLatestPullDate() {
        return latestPullDate;
    }

    public void setLatestPullDate(Date latestPullDate) {
        this.latestPullDate = latestPullDate;
    }

    public int getNicoOrder() {
        return nicoOrder;
    }

    public void setNicoOrder(int nicoOrder) {
        this.nicoOrder = nicoOrder;
    }

    public int getSaleDay() {
        return saleDay;
    }

    public void setSaleDay(int saleDay) {
        this.saleDay = saleDay;
    }
}
