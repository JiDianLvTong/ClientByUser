package com.android.jidian.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.MainActivity;
import com.android.jidian.client.mvp.ui.activity.NewWalletActivity;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESS;
import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDER;

/**
 * 支付确认页面
 * mFromActivity
 * PubFunction.PURCHASE_SHOPPING_BUY 购买支付
 * PubFunction.LEASING_SHOPPING_LEASE 租赁支付
 * PubFunction.MY_WALLET_EWNEW 续费支付
 * ubFunction.RECHARGE_HELLO_COIN 充值换电币支付
 */
@EActivity(R.layout.activity_order_confirm)
public class OrderConfirm extends BaseActivity {
    @ViewById
    LinearLayout data_panel, no_data_panel, tv_order_confirm_more_type;
    @ViewById
    TextView rest_time, price, submit, tv_title;
    @ViewById
    ListView listview;

    private String orderno;
    private MyBaseAdapter myBaseAdapter;
    private int payType = 1;
    //这是分钟
    private long minute = 15;
    //这是分钟后面的秒数
    private long second = 00;
    private Timer timer;
    private TimerTask timerTask;
    private String otype = "";
    private String mFromActivity = "";
    private String restTimeStr = "支付剩余时间 ";

    //这是接收回来处理的消息
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("xiaoming812", "handleMessage: ");
            if (minute == 0) {
                if (second == 0) {
                    data_panel.setVisibility(View.GONE);
                    no_data_panel.setVisibility(View.VISIBLE);
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        rest_time.setText(restTimeStr + "0" + minute + ":" + second);
                    } else {
                        rest_time.setText(restTimeStr + "0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        rest_time.setText(restTimeStr + minute + ":" + second);
                    } else {
                        rest_time.setText(restTimeStr + "0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            rest_time.setText(restTimeStr + minute + ":" + second);
                        } else {
                            rest_time.setText(restTimeStr + "0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            rest_time.setText(restTimeStr + minute + ":0" + second);
                        } else {
                            rest_time.setText(restTimeStr + "0" + minute + ":0" + second);
                        }
                    }
                }
            }
        }

    };

    @AfterViews
    void afterview() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        orderno = getIntent().getStringExtra("orderno");
        mFromActivity = getIntent().getStringExtra(PubFunction.FROM_ACTOVITY);
        tv_title.setText("支付订单");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PubFunction.PURCHASE_SHOPPING_BUY.equals(mFromActivity)) {
                    //购买支付页访问
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_PURCHASE_PAYMENT);
                } else if (PubFunction.LEASING_SHOPPING_LEASE.equals(mFromActivity)) {
                    //租赁支付页访问
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_LEASE_PAYMENT);
                } else if (PubFunction.MY_WALLET_EWNEW.equals(mFromActivity)) {
                    //续费支付页面访问
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_RENEWAL_PAYMENT);
                } else if (PubFunction.RECHARGE_HELLO_COIN.equals(mFromActivity)) {
                    //换电币支付页面
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_HELLO_COIN_PAYMENT);
                }
            }
        }, 500);
    }

    @Click
    void iv_back() {
        this.finish();
    }

    @Click
    void to_wallte() {
        startActivity(new Intent(activity, MainActivity.class));
        this.finish();
    }

    @Click
    void submit() {
        switch (payType) {
            case PubFunction.PAT_TYPE_ALIPAY:
                //点击确认支付按钮（支付宝）
                if (PubFunction.PURCHASE_SHOPPING_BUY.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_ALIPAY);
                } else if (PubFunction.LEASING_SHOPPING_LEASE.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_LEASE_PAYMENT_ALIPAY);
                } else if (PubFunction.MY_WALLET_EWNEW.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_ALIPAY);
                }
                break;
            case PubFunction.PAT_TYPE_WECHAT:
                //点击确认支付按钮（微信）
                if (PubFunction.PURCHASE_SHOPPING_BUY.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_WECHAT);
                } else if (PubFunction.LEASING_SHOPPING_LEASE.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_WECHAT);
                } else if (PubFunction.MY_WALLET_EWNEW.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_WECHAT);
                }
                break;
            case PubFunction.PAT_TYPE_OFFLINE:
                //点击确认支付按钮（线下支付）
                if (PubFunction.PURCHASE_SHOPPING_BUY.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_OFFLINE);
                } else if (PubFunction.LEASING_SHOPPING_LEASE.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_OFFLINE);
                } else if (PubFunction.MY_WALLET_EWNEW.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_OFFLINE_PAYMENT);
                }
                break;
            case PubFunction.PAT_TYPE_CREDITFREE:
                //点击确认支付按钮（信用免押）
                if (PubFunction.LEASING_SHOPPING_LEASE.equals(mFromActivity)) {
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_CREDIT_FREE);
                }
                break;
            default:
                break;
        }
        PubFunction.PAT_ING_TYPE = mFromActivity;
        new MainPay(activity, payType + "", orderno, uid, otype, 1, 1, 1, 1, 1, 1, 1, 1);
    }

    @Background
    void HttpPayBefpay() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("orderno", orderno));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befpay.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpPayBefpay(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpPayBefpay(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                String status = jsonObject1.getString("status");
                otype = jsonObject1.getString("otype");
                String msg = jsonObject1.getString("msg");
                String guoqi = jsonObject1.getString("guoqi");
                if ("1".equals(status)) {
                    String rprice = jsonObject1.getString("rprice");
                    price.setText("¥" + rprice);
                    submit.setText("确认支付 ¥" + rprice + "元");
                    JSONArray jsonArray = jsonObject1.getJSONArray("payList");
                    final List<Map<String, String>> datalist = new ArrayList<>();
                    int wxIndex = -1;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("type", jsonObject.getString("type"));
                        map.put("payer", jsonObject.getString("payer"));
                        if (jsonObject.has("img_url")) {
                            map.put("img_url", jsonObject.getString("img_url"));
                        } else {
                            map.put("img_url", "");
                        }
//                        if (i == 0) {
//                            payType = Integer.parseInt(jsonObject.getString("type"));
//                        }
                        if (jsonObject.getString("type").equals("2")) {
                            wxIndex = i;
                        }
                        datalist.add(map);
                    }
                    if (wxIndex != -1) {
                        Map<String, String> wxMap = datalist.get(wxIndex);
                        datalist.remove(wxIndex);
                        datalist.add(wxMap);
                    }
                    for (int i = 0; i < datalist.size(); i++) {
                        if (i == 0) {
                            payType = Integer.parseInt(datalist.get(0).get("type"));
                        }
                    }
                    myBaseAdapter = new MyBaseAdapter(activity, datalist, R.layout.main_shop_order_item);
                    listview.setAdapter(myBaseAdapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (!myBaseAdapter.getShowWxChatPay() && position == datalist.size() - 1) {
                                myBaseAdapter.setShowWxChatPay(true);
                                myBaseAdapter.notifyDataSetChanged();
                                return;
                            }
                            myBaseAdapter.set_select(position);
                            myBaseAdapter.notifyDataSetChanged();
                            payType = Integer.parseInt(datalist.get(position).get("type"));
                        }
                    });
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //获取当前时间
                    Date startDate = null;
                    Date endDate = null;
                    try {
                        startDate = new Date(System.currentTimeMillis());
                        endDate = simpleDateFormat1.parse(guoqi);
                        getTimeExpend(startDate, endDate);
                        rest_time.setText(restTimeStr + minute + ":" + second);
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            }
                        };
                        timer = new Timer();
                        timer.schedule(timerTask, 0, 1000);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("xiaoming812", "onDataHttpPayBefpay: " + e.getLocalizedMessage());
                    }
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTimeExpend(Date startTime, Date endTime) {
        //传入字串类型 2016/06/28 08:30:33
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar1.setTime(endTime);
        long longStartMin = calendar.get(Calendar.MINUTE);
        long longStartSen = calendar.get(Calendar.SECOND);
        long longEndMin = calendar1.get(Calendar.MINUTE);
        long longEndSen = calendar1.get(Calendar.SECOND);
        Log.d("xiaoming812", "getTimeExpend: " + longStartMin + "  " + longEndMin + "  " + longStartSen + "  " + longEndSen);

        minute = longEndMin - longStartMin;
        second = longEndSen - longStartSen;
        if (second < 0) {
            second = 60 + second;
            minute -= 1;
        }
        if (minute < 0) {
            minute = 60 + minute;
        }

        Log.d("xiaoming812", "getTimeExpend: " + minute + "  " + second);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESS) {
                if (Util.isTopActivity("OrderConfirm", this)) {
                    finish();
                    EventBus.getDefault().post(new MainAppEventBean(PAYSUCCESSCLOSEORDER));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        minute = -1;
        second = -1;
        HttpPayBefpay();
    }

    class MyBaseAdapter extends BaseAdapter {
        private Activity activity;
        private List<Map<String, String>> data;
        private int layout;

        private int is_select = 0;
        private boolean showWxChatPay = false;

        public MyBaseAdapter(Activity activity, List<Map<String, String>> data, int layout) {
            this.activity = activity;
            this.data = data;
            this.layout = layout;
        }

        public void set_select(int i) {
            this.is_select = i;
        }

        public void setShowWxChatPay(boolean b) {
            this.showWxChatPay = b;
        }

        public boolean getShowWxChatPay() {
            return showWxChatPay;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(activity, layout, null);
                viewHolder = new ViewHolder();
                viewHolder.t_1 = convertView.findViewById(R.id.t_1);
                viewHolder.i_2 = convertView.findViewById(R.id.i_2);
                viewHolder.i_1 = convertView.findViewById(R.id.i_1);
                viewHolder.tv_order_confirm_more_type = convertView.findViewById(R.id.tv_order_confirm_more_type);
                viewHolder.tv_order_confirm_type_one = convertView.findViewById(R.id.tv_order_confirm_type_one);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(data.get(position).get("img_url"))) {
                Glide.with(activity).load(data.get(position).get("img_url")).into(viewHolder.i_1);
            }
            if (position == is_select) {
                Glide.with(activity).load(R.drawable.select_paytype2).into(viewHolder.i_2);
            } else {
                Glide.with(activity).load(R.drawable.select_paytype1).into(viewHolder.i_2);
            }
            viewHolder.t_1.setText(data.get(position).get("payer"));
//            if (!showWxChatPay && position == data.size() - 1) {
//                viewHolder.tv_order_confirm_more_type.setVisibility(View.VISIBLE);
//            }
            viewHolder.tv_order_confirm_type_one.setVisibility((!showWxChatPay && position == data.size() - 1) ? View.GONE : View.VISIBLE);
            viewHolder.tv_order_confirm_more_type.setVisibility((!showWxChatPay && position == data.size() - 1) ? View.VISIBLE : View.GONE);
            return convertView;
        }

        private class ViewHolder {
            TextView t_1;
            ImageView i_1;
            ImageView i_2;
            LinearLayout tv_order_confirm_more_type;
            LinearLayout tv_order_confirm_type_one;
        }
    }
}