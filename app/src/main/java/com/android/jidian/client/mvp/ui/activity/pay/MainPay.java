package com.android.jidian.client.mvp.ui.activity.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.jidian.client.Main;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.main.MainActivity;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.dialog.DialogByLoading;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.pub.zhifubao.PayResult;
import com.android.jidian.client.pub.zhifubao.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainPay {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    //支付宝信息
    private static String PARTNER = "";
    private static String SELLER = "";
    private static String RSA_PRIVATE = "";
    //订单信息
    private String pay_type;
    private String gid;
    private String uid;
    private String select_pack_month;

    private int is_qibei = 0;
    private boolean is_zhjima = false;
    private String otype = "";

    //其他参数
    private Activity activity;
    private DialogByLoading progressDialog;
    private Handler mHandler, succcessHandler, succcessNoDataHandler, errorHandler;
    private String price = "";
    private String trade_sn = "";
    private String notify_url = "";
    private String name = "";
    private String orderno;
    private String zhimaStep1 = "";
    private int zhimaAuthStep = 1;
    private String is_dep = "0";

    public MainPay(Activity activity, String pay_type, String gid, String uid, String select_pack_month) {
        this.activity = activity;
        this.pay_type = pay_type;
        this.gid = gid;
        this.uid = uid;
        this.select_pack_month = select_pack_month;
        handler();
        if (PubFunction.isConnect(activity, true)) {
            HttpGetOrderInfo_1();
            progressDialog = new DialogByLoading(activity);
            progressDialog.show();
        }
    }

    //钱包支付
    public MainPay(Activity activity, String pay_type, String gid, String uid, int a, int b, String select_pack_month) {
        this.activity = activity;
        this.pay_type = pay_type;
        this.gid = gid;
        this.uid = uid;
        this.select_pack_month = select_pack_month;
        handler();
        if (PubFunction.isConnect(activity, true)) {
            HttpGetOrderInfo_3();
            progressDialog = new DialogByLoading(activity);
            progressDialog.show();
        }
    }

    //钱包月租和包月一起支付
    public MainPay(Activity activity, String pay_type, String gid, String uid, int a, int b, int c, int d, int e, int f, int g, String select_pack_month) {
        this.activity = activity;
        this.pay_type = pay_type;
        this.gid = gid;
        this.uid = uid;
        this.select_pack_month = select_pack_month;
        handler();
        if (PubFunction.isConnect(activity, true)) {
            HttpGetOrderInfo_8();
            progressDialog = new DialogByLoading(activity);
            progressDialog.show();
        }
    }

    //生成订单之后支付
    public MainPay(Activity activity, String pay_type, String orderno, String uid, String otype, int a, int b, int c, int d, int e, int f, int g, int h) {
        this.activity = activity;
        this.pay_type = pay_type;
        this.orderno = orderno;
        this.uid = uid;
        this.otype = otype;
        handler();
        if (PubFunction.isConnect(activity, true)) {
            HttpGetOrderInfo_9();
            progressDialog = new DialogByLoading(activity);
            progressDialog.show();
        }
    }

    @SuppressLint("HandlerLeak")
    private void handler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);
//同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
//detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1) 建议商户依赖异步通知
                        String total_fee = "0.0";
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String[] results = resultInfo.split("&");
                        for (String s : results) {
                            if (s.startsWith("total_fee")) {
                                total_fee = s.substring(11, s.length() - 1);
                            }
                        }
                        System.out.println(resultInfo);

                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            MyToast.showTheToast(activity, "支付成功！");
                            EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.LOGIN_SUCCESS));
