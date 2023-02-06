package com.android.jidian.repair.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author : PTT
 * date: 2020/7/31 10:33
 * company: 兴达智联
 * description: 显示工具类
 */
public class DisplayUtils {
    private static int mScreenWidth;
    private static int mScreenHeight;
    private static float mScreenDensity;
    private static int mStatusBarHeight;
    private static int mNavigationBarHeight;

    /**
     * 获取手机屏幕高度
     *
     * @param context 上下文
     * @return 屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        if (mScreenHeight <= 0) {
            mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        }
        return mScreenHeight;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        if (mScreenWidth <= 0) {
            mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 获取手机屏幕像素密度
     *
     * @param context 上下文
     * @return 像素密度
     */
    public static float getDensity(Context context) {
        if (mScreenDensity <= 0) {
            mScreenDensity = context.getResources().getDisplayMetrics().density;
        }
        return mScreenDensity;
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度，如果界面没有呈现将返回0
     */
    public static int getStatusBarHeight(Activity context) {
        if (mStatusBarHeight <= 0) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                try {
                    mStatusBarHeight = resources.getDimensionPixelSize(resourceId);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
            // 直接获取失败，尝试从显示视图中获取，必须在界面显示之后才能获取到值
            if (mStatusBarHeight <= 0) {
                Rect frame = new Rect();
                context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                mStatusBarHeight = frame.top;
            }
        }
        return mStatusBarHeight;
    }

    public static int getNavigationBarHeight(Activity context) {
        if (mNavigationBarHeight <= 0) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                try {
                    mNavigationBarHeight = resources.getDimensionPixelSize(resourceId);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return mNavigationBarHeight;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dp      相对像素密度值
     * @return 像素值
     */
    public static int dp2px(Context context, float dp) {
        return (int) (dp * getDensity(context) + 0.5f);
    }

    /**
     * 转Utf8
     */
    public static String unicodeToUtf8(String src) {
        if (null == src) {
            return null;
        }
        System.out.println("src: " + src);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();
    }

    /**
     * @param date
     * @param formatStr 格式
     * @return
     */
    public static String getTime(Date date, String formatStr) {//可根据需要自行截取数据显示
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        if (context != null && !TextUtils.isEmpty(phoneNum)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            context.startActivity(intent);
        }
    }
}