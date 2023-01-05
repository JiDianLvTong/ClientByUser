package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDERLIST;
import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDERLISTPAGE;

@SuppressLint("Registered")
@EActivity(R.layout.main_shop_order)
public class MainShopOrder extends BaseActivity {
    @ViewById
    TextView submit, price_1;
    @ViewById
    ListView listview;
    private int pay_type = 1;
    private String id = "";
    private String type = "";
    private String order_num = "";
    private String select_pack_month = "";
    private MyBaseAdapter myBaseAdapter;
    private long mLastClickTime = 0;
    private String mjson;
    private String otype = "";

    @AfterViews
    void afterViews() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        id = getIntent().getStringExtra("id");
        if ("order".equals(getIntent().getStringExtra("from"))) {
            type = getIntent().getStringExtra("type");
            order_num = getIntent().getStringExtra("order_num");
            select_pack_month = getIntent().getStringExtra("select_pack_month");
            HttpGetType_ordernum(order_num);
            System.out.println("订单支付");
        } else if ("hourly".equals(getIntent().getStringExtra("from"))) {
            type = "relet";
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            Intent intent = this.getIntent();
            String devid = intent.getStringExtra("devid");
            String otype = intent.getStringExtra("otype");
            String relet = intent.getStringExtra("relet");
            String numt = intent.getStringExtra("numt");
            try {
                jsonObject.put("devid", devid);
                jsonObject.put("otype", otype);
                jsonObject.put("relet", relet);
                jsonObject.put("numt", numt);
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
            mjson = jsonArray.put(jsonObject).toString();
            HttpGetType_hourly(mjson);
        } else if ("product".equals(getIntent().getStringExtra("from"))) {
            System.out.println("商品支付");
            HttpGetType(id);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //待支付订单支付页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_ORDER_TO_BE_PAID);
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESSCLOSEORDERLIST) {
                if (Util.isTopActivity("MainShopOrder", this)) {
                    finish();
                    EventBus.getDefault().post(new MainAppEventBean(PAYSUCCESSCLOSEORDERLISTPAGE));
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

    @Click
    void submit() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > 1000L) {
            //我的订单待支付
            PubFunction.PAT_ING_TYPE = PubFunction.MY_ORDER_PAID;
            if (TextUtils.isEmpty(type)) {
                new MainPay(activity, pay_type + "", id, uid, select_pack_month);
                System.out.println("type_1");
            } else if ("relet".equals(type)) {
                new MainPay(activity, pay_type + "", mjson, uid, 1, 1, select_pack_month);
            } else {
                new MainPay(activity, pay_type + "", order_num, uid, otype, 1, 1, 1, 1, 1, 1, 1, 1);
                System.out.println("type_2");
            }
            mLastClickTime = nowTime;

            //确认支付 节点统计
            if (!uid.isEmpty()) {
                List<ParamTypeData> dataList1 = new ArrayList<>();
                dataList1.add(new ParamTypeData("title", "确认支付"));
                dataList1.add(new ParamTypeData("sourceType", "5"));
                dataList1.add(new ParamTypeData("sourceId", uid));
                dataList1.add(new ParamTypeData("type", "108"));
                dataList1.add(new ParamTypeData("client", "Android"));
                HttpVisitLogs(dataList1);
            }
            //线下支付，在这里传一次
            if (pay_type == 100) {
                List<ParamTypeData> dataList1 = new ArrayList<>();
                dataList1.add(new ParamTypeData("title", "线下支付"));
                dataList1.add(new ParamTypeData("sourceType", "5"));
                dataList1.add(new ParamTypeData("sourceId", uid));
                dataList1.add(new ParamTypeData("type", "203"));
                dataList1.add(new ParamTypeData("client", "Android"));
                HttpVisitLogs(dataList1);
            }
        } else {
            Toast.makeText(activity, "请勿重复点击", Toast.LENGTH_SHORT).show();
        }
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

    @Background
    void HttpGetType_hourly(String mjson) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("mjson", mjson));
        dataList.add(new ParamTypeData("froms", "20"));
        new OkHttpConnect(activity, PubFunction.api + "Pay/befv7.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
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
        new OkHttpConnect(activity, PubFunction.api + "Pay/befpay.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
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
                    JSONArray jsonObject_data = jsonObject_response.getJSONArray("payList");
                    String rprice = jsonObject_response.getString("rprice");
                    otype = jsonObject_response.getString("otype");
                    price_1.setText(rprice);
                    submit.setText("确定支付  ¥ " + rprice + " 元");
                    final List<Map<String, String>> datalist = new ArrayList<>();
                    int wxIndex = -1;
                    for (int i = 0; i < jsonObject_data.length(); i++) {
                        JSONObject jsonObject = jsonObject_data.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("type", jsonObject.getString("type"));
                        map.put("payer", jsonObject.getString("payer"));
                        if (jsonObject.has("img_url")) {
                            map.put("img_url", jsonObject.getString("img_url"));
                        } else {
                            map.put("img_url", "");
                        }
//                        if (i == 0) {
//                            pay_type = Integer.parseInt(jsonObject.getString("type"));
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
                            pay_type = Integer.parseInt(datalist.get(0).get("type"));
                        }
                    }
                    myBaseAdapter = new MyBaseAdapter(activity, datalist, R.layout.main_shop_order_item);
                    listview.setAdapter(myBaseAdapter);
                    listview.setDividerHeight(0);
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
                            pay_type = Integer.parseInt(datalist.get(position).get("type"));
                        }
                    });
                    PubFunction.setListViewHeight(listview);
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    static class ViewHolder {
        TextView t_1;
        ImageView i_1;
        ImageView i_2;
        LinearLayout tv_order_confirm_more_type;
        LinearLayout tv_order_confirm_type_one;
    }

    class MyBaseAdapter extends BaseAdapter {
        private Activity activity;
        private List<Map<String, String>> data;
        private int layout;
        private int is_select = 0;
        private boolean showWxChatPay = false;

        MyBaseAdapter(Activity activity, List<Map<String, String>> data, int layout) {
            this.activity = activity;
            this.data = data;
            this.layout = layout;
        }

        public void setShowWxChatPay(boolean b) {
            this.showWxChatPay = b;
        }

        public boolean getShowWxChatPay() {
            return showWxChatPay;
        }

        void set_select(int i) {
            this.is_select = i;
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
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(activity, layout, null);
                viewHolder = new ViewHolder();
                viewHolder.t_1 = (TextView) convertView.findViewById(R.id.t_1);
                viewHolder.i_2 = (ImageView) convertView.findViewById(R.id.i_2);
                viewHolder.i_1 = (ImageView) convertView.findViewById(R.id.i_1);
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
                Glide.with(activity).load(R.drawable.zf3).into(viewHolder.i_2);
            } else {
                Glide.with(activity).load(R.drawable.zf4).into(viewHolder.i_2);
            }
            viewHolder.t_1.setText(data.get(position).get("payer"));
            viewHolder.tv_order_confirm_type_one.setVisibility((!showWxChatPay && position == data.size() - 1) ? View.GONE : View.VISIBLE);
            viewHolder.tv_order_confirm_more_type.setVisibility((!showWxChatPay && position == data.size() - 1) ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }
}