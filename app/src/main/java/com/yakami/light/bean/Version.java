package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

/**
 * Created by Yakami on 2016/8/5, enjoying it!
 */

public class Version extends Entity {

    private String version;
    private String intro;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
