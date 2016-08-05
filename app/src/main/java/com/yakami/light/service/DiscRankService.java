package com.yakami.light.service;

import com.yakami.light.AppManager;
import com.yakami.light.bean.BangumiRank;
import com.yakami.light.bean.DiscNotificationResult;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.bean.RawTimeRankContainer;
import com.yakami.light.bean.TimeRankContainer;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 */

public class DiscRankService extends BaseService {

    private ArrayList<TimeRankContainer> mRankList = new ArrayList<>();
    private Map<Integer, DiscRank> mRankMap = new HashMap<>();

    public DiscRankService() {
    }

    public DiscRankService(List<RawTimeRankContainer> list) {
        for (RawTimeRankContainer item : list) {
            mRankList.add(new TimeRankContainer(item));
        }
        mRankMap = getAllRankMap();
    }

    private Map<Integer, DiscRank> getAllRankMap() {
        Map<Integer, DiscRank> result = new HashMap<>();
        for (TimeRankContainer container : mRankList) {
            for (DiscRank item : container.getDiscs()) {
                if (!result.containsKey(item.getId())) {
                    result.put(item.getId(), item);
                }
            }
        }
        return result;
    }

    public Map<Integer, DiscRank> getRankMap() {
        return mRankMap;
    }

    public List<DiscRank> updateList(DiscRankService container) {
        List<DiscRank> result = new ArrayList<>();

        for (Map.Entry<Integer, DiscRank> entry : container.getRankMap().entrySet()) {
            DiscRank newItem = entry.getValue();
            if (mRankMap.containsKey(entry.getKey())) {
                DiscRank oldItem = this.getRankMap().get(entry.getKey());
                if (newItem.getUpdateDate().compareTo(oldItem.getUpdateDate()) == 1) {
                    result.add(newItem);
                    mRankMap.remove(oldItem.getId());
                    mRankMap.put(newItem.getId(), newItem);
                }
            } else {
                result.add(newItem);
                mRankMap.put(newItem.getId(), newItem);
            }
        }
        return result;
    }


    public List<DiscRank> getWatchedDiscRank() {
        List<DiscNotificationResult> list = AppManager.getNotificationService()
                .getAliveList((ArrayList<DiscRank>) getAllRankList());
        List<DiscRank> result = new ArrayList<>();
        for (DiscNotificationResult item : list) {
            if (item.getNotificationItem().isWatched())
                result.add(item.getDiscRank());
        }
        return result;
    }

    public DiscRank toDiscRank(BangumiRank rank) {
        return mRankMap.get(rank.getId());
    }

    public List<BangumiRank> getWatchedBangumiRank() {
        List<DiscRank> list = getWatchedDiscRank();
        return toBangumiRank(list);
    }

    public ArrayList<TimeRankContainer> getTimeRanksList() {
        return mRankList;
    }

    public List<DiscRank> getAllRankList() {
        ArrayList<DiscRank> list = new ArrayList<>();
        for (TimeRankContainer container : mRankList) {
            if (container.getsName().equals("top_100"))
                continue;
            list.addAll(container.getDiscs());
        }
        //因为top100里会存在和其他季番列表里一样的重复项，需要特殊 处理
        List<DiscRank> cloneList = (ArrayList<DiscRank>) list.clone();
        for (TimeRankContainer container : mRankList) {
            if (container.getsName().equals("top_100")) {
                List<DiscRank> topList = container.getDiscs();
                for (DiscRank item : topList) {
                    boolean isExisted = false;
                    for (Iterator it = cloneList.iterator(); it.hasNext(); ) {
                        DiscRank tmp = (DiscRank) it.next();
                        if (tmp.getId() == item.getId()) {
                            it.remove();
                            isExisted = true;
                            break;
                        }
                    }
                    if (!isExisted)
                        list.add(item);
                }
            }
        }


        return list;
    }

    public static BangumiRank toBangumiRank(DiscRank disc) {
        BangumiRank tmp = new BangumiRank();
        tmp.setId(disc.getId());
        tmp.setsName(disc.getsName());
        String currentRank = disc.getCurrentRank() != 0 ? String.valueOf(disc.getCurrentRank()) : "----";
        String preRank = disc.getCurrentRank() != 0 ? String.valueOf(disc.getPreRank()) : "----";
        tmp.setRank(String.format(Locale.CHINA, "%s/%s", currentRank, preRank));
        tmp.setSituation(disc.getPreRank() - disc.getCurrentRank() + "");
        tmp.setName(disc.getName());
        tmp.setPT(disc.getPT() + "");
//        rank.setSerialNum(String.format(Locale.US, "%03d", i));
        tmp.setLastUpdate(Tools.getTimeGap(disc.getUpdateDate()));
        tmp.setNicoOrder(disc.getNicoOrder() + "");
        tmp.setAmazonRank(disc.getAmazonRank() + "");
        tmp.setSaleDay(disc.getSaleDay() + "");
        tmp.initType();
        return tmp;
    }

    public static List<BangumiRank> toBangumiRank(List<DiscRank> list) {
        List<BangumiRank> result = new ArrayList<>();
        int i = 1;
        for (DiscRank item : list) {
            BangumiRank rank = toBangumiRank(item);
            rank.setSerialNum(String.format(Locale.US, "%03d", i));
            result.add(rank);
            i++;
        }
        return result;
    }

    public DiscRank toDicsRank(BangumiRank rank) {
        for (DiscRank item : getAllRankList()) {
            if (item.getId() == rank.getId()) {
                return item;
            }
        }
        return null;
    }

    public List<BangumiRank> getBangumiRankList(int pos) {
        return toBangumiRank(mRankList.get(pos).getDiscs());
    }

    public void setRankList(ArrayList<TimeRankContainer> rankList) {
        mRankList = rankList;
    }

}
