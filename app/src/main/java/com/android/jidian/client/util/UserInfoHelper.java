package com.android.jidian.client.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author fight runningpig66@163.com
 * 2019/10/25
 */
public class UserInfoHelper {
    @SuppressLint("StaticFieldLeak")
    private static UserInfoHelper instance;
    private Context context;

    private UserInfoHelper(Context context) {
        this.context = context;
    }

    public static void init(Application application) {
        instance = new UserInfoHelper(application);
    }

    public static synchronized UserInfoHelper getInstance() {
        return instance;
    }

    public void setUid(String uid) {
        PreferenceManager.putString(context, "id", uid);
    }

    public void setAccess(String access) {
        PreferenceManager.putString(context, "access", access);
    }

    public String getUid() {
        return PreferenceManager.getString(context, "id", "");
    }

    public String getAccess() {
        return PreferenceManager.getString(context, "access", "");
    }

    public String getAuth() {
        return PreferenceManager.getString(context, "auth", "");
    }

    public void setAuth(String autb) {
        PreferenceManager.putString(context, "auth", "");
    }

}
