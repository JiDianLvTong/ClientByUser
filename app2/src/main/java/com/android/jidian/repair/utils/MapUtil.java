package com.android.jidian.repair.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.android.jidian.repair.widgets.dialog.DialogByToast;
import com.android.jidian.repair.widgets.dialog.DialogBySelectNavigation;

import java.io.File;
import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:52
 * description:
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
    public static void showNavigationDialog(Context context, String name, String lat, String lng) {
        if (context == null) {
            return;
        }
        boolean hasGaode = isGdMapInstalled(context);
        boolean hasBaidu = isBaiduMapInstalled(context);
        boolean hasTencent = isTencentMapInstalled(context);
        if (!hasGaode && !hasBaidu && !hasTencent) {
            DialogByToast.showTheToast(context, "没有安装任何地图应用");
            return;
        }
        DialogBySelectNavigation dialog = new DialogBySelectNavigation(context, hasGaode, hasBaidu, hasTencent, new DialogBySelectNavigation.DialogChoiceListener() {
            @Override
            public void onChoose(String selectType) {
                if ("GaoDe".equals(selectType)) {
                    openGaoDeMapCyclingNavi(context, name, lat, lng);
                } else if ("Baidu".equals(selectType)) {
                    openBaiduMapCyclingNavi(context, name, lat, lng);
                } else if ("tencent".equals(selectType)) {
                    openTencentMapCyclingNavi(context, name, lat, lng);
                }
            }

        });
        dialog.showPopupWindow();
    }

    /**
     * 检查地图应用是否安装
     */
    public static boolean isBaiduMapInstalled(Context context) {
        return checkAppInstalled(context, PN_BAIDU_MAP);
    }

    public static boolean isGdMapInstalled(Context context) {
        return checkAppInstalled(context, PN_GAODE_MAP);
    }

    public static boolean isTencentMapInstalled(Context context) {
        return checkAppInstalled(context, PN_TENCENT_MAP);
    }

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
//        for (int i = 0; i < info.size(); i++) {
//            Log.d("xiaoming", "checkAppInstalled: " + info.get(i).packageName);
//        }
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
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
        if (isBaiduMapInstalled(context)) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("baidumap://map/direction?destination=name:" + name + "|latlng:" + lat + "," + lng
                    + "&coord_type=gcj02&mode=riding&src=andr.xingdazhilian.HelloDream"));
            context.startActivity(i1);
        } else {
            DialogByToast.showTheToast(context, "百度地图未安装");
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
        if (isGdMapInstalled(context)) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("amapuri://route/plan/?sourceApplication=maxuslife&dname=" + name + "&dlat=" + lat + "&dlon=" + lng
                    + "&dev=0&t=3"));
            context.startActivity(i1);
        } else {
            DialogByToast.showTheToast(context, "高德地图未安装");
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
        if (isTencentMapInstalled(context)) {
            Intent i1 = new Intent();
            //qqmap://map/routeplan?type=drive&from=清华&fromcoord=39.994745,116.247282&to=怡和世家&tocoord=39.867192,116.493187&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
            i1.setData(Uri.parse("qqmap://map/routeplan?type=bike&to=" + name + "&tocoord=" + lat + "," + lng
                    + "&referer=zhongshuo"));
            context.startActivity(i1);
        } else {
            DialogByToast.showTheToast(context, "腾讯地图未安装");
        }
    }
}
