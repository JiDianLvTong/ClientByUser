package com.android.jidian.extension.net;

public class HttpUrlMap {

    private static String api = "https://apix.mixiangx.com";
    private static String apu = "https://apux.mixiangx.com";

    //获取验证码
    public static String getMsg = api + "/Sms/msgSend.html";
    //登录
    public static String login = apu + "/Login/login.html";

}
