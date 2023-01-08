package com.android.jidian.client.mvp.ui.activity.coupon;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jidian.client.Newpay;
import com.android.jidian.client.Newshoppay;
import com.android.jidian.client.R;
import com.android.jidian.client.ShopRentZhima;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.bean.CouponBefBeans;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCreateOrderActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCreateOrderZhiMaActivity;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.widgets.MyToast;
import com.google.gson.Gson;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponPickActivity extends U6BaseActivity {

    @BindView(R.id.none_panel)
    public LinearLayout none_panel;
    @BindView(R.id.submit)
    public LinearLayout submit;

    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.listview)
    public ListView listview;

    @BindView(R.id.t_5)
    public TextView t_5;

    private List<CouponBefBeans.DataBean> datalist;
    private CouponsListAdapter couponsListAdapter;
    private String json_data;
    private JSONArray cjson;
    private android.os.Handler Handler;
    private int coupons_num;
    private int coupons_price;
    private String from;
    private String cList_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_coupon_pick);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        handler();
        json_data = getIntent().getStringExtra("json_data");
        from = getIntent().getStringExtra("from");
        cList_json = getIntent().getStringExtra("cList_json");
        HttpCouponBefClists(json_data);

        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(activity);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                HttpCouponBefClists(json_data);
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    void handler(){
        Handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                coupons_num = 0;
                coupons_price = 0;
                cjson = new JSONArray();
                for (int i = 0; i < datalist.size(); i++) {
                    if (datalist.get(i).getIs_choose() == 1){
                        coupons_num++;
                        coupons_price += Integer.parseInt(datalist.get(i).getFvalue());
                        cjson.put(datalist.get(i).getId());
                    }
                }
//                coupons_num = cjson.length();
                String s = "已选择" + coupons_num + "张,可抵扣<font color='#d7a64a'>" +"¥" +coupons_price + "</font>元";
                t_5.setText(Html.fromHtml(s));
            }
        };
    }

    void HttpCouponBefClists(String data) {
        progressDialog.show();
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("data", json_data));
        dataList.add(new ParamTypeData("clists", cList_json));
        new OkHttpConnect(activity, PubFunction.app + "Coupon/befClists.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpCouponBefClists(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    void onHttpCouponBefClists(String response, String type){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (type.equals("0")){
                    MyToast.showTheToast(activity,response);
                    none_panel.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }else {
                    CouponBefBeans couponBefBeans = new Gson().fromJson(response, CouponBefBeans.class);
                    int status = couponBefBeans.getStatus();
                    String msg = couponBefBeans.getMsg();
                    if (status == 1){
                        Handler.sendEmptyMessage(1);
                        none_panel.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);
                        datalist = new ArrayList<>();
                        cjson = new JSONArray();
                        List<CouponBefBeans.DataBean> dataBeans = couponBefBeans.getData();
                        if (dataBeans.size()>0){
                            datalist.addAll(dataBeans);
                        }else {
                            none_panel.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                            return;
                        }
                        couponsListAdapter = new CouponsListAdapter(activity,datalist);
                        listview.setAdapter(couponsListAdapter);
//                listview.setDividerHeight(0);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                CouponBefBeans.DataBean dataBean = (CouponBefBeans.DataBean) view.findViewById(R.id.t_1).getTag();
                                if (dataBean.getIs_use() == 1){
                                    if (dataBean.getIs_choose() == 1){
                                        datalist.get(i).setIs_choose(0);
                                    }else {
                                        datalist.get(i).setIs_choose(1);
                                    }
                                    Handler.sendEmptyMessage(1);
                                    couponsListAdapter.notifyDataSetChanged();
                                }else {
                                    MyToast.showTheToast(activity,dataBean.getReason());
                                }
                            }
                        });

                    }else {
                        none_panel.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.GONE);
                        MyToast.showTheToast(activity,msg);
                    }
                }
                smartRefreshLayout.finishRefresh();
            }
        });


    }

    @OnClick(R.id.submit)
    void submit() {
        this.finish();
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("cjson", cjson.toString());
        message.setData(bundle);
        if (from.equals("newpay")) {
            Newpay.NewpayReloadHandler.sendMessage(message);
        } else if (from.equals("newshoppay")) {
            PayByCreateOrderActivity.NewshoppayReloadHandler.sendMessage(message);
        } else {
            PayByCreateOrderZhiMaActivity.ShopRentZhimaReloadHandler.sendMessage(message);
        }
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }

    class CouponsListAdapter extends BaseAdapter {

        private List<CouponBefBeans.DataBean> dataBeanList;
        private Activity activity;

        CouponsListAdapter(Activity activity,List<CouponBefBeans.DataBean> dataBeanList){
            this.activity = activity;
            this.dataBeanList = dataBeanList;
        }

        @Override
        public int getCount() {
            return dataBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataBeanList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (null == convertView){
                convertView = View.inflate(activity, R.layout.u6_activity_coupon_pick_item, null);
                viewHolder = new ViewHolder();
                viewHolder.t_1 = convertView.findViewById(R.id.t_1);
                viewHolder.t_2 = convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = convertView.findViewById(R.id.t_4);
                viewHolder.coupon_select = convertView.findViewById(R.id.coupon_select);
                viewHolder.i_1 = convertView.findViewById(R.id.i_1);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CouponBefBeans.DataBean dataBean = dataBeanList.get(i);
//            Glide.with(activity).load(dataBean.getBg_img()).into(viewHolder.i_1);
            viewHolder.t_1.setText(dataBean.getName());
            viewHolder.t_2.setText(dataBean.getExpire());
            viewHolder.t_3.setText(dataBean.getUrules());
            viewHolder.t_4.setText("¥"+dataBean.getFvalue()+dataBean.getUnit());
            if (dataBean.getIs_choose() == 1){
                viewHolder.coupon_select.setVisibility(View.VISIBLE);
            }else {
                viewHolder.coupon_select.setVisibility(View.GONE);
            }
            if (dataBean.getIs_use() == 0){
                viewHolder.t_1.setTextColor(Color.parseColor("#999999"));
                viewHolder.t_2.setTextColor(Color.parseColor("#999999"));
                viewHolder.t_3.setTextColor(Color.parseColor("#999999"));
                viewHolder.t_4.setTextColor(Color.parseColor("#999999"));
            }else {
                viewHolder.t_1.setTextColor(Color.parseColor("#010101"));
                viewHolder.t_2.setTextColor(Color.parseColor("#444444"));
                viewHolder.t_3.setTextColor(Color.parseColor("#666666"));
                viewHolder.t_4.setTextColor(Color.parseColor("#F94A2C"));
            }
            viewHolder.t_1.setTag((CouponBefBeans.DataBean) dataBean);
            return convertView;
        }
    }

    class ViewHolder{
        TextView t_1;
        TextView t_2;
        TextView t_3;
        TextView t_4;
        ImageView i_1;
        LinearLayout coupon_select;
    }
}
