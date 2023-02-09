package com.android.jidian.repair.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.jidian.repair.R;
import com.android.jidian.repair.mvp.main.MainActivity;
import com.android.jidian.repair.mvp.main.MainEvent;
import com.android.jidian.repair.mvp.task.TimeTaskDetailActivity;
import com.android.jidian.repair.utils.WebSocketLongLink;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * @author : xiaoming
 * date: 2023/1/12 10:37
 * description:
 */
public class LongLinkService extends Service {
    private static final String TAG = "MyService";
    private NotificationManager notificationManager;
    private String notificationId = "channel_Id";
    private String notificationName = "channel_Name";

    public LongLinkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ...");

        new WebSocketLongLink().init(LongLinkService.this, new WebSocketLongLink.IFHttpOpenLongLinkLinstener() {
            @Override
            public void onHttpReTurnIDResult(String code) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onHttpReturnDataResult(String data) {
                System.out.println("longLink - onHttpReturnDataResult - " + data);
                EventBus.getDefault().post(new MainEvent(MainEvent.RECEIVE_LONG_LINK, data));
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//获取通知管理对象

                NotificationChannel channel = new NotificationChannel("channel_1", "test", NotificationManager.IMPORTANCE_HIGH);//创建通知通道
                manager.createNotificationChannel(channel);//设置通道到管理中
                Intent intent = new Intent(LongLinkService.this, TimeTaskDetailActivity.class);
                intent.putExtra("wtid", "1");
                // 加上PendingIntent之后，点击通知就会弹出到另一个layout中
                PendingIntent pendingIntent = PendingIntent.getActivity(LongLinkService.this,0,intent,0);
                Notification notification = new NotificationCompat.Builder(LongLinkService.this, "channel_1")//设置通知对象
                        .setContentTitle("收到一个任务")
                        .setContentText("没有展开的内容没有展开的内容没有展开的内容")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))

                        .setContentIntent(pendingIntent)//构建意图
                        .setAutoCancel(true)//点击通知之后自动消失
                        //设置通知效果
//                        .setSound(Uri.fromFile(new File("音频文件路径")))//控制手机播放歌曲
//                        .setVibrate(new long[]{0,1000,1000,1000})//控制手机震动，需要申请权限
                        .setDefaults(NotificationCompat.DEFAULT_ALL)//使用通知的默认效果

                        .setStyle(new NotificationCompat.BigTextStyle().bigText("展开的任务展开的任务展开的任务展开的任务" +
                                "展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务" +
                                "展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务展开的任务" +
                                "展开的任务展开的任务展开的任务展开的任务展开的任务").setBigContentTitle("这是展开的任务标题").setSummaryText("这个是啥啥啥啥啥啥"))
//                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(
//                                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))

                        .setPriority(NotificationCompat.PRIORITY_MAX)

                        .build();
                manager.notify(1, notification);//显示通知
            }

            @Override
            public void onHttpReturnErrorResult(int data) {

            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ....");
        super.onDestroy();
        Intent intent=new Intent();
        intent.setAction("restartService");
        LongLinkService.this.sendBroadcast(intent);
    }
}
