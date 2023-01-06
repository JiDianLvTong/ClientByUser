package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.MainActivity;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

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
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDERLISTPAGE;

/**
 * Created by hasee on 2017/6/6.
 */
@SuppressLint("Registered")
@EActivity(R.layout.main_message_1)
public class MainMessage_1 extends BaseActivity implements AbsListView.OnScrollListener {
    @ViewById
    LinearLayout none_panel,page_return;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    private List<Map<String, String>> dataList = new ArrayList<>();
    private MySimpleAdapter_message_1 mySimpleAdapter_message_1;
    private String lastid = "";
    private int count_page = 2;
    private int current_page = 1;

    @AfterViews
    void afterviews() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        init();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //我的订单页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MY_ORDER);
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESSCLOSEORDERLISTPAGE) {
                if (Util.isTopActivity("MainMessage_1", this)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Click
    void page_return() {
        this.finish();
    }

    private void init() {
        refresh.setCanLoadMore(false);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                lastid = "";
                count_page = 2;
                current_page = 1;
                dataList.clear();
                HttpMessage_1();
            }

            @Override
            public void loadMore() {
            }
        });
        listview.setOnScrollListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastid = "";
        count_page = 2;
        current_page = 1;
        HttpMessage_1();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && current_page < count_page) {
                HttpMessage_1_RE(lastid);
                progressDialog.show();
                current_page = count_page;
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    /**
     * http接口：User/uOrder.html   获取消息信息
     */
    @Background
    void HttpMessage_1() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "User/uOrder.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpMessage_1(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpMessage_1(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            Log.d("xiaoming0106", "onDataHttpMessage_1_RE: " + response);
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    none_panel.setVisibility(View.GONE);
                    dataList = new ArrayList<>();
                    JSONObject jsonObject_data = jsonObject_response.getJSONObject("data");
                    JSONArray jSONArray = jsonObject_data.getJSONArray("lists");
                    this.lastid = jsonObject_data.getString("lastid");
                    if (jSONArray.equals("null") || jSONArray.equals("") || jSONArray.equals("[]")) {
                        none_panel.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jsonObject = jSONArray.getJSONObject(i);
                            Map<String, String> map = new HashMap<>();
                            map.put("id", jsonObject.getString("id"));
                            map.put("gid", jsonObject.getString("gid"));
                            map.put("order_time", jsonObject.getString("order_time"));
                            map.put("type", jsonObject.getString("type"));
                            map.put("order_num", jsonObject.getString("order_num"));
                            map.put("order_title", jsonObject.getString("order_title"));
                            map.put("order_fee", jsonObject.getString("order_fee"));
                            if (jsonObject.has("select_pack_month")) {
                                map.put("select_pack_month", jsonObject.getString("select_pack_month"));
                            } else {
                                map.put("select_pack_month", jsonObject.getString(""));
                            }
                            if (jsonObject.has("refund_info")) {
                                map.put("refund_info", jsonObject.getString("refund_info"));
                            } else {
                                map.put("refund_info", jsonObject.getString(""));
                            }
                            if (jsonObject.has("refund_color")) {
                                map.put("refund_color", jsonObject.getString("refund_color"));
                            } else {
                                map.put("refund_color", jsonObject.getString(""));
                            }
                            map.put("order_status", jsonObject.getString("order_status"));
                            map.put("pay_time", jsonObject.getString("pay_time"));
                            map.put("auth", jsonObject.getString("auth"));
                            if (jsonObject.has("typer")) {
                                map.put("typer", jsonObject.getString("typer"));
                            } else {
                                map.put("typer", "线下支付");
                            }
                            map.put("status_desc", jsonObject.getString("status_desc"));
                            map.put("goods", jsonObject.getString("goods"));
                            dataList.add(map);
                        }
                        mySimpleAdapter_message_1 = new MySimpleAdapter_message_1(activity, dataList, R.layout.main_message_1_item);
                        listview.setAdapter(mySimpleAdapter_message_1);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if ("待支付".equals(dataList.get(i).get("status_desc"))) {
                                    //点击待支付订单
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_MY_ORDER_ORDER_TO_BE_PAID);
                                    Intent intent = new Intent(activity, MainShopOrder_.class);
                                    intent.putExtra("order_num", dataList.get(i).get("order_num"));
                                    intent.putExtra("type", dataList.get(i).get("type"));
                                    intent.putExtra("from", "order");
                                    intent.putExtra("select_pack_month", dataList.get(i).get("select_pack_month"));
                                    activity.startActivity(intent);
                                }
                            }
                        });
                        listview.setDividerHeight(0);
                        if (refresh != null) {
                            refresh.finishRefresh();
                        }
                    }
                } else {
                    none_panel.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * http接口：User/uOrder.html    获取消息信息
     */
    @Background
    void HttpMessage_1_RE(String lastid_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("lastid", lastid_str));
        new OkHttpConnect(activity, PubFunction.app + "User/uOrder.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpMessage_1_RE(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpMessage_1_RE(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    JSONObject jsonObject_data = jsonObject_response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject_data.getJSONArray("lists");
                    this.lastid = jsonObject_data.getString("lastid");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("gid", jsonObject.getString("gid"));
                        map.put("order_time", jsonObject.getString("order_time"));
                        map.put("type", jsonObject.getString("type"));
                        map.put("order_num", jsonObject.getString("order_num"));
                        map.put("order_title", jsonObject.getString("order_title"));
                        map.put("order_fee", jsonObject.getString("order_fee"));
                        if (jsonObject.has("select_pack_month")) {
                            map.put("select_pack_month", jsonObject.getString("select_pack_month"));
                        } else {
                            map.put("select_pack_month", jsonObject.getString(""));
                        }
                        if (jsonObject.has("refund_info")) {
                            map.put("refund_info", jsonObject.getString("refund_info"));
                        } else {
                            map.put("refund_info", jsonObject.getString(""));
                        }
                        if (jsonObject.has("refund_color")) {
                            map.put("refund_color", jsonObject.getString("refund_color"));
                        } else {
                            map.put("refund_color", jsonObject.getString(""));
                        }
                        map.put("order_status", jsonObject.getString("order_status"));
                        map.put("pay_time", jsonObject.getString("pay_time"));
                        map.put("auth", jsonObject.getString("auth"));
                        if (jsonObject.has("typer")) {
                            map.put("typer", jsonObject.getString("typer"));
                        } else {
                            map.put("typer", "线下支付");
                        }
                        map.put("status_desc", jsonObject.getString("status_desc"));
                        map.put("goods", jsonObject.getString("goods"));
                        dataList.add(map);
                    }
                    mySimpleAdapter_message_1.notifyDataSetChanged();
                    count_page = count_page + 1;
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * 适配器
     */
    class MySimpleAdapter_message_1 extends BaseAdapter {
        private List<? extends Map<String, ?>> data;
        private int resource;

        MySimpleAdapter_message_1(Context context, List<? extends Map<String, ?>> data, int resource) {
            this.data = data;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
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
            convertView = View.inflate(activity, resource, null);
            TextView t_1 = (TextView) convertView.findViewById(R.id.t_1);//名字
            TextView t_2 = (TextView) convertView.findViewById(R.id.t_2);//审核
            TextView t_3 = (TextView) convertView.findViewById(R.id.t_3);//订单号\时间\支付方式
            TextView t_6 = (TextView) convertView.findViewById(R.id.t_6);//合计
            TextView tv_order_item_cycle = (TextView) convertView.findViewById(R.id.tv_order_item_cycle);//套餐周期
            TextView tv_order_item_refund_info = (TextView) convertView.findViewById(R.id.tv_order_item_refund_info);//押金退款成功、交易成功
            View view = convertView.findViewById(R.id.start_view);
            LinearLayout l_1 = convertView.findViewById(R.id.l_1);
            t_1.setText("订单名称:" + data.get(position).get("order_title").toString());
            t_3.setText("订单编号:" + data.get(position).get("order_num").toString()
                    + "\n" + "订单时间:" + data.get(position).get("order_time")
                    + "\n" + "支付方式:" + data.get(position).get("typer").toString());
            t_6.setText("合计:" + data.get(position).get("order_fee").toString() + "元");
            String select_pack_month = data.get(position).get("select_pack_month").toString();
            if (!TextUtils.isEmpty(select_pack_month)) {
                tv_order_item_cycle.setText("无".equals(select_pack_month) ? "套餐周期：无" : ("套餐周期：X" + select_pack_month));
            }
            if (!TextUtils.isEmpty(data.get(position).get("refund_info").toString()) && !TextUtils.isEmpty(data.get(position).get("refund_color").toString())) {
                tv_order_item_refund_info.setText(data.get(position).get("refund_info").toString());
                tv_order_item_refund_info.setTextColor(Color.parseColor(data.get(position).get("refund_color").toString()));
            }
            if ("100".equals(data.get(position).get("type").toString())) {
                String auth = data.get(position).get("auth").toString();
                if ("2".equals(auth)) {
                    view.setBackgroundColor(Color.parseColor("#FF8C00"));
                    t_2.setText("未审核");
                    t_2.setTextColor(Color.parseColor("#FF8C00"));
                    t_2.setBackgroundResource(R.drawable.line_ff8b00_corner_10);
                } else {
                    view.setBackgroundColor(Color.parseColor("#00C86C"));
                    t_2.setText("已支付");
                    t_2.setTextColor(Color.parseColor("#00C86C"));
                    t_2.setBackgroundResource(R.drawable.line_00c76b_corner_10);
                }
            } else {
                t_2.setText(data.get(position).get("status_desc").toString());
                if ("待支付".equals(data.get(position).get("status_desc").toString())) {
                    view.setBackgroundColor(Color.parseColor("#00A8FF"));
                    t_2.setTextColor(Color.parseColor("#00A8FF"));
                    t_2.setBackgroundResource(R.drawable.line_00a8ff_corner_10);
                } else {
                    view.setBackgroundColor(Color.parseColor("#00C86C"));
                    t_2.setTextColor(Color.parseColor("#00C86C"));
                    t_2.setBackgroundResource(R.drawable.line_00c76b_corner_10);
                }
            }
            String goods = data.get(position).get("goods").toString();
            if (!"[]".equals(goods)) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(goods);
                    JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject_1 = jsonArray.getJSONObject(i);
                        LinearLayout linearLayout = (LinearLayout) View.inflate(activity, R.layout.main_message_1_item_1, null);
                        TextView t_11 = linearLayout.findViewById(R.id.t_1);
                        TextView t_22 = linearLayout.findViewById(R.id.t_2);
                        String name_1 = jsonObject_1.getString("name");
                        String value_1 = jsonObject_1.getString("rprice");
                        t_11.setText(name_1);
                        t_22.setText(value_1 + "元");
                        l_1.addView(linearLayout);
                    }
                } catch (JSONException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
            return convertView;
        }
    }
}