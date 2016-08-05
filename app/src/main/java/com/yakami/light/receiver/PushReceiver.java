package com.yakami.light.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;
import com.yakami.light.bean.ServerResponse;
import com.yakami.light.receiver.base.BaseReceiver;
import com.yakami.light.service.PushService;

/**
 * Created by Yakami on 2016/6/15, enjoying it!
 */

public class PushReceiver extends BaseReceiver {

    private String mContent = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                break;
            case PushConsts.GET_MSG_DATA:
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    Gson gson = new Gson();
                    ServerResponse response = gson.fromJson(data, ServerResponse.class);
                    processCustomMessage(response);
                }
                break;
            default:
                break;
        }
    }

    private void processCustomMessage(ServerResponse response) {
        PushService.getInstance().with(response);
//        switch (response.getMessage()) {
//            case "update":
////                getNotificationText();
//                break;
//        }
    }

//    private void getNotificationText() {
//        mContent = "";
//        ServerAPI.getSakuraAPI()
//                .getData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .subscribe(container -> {
//                    AppManager.getDiscRankService().setRankList(new ArrayList<>());
//                    for (RawTimeRankContainer item : container) {
//                        AppManager.getDiscRankService().getTimeRanksList().add(new TimeRankContainer(item));
//                    }
//                    List<DiscRank> totalRankList = AppManager.getDiscRankService().getAllRankList();
//                    //获取到符合条件的item
//                    List<DiscNotificationResult> aliveList = AppManager.getNotificationService()
//                            .getAliveList((ArrayList) totalRankList);
//                    TitleHelper titleHelper = new TitleHelper();
//                    //判断是否达到通知条件
//                    for (DiscNotificationResult item : aliveList) {
//                        NotificationItem ni = item.getNotificationItem();
//                        DiscRank dr = item.getDiscRank();
//                        boolean isQualified = false;
//                        //更新
//                        if (ni.isUpdate()) {
//                            if (dr.getPreRank() != dr.getCurrentRank())
//                                isQualified = true;
//                        }
//                        //爆上
//                        if (ni.isFly() && dr.getPreRank() != 0) {
//                            if ((dr.getCurrentRank() / (dr.getPreRank() * 1.0f)) < NotificationProfile.FLY_SCALE) {
//                                isQualified = true;
//                                titleHelper.pushTitleCandidate(dr.getsName(), TYPE_FLY, dr.getCurrentRank() / (dr.getPreRank() * 1.0f));
//                            }
//                        }
//                        //跳水
//                        if (ni.isDive() && dr.getPreRank() != 0) {
//                            if ((dr.getCurrentRank() / (dr.getPreRank() * 1.0f)) < NotificationProfile.DIVE_SCALE) {
//                                isQualified = true;
//                                titleHelper.pushTitleCandidate(dr.getsName(), TYPE_DIVE, dr.getCurrentRank() / (dr.getPreRank() * 1.0f));
//                            }
//                        }
//                        //登顶
//                        if (ni.isTop()) {
//                            if (dr.getCurrentRank() >= NotificationProfile.TOP && dr.getCurrentRank() != dr.getPreRank()) {
//                                isQualified = true;
//                                titleHelper.pushTitleCandidate(dr.getsName(), TYPE_TOP, 0);
//                            }
//                        }
//
//                        if (isQualified) {
//                            mContent += String.format("%s/%s    %s",
//                                    Tools.formatNumByChar(dr.getCurrentRank(), "*", 4),
//                                    Tools.formatNumByChar(dr.getPreRank(), "*", 4), dr.getsName());
//                            if (dr.getName().contains("DVD"))
//                                mContent += "  [DVD]";
//                            if (dr.getName().contains("尼限"))
//                                mContent += "  [尼限]";
//                            mContent += "\n";
//                        }
//                    }
//
//                    if (!Tools.isEmpty(mContent)) {
//                        AppManager.getNotificationService()
//                                .pushNotification(new NotificationService
//                                        .NotificationText(titleHelper.getTitle(), mContent));
//                    }
//                }, Throwable::printStackTrace);
//    }
//
//    public class TitleHelper {
//
//        private String mTitle = "数据更新";
//        private boolean mIsLocked = false;
//        private float mScale;
//        public int mType;
//        public static final int TYPE_FLY = 0;
//        public static final int TYPE_DIVE = 1;
//        public static final int TYPE_TOP = 2;
//
//        public String getTitle() {
//            return mTitle;
//        }
//
//        public void pushTitleCandidate(String name, int UPDATE_TYPE, float scale) {
//            if (mIsLocked)
//                return;
//            switch (UPDATE_TYPE) {
//                case TYPE_FLY:
//                    if (mScale == 0f || scale < mScale) {
//                        mType = TYPE_FLY;
//                        mScale = scale;
//                        mTitle = formatTitle(name, UPDATE_TYPE);
//                    }
//                    break;
//                case TYPE_DIVE:
//                    if (mType == TYPE_FLY)
//                        return;
//                    if (mScale == 0f || scale > mScale) {
//                        mType = TYPE_DIVE;
//                        mScale = scale;
//                        mTitle = formatTitle(name, UPDATE_TYPE);
//                    }
//                    break;
//                case TYPE_TOP:
//                    mTitle = formatTitle(name, UPDATE_TYPE);
//                    mIsLocked = true;
//                    break;
//            }
//
//        }
//
//        private String formatTitle(String name, int type) {
//            switch (type) {
//                case TYPE_TOP:
//                    return String.format("登顶！！！ %s", name);
//                case TYPE_DIVE:
//                    return String.format("绝赞跳水中！ %s", name);
//                case TYPE_FLY:
//                    return String.format("霸权起航！ %s", name);
//                default:
//                    return "数据更新";
//            }
//        }
//    }
}


