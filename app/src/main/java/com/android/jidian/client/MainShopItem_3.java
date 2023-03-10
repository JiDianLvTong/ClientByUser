package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.pub.ScanCodeActivity;
import com.android.jidian.client.mvp.ui.dialog.DialogByChoiceType2;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.main_shop_item_3)
public class MainShopItem_3 extends BaseFragment {
    public static Handler handler;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    LinearLayout scan_panel;

    private String url = "";
    private String tip = "";
    private String id_auth_tip = "";
    private int local_is_skip;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.main_shop_item_3, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //???????????????????????????????????????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_BUSINESS_LEASING);
                }
            }, 500);
        }
    }

    @AfterViews
    void afterViews() {
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
        initParam();
        initHandler();
        HttpGetUserInfo_2();
        refresh.setCanLoadMore(false);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        if (PubFunction.isConnect(requireActivity(), true)) {
                            HttpGetShopInfoItem(url, MainShop.page_3_code, MainShop.mtoken);
                            HttpGetUserInfo_2();
                        } else {
                            if (refresh != null) {
                                refresh.finishRefresh();
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

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                scan_panel.setVisibility(View.GONE);
                HttpGetShopInfoItem(url, MainShop.page_3_code, MainShop.mtoken);
            }
        };
    }

    @Click
    void scan_panel_text() {
        if (getActivity() != null) {
            //?????????????????????
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HELLO_MALL_SCAN);
            Intent intent = new Intent(getActivity(), ScanCodeActivity.class);
            getActivity().startActivityForResult(intent, 0x00010);
        }
    }

    @Background
    void HttpGetShopInfoItem(String path, String merid, String mtoken) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("mtoken", mtoken));
        dataList.add(new ParamTypeData("credit", "1"));
        new OkHttpConnect(getActivity(), path, dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetShopInfoItem(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetShopInfoItem(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            Gson gson = new Gson();
            ShopRentBean shopRentBean = gson.fromJson(response, ShopRentBean.class);
            int status = shopRentBean.getStatus();
            String msg = shopRentBean.getMsg();
            if (status == 1) {
                if (getActivity() == null) {
                    return;
                }
                scan_panel.setVisibility(View.GONE);
                local_is_skip = shopRentBean.getIs_skip();
                tip = shopRentBean.getTips();
                List<ShopRentBean.DataBean.PacksBean> packsBean = shopRentBean.getData().getPacks();
                if (packsBean == null) {
                    MyToast.showTheToast(getActivity(), "??????????????????");
                } else {
                    if (packsBean.size() < 1) {
                        MyToast.showTheToast(getActivity(), "??????????????????");
                    }
                }
                MyGridAdapter myGridAdapter = new MyGridAdapter(getActivity(), packsBean, R.layout.main_shop_recyclerview_item);
                listview.setAdapter(myGridAdapter);
                listview.setDividerHeight(0);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //??????????????????
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BUSINESS_LEASING_LEASE);
                        int is_sell = packsBean.get(position).getIs_sell();
                        String id_ = packsBean.get(position).getId();
                        if (is_sell == -1) {
                            MyToast.showTheToast(requireActivity(), "???????????????????????????????????????????????????????????????~");
                        } else if (is_sell == 1) {
                            if (local_is_skip == 0) {
                                if (getActivity() != null) {
                                    if ("?????????".equals(id_auth_tip)) {
                                        Intent intent = new Intent(getActivity(), ShopRentZhima_.class);
                                        intent.putExtra("id", id_);
                                        intent.putExtra("from", "product");
                                        getActivity().startActivity(intent);
                                    } else {
                                        new DialogByChoiceType2(getActivity(), "????????????????????????","????????????????????????", new DialogByChoiceType2.DialogChoiceListener() {
                                            @Override
                                            public void enterReturn() {
                                                getActivity().startActivity(new Intent(getActivity(), MainAuthentication_.class));
                                            }

                                            @Override
                                            public void cancelReturn() {

                                            }
                                        }).showPopupWindow();
                                    }
                                }
                            } else if (local_is_skip == 1) {
                                //??????????????????1
                            } else {
                                MyToast.showTheToast(requireActivity(), tip);
                            }
                        } else {
                            MyToast.showTheToast(requireActivity(), "is_sell??????????????????1???-1");
                        }
                    }
                });
            } else {
                MyToast.showTheToast(requireActivity(), msg);
            }
        }
    }

    /**
     * http?????????User/personal.html   ??????????????????
     */
    @Background
    void HttpGetUserInfo_2() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(getActivity(), PubFunction.app + "User/personal.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUserInfo_2(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpGetUserInfo_2();
    }

    @UiThread
    void onDataHttpGetUserInfo_2(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String msg = jsonResponse.getString("msg");
                String status = jsonResponse.getString("status");
                System.out.println(jsonResponse);
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonResponse.getJSONObject("data");
                    id_auth_tip = jsonObject.getString("id_auth_tip");
                } else {
                    MyToast.showTheToast(getActivity(), msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(getActivity(), "JSON???" + e.toString());
            }
        }
    }

    class MyGridAdapter extends BaseAdapter {
        private Activity activity;
        private List<ShopRentBean.DataBean.PacksBean> data;
        private int layout;

        MyGridAdapter(Activity activity, List<ShopRentBean.DataBean.PacksBean> data, int layout) {
            this.activity = activity;
            this.data = data;
            this.layout = layout;
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
                viewHolder.t_1 = convertView.findViewById(R.id.t_1);
                viewHolder.t_2 = convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = convertView.findViewById(R.id.t_4);
                viewHolder.t_2_layout = convertView.findViewById(R.id.t_2_layout);
                viewHolder.l_1 = convertView.findViewById(R.id.l_1);
                viewHolder.l_2 = convertView.findViewById(R.id.l_2);
                viewHolder.bottom_image = convertView.findViewById(R.id.bottom_image);
                viewHolder.i_1 = convertView.findViewById(R.id.i_1);
                viewHolder.i_2 = convertView.findViewById(R.id.i_2);
                viewHolder.i_3 = convertView.findViewById(R.id.i_3);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (!TextUtils.isEmpty(data.get(position).getBg_img())) {
                Glide.with(activity).load(data.get(position).getBg_img()).into(viewHolder.bottom_image);
            }
            //3?????????
            String otype = data.get(position).getOtype();
            if (!TextUtils.isEmpty(otype) && ("230".equals(otype) || "231".equals(otype) || "232".equals(otype))) {
                viewHolder.t_2_layout.setVisibility(View.GONE);
                viewHolder.t_1.setVisibility(View.GONE);
                viewHolder.l_2.setVisibility(View.GONE);
            } else {
                viewHolder.t_2_layout.setVisibility(View.VISIBLE);
                viewHolder.t_1.setVisibility(View.VISIBLE);
                viewHolder.l_2.setVisibility(View.VISIBLE);
            }
            viewHolder.t_1.setText(data.get(position).getName());
            viewHolder.t_2.setText(data.get(position).getRprice() + "???");
            viewHolder.t_3.setText("??????:??" + data.get(position).getOprice() + "???");
            viewHolder.t_3.setPaintFlags(viewHolder.t_3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.t_4.setText("??????");
            viewHolder.i_1.setVisibility(View.GONE);
            viewHolder.i_2.setVisibility(View.GONE);
            viewHolder.i_3.setVisibility(View.GONE);

            List<ShopRentBean.DataBean.PacksBean.MainsBean.LeftBean> mainsBean = data.get(position).getMains().getLeft();
            viewHolder.l_2.removeAllViews();
            if (mainsBean.size() > 0) {
                for (int i = 0; i < mainsBean.size(); i++) {
                    if (mainsBean.get(0).getName().contains("???")) {
                        break;
                    }
                    LinearLayout linearLayout = (LinearLayout) View.inflate(activity, R.layout.main_shop_recyclerview_item_item, null);
                    if (i != 0) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(20, 0, 0, 0);
                        linearLayout.setLayoutParams(layoutParams);
                    }
                    TextView t_1 = linearLayout.findViewById(R.id.t_1);
                    TextView t_2 = linearLayout.findViewById(R.id.t_2);
                    t_1.setText(mainsBean.get(i).getName());
                    t_2.setText(mainsBean.get(i).getSize());
                    viewHolder.l_2.addView(linearLayout);
                }
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView bottom_image;
            ImageView i_1;
            ImageView i_2;
            ImageView i_3;
            TextView t_1;
            TextView t_2;
            TextView t_3;
            TextView t_4;
            LinearLayout t_2_layout;
            LinearLayout l_1;
            LinearLayout l_2;
        }
    }
}