package com.android.jidian.client;

import android.app.Activity;
import android.content.Context;
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
public class MainShopItem_3FromCabinet extends BaseFragment {
    public static Handler handler;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    LinearLayout scan_panel;
    @ViewById
    TextView scan_panel_text;

    private String id_auth_tip = "";
    private Context mContext;
    private String merid = "";
    private String froms = "";
    private String mtoken;
    private String is_skip;
    public static Handler MainShopItem_3FromCabinetScanHandler;
    private int local_is_skip;
    private String tip = "";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //??????????????????????????????????????????-????????????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_BUSINESS_LEASING_SHOPPING);
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_shop_item_3, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    void handler() {
        MainShopItem_3FromCabinetScanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    scan_panel.setVisibility(View.GONE);
                    merid = msg.getData().getString("code");
                    mtoken = msg.getData().getString("mtoken");
                    HttpGetShopInfoItem1();
                } else {
                    scan_panel.setVisibility(View.GONE);
                    merid = msg.getData().getString("code");
                    mtoken = msg.getData().getString("mtoken");
                    HttpGetShopInfoItem();
                }
            }
        };
    }

    @AfterViews
    void afterViews() {
        initParam();
        handler();
        scan_panel.setVisibility(View.GONE);
        if (getArguments() != null) {
            froms = getArguments().getString("froms");
            merid = getArguments().getString("merid");
            is_skip = getArguments().getString("is_skip");
        }
        HttpGetUserInfo();
        if ("0".equals(is_skip)) {
            //????????????
            HttpGetShopInfoItem();
        } else {
            //????????????
        }
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
                        if (PubFunction.isConnect(mContext, true)) {
                            HttpGetShopInfoItem();
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

    @Click
    void scan_panel_text() {
        Intent intent = new Intent(requireActivity(), ScanCodeActivity.class);
        requireActivity().startActivityForResult(intent, 0x000109);
    }

    @Background
    void HttpGetShopInfoItem1() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("mtoken", mtoken));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", "30"));
        dataList.add(new ParamTypeData("credit", "1"));
        new OkHttpConnect(getActivity(), PubFunction.app + "Goodsv2/rentv3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), (response, type) -> {
            onDataHttpGetShopInfoItem(response, type);
            progressDialog.dismiss();
        }).startHttpThread();
    }

    @Background
    void HttpGetShopInfoItem() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("gtype", "rent"));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", froms));
        new OkHttpConnect(getActivity(), PubFunction.app + "Goodsv2/cab2Goodsv4.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
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
            if (shopRentBean.getStatus() == 1) {
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
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BUSINESS_LEASING_SHOPPING_LEASE);
                        int is_sell = packsBean.get(position).getIs_sell();
                        String id_ = packsBean.get(position).getId();
                        if (is_sell == -1) {
                            MyToast.showTheToast(mContext, "???????????????????????????????????????????????????????????????~");
                        } else if (is_sell == 1) {
                            if (local_is_skip == 0) {
                                if (getActivity() != null) {
                                    if ("?????????".equals(id_auth_tip)) {
                                        Intent intent = new Intent(getActivity(), ShopRentZhima_.class);
                                        intent.putExtra("id", id_);
                                        intent.putExtra("from", "product");
                                        intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.LEASING_SHOPPING_LEASE);
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
                            MyToast.showTheToast(mContext, "is_sell??????????????????1???-1");
                        }
                    }
                });
            } else {
                scan_panel.setVisibility(View.VISIBLE);
                MyToast.showTheToast(requireActivity(), shopRentBean.getMsg());
            }
        }
    }

    /**
     * http?????????User/personal.html   ??????????????????
     */
    @Background
    void HttpGetUserInfo() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(getActivity(), PubFunction.app + "User/personal.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUserInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetUserInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                    id_auth_tip = jsonObject.getString("id_auth_tip");
                } else {
                    MyToast.showTheToast(getActivity(), msg);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
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