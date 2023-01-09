package com.android.jidian.client.mvp.ui.activity.pay;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.bean.DialogListBean;
import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.coupon.CouponPickActivity;
import com.android.jidian.client.mvp.ui.dialog.SelectTypeDialog;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyListView;
import com.android.jidian.client.widgets.MyToast;

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

import butterknife.BindView;
import butterknife.OnClick;

public class PayByReNew extends U6BaseActivity {

    @BindView(R.id.to_couponsList)
    public LinearLayout to_couponsList;
    @BindView(R.id.li_sure_order_package)
    public LinearLayout li_sure_order_package;
    @BindView(R.id.dataPanel)
    public LinearLayout dataPanel;

    @BindView(R.id.cheap_price)
    public TextView cheap_price;
    @BindView(R.id.total_price)
    public TextView total_price;
    @BindView(R.id.subtotalPrice)
    public TextView subtotalPrice;
    @BindView(R.id.price_1)
    public TextView price_1;
    @BindView(R.id.select_coupons)
    public TextView select_coupons;
    @BindView(R.id.tv_sure_order_package)
    public TextView tv_sure_order_package;

    @BindView(R.id.order_list)
    public MyListView order_list;

    List<Map<String, String>> datalist_order = new ArrayList<>();
    List<List<Map<String, String>>> select_rent_lists = new ArrayList<>();
    private String mjson = "";
    private String order_type = "";
    private int pay_type = 1;
    private JSONArray jsonArray222 = new JSONArray();
    private JSONArray json_data = new JSONArray();
    public static Handler NewpayReloadHandler;
    private String cjson = "";
    private String cList_json = "";
    private String is_fst = "";
    private List<DialogListBean> packMonthList = new ArrayList<>();
    private String select_pack_month = "";
    private String mFullCutData = "";//活动数据
    private Handler mFullCutHandler;
    private boolean mIsFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_pay_order_create);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        to_couponsList.setVisibility(View.VISIBLE);
        handler();
        if (getIntent().hasExtra("type")) {
            order_type = getIntent().getStringExtra("type");
        }
        if (getIntent().hasExtra("mjson")) {
            mjson = getIntent().getStringExtra("mjson");
            HttpGetType(mjson, order_type);
        }
    }

    void handler() {
        NewpayReloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                cjson = msg.getData().getString("cjson");
                if (cjson.equals("[]")) {
                    cjson = "";
                }
                HttpGetTypeWithCjson(mjson, order_type);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESSCLOSEORDER) {
                if (Util.isTopActivity("Newpay", this)) {
                    finish();
                }
            }
        }
    }

    @OnClick(R.id.submit)
    void submit() {
        for (int i = 0; i < jsonArray222.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray222.getJSONObject(i);
                if (jsonObject.has("relet")) {
                    switch (jsonObject.getString("relet")) {
                        case "日租":
                            jsonObject.put("relet", "daily");
                            break;
                        case "周租":
                            jsonObject.put("relet", "weekly");
                            break;
                        case "月租":
                            jsonObject.put("relet", "monthly");
                            break;
                        case "季租":
                            jsonObject.put("relet", "qtrly");
                            break;
                        case "年租":
                            jsonObject.put("relet", "yearly");
                            break;
                        case "时租":
                            jsonObject.put("relet", "hourly");
                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        for (int i = 0; i < jsonArray222.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray222.getJSONObject(i);
                if (jsonObject.length() == 0) {
                    jsonArray222.remove(i);
                }
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < datalist_order.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (datalist_order.get(i).containsKey("otype")) {
                    if (datalist_order.get(i).containsKey("relet")) {
                        jsonObject.put("devid", datalist_order.get(i).get("devid"));
                        jsonObject.put("otype", datalist_order.get(i).get("otype"));
                        jsonObject.put("relet", datalist_order.get(i).get("relet"));
                        jsonObject.put("numt", datalist_order.get(i).get("numt"));
                    } else {
                        jsonObject.put("gid", datalist_order.get(i).get("gid"));
                        jsonObject.put("otype", datalist_order.get(i).get("otype"));
                    }
                    jsonArray.put(jsonObject);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (pay_type != PubFunction.PAT_TYPE_OFFLINE) {
            HttpPayInitv6();
        } else {
            //100 线下支付
            LayoutInflater inflater = LayoutInflater.from(activity);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            final AlertDialog mAlertDialog = builder.create();
            View view = inflater.inflate(R.layout.alertdialog_pay_sure, null);
            view.findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MainPay(activity, pay_type + "", mjson.toString(), uid, 1, 1, 1, 1, 1, 1, 1, select_pack_month);
                    mAlertDialog.dismiss();
                }
            });
            view.findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                }
            });
            mAlertDialog.setCancelable(false);
            mAlertDialog.show();
            mAlertDialog.getWindow().setContentView(view);
        }
    }

    void HttpPayInitv6() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", "40"));
        dataList.add(new ParamTypeData("cjson", cjson.toString()));
        dataList.add(new ParamTypeData("mjson", mjson.toString()));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/initv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpPayInit(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    void onHttpPayInit(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("0".equals(type)) {
                    MyToast.showTheToast(activity, response);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        if ("1".equals(jsonObject.getString("status"))) {
                            String orderno = jsonObject.getString("orderno");
                            String fee = jsonObject.getString("fee");
                            Intent intent = new Intent(activity, PayByConfirmOrder.class);
                            intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.MY_WALLET_EWNEW);
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
        });
    }

    @OnClick(R.id.to_couponsList)
    void to_couponsList() {
        Intent intent = new Intent(activity, CouponPickActivity.class);
        intent.putExtra("json_data", json_data.toString());
        intent.putExtra("from", "newpay");
        intent.putExtra("cList_json", cList_json);
        startActivity(intent);
    }

    @OnClick(R.id.li_sure_order_package)
    void li_sure_order_package() {
        //点击套餐 - 请选择
        if (packMonthList.size() > 0) {
            new SelectTypeDialog().init(select_pack_month, packMonthList, item -> {
                if (item != null) {
                    select_pack_month = item.getValue();
                    tv_sure_order_package.setText(item.getName());
                    tv_sure_order_package.setTextColor(ContextCompat.getColor(this, R.color.yellow_D7A64A));
                    HttpGetTypeWithCjson(mjson, order_type);
                }
            }).setPosition(Gravity.BOTTOM).setWidth(1).setOutCancel(true).show(getSupportFragmentManager());
        }
    }

    void onVisitLogs(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    void HttpGetType(String mjson, String type) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", type));
        dataList.add(new ParamTypeData("mjson", mjson));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    void HttpGetTypeWithCjson(String mjson, String type) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("froms", type));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("mjson", mjson));
        dataList.add(new ParamTypeData("cjson", cjson));
        dataList.add(new ParamTypeData("select_pack_month", select_pack_month));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    void onDataHttpGetType(String response, String type) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataPanel.setVisibility(View.VISIBLE);
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
                                            tv_sure_order_package.setTextColor(ContextCompat.getColor(activity, R.color.yellow_D7A64A));
                                        }
                                    }
                                }
                            }

                            select_rent_lists.clear();
                            datalist_order.clear();
                            is_fst = jsonObject_response.getString("is_fst");
                            JSONArray cList_jsonArray = jsonObject_response.getJSONArray("clists");
                            cList_json = cList_jsonArray.toString();
                            JSONArray jsonArray_order = jsonObject_response.getJSONArray("data");
                            json_data = jsonArray_order;
                            cheap_price.setText("已优惠¥" + jsonObject_response.getString("cprices"));
                            price_1.setText("¥" + jsonObject_response.getString("cprices"));
                            total_price.setText("¥" + jsonObject_response.getString("rprice"));
                            subtotalPrice.setText("¥" + jsonObject_response.getString("rprice"));

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
                            datalist_order = new ArrayList<>();
                            for (int i1 = 0; i1 < jsonArray_order.length(); i1++) {
                                Map<String, String> map = new HashMap<>();
                                JSONObject jsonObject_order = jsonArray_order.getJSONObject(i1);
                                map.put("mtitle", jsonObject_order.getString("mtitle"));
                                map.put("view_type", "2");
                                datalist_order.add(map);

                                List<Map<String, String>> list1 = new ArrayList<>();
                                select_rent_lists.add(list1);

                                if (jsonObject_order.has("olists") && !(jsonObject_order.get("olists").equals(JSONObject.NULL))) {
                                    JSONArray jsonArray_order_data = jsonObject_order.getJSONArray("olists");
                                    for (int i = 0; i < jsonArray_order_data.length(); i++) {
                                        JSONObject jsonArray_olist = jsonArray_order_data.getJSONObject(i);
                                        List<Map<String, String>> list = new ArrayList<>();
                                        if (jsonArray_olist.has("enRent")) {
                                            JSONArray jsonArray = jsonArray_olist.getJSONArray("enRent");
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                                Map<String, String> map1 = new HashMap<>();
                                                map1.put("name", jsonObject.getString("name"));
                                                map1.put("rprice", jsonObject.getString("rprice"));
                                                map1.put("relet", jsonObject.getString("relet"));
                                                list.add(map1);
                                            }
                                            select_rent_lists.add(list);
                                        } else {
                                            select_rent_lists.add(list1);
                                        }
                                        JSONObject jsonArray_order_data_item = jsonArray_order_data.getJSONObject(i);
                                        if (jsonArray_order_data_item.has("default")) {
                                            Map<String, String> map1 = new HashMap<>();
                                            map1.put("otype", jsonArray_order_data_item.getString("otype"));
                                            map1.put("name", jsonArray_order_data_item.getString("name"));
                                            map1.put("rprice", jsonArray_order_data_item.getString("rprice"));
                                            map1.put("devid", jsonArray_order_data_item.getString("devid"));
                                            if (jsonArray_order_data_item.has("rname")) {
                                                map1.put("rentunit", jsonArray_order_data_item.getString("rname"));
                                            }
                                            if (jsonArray_order_data_item.has("relet")) {
                                                map1.put("relet", jsonArray_order_data_item.getString("relet"));
                                            }
                                            map1.put("view_type", "1");
                                            datalist_order.add(map1);
                                        } else {
                                            Map<String, String> map1 = new HashMap<>();
                                            map1.put("otype", jsonArray_order_data_item.getString("otype"));
                                            map1.put("name", jsonArray_order_data_item.getString("name"));
                                            map1.put("rprice", jsonArray_order_data_item.getString("rprice"));
                                            map1.put("oprice", jsonArray_order_data_item.getString("oprice"));
                                            map1.put("view_type", "3");
                                            datalist_order.add(map1);
                                        }
                                        JSONObject jsonObject = new JSONObject();
                                        if (jsonObject.has("devid")) {
                                            jsonObject.put("devid", jsonArray_order_data_item.getString("devid"));
                                        }
                                        jsonObject.put("otype", jsonArray_order_data_item.getString("otype"));
                                        jsonArray222.put(jsonObject);
                                    }
                                }
                            }
                            MyBaseAdapter1 simpleAdapter = new MyBaseAdapter1(datalist_order, new int[]{R.layout.u6_activity_pay_create_order_item_v4, R.layout.u6_activity_pay_create_order_item_v2, R.layout.u6_activity_pay_create_order_item_v5});
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
                                    HttpGetTypeWithCjson(mjson, order_type);
                                }
                            }
                        }
                    } catch (Exception e) {
                        MyToast.showTheToast(activity, "JSON：" + e.toString());
                    }
                }
            }
        });


    }

    /***
     * 首页访问节点日志
     * @param dataList
     */
    void HttpVisitLogs(List<ParamTypeData> dataList) {
        new OkHttpConnect(activity, PubFunction.api + "VisitLogs/index.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
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
                final LayoutInflater inflater = LayoutInflater.from(activity);
                view = inflater.inflate(resource[0], null);
                TextView t_1 = view.findViewById(R.id.t_1);
                TextView t_2 = view.findViewById(R.id.t_2);
                TextView t_3 = view.findViewById(R.id.t_3);
                LinearLayout select_paytype = view.findViewById(R.id.select_paytype);
                LinearLayout select_unit = view.findViewById(R.id.select_unit);
                t_1.setText(data.get(position).get("name").toString());
                t_2.setText("¥" + data.get(position).get("rprice").toString());
                if (data.get(position).containsKey("rentunit")) {
                    select_unit.setVisibility(View.VISIBLE);
                    t_3.setText(data.get(position).get("rentunit").toString());
                    select_paytype.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Map<String, String>> list = select_rent_lists.get(position);
                            String devid = datalist_order.get(position).get("devid").toString();
                            LayoutInflater inflater = LayoutInflater.from(activity);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            final AlertDialog mAlertDialog = builder.create();
                            View view = inflater.inflate(R.layout.alert_dialog_pay_rent, null);
                            ListView listview_alert = view.findViewById(R.id.listview);
                            listview_alert.setVerticalScrollBarEnabled(false);
                            MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(activity, list, R.layout.pay_rent_select_item);
                            listview_alert.setAdapter(mySimpleAdapter);
                            listview_alert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        if (list != null) {
                                            String nameStr = list.get(position).get("name").toString();
                                            if ("日租".equals(nameStr)) {
                                                //日租
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_DAILY_RENT);
                                            } else if ("周租".equals(nameStr)) {
                                                //周租
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_WEEKLY_RENT);
                                            } else if ("月租".equals(nameStr)) {
                                                //月租
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_MONTHLY_RENT);
                                            } else if ("季租".equals(nameStr)) {
                                                //季租
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_QUARTERLY_RENT);
                                            }
                                        }
                                        JSONArray jsonArray = new JSONArray(mjson);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            if (jsonArray.getJSONObject(i).has("devid")) {
                                                if (devid.equals(jsonArray.getJSONObject(i).getString("devid"))) {
                                                    jsonArray.getJSONObject(i).put("relet", list.get(position).get("relet"));
                                                }
                                            }
                                        }
                                        mjson = jsonArray.toString();
                                        //选择续租方式，重新请求
                                        cjson = "";
                                        HttpGetTypeWithCjson(mjson, order_type);
                                    } catch (JSONException e) {
                                        MyToast.showTheToast(activity, "商品信息错误");
                                        Log.e("xiaoming720", "onItemClick: e    " + e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                    mAlertDialog.dismiss();
                                }
                            });
                            mAlertDialog.show();
                            mAlertDialog.getWindow().setContentView(view);
                        }
                    });
                } else {
                    select_unit.setVisibility(View.GONE);
                }
            } else if ("2".equals(view_type)) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                view = inflater.inflate(resource[1], null);
                TextView order_list_item = view.findViewById(R.id.order_list_item);
                order_list_item.setText(data.get(position).get("mtitle").toString());
            } else if ("3".equals(view_type)) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                view = inflater.inflate(resource[2], null);
                TextView t_1 = view.findViewById(R.id.t_1);
                TextView t_2 = view.findViewById(R.id.t_2);
                TextView t_3 = view.findViewById(R.id.t_3);
                TextView t_4 = view.findViewById(R.id.t_4);
                t_1.setText(data.get(position).get("name").toString());
                t_2.setText("¥" + data.get(position).get("rprice").toString());
                t_3.setText("¥" + data.get(position).get("oprice").toString());
                t_3.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                if ("1".equals(is_fst)) {
                    t_4.setVisibility(View.VISIBLE);
                } else {
                    t_4.setVisibility(View.GONE);
                }
            }
            return view;
        }
    }

    class MySimpleAdapter extends BaseAdapter {
        private Activity activity;
        private List<Map<String, String>> data;

        private int resource;

        public MySimpleAdapter(Activity activity, List<Map<String, String>> data, int resource) {
            this.activity = activity;
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(resource, null);
            TextView t_1 = view.findViewById(R.id.date_text);
            t_1.setText(data.get(position).get("name"));
            view.setTag(data.get(position).get("rprice"));
            return view;
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }
}
