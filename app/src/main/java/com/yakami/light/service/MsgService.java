package com.yakami.light.service;

import com.yakami.light.bean.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakami on 2016/8/4, enjoying it!
 * 开发搁浅，个推有日推送 数量限制
 */
public class MsgService {

    private List<Message> messageList = new ArrayList<>();

    private static MsgService ourInstance = new MsgService();

    public static MsgService getInstance() {
        return ourInstance;
    }

    private MsgService() {
    }

    

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
