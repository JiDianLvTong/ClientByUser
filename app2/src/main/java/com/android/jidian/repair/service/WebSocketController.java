package com.android.jidian.repair.service;

import android.content.Context;

import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.net.BaseHttp;
import com.android.jidian.repair.net.BaseHttpParameterFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例
 * 长链接 控制类
 * 主要做注册和分发 和 简单控制
 */
public class WebSocketController {

    private static volatile WebSocketController webSocketController = null;
    private WebSocketController(){};
    public static WebSocketController getInstance(){
        if(webSocketController == null){
            synchronized (WebSocketController.class){
                if(webSocketController == null){
                    webSocketController = new WebSocketController();
                }
            }
        }
        return webSocketController;
    }

    private Context context;
    private WebSocketLongLink webSocketLongLink;

    private Boolean hasReceiverCabId = false;

    public void init(Context context){
        this.context = context;
        onStart();
    }

    public void onStart(){
        System.out.println("longLink - 启动");
        if(webSocketLongLink == null){
            webSocketLongLink = new WebSocketLongLink();
            webSocketLongLink.init(context, new WebSocketLongLink.IFHttpOpenLongLinkLinstener() {
                @Override
                public void onHttpReTurnIDResult(String code) {
                    //绑定长链接
                    final String fCode = code;
                    List<BaseHttpParameterFormat> baseHttpParameterFormats =  new ArrayList<>();
                    UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.fd , code);
                    baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid",UserInfoSp.getInstance().getId()));
                    baseHttpParameterFormats.add(new BaseHttpParameterFormat("fd",code));

                    BaseHttp baseHttp = new BaseHttp(context,"https://apex.mixiangx.com/Connection/workBind.html", baseHttpParameterFormats, 15, new BaseHttp.BaseHttpListener() {
                        @Override
                        public void dataReturn(int code, String message, String data) {
                            System.out.println("longLink - code - " + code + " - message - " + message + " - data - " + data);
                            if(code != 1){
                                System.out.println("longLink - 重新绑定");
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            sleep(30 * 1000);
                                            onHttpReTurnIDResult(fCode);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        }
                    });
                    baseHttp.onStart();
                }

                @Override
                public void onHttpReturnDataResult(String data) {
                    //正常数据处理
                }

                @Override
                public void onHttpReturnErrorResult(int data) {
                    //长连接错误
                }
            });
        }
    }

    public void onDestroy(){
        if(webSocketLongLink != null){
            webSocketLongLink.onDestroy();
            webSocketLongLink = null;
        }
    }

}
