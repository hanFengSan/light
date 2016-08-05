package lecho.lib.hellocharts.util;

/**
 * Created by Yakami on 2016/8/2, enjoying it!
 */

public class YakamiUtils {
    /**
     * 负数转正数
     *
     * @param num
     * @return
     * @author Yakami
     */
    public static char[] toPositiveNumber(char[] num) {
        for (int i = 0; i < num.length; i++) {
            if (num[i] == '-')
                num[i] = '\u0000';
        }
        return num;
    }

    /**
     * 检测是否为负数
     * @param num
     * @return
     */
    public static boolean isNegativeNUmber(char[] num) {
        for (int i = 0; i < num.length; i++) {
            if (num[i] == '-')
                return true;
        }
        return false;
    }

}
