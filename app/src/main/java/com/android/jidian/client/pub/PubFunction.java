package com.android.jidian.client.pub;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.jidian.client.BuildConfig;
import com.android.jidian.client.widgets.MyToast;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

public class PubFunction {
    /**
     * LEVEL = 1;线上
     * LEVEL = 2;仿真
     * LEVEL = 3;测试
     */
    private static final int LEVEL = BuildConfig.CUSTOMIZED_VERSION;

    private static final String SCHEME_HTTPS = "https://";
    private static final String SCHEME_HTTP = "http://";

    private static final String HOST_PRE_TEST = "test";
    private static final String HOST_PRE_FZ = "fz";
    /**
     * app.mixiangx.com   => appx.mixiangx.com
     * ape.mixiangx.com   => apex.mixiangx.com
     * api.mixiangx.com   => apix.mixiangx.com
     * h5.mixiangx.com    => h5x.mixiangx.com
     * apm.mixiangx.com   => apmx.mixiangx.com
     */
    private static final String HOST_APP = "appx.mixiangx.com/";
    private static final String HOST_API = "apix.mixiangx.com/";
    private static final String HOST_APM = "apmx.mixiangx.com/";
    private static final String HOST_APE = "apex.mixiangx.com/";
    private static final String HOST_H5 = "h5x.mixiangx.com/";

    public static String app;
    public static String api;
    public static String apm;
    public static String ape;
    public static String h5;

    public static String getLevel() {
        String s = "";
        //  online,           fz,               test
        // LEVEL = 1;线上    LEVEL = 2;仿真    LEVEL = 3;测试
        if (LEVEL == 1) {
            s = "online";
        } else if (LEVEL == 2) {
            s = "fz";
        } else if (LEVEL == 3) {
            s = "test";
        }
        return s;
    }

    static {
        switch (LEVEL) {
            case 2:
                app = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APP;
                api = SCHEME_HTTPS + HOST_PRE_FZ + HOST_API;
                apm = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APM;
                ape = SCHEME_HTTPS + HOST_PRE_FZ + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_PRE_FZ + HOST_H5;
                break;
            case 3:
                app = SCHEME_HTTP + HOST_PRE_TEST + HOST_APP;
                api = SCHEME_HTTP + HOST_PRE_TEST + HOST_API;
                apm = SCHEME_HTTP + HOST_PRE_TEST + HOST_APM;
                ape = SCHEME_HTTP + HOST_PRE_TEST + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_PRE_TEST + HOST_H5;
                break;
            default:
                app = SCHEME_HTTPS + HOST_APP;
                api = SCHEME_HTTPS + HOST_API;
                apm = SCHEME_HTTPS + HOST_APM;
                ape = SCHEME_HTTPS + HOST_APE;
                h5 = SCHEME_HTTPS + HOST_H5;
                break;
        }
    }

    public static String car_ID = "";
    /**
     * 目标坐标地址
     */
    public static double[] marker = null;
    /**
     * 自己坐标地址
     */
    public static double[] local = null;

    /**
     * 检查是否有网络
     */
    public static boolean isConnect(Context context, boolean b) {
        boolean _isConnect = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            _isConnect = conManager.getActiveNetworkInfo().isAvailable();
        }
        if (b) {
            if (!_isConnect) {
                MyToast.showTheToast(context, "无网络链接，请检查您的网络设置！");
            }
        }
        return _isConnect;
    }

    //设置girdview高度
    public static void setGridViewHeight(GridView gridView, int size, int numColumns) {
        try {
            ListAdapter adapter = gridView.getAdapter();

            int a = size;
            int b = a / numColumns;
            int c = a % numColumns;

            if (c == 0) {
                a = b;
            } else {
                a = b + 1;
            }

            int row = a;

            View listItem = adapter.getView(0, null, gridView);
            if (listItem == null)
                return;
            listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            int totalHeight = listItem.getMeasuredHeight() * row + (gridView.getVerticalSpacing() * (row - 1));
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    //动态设置listview高度
    public static void setListViewHeight(ListView listView) { //获取listView的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0; //listAdapter.getCount()返回数据项的数目
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));


        listView.setLayoutParams(params);
    }

    //获得版本名
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return localVersion;
    }

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return localVersion;
    }

    public static void setHandlerWhit_APTK_APUD(final Activity activity, final String type, final String uid, HttpPost httpPost) {
        Header header1 = new Header() {
            @Override
            public String getName() {
                return "aptk";
            }

            @Override
            public String getValue() {
                return Md5.getAptk();
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        httpPost.addHeader(header1);


        Header header2 = new Header() {
            @Override
            public String getName() {
                return "version";
            }

            @Override
            public String getValue() {
                return getLocalVersionName(activity);
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        httpPost.addHeader(header2);


        Header header3 = new Header() {
            @Override
            public String getName() {
                return "verName";
            }

            @Override
            public String getValue() {
                return getLocalVersion(activity) + "";
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        httpPost.addHeader(header3);

        Header header4 = new Header() {
            @Override
            public String getName() {
                return "osName";
            }

            @Override
            public String getValue() {
                return "Android";
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        httpPost.addHeader(header4);

        Header header6 = new Header() {
            @Override
            public String getName() {
                return "apud";
            }

            @Override
            public String getValue() {
                return uid;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
        httpPost.addHeader(header6);

    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static final String FROM_ACTOVITY = "fromActivity";
    /**
     * 首页站点商城购买支付
     */
    public static final String PURCHASE_SHOPPING_BUY = "PURCHASE_SHOPPING_BUY";
    /**
     * 首页站点商城租赁支付
     */
    public static final String LEASING_SHOPPING_LEASE = "LEASING_SHOPPING_LEASE";
    /**
     * 我的钱包续费
     */
    public static final String MY_WALLET_EWNEW = "MY_WALLET_EWNEW";
    /**
     * 我的钱包充值换电币
     */
    public static final String RECHARGE_HELLO_COIN = "RECHARGE_HELLO_COIN";
    /**
     * 我的订单待支付
     */
    public static final String MY_ORDER_PAID = "MY_ORDER_PAID";
    /**
     * 支付类型 （以上五种）
     */
    public static String PAT_ING_TYPE = "";

    /**
     * "type":,"payer":"线下支付","type":10,"payer":""
     * 支付宝 1
     * 微信 2
     * 线下支付 100
     * 信用免押 10
     * 骑呗 20
     */
    public static final int PAT_TYPE_ALIPAY = 1;
    public static final int PAT_TYPE_WECHAT = 2;
    public static final int PAT_TYPE_OFFLINE = 100;
    public static final int PAT_TYPE_CREDITFREE = 10;
    public static final int PAT_TYPE_RIDE = 20;

    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

}