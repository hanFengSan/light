package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;
import com.yakami.light.utils.Tools;

/**
 * Created by Yakami on 2016/5/23, enjoying it!
 */
public class BangumiRank extends Entity {

    private int id;
    private String serialNum; //序列排名
    private String sName; //简化名称
    private String name; //全名称
    private String rank;
    private String situation;

    private String PT;
    private String lastUpdate; //距离上次更新
    private String nicoOrder;
    private String amazonRank;
    private String saleDay; //距离发售天数
    private String type;

    public void initType() {
        if (name != null) {
            if (name.contains("Blu-ray")) {
                addTypeItem("BD");
            }
            if (name.contains("DVD")) {
                addTypeItem("DVD");
            }
            if (name.contains("尼限")) {
                addTypeItem("尼限");
            } else {
                addTypeItem("非尼");
            }
            if (name.contains("BOX")) {
                addTypeItem("BOX");
            }
        }
    }

    private void addTypeItem(String item) {
        if (Tools.isEmpty(type)) {
            type = item;
        } else
            type += "、" + item;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPT() {
        return PT;
    }

    public void setPT(String PT) {
        this.PT = PT;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getNicoOrder() {
        return nicoOrder;
    }

    public void setNicoOrder(String nicoOrder) {
        this.nicoOrder = nicoOrder;
    }

    public String getAmazonRank() {
        return amazonRank;
    }

    public void setAmazonRank(String amazonRank) {
        this.amazonRank = amazonRank;
    }

    public String getSaleDay() {
        return saleDay;
    }

    public void setSaleDay(String saleDay) {
        this.saleDay = saleDay + "天";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
