package com.yakami.light.service;

import com.yakami.light.AppManager;
import com.yakami.light.bean.DiscRank;
import com.yakami.light.service.base.BaseService;
import com.yakami.light.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 * 提供识别番剧功能
 */

public class BangumiNameService extends BaseService {

    private String mName;
    private List<String> mBlackList = new ArrayList<>();  //黑名单

    public static BangumiNameService newInstance(String name) {
        BangumiNameService instance = new BangumiNameService();
        instance.addBlackList(new String[]{"DVD", "限定版", "配信版", "Blu-ray", "REBOOT", "第.*?松",
                "特装限定", "剧场版", "BD", "BOX", "上卷", "下卷", "´", "【尼限定】", "Vol.*?001", "第.*?卷",
                "Vol.*?[1-9]", "Ⅱ", "初回盘", "初回", "第.*?集", " [1-9] ", " [0-9][0-9] "});
        if (Tools.isAvailableStr(name)) {
            instance.mName = name;
            return instance;
        } else
            return null;
    }

    public void addBlackStr(String str) {
        mBlackList.add(str);
    }

    public void addBlackList(String[] array) {
        for (String item : array)
            mBlackList.add(item);
    }

    /**
     * 清洁名称，过滤黑名单等
     */
    public String getTrueName(String name) {
        //过滤正则表达式中的特殊字符
        String[] regBlackArray = {"*", ".", "?", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\\", "/"};
        for (String arrayItem : regBlackArray)
            name = name.replace(arrayItem, " ");
        for (String item : mBlackList)
            name = name.replaceAll(item, " ");
        return name.trim();
    }

    public String getTrueName() {
        return getTrueName(mName);
    }

    /**
     * 获取番剧系列
     */
    public List<DiscRank> getBangumiRankGroup() {
        List<DiscRank> result = new ArrayList<>();
        String bangumiName = getTrueName();
        for (DiscRank item : AppManager.getDiscRankService().getAllRankList()) {
            if (getTrueName(item.getName()).equals(bangumiName))
                result.add(item);
        }
        return result;
    }
}
