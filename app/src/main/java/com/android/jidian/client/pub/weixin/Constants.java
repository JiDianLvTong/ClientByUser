package com.android.jidian.client.pub.weixin;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Constants {
    /**
     * APP_ID 替换为你的应用从官方网站申请到的合法appId
     */
//    public static final String APP_ID = "wx3abaaaaf0a0e0d57";
    public static String APP_ID = "wx691a6d940882daa1";

    /**
     * 判断微信是否安装
     *
     * @param context
     * @return true 已安装   false 未安装
     */
    public static boolean isWxAppInstalled(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(APP_ID);
        boolean bIsWXAppInstalled = false;
        bIsWXAppInstalled = wxApi.isWXAppInstalled();
        return bIsWXAppInstalled;
    }
}