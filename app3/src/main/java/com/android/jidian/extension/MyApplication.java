package com.android.jidian.extension;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.jidian.extension.base.permissionManager.PermissionManager;
import com.android.jidian.extension.dao.sp.UserInfoSp;

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
        //用户信息初始化
        UserInfoSp.getInstance().init(getApplicationContext());
        //权限管理初始化
        PermissionManager.getInstance().init(getApplicationContext());
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
