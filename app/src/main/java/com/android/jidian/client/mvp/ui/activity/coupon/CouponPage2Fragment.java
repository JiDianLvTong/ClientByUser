package com.android.jidian.client.mvp.ui.activity.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jidian.client.MainDiscount_2;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseFragment;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.dialog.DialogByLoading;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.widgets.MyToast;
import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CouponPage2Fragment extends U6BaseFragment implements AbsListView.OnScrollListener {

    public static Handler reloadHandler;

    @BindView(R.id.none_panel)
    LinearLayout none_panel;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private DiscountSimoleAdapter discountSimoleAdapter;
    private List<Map<String, String>> dataList = new ArrayList<>();
    private String lastid = "";
    private int count_page = 2;
    private int current_page = 1;

    protected DialogByLoading progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_coupon_item;
    }

    @Override
    public void initView(View view) {
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        progressDialog = new DialogByLoading(requireActivity());
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
                    //???????????????-?????????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MY_COUPONS);
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
                HttpGetDis();
            }
        };
        listView.setOnScrollListener(this);

        //????????????
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                lastid = "";
                count_page = 2;
                current_page = 1;
                HttpGetDis();
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
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
     * http?????????Coupon/listsV2.html   ????????????????????????
     */
    void HttpGetDis_Re(String lastid_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "2"));
        dataList.add(new ParamTypeData("lastid", lastid_str));

        new OkHttpConnect(getActivity(), PubFunction.app + "Coupon/listsV2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetDis_Re(response, type);
                progressDialog.dismiss();

            }
        }).startHttpThread();
    }

    void onDataHttpGetDis_Re(String response, String type) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                smartRefreshLayout.finishRefresh();
                if ("0".equals(type)) {
                    MyToast.showTheToast(getActivity(), response);
                } else {
                    try {
                        JSONObject jsonObject_response = new JSONObject(response);
                        String msg = jsonObject_response.getString("msg");
                        String status = jsonObject_response.getString("status");
                        if ("1".equals(status)) {
                            JSONArray jSONArray = jsonObject_response.getJSONArray("data");
                            String mLastId = jsonObject_response.getString("lastid");
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jsonObject = jSONArray.getJSONObject(i);
                                Map<String, String> map = new HashMap<>();
                                map.put("id", jsonObject.getString("id"));
                                map.put("code", jsonObject.getString("code"));
                                map.put("ustatus", jsonObject.getString("ustatus"));
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
                                dataList.add(map);
                            }
                            lastid = mLastId;
                            discountSimoleAdapter.notifyDataSetChanged();
                            count_page = count_page + 1;
                        } else {
                            MyToast.showTheToast(getActivity(), msg);
                        }
                    } catch (Exception e) {
                        MyToast.showTheToast(getActivity(), "JSON???" + e.toString());
                    }
                }
            }
        });
    }

    /**
     * http?????????Coupon/listsV2.html   ??????????????????
     */
    void HttpGetDis() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "2"));
        new OkHttpConnect(getActivity(), PubFunction.app + "Coupon/listsV2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetDis(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    void onDataHttpGetDis(String response, String type) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                smartRefreshLayout.finishRefresh();
                if ("0".equals(type)) {
                    MyToast.showTheToast(getActivity(), response);
                } else {
                    try {
                        JSONObject jsonObject_response = new JSONObject(response);
                        String status = jsonObject_response.getString("status");
                        System.out.println(jsonObject_response);
                        if ("1".equals(status)) {
                            dataList.clear();
                            JSONArray jSONArray = jsonObject_response.getJSONArray("data");
                            String mLastId = jsonObject_response.getString("lastid");
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
                                map.put("ustatus", jsonObject.getString("ustatus"));
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
                                dataList.add(map);
                            }
                            if (discountSimoleAdapter == null) {
                                discountSimoleAdapter = new DiscountSimoleAdapter(getActivity(), dataList, R.layout.main_discount_item_1);
                                listView.setAdapter(discountSimoleAdapter);
                                listView.setDividerHeight(0);
                            } else {
                                discountSimoleAdapter.notifyDataSetChanged();
                            }
                            lastid = mLastId;
                            smartRefreshLayout.finishRefresh();
                        } else {
                            none_panel.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        MyToast.showTheToast(getActivity(), "JSON???" + e.toString());
                    }
                }
            }
        });
    }

    /**
     * ?????????
     */
    class DiscountSimoleAdapter extends BaseAdapter {
        private Context context;
        private List<? extends Map<String, ?>> data;
        private int resource;

        public DiscountSimoleAdapter(Context context, List<? extends Map<String, ?>> data, int resource) {
            this.context = context;
            this.data = data;
            this.resource = resource;
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
            DiscountSimoleAdapter.ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(context, resource, null);
                viewHolder = new DiscountSimoleAdapter.ViewHolder();
                viewHolder.t_1 = (TextView) convertView.findViewById(R.id.t_1);
                viewHolder.t_2 = (TextView) convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = (TextView) convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = (TextView) convertView.findViewById(R.id.t_4);
                viewHolder.t_5 = (TextView) convertView.findViewById(R.id.is_use);
                viewHolder.iv_coupon_status = (ImageView) convertView.findViewById(R.id.iv_coupon_status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (DiscountSimoleAdapter.ViewHolder) convertView.getTag();
            }
            viewHolder.t_1.setText(data.get(position).get("name").toString());
//            if (TextUtils.isEmpty(data.get(position).get("unit").toString())) {
//                viewHolder.t_2.setText(data.get(position).get("fval").toString() + "???");
//            } else {
//                viewHolder.t_2.setText(data.get(position).get("fval").toString() + data.get(position).get("unit").toString());
//            }

            if ("1".equals(data.get(position).get("show"))) {
                viewHolder.t_2.setText(data.get(position).get("fval").toString() + data.get(position).get("unit").toString());
            }else {
                viewHolder.t_2.setVisibility(View.GONE);
            }

            String ustatus = data.get(position).get("ustatus").toString();
            //is_use = 1 ????????????  -1 = ??????
            //ustatus  2 ????????????  -1 = ?????? 1 = ????????????
            if ("-1".equals(ustatus)) {
                viewHolder.t_5.setVisibility(View.GONE);
                viewHolder.iv_coupon_status.setVisibility(View.VISIBLE);
                viewHolder.iv_coupon_status.setImageResource(R.drawable.u6_coupon_expire);
                viewHolder.t_1.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_2.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_3.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_4.setTextColor(Color.parseColor("#FFFFFF"));
            } else if ("1".equals(ustatus)){
                viewHolder.t_5.setVisibility(View.GONE);
                viewHolder.iv_coupon_status.setVisibility(View.VISIBLE);
                viewHolder.iv_coupon_status.setImageResource(R.drawable.u6_coupon_used);
                viewHolder.t_1.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_2.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_3.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_4.setTextColor(Color.parseColor("#FFFFFF"));
            }else {
                viewHolder.t_5.setVisibility(View.VISIBLE);
                viewHolder.iv_coupon_status.setVisibility(View.GONE);
                viewHolder.t_1.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_2.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_3.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.t_4.setTextColor(Color.parseColor("#FFFFFF"));
            }

            viewHolder.t_3.setText(data.get(position).get("expire").toString());
            viewHolder.t_4.setText(data.get(position).get("urules").toString());
            viewHolder.t_5.setVisibility(View.GONE);
            String is_use_str = data.get(position).get("is_use").toString();
            if ("-1".equals(is_use_str)) {
                viewHolder.t_2.setTextColor(0xffaaaaaa);
            } else {
                viewHolder.t_2.setTextColor(0xffaaaaaa);
            }
//            Glide.with(getActivity()).load(data.get(position).get("bg_img").toString()).into(viewHolder.i_1);
            return convertView;
        }

        private class ViewHolder {
            TextView t_1;
            TextView t_2;
            TextView t_3;
            TextView t_4;
            TextView t_5;
            ImageView iv_coupon_status;
        }
    }


}
