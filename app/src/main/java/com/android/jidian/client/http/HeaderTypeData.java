package com.android.jidian.client.http;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.jidian.client.pub.Md5;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.UserInfoHelper;

public class HeaderTypeData {

    //another: com.android.mixiang.client.net.RetrofitClient.getHeaderInterceptor()
    public static String[][] HEADER_Whit_APTK_APUD_PRO(Activity activity, String uid) {
        if (activity == null) {
            return new String[][]{};
        }
        return new String[][]{
                {"apud", uid},
                {"aptk", Md5.getAptk()},
                {"version", String.valueOf(getLocalVersion(activity))},
                {"verName", getLocalVersionName(activity)},
                {"osname", "Android"},
                {"proname", "app"},
                {"access", UserInfoHelper.getInstance().getAccess()},
                {"SERV_ENV", PubFunction.getLevel()},
                {"company", "jidianlvtong"},
                {"device", getModel()},
                {"systemver", getSystemver()}};
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

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getSystemver() {
        return Build.VERSION.RELEASE;
    }
}
