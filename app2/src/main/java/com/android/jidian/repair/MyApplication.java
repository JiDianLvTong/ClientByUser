package com.android.jidian.repair;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.amap.api.location.AMapLocationClient;
import com.android.jidian.repair.base.permissionManager.PermissionManager;
import com.android.jidian.repair.dao.sp.UserInfoSp;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:15
 * description:
 */
public class MyApplication extends Application {

    private static MyApplication context;
    private static Context mContext;

    public static MyApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        //权限管理初始化
//        PermissionManager.getInstance().init(getApplicationContext());
        //本地储存初始化 - 用户sp
        UserInfoSp.getInstance().init(getApplicationContext());

        AMapLocationClient.updatePrivacyAgree(getApplicationContext(), true);
        AMapLocationClient.updatePrivacyShow(getApplicationContext(), true, true);
    }

    /**
     * 全局获取上下文
     */
    public static Context getAppContext() {
        return mContext;
    }

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

    //获得版本号
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
}
