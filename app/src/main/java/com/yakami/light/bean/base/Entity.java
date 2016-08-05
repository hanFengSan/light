package com.yakami.light.bean.base;

import java.io.Serializable;

/**
 * @author Yakami, Created on 2016/4/10
 */
public class Entity implements Serializable {

    protected static final long serialVersionUID = 1L;

    private int _id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


}
