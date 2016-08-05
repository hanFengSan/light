package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

import java.util.Date;

/**
 * Created by Yakami on 2016/8/4, enjoying it!
 */

public class Message extends Entity {

    private String userName;
    private String msg;
    private Date date;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
