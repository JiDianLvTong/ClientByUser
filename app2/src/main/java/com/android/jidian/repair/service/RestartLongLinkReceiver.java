package com.android.jidian.repair.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author : xiaoming
 * date: 2023/2/8 10:59
 * description:
 */
public class RestartLongLinkReceiver extends BroadcastReceiver {

    public RestartLongLinkReceiver() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("restartService")){
            Intent nIntent=new Intent();
            nIntent.setClass(context, LongLinkService.class);
            context.startService(nIntent);
        }
    }

}
