package com.android.jidian.client.jpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.jidian.client.Main;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Modified by tianyanyu 20200630
 */
public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";

    /**
     * 收到自定义消息回调
     * 说明 如果需要在旧版本的Receiver接收cn.jpush.android.intent.MESSAGE_RECEIVED广播
     * 可以不重写此方法，或者重写此方法且调用super.onMessage
     * 如果重写此方法，没有调用super，则不会发送广播到旧版本Receiver
     *
     * @param context       应用的 Application Context。
     * @param customMessage 接收自定义消息内容
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    /**
     * 点击通知回调
     * 说明 如果需要在旧版本的Receiver接收cn.jpush.android.intent.NOTIFICATION_OPENED广播
     * 可以不重写此方法，或者重写此方法且调用super.onNotifyMessageOpened
     * 如果重写此方法，没有调用super，则不会发送广播到旧版本Receiver
     *
     * @param context 应用的 Application Context。
     * @param message 点击的通知内容
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            //打开自定义的Activity
            Intent i = new Intent(context, TestActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        } catch (Throwable throwable) {

        }
    }

    /**
     * 通知的MultiAction回调
     * 说明 如果需要在旧版本的Receiver接收cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION广播
     * 可以不重写此方法，或者重写此方法且调用super.onMultiActionClicked
     * 如果重写此方法，没有调用super，则不会发送广播到旧版本Receiver
     * <p>
     * 说明 注意这个方法里面禁止再调super.onMultiActionClicked,因为会导致逻辑混乱
     *
     * @param context 应用的 Application Context。
     * @param intent  点击后触发的Intent
     */
    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    /**
     * 收到通知回调
     * 说明 如果需要在旧版本的Receiver接收cn.jpush.android.intent.NOTIFICATION_RECEIVED广播
     * 可以不重写此方法，或者重写此方法且调用super.onNotifyMessageArrived
     * 如果重写此方法，没有调用super，则不会发送广播到旧版本Receiver
     *
     * @param context 应用的 Application Context。
     * @param message 接收到的通知内容
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
        String notificationExtras = message.notificationExtras;
        try {
            JSONTokener jsonTokener = new JSONTokener(notificationExtras);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            int play_sound = jsonObject.optInt("play_sound");
            if (play_sound == 1) {
                Log.d(TAG, "onNotifyMessageArrived: play_sound == 1");
                try {
                    if (Main.mediaPlayer != null) {
                        Main.mediaPlayer.start();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onNotifyMessageArrived: mediaPlayer" + e.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除通知回调
     * 说明:
     * 1.同时删除多条通知，可能不会多次触发清除通知的回调
     * 2.只有用户手动清除才有回调，调接口清除不会有回调
     *
     * @param context 应用的 Application Context。
     * @param message 清除的通知内容
     */
    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

//    /**
//     * 通知未展示的回调
//     * 说明:
//     * 1.3.5.8之后支持推送时指定前台不展示功能，当通知未展示时，会回调该接口
//     * @param context 应用的 Application Context。
//     * @param notificationMessage 未展示的通知内容
//     */
//    @Override
//    public void onNotifyMessageUnShow(Context context, NotificationMessage notificationMessage) {
//        super.onNotifyMessageUnShow(context, notificationMessage);
//    }

    /**
     * 注册成功回调
     *
     * @param context        应用的 Application Context。
     * @param registrationId 注册id
     */
    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
    }

    /**
     * 长连接状态回调
     *
     * @param context     应用的 Application Context。
     * @param isConnected 长连接状态
     */
    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    /**
     * 注册失败回调
     *
     * @param context    应用的 Application Context。
     * @param cmdMessage 错误信息
     */
    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     *
     * @param context      应用的 Application Context。
     * @param jPushMessage tag 相关操作返回的消息结果体，具体参考 JPushMessage 类的说明。
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context      应用的 Application Context。
     * @param jPushMessage check tag 与当前用户绑定状态的操作返回的消息结果体，具体参考 JPushMessage 类的说明。
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param context      应用的 Application Context。
     * @param jPushMessage alias 相关操作返回的消息结果体，具体参考 JPushMessage 类的说明。
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context      应用的 Application Context。
     * @param jPushMessage 设置手机号码返回的消息结果体，具体参考 JPushMessage 类的说明。
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    //send msg to MainActivityU6
    private void processCustomMessage(Context context, CustomMessage customMessage) {
        if (Main.isForeground) {
            String message = customMessage.message;
            String extras = customMessage.extra;
            Intent msgIntent = new Intent(Main.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(Main.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(Main.KEY_EXTRAS, extras);
                    }
                } catch (JSONException ignored) {
                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }

    /**
     * 通知开关的回调
     * 说明
     * 该方法会在以下情况触发时回调。
     * 1.sdk每次启动后都会检查通知开关状态并通过该方法回调给开发者。
     * 2.当sdk检查到通知状态变更时会通过该方法回调给开发者。
     * <p>
     * 说明 sdk内部检测通知开关状态的方法因系统差异，在少部分机型上可能存在兼容问题(判断不准确)。
     *
     * @param context 应用的 Application Context。
     * @param isOn    通知开关状态
     * @param source  触发场景，0为sdk启动，1为检测到通知开关状态变更
     */
    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

}
