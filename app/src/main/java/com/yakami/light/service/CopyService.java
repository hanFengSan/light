package com.yakami.light.service;

import com.yakami.light.bean.DiscRank;
import com.yakami.light.utils.Tools;

import java.util.List;

/**
 * Created by Yakami on 2016/8/5, enjoying it!
 * 提供需要复制的内容的加工
 */

public class CopyService {

    public static String copyRank(DiscRank rank) {
        String cRank = Tools.formatNumByChar(rank.getCurrentRank(), "*", 4);
        String pRank = Tools.formatNumByChar(rank.getPreRank(), "*", 4);
        String result = String.format("%s/%s  (%dpt)  %s\n", cRank, pRank, rank.getPT(), rank.getName());
        return result;
    }

    public static String copyRank(List<DiscRank> list) {
        String result = "";
        for (DiscRank item : list) {
            result += copyRank(item);
        }
        return result;
    }
}
