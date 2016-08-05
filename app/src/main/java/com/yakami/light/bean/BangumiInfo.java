package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 * 番剧对应的一些信息
 */

public class BangumiInfo extends Entity {

    private int id;
    private String name = "";
    private String cName = ""; //中文名
    private String intro = "";
    private List<String> infoBox = new ArrayList<>(); //其他信息
    private String coverAddress = "";
    private byte[] cover = {};
    private float version = 1.0f; //更新打补丁用
    private int dominantColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getInfoBox() {
        return infoBox;
    }

    public void setInfoBox(List<String> infoBox) {
        this.infoBox = infoBox;
    }

    public String getCoverAddress() {
        return coverAddress;
    }

    public void setCoverAddress(String coverAddress) {
        this.coverAddress = coverAddress;
    }

    public int getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(int dominantColor) {
        this.dominantColor = dominantColor;
    }
}
