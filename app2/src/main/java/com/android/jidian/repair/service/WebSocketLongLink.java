package com.android.jidian.repair.service;

import android.content.Context;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.URI;


/**
 * 长链接 建立维持
 * 字段注册以及接收 还有 这个线程的生命维持
 */

public class WebSocketLongLink {

    //上下文
    private Context context;
    //长链接下发这次连接的id 需要进行HTTP绑定
    private String clientID;
    //客户端
    private WebSocketClient mWebSocketClient;

    //长链接保护进程
    private Thread longLinkProtectThread = null;
    //长链接保护进程参数
    private int longLinkProtectThreadState = 0;
    //长链接保护进程计数参数
    private int longLinkProtectThreadCode = 5;
    private boolean longLinkHasConnection = false;
    private final int reConnectCountByConnection = 60;
    private final int reConnectCountByUnConnection = 5;

    //长链接心跳线程
    private Thread longLinkHeartThread = null;
    //长链接心跳线程参数
    private int longLinkHeartThreadCode = 0;
    //长链接心跳线程计数
    private int longLinkHeartThreadCount = 0;

    //返回接口
    public interface IFHttpOpenLongLinkLinstener {
        void onHttpReTurnIDResult(String code);

        void onHttpReturnDataResult(String data);

        void onHttpReturnErrorResult(int data);
    }

    //接口
    private IFHttpOpenLongLinkLinstener ifHttpOpenLongLinkLinstener;

    //初始化
    public void init(Context context, IFHttpOpenLongLinkLinstener ifHttpOpenLongLinkLinstener) {
        this.context = context;
        this.ifHttpOpenLongLinkLinstener = ifHttpOpenLongLinkLinstener;
        onStart();
    }

    public void onStart() {
        //初始化保护进程
        if (longLinkProtectThread == null) {
            longLinkProtectThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (longLinkProtectThreadState == 0) {
                        try {
                            if (longLinkProtectThreadCode > 0) {
                                longLinkProtectThreadCode = longLinkProtectThreadCode - 1;
                            } else {
                                closeConnect();
                                initSocketClient();
                                mWebSocketClient.connect();
                                ifHttpOpenLongLinkLinstener.onHttpReturnErrorResult(0);
                                if(longLinkHasConnection == true){
                                    longLinkProtectThreadCode = reConnectCountByConnection;
                                }else{
                                    longLinkProtectThreadCode = reConnectCountByUnConnection;
                                }
                            }
                            sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
            };
            longLinkProtectThread.start();
        }
    }


    //客户端初始化
    private void initSocketClient(){
        try {
            if (mWebSocketClient == null) {
                mWebSocketClient = new WebSocketClient(new URI("http://link.mixiangx.com:2216")) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        System.out.println("longLink - init - " + serverHandshake.getHttpStatusMessage());
                    }
                    @Override
                    public void onMessage(final String s) {
                        //这个是收到服务器推送下来的消息
                        System.out.println("longLink - receive - " + s);
                        longLinkProtectThreadCode = reConnectCountByConnection;
                        longLinkHasConnection = true;
                        try {
                            JSONTokener jsonTokener = new JSONTokener(s);
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            String type = jsonObject.getString("type");

                            if (type.equals("init")) {
                                clientID = jsonObject.getString("fd");
                                ifHttpOpenLongLinkLinstener.onHttpReTurnIDResult(clientID);

                                if(longLinkHeartThread == null){
                                    longLinkHeartThread = new Thread(){
                                        @Override
                                        public void run() {
                                            super.run();
                                            longLinkHeartThreadCount = 0;
                                            while (longLinkHeartThreadCode == 0){
                                                try {
                                                    sleep(1000);
                                                    longLinkHeartThreadCount = longLinkHeartThreadCount + 1;
                                                    if(longLinkHeartThreadCount % 5 == 0){
                                                        if(mWebSocketClient!=null){
                                                            String send = "{\"type\":\"ping\",\"number\":\""+ "XXXXXXXXXX" +"\"}";
                                                            mWebSocketClient.send(send);
                                                            System.out.println("longLink - send - " + send);
                                                            ifHttpOpenLongLinkLinstener.onHttpReturnErrorResult(1);
                                                        }
                                                    }
                                                    if(longLinkHeartThreadCount >= 80){
                                                        longLinkHeartThreadCount = 0;
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    };
                                    longLinkHeartThread.start();
                                }
                            }
                            else {

                            }
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onClose(int i, String s, boolean remote) {
                        //连接断开，remote判定是客户端断开还是服务端断开
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                };
            }
        } catch (Exception e) {
        }
    }

    private void closeConnect() {
        if (mWebSocketClient == null) {
            return;
        } else {
            mWebSocketClient.closeConnection(0, "onDestory");
            mWebSocketClient.close();
            mWebSocketClient = null;
            longLinkHeartThreadCode = 1;
            longLinkHeartThread = null;
        }
    }

    public void onDestroy() {
        longLinkProtectThreadState = 1;
        longLinkHeartThreadCode = 1;
        closeConnect();
    }
}
