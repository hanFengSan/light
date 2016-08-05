package com.yakami.light.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakami
 * on 2016/8/4.
 */
public class NumFlagBitUtils {

    public static String toFlagBit(int num) {
        char[] str = new char[num + 1];
        for (int i = 0; i < str.length; i++) {
            str[i] = '0';
        }
        str[num] = '1';
        return new String(str);
    }

    public static int toInt(String flagBit) {
        char[] tmp = flagBit.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] == '1')
                return i;
        }
        return -1;
    }

    public static List<Integer> toIntList(String flagBit) {
        List<Integer> result = new ArrayList<>();
        char[] tmp = flagBit.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] == '1')
                result.add(i);
        }
        return result;
    }

    public static int getMax(List<Integer> list) {
        int max = list.get(0);
        for (int item : list) {
            if (item > max)
                max = item;
        }
        return max;
    }

    public static String toFlagBit(List<Integer> list) {
        char[] result = new char[getMax(list) + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = '0';
        }
        for (int item : list) {
            result[item] = '1';
        }
        return new String(result);
    }
}
