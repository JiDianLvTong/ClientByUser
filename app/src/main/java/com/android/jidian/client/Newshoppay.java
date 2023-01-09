package com.android.jidian.client;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.bean.DialogListBean;
import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.dialog.SelectTypeDialog;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyListView;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDER;
import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSESHOP;

@EActivity(R.layout.activity_newshoppay)
public class Newshoppay extends BaseActivity {
    @ViewById
    LinearLayout to_couponsList, li_sure_order_package;
    @ViewById
    TextView price_1, tv_title, total_price, cheap_price, subtotalPrice, select_coupons, tv_sure_order_package;
    @ViewById
    MyListView order_list;

    List<Map<String, String>> datalist_order = new ArrayList<>();
    private String id = "";
    private String order_num = "";
    private long mLastClickTime = 0;
    private JSONArray json_data;
    public static Handler NewshoppayReloadHandler;
    private String cjson = "";
    private String cList_json = "";
    private String is_fst = "";

    private String mFromActivity = "";
    private List<DialogListBean> packMonthList = new ArrayList<>();
    private String select_pack_month = "";
    private String mFullCutData = "";//活动数据
    private Handler mFullCutHandler;
    private boolean mIsFirst = false;


    void handler() {
        NewshoppayReloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                cjson = msg.getData().getString("cjson");
                if (cjson.equals("[]")) {
                    cjson = "";
                }
                HttpGetTypeWithCjson(id);
            }
        };
        mFullCutHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if (msg.what == 1) {
//                    web_view.setVisibility(View.VISIBLE);
//                    web_view.loadUrl(PubFunction.h5 + "Activity/layerFullCut.html");
//                } else {
//                    web_view.setVisibility(View.GONE);
//                }
            }
        };
    }

    @AfterViews
    void afterViews() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tv_title.setText("支付");
        id = getIntent().getStringExtra("id");
        mFromActivity = getIntent().getStringExtra(PubFunction.FROM_ACTOVITY);
        handler();
        if (getIntent().hasExtra("type")) {
            order_num = getIntent().getStringExtra("order_num");
            HttpGetType_ordernum(order_num);
        } else {
            HttpGetType(id);
        }
