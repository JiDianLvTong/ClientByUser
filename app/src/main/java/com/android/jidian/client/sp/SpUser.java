package com.android.jidian.client.sp;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUser {

    private volatile static SpUser spUser;
    private SpUser(){};
    public static SpUser getInstance(){
        if(spUser == null){
            synchronized (SpUser.class){
                if(spUser == null){
                    spUser = new SpUser();
                }
            }
        }
        return spUser;
    }

    private Context context;
    private SharedPreferences sharedPreferences;

    public void init(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("SpUser" , Context.MODE_PRIVATE);
    }

    public boolean isLogin(){
        return false;
    }

}
