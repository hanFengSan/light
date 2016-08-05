package com.yakami.light.bean;

import com.yakami.light.bean.base.Entity;

/**
 * Created by Yakami on 2016/7/29, enjoying it!
 * 用于查询结果
 */

public class DiscNotificationResult extends Entity {

    private DiscRank discRank;
    private NotificationItem notificationItem;

    public DiscNotificationResult(DiscRank discRank, NotificationItem notificationItem) {
        this.discRank = discRank;
        this.notificationItem = notificationItem;
    }

    public DiscRank getDiscRank() {
        return discRank;
    }

    public void setDiscRank(DiscRank discRank) {
        this.discRank = discRank;
    }

    public NotificationItem getNotificationItem() {
        return notificationItem;
    }

    public void setNotificationItem(NotificationItem notificationItem) {
        this.notificationItem = notificationItem;
    }
}
