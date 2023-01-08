package com.android.jidian.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.main.MainActivity;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.timmy.tdialog.TDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2017/6/6.
 */
@EFragment(R.layout.main_discount_1)
public class MainDiscount_1 extends BaseFragment implements AbsListView.OnScrollListener {
    public static Handler reloadHandler;
    @ViewById
    LinearLayout none_panel;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    private DiscountSimoleAdapter discountSimoleAdapter;
    private List<Map<String, String>> dataList = new ArrayList<>();
    private String lastid = "";
    private int count_page = 2;
    private int current_page = 1;

    @AfterViews
    void afterVoids() {
        initParam();
        init();
        main();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //我的优惠券-未使用页面访问（默认页）
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_UNUSED_MY_COUPONS);
                }
            }, 500);
        }
    }

    private void main() {
        if (PubFunction.isConnect(getActivity(), true)) {
            progressDialog.show();
            HttpGetDis();
        }
    }

    private void init() {
        reloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                lastid = "";
                count_page = 2;
                current_page = 1;
                if (PubFunction.isConnect(getActivity(), true)) {
                    HttpGetDis();
                } else {
                    if (refresh != null) {
                        refresh.finishRefresh();
                    }
                }
            }
        };
        listview.setOnScrollListener(this);
        refresh.setCanLoadMore(false);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                lastid = "";
                count_page = 2;
                current_page = 1;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        if (getActivity() != null) {
                            if (PubFunction.isConnect(getActivity(), true)) {
                                HttpGetDis();
                            } else {
                                if (refresh != null) {
                                    refresh.finishRefresh();
                                }
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void loadMore() {
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && current_page < count_page) {
                HttpGetDis_Re(lastid);
                progressDialog.show();
                current_page = count_page;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    /**
     * http接口：Coupon/listsV2.html   除了第一页的数据
     */
    @Background
    void HttpGetDis_Re(String lastid_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "1"));
        dataList.add(new ParamTypeData("lastid", lastid_str));
        new OkHttpConnect(getActivity(), PubFunction.app + "Coupon/listsV2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetDis_Re(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetDis_Re(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    JSONArray jSONArray = jsonObject_response.getJSONArray("data");
                    String lastid = jsonObject_response.getString("lastid");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsonObject = jSONArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("code", jsonObject.getString("code"));
                        map.put("urules", jsonObject.getString("urules"));
                        map.put("is_use", jsonObject.getString("is_use"));
                        map.put("expire", jsonObject.getString("expire"));
                        map.put("name", jsonObject.getString("name"));
                        map.put("title", jsonObject.getString("title"));
                        map.put("bg_img", jsonObject.getString("bg_img"));
                        map.put("unit", jsonObject.getString("unit"));
                        map.put("fval", jsonObject.getString("fval"));
                        map.put("show", jsonObject.getString("show"));
                        map.put("alert", jsonObject.getString("alert"));
                        map.put("show_alert", jsonObject.getString("show_alert"));
                        dataList.add(map);
                    }
                    this.lastid = lastid;
                    discountSimoleAdapter.notifyDataSetChanged();
                    count_page = count_page + 1;
                } else {
                    MyToast.showTheToast(getActivity(), msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(getActivity(), "JSON：" + e.toString());
            }
        }
    }

    /**
     * http接口：Coupon/listsV2.html   第一页的数据
     */
    @Background
    void HttpGetDis() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "1"));
        new OkHttpConnect(getActivity(), PubFunction.app + "Coupon/listsV2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetDis(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetDis(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);

                String alter = jsonObject_response.optString("alter");
                Message message = new Message();
                message.obj = alter;
                MainDiscount.handler.sendMessage(message);

                if ("1".equals(status)) {
                    dataList.clear();
                    JSONArray jSONArray = jsonObject_response.getJSONArray("data");
                    String lastid = jsonObject_response.getString("lastid");
                    if (jSONArray.equals("null") || jSONArray.equals("") || jSONArray.equals("[]")) {
                        none_panel.setVisibility(View.VISIBLE);
                    } else {
                        none_panel.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsonObject = jSONArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("code", jsonObject.getString("code"));
                        map.put("urules", jsonObject.getString("urules"));
                        map.put("is_use", jsonObject.getString("is_use"));
                        map.put("expire", jsonObject.getString("expire"));
                        map.put("name", jsonObject.getString("name"));
                        map.put("title", jsonObject.getString("title"));
                        map.put("bg_img", jsonObject.getString("bg_img"));
                        map.put("unit", jsonObject.getString("unit"));
                        map.put("fval", jsonObject.getString("fval"));
                        //show_alert  ：1 =确认 ，0= 不确认
                        map.put("show", jsonObject.getString("show"));
                        map.put("alert", jsonObject.getString("alert"));
                        map.put("show_alert", jsonObject.getString("show_alert"));
                        dataList.add(map);
                    }
                    discountSimoleAdapter = new DiscountSimoleAdapter(getActivity(), dataList, R.layout.main_discount_item_1, new String[]{"a"}, new int[]{1});
                    listview.setAdapter(discountSimoleAdapter);
                    listview.setDividerHeight(0);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //点击未兑换优惠券
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_UNUSED_MY_COUPONS_UN_REDEEMED_COUPONS);
                            String id_sre = dataList.get(position).get("code").toString();
                            String show_alert = dataList.get(position).get("show_alert").toString();
                            String alert = dataList.get(position).get("alert").toString();
                            if (PubFunction.isConnect(getActivity(), true)) {
                                if ("1".equals(show_alert)) {
                                    confirmAgain(alert,id_sre);
                                }else {
                                    HttpDiscount(id_sre);
                                }
//                                dialogByLoading.show();
                            }
                        }
                    });
                    this.lastid = lastid;
                    if (refresh != null) {
                        refresh.finishRefresh();
                    }
                } else {
                    none_panel.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Toast.makeText(requireActivity(), "JSON：" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * http接口：Coupon/cashv3.html   使用优惠卷
     */
    @Background
    void HttpDiscount(String code_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("code", code_str));
        new OkHttpConnect(getActivity(), PubFunction.app + "Coupon/cashv3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpDiscount(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpDiscount(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
//                MyToast.showTheToast(getActivity(), msg);

                if ("1".equals(status)) {
                    if (jsonObject_response.has("jump")) {
                        String data = jsonObject_response.getString("jump");
                        if ("1".equals(data)) {
                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            MyToast.showTheToast(getActivity(), msg);
                        }
                    } else {
                        MyToast.showTheToast(getActivity(), msg);
                    }
                } else {
                    MyToast.showTheToast(getActivity(), msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(getActivity(), "JSON：" + e.toString());
            }
        }
        MainDiscount_1.reloadHandler.sendMessage(new Message());
        MainDiscount_2.reloadHandler.sendMessage(new Message());
    }

    /**
     * 适配器
     */
    class DiscountSimoleAdapter extends SimpleAdapter {
        private Context context;
        private List<? extends Map<String, ?>> data;
        private int resource;

        public DiscountSimoleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.data = data;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(context, resource, null);
                viewHolder = new ViewHolder();
                viewHolder.t_1 = (TextView) convertView.findViewById(R.id.t_1);
                viewHolder.t_2 = (TextView) convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = (TextView) convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = (TextView) convertView.findViewById(R.id.t_4);
                viewHolder.t_5 = (TextView) convertView.findViewById(R.id.is_use);
                viewHolder.i_1 = (ImageView) convertView.findViewById(R.id.i_1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.t_1.setText(data.get(position).get("name").toString());
//            if (TextUtils.isEmpty(data.get(position).get("unit").toString())) {
//                viewHolder.t_2.setText(data.get(position).get("fval").toString() + "元");
//            } else {
//                viewHolder.t_2.setText(data.get(position).get("fval").toString() + data.get(position).get("unit").toString());
//            }

            if ("1".equals(data.get(position).get("show"))) {
                viewHolder.t_2.setText(data.get(position).get("fval").toString() + data.get(position).get("unit").toString());
            }else {
                viewHolder.t_2.setVisibility(View.GONE);
            }

            viewHolder.t_3.setText(data.get(position).get("expire").toString());
            viewHolder.t_4.setText(data.get(position).get("urules").toString());

            String is_use_str = data.get(position).get("is_use").toString();
            if ("-1".equals(is_use_str)) {
                viewHolder.t_5.setVisibility(View.GONE);
                viewHolder.t_1.setTextColor(Color.parseColor("#666666"));
                viewHolder.t_2.setTextColor(Color.parseColor("#666666"));
                viewHolder.t_3.setTextColor(Color.parseColor("#666666"));
                viewHolder.t_4.setTextColor(Color.parseColor("#666666"));
            } else {
                viewHolder.t_5.setVisibility(View.VISIBLE);
                viewHolder.t_1.setTextColor(Color.parseColor("#333333"));
                viewHolder.t_2.setTextColor(Color.parseColor("#FC481E"));
                viewHolder.t_3.setTextColor(Color.parseColor("#666666"));
                viewHolder.t_4.setTextColor(Color.parseColor("#666666"));
            }
            Glide.with(getActivity()).load(data.get(position).get("bg_img").toString()).into(viewHolder.i_1);
            return convertView;
        }

        private class ViewHolder {
            TextView t_1;
            TextView t_2;
            TextView t_3;
            TextView t_4;
            TextView t_5;
            ImageView i_1;
        }
    }

    public void confirmAgain(String alter, String id_sre) {
        View dialog_view = View.inflate(requireActivity(), R.layout.cashv2_confirm, null);
        TextView textView = dialog_view.findViewById(R.id.textView);
        textView.setText(alter);
        new TDialog.Builder(requireActivity().getSupportFragmentManager())
                .setDialogView(dialog_view)
                .setScreenWidthAspect(requireActivity(), 0.7f)
                .addOnClickListener(R.id.textView2, R.id.textView3)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.textView2:
                            tDialog.dismiss();
                            break;
                        case R.id.textView3:
//                            dialogByLoading.show();
                            HttpDiscount(id_sre);
//                            number.setText("");
                            tDialog.dismiss();
                            break;
                    }
                })
                .create()
                .show();
    }

}