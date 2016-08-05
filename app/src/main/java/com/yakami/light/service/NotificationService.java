package com.yakami.light.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;

import com.yakami.light.AppManager;
import com.yakami.light.R;
import com.yakami.light.bean.DiscNotificationResult;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.bean.NotificationItem;
import com.yakami.light.bean.NotificationProfile;
import com.yakami.light.bean.SettingProfile;
import com.yakami.light.model.CacheManager;
import com.yakami.light.receiver.CopyReceiver;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.IntentHelper;
import com.yakami.light.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.yakami.light.bean.NotificationProfile.CACHE_NAME;
import static com.yakami.light.bean.NotificationProfile.FISH_LINE;
import static com.yakami.light.bean.NotificationProfile.SWITCH_DIVE;
import static com.yakami.light.bean.NotificationProfile.SWITCH_FLY;
import static com.yakami.light.bean.NotificationProfile.SWITCH_IGNORE_FISH;
import static com.yakami.light.bean.NotificationProfile.SWITCH_TOP;
import static com.yakami.light.bean.NotificationProfile.SWITCH_UPDATE;
import static com.yakami.light.bean.NotificationProfile.SWITCH_WATCH;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 * 推送相关设置和提交推送
 */

public class NotificationService extends BaseService {

    private NotificationProfile mProfile;
    private static int mNotifyID = 1;

    public NotificationService() {
        if (CacheManager.isExist4DataCache(getContext(), CACHE_NAME)) {
            mProfile = CacheManager.readObject(getContext(), CACHE_NAME);
            if (mProfile == null)
                mProfile = new NotificationProfile();
        } else
            mProfile = new NotificationProfile();
    }

    /**
     * 移除所有推送和关注
     */
    public void removeAllNotification() {
        mProfile = new NotificationProfile();
        save();
    }

    public ArrayList<NotificationItem> getList() {
        return mProfile.getList();
    }

    public List<DiscNotificationResult> getAliveList(ArrayList<DiscRank> discList) {
        ArrayList<NotificationItem> list = (ArrayList<NotificationItem>) mProfile.getList().clone();
        List<DiscNotificationResult> result = new ArrayList<>();
        for (DiscRank discItem : discList) {
            for (NotificationItem tmp : list) {
                if (discItem.getId() == tmp.getId()) {
                    if (tmp.isUpdate() || tmp.isFly() || tmp.isDive() || tmp.isTop() || tmp.isWatched()) {
                        if (!(tmp.isIgnoreFish() && discItem.getCurrentRank() > FISH_LINE)) {
                            result.add(new DiscNotificationResult(discItem, tmp));
                        }
                        list.remove(tmp);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void save() {
        CacheManager.saveObject(AppManager.getContext(), mProfile, CACHE_NAME);
    }

    public void setItemValue(int id, int switchId, boolean isChecked) {
        NotificationItem item = new NotificationItem();

        for (Iterator it = mProfile.getList().iterator(); it.hasNext(); ) {
            NotificationItem tmp = (NotificationItem) it.next();
            if (tmp.getId() == id) {
                item = tmp;
                it.remove();
            }
        }
        item.setId(id);
        switch (switchId) {
            case SWITCH_UPDATE:
                item.setUpdate(isChecked);
                break;
            case SWITCH_FLY:
                item.setFly(isChecked);
                break;
            case SWITCH_DIVE:
                item.setDive(isChecked);
                break;
            case SWITCH_IGNORE_FISH:
                item.setIgnoreFish(isChecked);
                break;
            case SWITCH_TOP:
                item.setTop(isChecked);
                break;
            case SWITCH_WATCH:
                item.setWatched(isChecked);
                break;
        }
        mProfile.getList().add(item);
        save();
    }

    public void pushNotification(NotificationText text) {
        final Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(); // 建立BigTextStyle
        bigTextStyle.setBigContentTitle(text.getTitle()); // 當BigTextStyle顯示時，用BigTextStyle的setBigContentTitle覆蓋setContentTitle的設定
        bigTextStyle.bigText(text.getContent()); // 設定BigTextStyle的文字內容

        final int notifyID = 1; // 通知的識別號碼

        final Intent openIntent = new Intent(AppManager.getContext(), MainActivity.class); // 目前Activity的Intent
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final int flags = PendingIntent.FLAG_CANCEL_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
        final PendingIntent pendingIntent = PendingIntent.getActivity(AppManager.getContext(), notifyID, openIntent, flags); // 取得PendingIntent

        final IntentHelper intentHelper = IntentHelper.newInstance(AppManager.getContext(), CopyReceiver.class);
        final Intent copyIntent = new Intent(AppManager.getContext(), CopyReceiver.class);
        copyIntent.putExtra("content", String.format("%s\n%s", text.getTitle(), text.getContent()));
        final PendingIntent copyPIntent = PendingIntent.getBroadcast(getContext(), 0, copyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationManager notificationManager = (NotificationManager) AppManager.getContext().getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        Notification.Builder builder = new Notification.Builder(AppManager.getContext())
                .setSmallIcon(R.drawable.jpush_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(AppManager.getRes(), R.mipmap.ic_launcher_light))
                .setContentTitle(text.getTitle())
                .setContentText(text.getContent())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(R.drawable.ic_content_copy_black_24dp, getRes().getString(R.string.copy), copyPIntent);
        SettingProfile setting = AppManager.getSettingService().getProfile();
        if (setting.isHasSound())
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        if (setting.isHasLight())
            builder.setLights(Color.GREEN, 1000, 2000);
        final Notification notification = builder.build();
        if (setting.isHasVibrate())
            notification.defaults = Notification.DEFAULT_VIBRATE;
        if (setting.isSingleNotification())
            notificationManager.notify(notifyID, notification);
        else {
            notificationManager.notify(mNotifyID, notification);
            mNotifyID++;
        }
    }

    public static class NotificationText {

        private String title;
        private String content;

        public NotificationText() {
        }

        public NotificationText(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
