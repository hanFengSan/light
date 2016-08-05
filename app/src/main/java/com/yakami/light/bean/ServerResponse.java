package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

/**
 * Created by Yakami on 2016/8/3, enjoying it!
 */

public class ServerResponse extends Entity {

    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}