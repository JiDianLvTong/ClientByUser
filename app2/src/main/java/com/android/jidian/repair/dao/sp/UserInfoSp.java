package com.android.jidian.repair.dao.sp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoSp {

    private static volatile UserInfoSp userInfoSp;
    private UserInfoSp(){};
    public static UserInfoSp getInstance(){
        if(userInfoSp == null) {
            synchronized (UserInfoSp.class){
                if(userInfoSp == null){
                    userInfoSp = new UserInfoSp();
                }
            }
        }
        return userInfoSp;
    };

    public enum UserInfoEnum{
        id,dwrole,apptoken,adname,phone,is_dwfirst,avater
    }

    //上下文
    private Context context;
    //用户数据
    protected SharedPreferences sharedPreferences;

    public void init(Context context){
        sharedPreferences = context.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
    }

    public String getUserInfoData(UserInfoEnum userInfoEnum){
        String data = sharedPreferences.getString(userInfoEnum.toString(),"");
        return data;
    }

    public void setUserInfoData(UserInfoEnum userInfoEnum , String data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userInfoEnum.toString(),data);
        editor.apply();
    }

    public boolean isLogin(){
        boolean isLogin = false;
        String uid = getUserInfoData(UserInfoEnum.id);
        if(uid.equals("")){
            isLogin = false;
        }else{
            isLogin = true;
        }
        return isLogin;
    }

    public String getPhone(){
        return getUserInfoData(UserInfoEnum.phone);
    }

    public String getId(){
        return getUserInfoData(UserInfoEnum.id);
    }

    public String getName(){
        return getUserInfoData(UserInfoEnum.adname);
    }

    public String getToken(){
        return getUserInfoData(UserInfoEnum.apptoken);
    }

    public void cleanUserInfoData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
