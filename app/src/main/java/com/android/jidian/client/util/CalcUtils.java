package com.android.jidian.client.util;

/**
 * @author : PTT
 * date: 2020/9/16 9:46
 * company: 兴达智联
 * description: 防连点
 */
public class CalcUtils {
    private static long lastClickTime;
    public static int TIME_INTERVAL_1000 = 1000;
    public static int TIME_INTERVAL_500 = 500;
    public static int TIME_INTERVAL_DEFALUT;

    public CalcUtils() {
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0L && timeD <= 500L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    public static boolean isFastDoubleClick(long lastClickTime) {
        return isFastDoubleClick(lastClickTime, TIME_INTERVAL_DEFALUT);
    }

    public static boolean isFastDoubleClick(long lastClickTime, int interval) {
        long timeD = System.currentTimeMillis() - lastClickTime;
        return timeD >= 0L && timeD <= (long) interval;
    }

    static {
        TIME_INTERVAL_DEFALUT = TIME_INTERVAL_500;
    }
}