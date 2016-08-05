package com.yakami.light.utils;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.yakami.light.AppManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yakami on 2015/11/28.
 */
public class Tools {

    private static Toast mToast;

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().equals(""));
    }

    public static boolean isAvailableStr(String str) {
        return str != null && !str.equals("");
    }

    public static byte[] serial(Object objects) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(objects);
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.print("Tools.Serial.IOException");
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.print("Tools.Serial.IOException");
            }
            try {
                bos.close();

            } catch (IOException ex) {
                System.out.print("Tools.Serial.IOException");
            }
        }

    }

    public static int dp2px(int dp) {
        float scale = AppManager.getRes().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(int px) {
        DisplayMetrics displayMetrics = AppManager.getRes().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//    public static float px2dp(int px) {
//        Resources resources = AppManager.getContext().getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//        return dp;
//    }

    public static Object unSerial(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } catch (IOException e) {
            System.out.print("Tools.unSerial.IOException");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.print("Tools.unSerial.ClassNotFoundException");
            return null;
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                System.out.print("Tools.unSerial.IOException");
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                System.out.print("Tools.unSerial.IOException");
            }
        }
    }

    public static void toast(String str) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(AppManager.getContext(), str, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, T tmp) {
        return (T) obj;
    }

    /**
     * 用于解决class的toString生成的包名再次逆过程时由于toString不纯粹而导致的报错。
     */
    public static Class ClassForClearName(String packageName) {
        try {
            return Class.forName(packageName.replace(" ", "").replace("class", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getTimeGap(Date time) {
        long timeStamp = Calendar.getInstance().getTimeInMillis();
        long tmp = time.getTime();
        return getTimeFromMillis(timeStamp - time.getTime());
    }

    public static String getTimeFromMillis(long time) {
        if (time >= 0) {
            long sec = time / 1000;
            long hour = (sec - sec % 3600) / 3600;
            sec -= hour * 3600;
            long min = (sec - sec % 60) / 60;
            sec -= min * 60;
            return String.format("%02d:%02d:%02d", hour, min, sec);
        } else {
            return "error";
        }
    }

    /**
     * 格式化数字，右对齐，往左边补字符
     *
     * @param src
     * @param fill
     * @param len
     * @return
     */
    public static String formatNumByChar(int src, String fill, int len) {
        int gap = len - String.valueOf(src).length();
        if (gap > 0) {
            String tmp = "";
            for (int i = 0; i < gap; i++) {
                tmp += fill;
            }
            return tmp + String.valueOf(src);
        }
        return String.valueOf(src);
    }

    /**
     * 将因编码格式识别错误导致string乱码问题恢复
     *
     * @param str
     * @return
     * @throws Throwable
     */
    public static String recoverGBK2UTF8(String str) throws Throwable {
        return new String(str.getBytes("GBK"), "UTF-8");
    }

    public static String getStrHash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return toHex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将16位byte[] 转换为32位String
     *
     * @param buffer
     * @return
     */
    private static String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }

    public static int getMaxNum(List<Integer> values) {
        int max = values.get(0);
        for (int tmp : values) {
            if (tmp > max)
                max = tmp;
        }
        return max;
    }

    public static int getMaxAndNoZeroNum(List<Integer> values) {
        int max = 1;
        for (int tmp : values) {
            if (tmp != 0)
                max = tmp;
        }
        for (int tmp : values) {
            if (tmp != 0 && tmp > max)
                max = tmp;
        }
        return max;
    }

    public static int getMinNum(List<Integer> values) {
        int min = values.get(0);
        for (int tmp : values) {
            if (tmp < min)
                min = tmp;
        }
        return min;
    }

    public static SpannableString getBoldSpannableStr(String str) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        return ss;
    }

    public static boolean isInHourInterval(int start, int end) {
        Date date = new Date();
        if (start < date.getHours() && date.getHours() <= end) {
            return true;
        }
        return false;
    }

}
