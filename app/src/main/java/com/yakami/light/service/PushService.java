package com.yakami.light.service;

import com.google.gson.Gson;
import com.yakami.light.AppManager;
import com.yakami.light.ServerAPI;
import com.yakami.light.bean.CommonMsg;
import com.yakami.light.bean.DiscNotificationResult;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.bean.NotificationItem;
import com.yakami.light.bean.NotificationProfile;
import com.yakami.light.bean.ServerResponse;
import com.yakami.light.bean.Version;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.NumFlagBitUtils;
import com.yakami.light.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.yakami.light.service.PushService.TitleHelper.TYPE_TOP;

/**
 * Created by Yakami on 2016/8/4, enjoying it!
 * 处理服务器发来的消息
 */

public class PushService extends BaseService {

    private static PushService mInstance = new PushService();

    private String mContent = "";

    private PushService() {
    }

    public static PushService getInstance() {
        return mInstance;
    }

    public static void setTag(List<String> tags) {

    }

    public void with(ServerResponse response) {
        Observable.just(response.getMessage())
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe(msg -> {
                    switch (msg) {
                        case "update":
                            if (!AppManager.getSettingService().getProfile().isBlocking())
                                if (!notDisturb())
                                    commonUpdate(response);
                            break;
                        case "all_update":
                            if (!AppManager.getSettingService().getProfile().isBlocking())
                                if (!notDisturb())
                                    updateAll();
                            break;
                        case "common_msg":
                            sendCommonMsg(response.getData());
                            break;
                        case "app_update":
                            sendAppUpdate(response.getData());
                            break;
                    }
                }, Throwable::printStackTrace);
    }

    private void sendAppUpdate(String str) {
        Gson gson = new Gson();
        Version ver = gson.fromJson(str, Version.class);
        VersionService.getInstance().setVersion(ver);
        AppManager.getNotificationService().pushNotification(new NotificationService
                .NotificationText("可用更新：v" + ver.getVersion(), ver.getIntro()), false);
    }

    private void sendCommonMsg(String str) {
        Gson gson = new Gson();
        CommonMsg msg = gson.fromJson(str, CommonMsg.class);
        AppManager.getNotificationService().pushNotification(new NotificationService
                .NotificationText(msg.getTitle(), msg.getContent()), false);
    }


    protected boolean notDisturb() {
        if (AppManager.getSettingService().getProfile().isNotDisturb())
            return Tools.isInHourInterval(0, 8);
        return false;
    }

    /**
     * 由于个推的用户Tag设置有时间限制，貌似是一个小时还是一天才能设置一次，遂采用标志位形式，例如某项的id为3，则
     * 若是服务器发来了0001便会匹配成功，启动刷新
     *
     * @param data
     * @return
     */
    private List<NotificationItem> getNotificationItemList(String data) {
        List<NotificationItem> result = new ArrayList<>();
        List<Integer> idList = NumFlagBitUtils.toIntList(data);
        for (int id : idList) {
            for (NotificationItem item : AppManager.getNotificationService().getList()) {
                if (item.getId() == id)
                    result.add(item);
            }
        }
        return result;
    }

    private void commonUpdate(ServerResponse response) {
        if (getNotificationItemList(response.getData()).size() > 0)
            getNotificationTextAndPush();
    }

    private void updateAll() {
        getNotificationTextAndPush();
    }

