package com.android.jidian.repair.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

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

    public String getAdname() {
        return PreferenceManager.getString(context, "adname", "");
    }

    public String getPhone() {
        return PreferenceManager.getString(context, "phone", "");
    }
}
