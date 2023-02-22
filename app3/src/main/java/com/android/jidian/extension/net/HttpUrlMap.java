package com.android.jidian.extension.net;

public class HttpUrlMap {

    private static String api = "https://apix.mixiangx.com";
    private static String apu = "https://apux.mixiangx.com";

    //获取验证码
    public static String getMsg = api + "/Sms/msgSend.html";
    //登录
    public static String login = apu + "/Login/login.html";
    //检测accToken
    public static String checkAccToken = apu + "/Login/checkAcc.html";
    //获取用户信息
    public static String getUserInfo = apu + "/User/userInfo.html";
    //收益列表
    public static String getProfitList = apu + "/User/lists.html";
    //获取上传图片列表
    public static String getUploadImagePath = apu + "/Upload/uploadUrlSet.html";
    //获取用户信息
    public static String getUploadUserInfo = apu + "/User/edit.html";

}
