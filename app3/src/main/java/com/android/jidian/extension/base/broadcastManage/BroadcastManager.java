package com.android.jidian.extension.base.broadcastManage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;


public class BroadcastManager {

    private Activity activity;
    private List<BroadcastReceiver> receivers = new ArrayList<>();

    //关闭命令
    public static String RECEIVER_ACTION_FINISH_ALL = "receiver_action_finish_all";
    public static String RECEIVER_ACTION_FINISH_LOGIN = "receiver_action_finish_login";
    public static String RECEIVER_ACTION_FINISH_PAY = "receiver_action_finish_pay";

    //切换首页页面命令
    public static String RECEIVER_ACTION_MAIN_CHANGE_PAGE = "receiver_action_main_change_page";

    public BroadcastManager(Activity activity) {
        this.activity = activity;
        registerFinishReceiverByAll();
    }

    private void registerFinishReceiverByAll() {
        FinishActivityReceiver activityReceiver = new FinishActivityReceiver();
        receivers.add(activityReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_FINISH_ALL);
        activity.registerReceiver(activityReceiver, intentFilter);
    }

    public void registerFinishReceiverByLogin() {
        FinishActivityReceiver activityReceiver = new FinishActivityReceiver();
        receivers.add(activityReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_FINISH_LOGIN);
        activity.registerReceiver(activityReceiver, intentFilter);
    }

    public void registerFinishReceiverByPay() {
        FinishActivityReceiver activityReceiver = new FinishActivityReceiver();
        receivers.add(activityReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_FINISH_PAY);
        activity.registerReceiver(activityReceiver, intentFilter);
    }

    public void registerMainChangePage(BroadcastReceiver broadcastReceiver) {
        receivers.add(broadcastReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_MAIN_CHANGE_PAGE);
        activity.registerReceiver(broadcastReceiver, intentFilter);
    }

    private class FinishActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //根据需求添加自己需要关闭页面的action
            if (RECEIVER_ACTION_FINISH_ALL.equals(intent.getAction()) || RECEIVER_ACTION_FINISH_LOGIN.equals(intent.getAction()) || RECEIVER_ACTION_FINISH_PAY.equals(intent.getAction())) {
                activity.finish();
            }
        }
    }

    public void onDestroy(){
        for(int i = 0 ; i < receivers.size() ; i++){
            BroadcastReceiver mReceiver = receivers.get(i);
            if (mReceiver != null) {
                activity.unregisterReceiver(mReceiver);
            }
        }
    }
}
