package com.android.jidian.client.base;

import android.content.Context;
import android.location.LocationManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * @author : PTT
 * date: 2020/8/27 14:15
 * company: 兴达智联
 * description: 定位管理
 */
public class AppLocationManager {

    /**
     * 判断定位设置是否打开
     * true是 false否
     */
    public static boolean isLocationEnabled(Context mContext) {
        if (mContext == null) {
            return false;
        }
        LocationManager lm = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}