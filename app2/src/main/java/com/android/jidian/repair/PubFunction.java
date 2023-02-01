package com.android.jidian.repair;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.List;

public class PubFunction {
    /**
     * LEVEL = 1;线上
     * LEVEL = 2;仿真
     * LEVEL = 3;测试
     */
    public static int LEVEL = 1;
    private static final String SCHEME_HTTPS = "https://";
    private static final String SCHEME_HTTP = "http://";

    private static final String HOST_PRE_TEST = "test";
    private static final String HOST_PRE_FZ = "fz";
    /**
     * app.mixiangx.com   => appx.mixiangx.com
     * ape.mixiangx.com   => apex.mixiangx.com
     * api.mixiangx.com   => apix.mixiangx.com
     * h5.mixiangx.com    => h5x.mixiangx.com
     * apm.mixiangx.com   => apmx.mixiangx.com
     */
    private static final String HOST_APP = "appx.mixiangx.com/";
    private static final String HOST_API = "apix.mixiangx.com/";
    private static final String HOST_APM = "apmx.mixiangx.com/";
    private static final String HOST_APE = "apex.mixiangx.com/";
    private static final String HOST_H5 = "h5x.mixiangx.com/";

    public static String app;
    public static String api;
    public static String apm;
    public static String ape;
    public static String h5;
    public static String upload;

    public static String getLevel() {
        String s = "";
        //  online,           fz,               test
        // LEVEL = 1;线上    LEVEL = 2;仿真    LEVEL = 3;测试
        if (LEVEL == 1) {
            s = "online";
        } else if (LEVEL == 2) {
            s = "fz";
        } else if (LEVEL == 3) {
            s = "test";
        }
        return s;
    }

    static {
        switch (LEVEL) {
            case 2:
                app = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APP;
                api = SCHEME_HTTPS + HOST_PRE_FZ + HOST_API;
                apm = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APM;
                ape = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_PRE_FZ + HOST_H5;
                break;
            case 3:
                app = SCHEME_HTTP + HOST_PRE_TEST + HOST_APP;
                api = SCHEME_HTTP + HOST_PRE_TEST + HOST_API;
                apm = SCHEME_HTTP + HOST_PRE_TEST + HOST_APM;
                ape = SCHEME_HTTP + HOST_PRE_TEST + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_PRE_TEST + HOST_H5;
                break;
            default:
                app = SCHEME_HTTPS + HOST_APP;
                api = SCHEME_HTTPS + HOST_API;
                apm = SCHEME_HTTPS + HOST_APM;
                ape = SCHEME_HTTPS + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_H5;
                break;
        }
    }

    public static String car_ID = "";
    /**
     * 目标坐标地址
     */
    public static double[] marker = null;
    /**
     * 自己坐标地址
     */
    public static double[] local = null;

    //获得版本名
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return localVersion;
    }

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return localVersion;
    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getSystemver() {
        return Build.VERSION.RELEASE;
    }

}