    /**
     * 内容修改在mContent中
     *
     * @param aliveList
     * @return
     */
    private TitleHelper getContentAndTitle(List<DiscNotificationResult> aliveList) {
        mContent = "";
        TitleHelper titleHelper = new TitleHelper();
        //判断是否达到通知条件
        for (DiscNotificationResult item : aliveList) {
            NotificationItem ni = item.getNotificationItem();
            DiscRank dr = item.getDiscRank();
            boolean isQualified = false;
            //更新
            if (ni.isUpdate()) {
                if (dr.getPreRank() != dr.getCurrentRank())
                    isQualified = true;
            }
            //爆上
            if (ni.isFly() && dr.getPreRank() != 0) {
                if ((dr.getCurrentRank() / (dr.getPreRank() * 1.0f)) < NotificationProfile.FLY_SCALE) {
                    isQualified = true;
                    titleHelper.pushTitleCandidate(dr.getsName(), TitleHelper.TYPE_FLY, dr.getCurrentRank() / (dr.getPreRank() * 1.0f));
                }
            }
            //跳水
            if (ni.isDive() && dr.getPreRank() != 0) {
                if ((dr.getCurrentRank() / (dr.getPreRank() * 1.0f)) < NotificationProfile.DIVE_SCALE) {
                    isQualified = true;
                    titleHelper.pushTitleCandidate(dr.getsName(), TitleHelper.TYPE_DIVE, dr.getCurrentRank() / (dr.getPreRank() * 1.0f));
                }
            }
            //登顶
            if (ni.isTop()) {
                if (dr.getCurrentRank() == NotificationProfile.TOP && dr.getCurrentRank() != dr.getPreRank()) {
                    isQualified = true;
                    titleHelper.pushTitleCandidate(dr.getsName(), TYPE_TOP, 0);
                }
            }

            if (isQualified) {
                mContent += String.format("%s/%s    %s",
                        Tools.formatNumByChar(dr.getCurrentRank(), "*", 4),
                        Tools.formatNumByChar(dr.getPreRank(), "*", 4), dr.getsName());
                if (dr.getName().contains("DVD"))
                    mContent += "  [DVD]";
                if (dr.getName().contains("尼限"))
                    mContent += "  [尼限]";
                mContent += "\n";
            }
        }
        return titleHelper;
    }

    private void getNotificationTextAndPush() {
        ServerAPI.getSakuraAPI()
                .getData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(container -> {
                    DiscRankService newContainer = new DiscRankService(container);
                    //拿到有更新的DiscRank
                    List<DiscRank> updateList = AppManager.getDiscRankService().updateList(newContainer);
                    //获取到符合条件的item
                    List<DiscNotificationResult> aliveList = AppManager.getNotificationService()
                            .getAliveList((ArrayList) updateList);
                    TitleHelper titleHelper = getContentAndTitle(aliveList);
                    if (!Tools.isEmpty(mContent)) {
                        AppManager.getNotificationService()
                                .pushNotification(new NotificationService
                                        .NotificationText(titleHelper.getTitle(), mContent), true);
                    }
                }, Throwable::printStackTrace);
    }

    public static class TitleHelper {

        private String mTitle = "数据更新";
        private boolean mIsLocked = false;
        private float mScale;
        public int mType;
        public static final int TYPE_FLY = 0;
        public static final int TYPE_DIVE = 1;
        public static final int TYPE_TOP = 2;

        public String getTitle() {
            return mTitle;
        }

        public void pushTitleCandidate(String name, int UPDATE_TYPE, float scale) {
            if (mIsLocked)
                return;
            switch (UPDATE_TYPE) {
                case TYPE_FLY:
                    if (mScale == 0f || scale < mScale) {
                        mType = TYPE_FLY;
                        mScale = scale;
                        mTitle = formatTitle(name, UPDATE_TYPE);
                    }
                    break;
                case TYPE_DIVE:
                    if (mType == TYPE_FLY)
                        return;
                    if (mScale == 0f || scale > mScale) {
                        mType = TYPE_DIVE;
                        mScale = scale;
                        mTitle = formatTitle(name, UPDATE_TYPE);
                    }
                    break;
                case TYPE_TOP:
                    mTitle = formatTitle(name, UPDATE_TYPE);
                    mIsLocked = true;
                    break;
            }

        }

        private String formatTitle(String name, int type) {
            switch (type) {
                case TYPE_TOP:
                    return String.format("登顶！！！ %s", name);
                case TYPE_DIVE:
                    return String.format("绝赞跳水中！ %s", name);
                case TYPE_FLY:
                    return String.format("霸权起航！ %s", name);
                default:
                    return "数据更新";
            }
        }
    }

}
