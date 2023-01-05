package com.android.jidian.client.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;

import com.android.jidian.client.mvp.ui.dialog.SelectNavigationModeDialog;
import com.android.jidian.client.widgets.MyToast;

import java.io.File;

/**
 * @author : PTT
 * date: 2020/11/06 15:56
 * company: 兴达智联
 * description: 地图有关
 */
public class MapUtil {

    /**
     * 高德地图包名
     */
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";
    /**
     * 百度地图包名
     */
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap";
    /**
     * 腾讯地图包名
     */
    public static final String PN_TENCENT_MAP = "com.tencent.map";

    /**
     * 打开选择导航弹框
     */
    public static void showNavigationDialog(Context context, FragmentManager fragmentManager, String name, String lat, String lng) {
        if (context == null) {
            return;
        }
        boolean hasGaode = isGdMapInstalled();
        boolean hasBaidu = isBaiduMapInstalled();
        boolean hasTencent = isTencentMapInstalled();
        if (!hasGaode && !hasBaidu && !hasTencent) {
            MyToast.showTheToast(context, "没有安装任何地图应用");
            return;
        }
        new SelectNavigationModeDialog().init(hasGaode, hasBaidu, hasTencent, new SelectNavigationModeDialog.OnDismissListener() {
            @Override
            public void onDismiss(String selectMap) {
                if ("GaoDe".equals(selectMap)) {
                    openGaoDeMapCyclingNavi(context, name, lat, lng);
                } else if ("Baidu".equals(selectMap)) {
                    openBaiduMapCyclingNavi(context, name, lat, lng);
                } else if ("tencent".equals(selectMap)) {
                    openTencentMapCyclingNavi(context, name, lat, lng);
                }
            }
        }).setPosition(Gravity.BOTTOM).setWidth(1).setOutCancel(false).show(fragmentManager);
    }

    /**
     * 检查地图应用是否安装
     */
    public static boolean isBaiduMapInstalled() {
        return isInstallPackage(PN_BAIDU_MAP);
    }

    public static boolean isGdMapInstalled() {
        return isInstallPackage(PN_GAODE_MAP);
    }

    public static boolean isTencentMapInstalled() {
        return isInstallPackage(PN_TENCENT_MAP);
    }

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 打开百度地图骑行路线规划(http://lbsyun.baidu.com/index.php?title=uri/api/android)
     * URL接口：baidumap://map/direction
     * destination:终点名称和经纬度name:天安门|latlng:39.98871,116.43234
     * coord_type:坐标类型，必选参数。bd09ll（百度经纬度坐标）
     * mode:导航模式riding（骑行）
     * src:统计来源，必选参数。参数格式为：andr.companyName.appName
     */
    public static void openBaiduMapCyclingNavi(Context context, String name, String lat, String lng) {
        if (isBaiduMapInstalled()) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("baidumap://map/direction?destination=name:" + name + "|latlng:" + lat + "," + lng
                    + "&coord_type=gcj02&mode=riding&src=andr.xingdazhilian.HelloDream"));
            context.startActivity(i1);
        } else {
            MyToast.showTheToast(context, "百度地图未安装");
        }
    }

    /**
     * 打开高德地图骑行路线规划(https://lbs.amap.com/api/amap-mobile/guide/android/route)
     * URL接口：amapuri://route/plan/
     * sourceApplication:第三方调用应用名称。如 amap
     * dname:终点名称
     * dname:终点名称
     * dlon:终点经度
     * dev:起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * t:0驾车、1公交、2步行、3骑行、4火车、5长途客车（骑行仅在V7.8.8以上版本支持）
     */
    public static void openGaoDeMapCyclingNavi(Context context, String name, String lat, String lng) {
        if (isGdMapInstalled()) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("amapuri://route/plan/?sourceApplication=maxuslife&dname=" + name + "&dlat=" + lat + "&dlon=" + lng
                    + "&dev=0&t=3"));
            context.startActivity(i1);
        } else {
            MyToast.showTheToast(context, "高德地图未安装");
        }
    }

    /**
     * 打开腾讯地图骑行路线规划(https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute)
     * URL接口：qqmap://map/routeplan
     * type:路线规划方式参数：公交：bus 驾车：drive 步行：walk 骑行：bike
     * to:终点名称 to=奥林匹克森林公园
     * tocoord:终点坐标 tocoord=40.010024,116.392239
     * referer:请填写开发者key referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
     */
    public static void openTencentMapCyclingNavi(Context context, String name, String lat, String lng) {
        if (isTencentMapInstalled()) {
            Intent i1 = new Intent();
            //qqmap://map/routeplan?type=drive&from=清华&fromcoord=39.994745,116.247282&to=怡和世家&tocoord=39.867192,116.493187&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
            i1.setData(Uri.parse("qqmap://map/routeplan?type=bike&to=" + name + "&tocoord=" + lat + "," + lng
                    + "&referer=zhongshuo"));
            context.startActivity(i1);
        } else {
            MyToast.showTheToast(context, "腾讯地图未安装");
        }
    }
}