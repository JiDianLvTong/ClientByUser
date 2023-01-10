package com.android.jidian.repair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.jidian.repair.base.PermissionManager.PermissionManager;
import com.android.jidian.repair.utils.UserInfoHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:15
 * description:
 */
public class Application extends android.app.Application {
    private static Application context;
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

        mContext = getApplicationContext();

        //权限管理初始化
        PermissionManager.getInstance().init(getApplicationContext());
        //本地储存初始化 - 用户sp
//        SpUser.getInstance().init(getApplicationContext());

        UserInfoHelper.init(this);
    }

    public static Application getContext() {
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
