package com.android.jidian.client.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.android.jidian.client.base.PermissionManager.PermissionManager;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.UserInfoHelper;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static MyApplication context;
    public static int myWalletPanelSelect = -1;
    private static Context mContext;
    /*由于骑呗后台不稳定暂时下架，暂删 public static BridgeCallback mBridgeCallback = null;
    public static String qibeidata;*/

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println(SHA1(getApplicationContext()));
        mContext = getApplicationContext();

        //权限管理初始化
        PermissionManager.getInstance().init(getContext());











        /*
         * 初始化腾讯Bugly
         */
        context = this;
        //获取当前包名
        String packageName = context.getPackageName();
        //获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        //设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //初始化Bugly
        if (PubFunction.app.equals("https://appx.mixiangx.com/")) {
            CrashReport.initCrashReport(context, "6a0a4a0e67", false, strategy);
        }
        //制造一个Crash测试Bugly
//        CrashReport.testJavaCrash();

        UserInfoHelper.init(this);

        /*由于骑呗后台不稳定暂时下架，暂删 QiBei.init(new QiBeiCallback() {
            @Override
            public void scan(Activity activity) {//调用扫码
                Intent mIntent = new Intent(activity, ScanCodeActivity.class);
                mIntent.putExtra(ScanCodeActivity.IS_FROM, "MyApplication");
                activity.startActivityForResult(mIntent, QiBeiConstant.REQUEST_CODE_MERCHANTS_SCAN);
            }

            @Override
            public void alipay(Activity activity, BridgeCallback bridgeCallback, String data) {
                mBridgeCallback = bridgeCallback;
                qibeidata = data;
                new MainPay(activity, aaaa, vbbbb, cccc, data, "2", "3");
            }
        });*/

// 极光推送 start
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(false);
        // 初始化 JPush
        JPushInterface.init(this);
// 极光推送 end

// 极光认证 start
        JVerificationInterface.setDebugMode(true);
        JVerificationInterface.init(this);
// 极光认证 end

        UMConfigure.preInit(this,"619ef303e0f9bb492b6d5fe9","小明的渠道");
        UMConfigure.setLogEnabled(true);
        // 微信设置
        PlatformConfig.setWeixin("wx9a38e7cfbde9152f","fak73ls2df5a6s8dfd97asf8as87fd6d");
        PlatformConfig.setWXFileProvider("com.android.mixiang.client.fileprovider");


    }

    public static MyApplication getContext() {
        return context;
    }

    public static String SHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 全局获取上下文
     */
    public static Context getAppContext() {
        return mContext;
    }
}