//                            支付宝支付成功
                            if (!TextUtils.isEmpty(PubFunction.PAT_ING_TYPE)) {
                                switch (PubFunction.PAT_ING_TYPE) {
                                    case PubFunction.PURCHASE_SHOPPING_BUY:
                                        //商城购买支付
                                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_ALIPAY_SUCCESS);
                                        break;
                                    case PubFunction.LEASING_SHOPPING_LEASE:
                                        //商城租赁支付
                                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_ALIPAY_SUCCESS);
                                        break;
                                    case PubFunction.MY_WALLET_EWNEW:
                                        //我的钱包续费
                                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_ALIPAY_SUCCESS);
                                        break;
                                    case PubFunction.RECHARGE_HELLO_COIN:
                                        //充值换电币
                                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HELLO_COIN_PAYMENT_ALIPAY_SUCCESS);
                                        break;
                                    case PubFunction.MY_ORDER_PAID:
                                        //我的订单待支付
                                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ORDER_TO_BE_PAID_ALIPAY_SUCCESS);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (is_qibei == 1) {
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("status", 1 + "");
                                message.setData(bundle);
                                Main.qibeiHandler.sendMessage(message);
                            } else {
                                if (is_zhjima) {
                                    if ("61".equals(otype)) {
                                        if ("1".equals(is_dep)) {
                                            if (zhimaAuthStep == 1) {
                                                pay(zhimaStep1);
                                                zhimaAuthStep++;
                                            } else {
                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                            }
                                        } else {
                                            if (zhimaAuthStep == 1) {
                                                pay(zhimaStep1);
                                                zhimaAuthStep++;
                                            } else {
                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                            }
                                        }
                                    } else {
                                        activity.startActivity(new Intent(activity, MainActivity.class));
                                        activity.finish();
                                    }
                                } else {
                                    activity.startActivity(new Intent(activity, MainActivity.class));

                                    if (!uid.isEmpty()) {
                                        List<ParamTypeData> dataList1 = new ArrayList<>();
                                        dataList1.add(new ParamTypeData("sourceType", "5"));
                                        dataList1.add(new ParamTypeData("sourceId", uid));
                                        if (is_zhjima) {//预授权
                                            dataList1.add(new ParamTypeData("type", "300"));
                                            dataList1.add(new ParamTypeData("title", "确认授权"));
                                        } else {//普通支付
                                            dataList1.add(new ParamTypeData("type", "201"));
                                            dataList1.add(new ParamTypeData("title", "支付宝支付"));
                                            dataList1.add(new ParamTypeData("amount", total_fee));
                                        }
                                        dataList1.add(new ParamTypeData("client", "Android"));
                                        HttpVisitLogs(dataList1);
                                    }
                                    activity.finish();
                                }
                            }
                        } else {
                            if (is_qibei == 1) {
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("status", 0 + "");
                                message.setData(bundle);
                                Main.qibeiHandler.sendMessage(message);
                            } else if (is_zhjima) {
                                MyToast.showTheToast(activity, "支付失败！");
                            } else {
                                // 判断resultStatus 为非"9000"则代表可能支付失败
                                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                if (TextUtils.equals(resultStatus, "8000")) {
                                    MyToast.showTheToast(activity, "支付结果确认中");
                                } else {
                                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                    MyToast.showTheToast(activity, "支付失败！");
//                                    activity.startActivity(new Intent(activity, OrderListActivity.class));
//                                    activity.finish();
                                }
                            }
                        }
                        break;
                    }
                    case SDK_CHECK_FLAG: {
                        Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        succcessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.dismiss();
                if ("100".equals(pay_type)) {
//                  线下支付成功
                    if (!TextUtils.isEmpty(PubFunction.PAT_ING_TYPE)) {
                        switch (PubFunction.PAT_ING_TYPE) {
                            case PubFunction.PURCHASE_SHOPPING_BUY:
                                //商城购买支付
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_OFFLINE_SUCCESS);
                                break;
                            case PubFunction.LEASING_SHOPPING_LEASE:
                                //商城租赁支付
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_OFFLINE_SUCCESS);
                                break;
                            case PubFunction.MY_WALLET_EWNEW:
                                //我的钱包续费
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_OFFLINE_PAYMENT_SUCCESS);
                                break;
                            default:
                                break;
                        }
                    }
                    MyToast.showTheToast(activity, msg.getData().getString("msg"));

                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    return;
                }
                String data = msg.getData().getString("data");
                switch (pay_type) {
                    case "1":
                        try {
                            JSONTokener jsonTokener = new JSONTokener(data);
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            //预授权支付宝
//                            if (jsonObject.has("code_str") && !jsonObject.get("code_str").equals(JSONObject.NULL)) {
//                                String code_str = jsonObject.getString("code_str");
//                                pay(code_str);
//                            } else {//非预授权支付宝
                            price = jsonObject.getString("order_fee");//价格
                            trade_sn = jsonObject.getString("order_num");
                            PARTNER = jsonObject.getString("partner");//APPID
                            SELLER = jsonObject.getString("seller");
                            RSA_PRIVATE = jsonObject.getString("app_private_key");
                            notify_url = jsonObject.getString("notify_url");//通知服务器的地址
                            name = jsonObject.getString("order_title");

                            if (jsonObject.has("code_str")) {
                                String info = jsonObject.getString("code_str");
                                if (!TextUtils.isEmpty(info)) {
                                    pay(info);
                                } else {
                                    pay();
                                }
                            } else {
                                pay();
                            }

//                            }
                        } catch (JSONException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        break;
                    case "10":
                        try {
                            JSONTokener jsonTokener = new JSONTokener(data);
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            is_zhjima = true;
                            if ("61".equals(otype)) {
                                //预授权支付宝
                                String code_str = jsonObject.getString("code_str");
                                zhimaStep1 = jsonObject.getJSONObject("auth").getString("code_str");
                                is_dep = jsonObject.getString("is_dep");
                                if ("1".equals(is_dep)) {
                                    pay(zhimaStep1);
                                    zhimaAuthStep++;
                                } else {
                                    pay(code_str);
                                }
                            } else {
                                String code_str = jsonObject.getString("code_str");
                                pay(code_str);
                            }
                            //信用免押支付成功
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_CREDIT_FREE_SUCCESS);
                        } catch (JSONException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        break;
                    case "2":
                        //微信支付only
                        try {
                            JSONTokener jsonTokener = new JSONTokener(data);
                            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                            String appid = jsonObject.getString("appid");
                            String partnerid = jsonObject.getString("partnerid");
                            String prepayid = jsonObject.getString("prepayid");
                            String package_ = jsonObject.getString("package");
                            String noncestr = jsonObject.getString("noncestr");
                            String timestamp = jsonObject.getString("timestamp");
                            String sign = jsonObject.getString("sign");
                            String orderNum = jsonObject.getString("order_num");
                            trade_sn = orderNum;
                            IWXAPI api = WXAPIFactory.createWXAPI(activity, appid);
                            if (!api.isWXAppInstalled()) {
                                Toast.makeText(activity, "请先安装微信应用", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    if (!jsonObject.has("retmsg")) {
                                        PayReq req = new PayReq();
                                        req.appId = appid;
                                        req.partnerId = partnerid;
                                        req.prepayId = prepayid;
                                        req.packageValue = package_;
                                        req.nonceStr = noncestr;
                                        req.timeStamp = timestamp;
                                        req.sign = sign;
                                        req.extData = "app data"; // optional
//						Toast.makeText(getApplication(), "正常调起支付", Toast.LENGTH_SHORT).show();
                                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                        api.sendReq(req);
                                    } else {
                                        Log.d("PAY_GET", "返回错误" + jsonObject.getString("retmsg"));
                                        MyToast.showTheToast(activity, "返回错误" + jsonObject.getString("retmsg"));
                                    }
                                } catch (JSONException e) {
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        break;
                    case "20":
                        //由于骑呗后台不稳定暂时下架，暂删
                        break;
                    case "3":
                        break;
                    default:
                        break;
                }
            }
        };
        errorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgStr = msg.getData().getString("msg");
                MyToast.showTheToast(activity, msgStr);
                progressDialog.dismiss();
            }
        };
        succcessNoDataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String msgStr = msg.getData().getString("msg");
                MyToast.showTheToast(activity, msgStr);
                progressDialog.dismiss();
                activity.finish();
            }
        };
    }

    /**
     * 支付宝信息
     * call alipay sdk pay. 调用SDK支付
     */
    private void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    activity.finish();
                }
            }).show();
            return;
        }
        String orderInfo = getOrderInfo(name, price, price);
        //特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
        String sign = sign(orderInfo);
        try {
            //仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getLocalizedMessage());
        }
        //完整的符合支付宝参数规范的订单信息
        //项目原来正确的    测试完骑呗支付  要打开
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        //小明写的  测试骑呗支付
//        final String payInfo = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2019081666256101&biz_content=%7B%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%222017090080001939229%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22App%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95Java%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=%E5%95%86%E6%88%B7%E5%A4%96%E7%BD%91%E5%8F%AF%E4%BB%A5%E8%AE%BF%E9%97%AE%E7%9A%84%E5%BC%82%E6%AD%A5%E5%9C%B0%E5%9D%80&sign=YdaeFoql9LERJCfUumWZzX6VKFQEMcwjWXaHxoHTXnLfh6K4o1P%2BMUSACG3zexvwcSYZ7%2B6BOb59hTXf7EHZH8dNYVge0o6dkRGTNCwsmKHv9SbYxbZmucSeHm9HVxtOGlZcJvUnyWBjQxNo79LzmJJ6SmhvkxU3%2FD1b2byZ0MKBOAQp6zKBs4N4s14ublwHKD7XGUV8wuTJrYfQFem%2B41%2FqZ3V1y%2FaUwC1Q9Esx87L5ZRnwvv6tLrBUrK5Op170RZ0XUllauxHMVKSjQSw%2BTa73g%2B63nYzgJOX0asJxMfpxfJMs6Ksbrf05SN3jWXIDcW1j1nB5bD8sNiEpu%2Fh9OQ%3D%3D&sign_type=RSA2&timestamp=2019-11-05+10%3A22%3A02&version=1.0";
//        final String payInfo = "alipay_sdk=alipay-sdk-php-20180705&app_id=2017112400137717&biz_content=%7B%22out_order_no%22%3A%22test201903271118262426%22%2C%22out_request_no%22%3A%22test201903271118262426%22%2C%22order_title%22%3A%221234567%22%2C%22amount%22%3A%220.01%22%2C%22product_code%22%3A%22PRE_AUTH_ONLINE%22%2C%22payee_user_id%22%3A%222088821648184781%22%2C%22extra_param%22%3A%22%7B%5C%22category%5C%22%3A%5C%22RENT_CAR_GOODS%5C%22%7D%22%2C%22enable_pay_channels%22%3A%22%5B%7B%5C%22payChannelType%5C%22%3A%5C%22PCREDIT_PAY%5C%22%7D%2C%7B%5C%22payChannelType%5C%22%3A%5C%22MONEY_FUND%5C%22%7D%2C%7B%5C%22payChannelType%5C%22%3A%5C%22CREDITZHIMA%5C%22%7D%5D%22%7D&charset=UTF-8&format=json&method=alipay.fund.auth.order.app.freeze&notify_url=%2FCeshi%2Fnotify.html&sign_type=RSA2&timestamp=2019-03-27+11%3A18%3A26&version=1.0&sign=hjQHp4GebUgUx8XHCSp3uL8nr%2BoVspZzaiAHEJlBJCysfK%2F9zjXQsgYrY65IaD06ru3FGy%2FKTGA6keQfKLOHIcmBbL5QELfL%2FsICUEbm%2FW4YovKcDZw9IyW2k5FwYnarxm2P4CRKo5DZTtY8ELJ1mZkPjVUeL%2FwxSLBalqKUsd5FS2FaGRdMbbMdfPTkUxhKkgNRj4VMq%2FtdKWqzaF3sRyyI4it2sv39M%2FrNEYocuItuaivYmMRhqhCc7EU%2FG0evjlinHoD%2BGI5h6Lirrf6bFSWM6ivBoYkwGK0cD8xSzzEloohTSKklxcgjjJ10p5GtcZ2FTTzGZ%2Bh%2FcXwoRqcBLQ%3D%3D";
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝信息
     * call alipay sdk pay. 调用SDK支付
     */
    private void pay(String info) {
        final String payInfo = info;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + trade_sn + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private void sendMessage(Handler handler, String str) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msg", str);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendMessage(Handler handler, String str, String data) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msg", str);
        bundle.putString("data", data);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * http接口：   获取订单信息
     */
    private void HttpGetOrderInfo_1() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("ptype", pay_type));
        dataList.add(new ParamTypeData("gid", gid));
        dataList.add(new ParamTypeData("froms", "30"));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/initv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetOrderInfo_8(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    private void HttpGetOrderInfo_3() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("ptype", pay_type));
        dataList.add(new ParamTypeData("mjson", gid));
        dataList.add(new ParamTypeData("froms", "20"));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/initv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetOrderInfo_8(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    private void HttpGetOrderInfo_8() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("ptype", pay_type));
        dataList.add(new ParamTypeData("mjson", gid));
        dataList.add(new ParamTypeData("froms", "40"));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/initv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetOrderInfo_8(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    private void HttpGetOrderInfo_9() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("ptype", pay_type));
        dataList.add(new ParamTypeData("orderno", orderno));
        new OkHttpConnect(activity, PubFunction.api + "Pay/aftpay.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetOrderInfo_8(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    private void onDataHttpGetOrderInfo_8(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            sendMessage(errorHandler, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    if (jsonObject_response.has("data")) {
                        String data = jsonObject_response.getString("data");
                        sendMessage(succcessHandler, msg, data);
                    } else {
                        sendMessage(succcessNoDataHandler, msg, "");
                    }
                } else {
                    sendMessage(errorHandler, msg);
                }
            } catch (Exception e) {
                sendMessage(errorHandler, "JSON：" + e.toString());
            }
        }
    }

    private void HttpVisitLogs(List<ParamTypeData> dataList) {
        new OkHttpConnect(activity, PubFunction.api + "VisitLogs/index.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    private void onVisitLogs(String response, String type) {
        if ("0".equals(type)) {
            System.out.println(response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                System.out.println(msg);
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}