//        // 设置背景色
//        web_view.setBackgroundColor(0);
//        // 设置填充透明度
//        web_view.getBackground().setAlpha(0);
//        web_view.addJavascriptObject(new NewShopPayJsApi(), null);
//        web_view.getSettings().setTextZoom(100);
//        web_view.loadUrl("https://testh5.halouhuandian.com/Activity/layerFullCut.html");
//        tv_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (web_view.getVisibility() == View.GONE) {
//                    web_view.setVisibility(View.VISIBLE);
//                } else {
//                    web_view.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESSCLOSEORDER) {
                if (Util.isTopActivity("Newshoppay", this)) {
                    finish();
                    EventBus.getDefault().post(new MainAppEventBean(PAYSUCCESSCLOSESHOP));
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
    void iv_back() {
        this.finish();
    }

    @Click
    void submit() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > 1000L) {
            HttpPayInit();
            mLastClickTime = nowTime;
        } else {
            Toast.makeText(activity, "请勿重复点击", Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void li_sure_order_package() {
        //点击套餐 - 请选择
        if (packMonthList.size() > 0) {
            new SelectTypeDialog().init(select_pack_month, packMonthList, item -> {
                if (item != null) {
                    select_pack_month = item.getValue();
                    tv_sure_order_package.setText(item.getName());
                    tv_sure_order_package.setTextColor(ContextCompat.getColor(this, R.color.color_FC481E));
                    HttpGetTypeWithCjson(id);
                }
            }).setPosition(Gravity.BOTTOM).setWidth(1).setOutCancel(true).show(getSupportFragmentManager());
        }
    }

    /**
     * 首页访问节点日志
     */
    @Background
    void HttpPayInit() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("gid", id));
        dataList.add(new ParamTypeData("froms", "30"));
        dataList.add(new ParamTypeData("cjson", cjson.toString()));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/initv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpPayInit(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onHttpPayInit(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                if ("1".equals(jsonObject.getString("status"))) {
                    String orderno = jsonObject.getString("orderno");
                    String fee = jsonObject.getString("fee");
                    Intent intent = new Intent(activity, OrderConfirm_.class);
                    intent.putExtra(PubFunction.FROM_ACTOVITY, mFromActivity);
                    intent.putExtra("orderno", orderno);
                    intent.putExtra("fee", fee);
                    startActivity(intent);
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Click
    void to_couponsList() {
        Intent intent = new Intent(activity, CouponsListActivity_.class);
        intent.putExtra("json_data", json_data.toString());
        intent.putExtra("from", "newshoppay");
        intent.putExtra("cList_json", cList_json);
        startActivity(intent);
    }

    //首页访问节点日志
    @Background
    void HttpVisitLogs(List<ParamTypeData> dataList) {
        new OkHttpConnect(activity, PubFunction.api + "VisitLogs/index.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onVisitLogs(String response, String type) {
    }

    /**
     * http接口：Pay/payer.html   获取消息信息
     */
    @Background
    void HttpGetType(String gid) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("gid", gid));
        dataList.add(new ParamTypeData("froms", "30"));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @Background
    void HttpGetType_ordernum(String order_num) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("orderno", order_num));
        dataList.add(new ParamTypeData("froms", "10"));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @Background
    void HttpGetTypeWithCjson(String gid) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("gid", gid));
        dataList.add(new ParamTypeData("cjson", cjson));
        dataList.add(new ParamTypeData("froms", "30"));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetType(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    //是否周期套餐周期 1=显示；0=不显示
                    String is_show_pack = jsonObject_response.getString("is_show_pack");
                    if ("1".equals(is_show_pack)) {
                        li_sure_order_package.setVisibility(View.VISIBLE);
                    } else {
                        li_sure_order_package.setVisibility(View.GONE);
                    }
                    if (jsonObject_response.has("pack_month")) {
                        packMonthList.clear();
                        JSONArray jsonArray = jsonObject_response.getJSONArray("pack_month");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            DialogListBean bean = new DialogListBean();
                            bean.setName(jsonObject.getString("name"));
                            bean.setValue(jsonObject.getString("value"));
                            packMonthList.add(bean);
                        }
                    }
                    if (jsonObject_response.has("select_pack_month")) {
                        String str = jsonObject_response.getString("select_pack_month");
                        if (!TextUtils.isEmpty(str)) {
                            select_pack_month = str;
                            for (int i = 0; i < packMonthList.size(); i++) {
                                if (select_pack_month.equals(packMonthList.get(i).getValue())) {
                                    tv_sure_order_package.setText(packMonthList.get(i).getName());
                                    tv_sure_order_package.setTextColor(ContextCompat.getColor(this, R.color.color_FC481E));
                                }
                            }
                        }
                    }

                    to_couponsList.setVisibility(View.VISIBLE);
                    is_fst = jsonObject_response.getString("is_fst");
                    String rprice2 = jsonObject_response.getString("rprice");
                    String checppric = jsonObject_response.getString("cprices");
                    price_1.setText("¥" + checppric);
                    cheap_price.setText("已优惠¥" + checppric);
                    total_price.setText("¥" + rprice2);
                    subtotalPrice.setText("¥" + rprice2);
                    if (0 < jsonObject_response.getInt("coupons")) {
                        select_coupons.setText("已选择" + jsonObject_response.getString("cprices") + "元优惠券");
                        select_coupons.setTextColor(Color.parseColor("#F94A2C"));
                    } else {
                        if ("0".equals(jsonObject_response.getString("usable_coupons"))) {
                            select_coupons.setTextColor(Color.parseColor("#999999"));
                            select_coupons.setText("暂无优惠券");
                        } else {
                            select_coupons.setTextColor(Color.parseColor("#F94A2C"));
                            select_coupons.setText("有" + jsonObject_response.getString("usable_coupons") + "张优惠券可选择");
                        }
                    }
                    JSONArray jsonArray_order = jsonObject_response.getJSONArray("data");
                    JSONArray cList_jsonArray = jsonObject_response.getJSONArray("clists");
                    cList_json = cList_jsonArray.toString();
                    json_data = jsonArray_order;
                    datalist_order = new ArrayList<>();
                    for (int i1 = 0; i1 < jsonArray_order.length(); i1++) {
                        Map<String, String> map = new HashMap<>();
                        JSONObject jsonObject_order = jsonArray_order.getJSONObject(i1);
                        map.put("mtitle", jsonObject_order.getString("mtitle"));
                        map.put("view_type", "2");
                        datalist_order.add(map);
                        if (jsonObject_order.has("olists") && !(jsonObject_order.get("olists").equals(JSONObject.NULL))) {
                            JSONArray jsonArray_order_data = jsonObject_order.getJSONArray("olists");
                            for (int i = 0; i < jsonArray_order_data.length(); i++) {
                                Map<String, String> map1 = new HashMap<>();
                                JSONObject jsonArray_order_data_item = jsonArray_order_data.getJSONObject(i);
                                map1.put("otype", jsonArray_order_data_item.getString("otype"));
                                map1.put("name", jsonArray_order_data_item.getString("name"));
                                map1.put("rprice", jsonArray_order_data_item.getString("rprice"));
                                map1.put("oprice", jsonArray_order_data_item.getString("oprice"));
                                map1.put("view_type", "1");
                                datalist_order.add(map1);
                            }
                        }
                    }
                    Newshoppay.MyBaseAdapter1 simpleAdapter = new MyBaseAdapter1(datalist_order, new int[]{R.layout.u6_activity_pay_create_order_item, R.layout.u6_activity_pay_create_order_item_v2, R.layout.u6_activity_pay_create_order_item_v3});
                    order_list.setAdapter(simpleAdapter);
                    order_list.setDividerHeight(0);
                    simpleAdapter.notifyDataSetChanged();
                    if (!mIsFirst) {
                        if (jsonObject_response.has("activity")) {
                            JSONObject activityJSONObject = jsonObject_response.getJSONObject("activity");
                            if (activityJSONObject.has("full_cut")) {
                                JSONObject fullCutJSONObject = activityJSONObject.getJSONObject("full_cut");
                                if ("1".equals(fullCutJSONObject.getString("is_show"))) {
                                    if (fullCutJSONObject.has("data")) {
                                        JSONArray activityDataArray = fullCutJSONObject.getJSONArray("data");
                                        if (activityDataArray.length() > 0) {
                                            mFullCutData = activityDataArray.toString();
//                                    web_view.setVisibility(View.VISIBLE);
                                            Message message = new Message();
                                            message.what = 1;
                                            mFullCutHandler.sendMessage(message);
                                            mIsFirst = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    cjson = "";
                    MyToast.showTheToast(activity, msg);
                    if (jsonObject_response.has("is_reask")) {
                        if ("1".equals(jsonObject_response.getString("is_reask"))) {
                            HttpGetTypeWithCjson(id);
                        }
                    }
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    class MyBaseAdapter1 extends BaseAdapter {
        private List<? extends Map<String, ?>> data;
        private int[] resource;

        public MyBaseAdapter1(List<? extends Map<String, ?>> data, int[] resource) {
            this.data = data;
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            String view_type = data.get(position).get("view_type").toString();
            if ("1".equals(view_type)) {
                String otype = data.get(position).get("otype").toString();
                if ("20".equals(otype)) {
                    if ("1".equals(is_fst)) {
                        final LayoutInflater inflater = LayoutInflater.from(activity);
                        view = inflater.inflate(resource[2], null);
                        TextView t_1 = view.findViewById(R.id.t_1);
                        TextView t_2 = view.findViewById(R.id.t_2);
                        TextView t_3 = view.findViewById(R.id.t_3);
                        TextView t_4 = view.findViewById(R.id.t_4);
                        t_1.setText(data.get(position).get("name").toString());
                        t_2.setText("¥" + data.get(position).get("rprice").toString());
                        t_3.setText("¥" + data.get(position).get("oprice").toString());
                        t_3.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        t_4.setVisibility(View.VISIBLE);
                    } else {
                        final LayoutInflater inflater = LayoutInflater.from(activity);
                        view = inflater.inflate(resource[2], null);
                        TextView t_1 = view.findViewById(R.id.t_1);
                        TextView t_2 = view.findViewById(R.id.t_2);
                        TextView t_3 = view.findViewById(R.id.t_3);
                        TextView t_4 = view.findViewById(R.id.t_4);
                        t_1.setText(data.get(position).get("name").toString());
                        t_2.setText("¥" + data.get(position).get("rprice").toString());
                        if (!"0".equals(data.get(position).get("oprice"))) {
                            t_3.setVisibility(View.VISIBLE);
                            t_3.setText("¥" + data.get(position).get("oprice").toString());
                            t_3.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        } else {
                            t_3.setVisibility(View.GONE);
                        }
                        t_4.setVisibility(View.GONE);
                    }
                } else {
                    final LayoutInflater inflater = LayoutInflater.from(activity);
                    view = inflater.inflate(resource[0], null);
                    TextView t_1 = view.findViewById(R.id.t_1);
                    TextView t_2 = view.findViewById(R.id.t_2);
                    t_1.setText(data.get(position).get("name").toString());
                    t_2.setText("¥" + data.get(position).get("rprice").toString());
                }
            } else if ("2".equals(view_type)) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                view = inflater.inflate(resource[1], null);
                TextView order_list_item = view.findViewById(R.id.order_list_item);
                order_list_item.setText(data.get(position).get("mtitle").toString());
            }
            return view;
        }
    }


}