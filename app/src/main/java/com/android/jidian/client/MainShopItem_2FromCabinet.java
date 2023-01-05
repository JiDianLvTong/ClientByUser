package com.android.jidian.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.ScanCodeNewActivity;
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

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.main_shop_item_2)
public class MainShopItem_2FromCabinet extends BaseFragment {
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    LinearLayout scan_panel;
    private String merid = "";

    @ViewById
    TextView scan_panel_text;
    private Context context;
    private String is_skip;
    private String froms;
    private String mtoken;

    private int local_is_skip;
    private String tip = "";
    public static Handler MainShopItem_2FromCabinetScanHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //商家购买页访问（通过点击站点-商城按钮进入的）
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MERCHANT_PURCHASE_SHOPPING);
                }
            }, 500);
        }
    }

    void handler() {
        MainShopItem_2FromCabinetScanHandler = new Handler() {
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
        if ("0".equals(is_skip)) {
            //不用扫码
            HttpGetShopInfoItem();
        } else {
            //需要扫码
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
                        if (PubFunction.isConnect(context, true)) {
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
        Intent intent = new Intent(requireActivity(), ScanCodeNewActivity.class);
        requireActivity().startActivityForResult(intent, 0x000108);
    }

    @Background
    void HttpGetShopInfoItem1() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("mtoken", mtoken));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", "30"));
        new OkHttpConnect(getActivity(), PubFunction.app + "Goodsv2/buyv3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), (response, type) -> {
            onDataHttpGetShopInfoItem(response, type);
            progressDialog.dismiss();
        }).startHttpThread();
    }

    @Background
    void HttpGetShopInfoItem() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("gtype", "buy"));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", froms));
        new OkHttpConnect(getActivity(), PubFunction.app + "Goodsv2/cab2Goodsv4.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), (response, type) -> {
            onDataHttpGetShopInfoItem(response, type);
            progressDialog.dismiss();
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetShopInfoItem(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
            if (refresh != null) {
                refresh.finishRefresh();
            }
        } else {
            Gson gson = new Gson();
            ShopBuyBean shopBuyBean = gson.fromJson(response, ShopBuyBean.class);
            if (shopBuyBean.getStatus() == 1) {
                scan_panel.setVisibility(View.GONE);
                local_is_skip = shopBuyBean.getIs_skip();
                tip = shopBuyBean.getTips();
                List<ShopBuyBean.DataBean.PacksBean> packsBean = shopBuyBean.getData().getPacks();
                if (packsBean == null) {
                    MyToast.showTheToast(getActivity(), "商家暂无商品");
                } else {
                    if (packsBean.size() < 1) {
                        MyToast.showTheToast(getActivity(), "商家暂无商品");
                    }
                }
                MyGridAdapter myGridAdapter = new MyGridAdapter(getActivity(), packsBean, R.layout.main_shop_recyclerview_item);
                listview.setAdapter(myGridAdapter);
                listview.setDividerHeight(0);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //点击购买按钮
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_MERCHANT_PURCHASE_SHOPPING_BUY);
                        int is_sell = packsBean.get(position).getIs_sell();
                        int is_buy = packsBean.get(position).getIs_buy();
                        String otype = packsBean.get(position).getOprice();
                        String id_ = packsBean.get(position).getId();
                        if (is_sell == -1) {
                            MyToast.showTheToast(context, "如需购买商品，请联系商家扫描商家二维码购买~");
                        } else if (is_sell == 1) {
                            if (local_is_skip == 0) {
                                if ("230".equals(otype) || "231".equals(otype) || "232".equals(otype)) {
                                    if (is_buy == 1) {
                                        Intent intent = new Intent(getActivity(), Newshoppay_.class);
                                        intent.putExtra("id", id_);
                                        intent.putExtra("from", "product");
                                        intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.PURCHASE_SHOPPING_BUY);
                                        context.startActivity(intent);
                                    } else {
                                        MyToast.showTheToast(context, "每个用户年卡过期前，仅限购买同类型年卡~");
                                    }
                                } else {
                                    Intent intent = new Intent(getActivity(), Newshoppay_.class);
                                    intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.PURCHASE_SHOPPING_BUY);
                                    intent.putExtra("id", id_);
                                    intent.putExtra("from", "product");
                                    context.startActivity(intent);
                                }
                            } else if (local_is_skip == 1) {
                                //暂时不会返回这个
                            } else {
                                MyToast.showTheToast(requireActivity(), tip);
                            }
                        } else {
                            MyToast.showTheToast(context, "is_sell错误，必须是1和-1");
                        }
                    }
                });
            } else {
                scan_panel.setVisibility(View.VISIBLE);
                MyToast.showTheToast(requireActivity(), shopBuyBean.getMsg());
            }
        }
    }

    class MyGridAdapter extends BaseAdapter {
        private Activity activity;
        private List<ShopBuyBean.DataBean.PacksBean> data;
        private int layout;

        MyGridAdapter(Activity activity, List<ShopBuyBean.DataBean.PacksBean> data, int layout) {
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
                viewHolder.l_1 = convertView.findViewById(R.id.l_1);
                viewHolder.l_2 = convertView.findViewById(R.id.l_2);
                viewHolder.l_3 = convertView.findViewById(R.id.l_3);
                viewHolder.t_2_layout = convertView.findViewById(R.id.t_2_layout);
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
            //3种年卡
            String otype = data.get(position).getOtype();
            if (otype != null && ("230".equals(otype) || "231".equals(otype) || "232".equals(otype))) {
                viewHolder.t_2_layout.setVisibility(View.GONE);
                viewHolder.t_1.setVisibility(View.GONE);
                viewHolder.l_2.setVisibility(View.GONE);
            } else {
                viewHolder.t_2_layout.setVisibility(View.VISIBLE);
                viewHolder.t_1.setVisibility(View.VISIBLE);
                viewHolder.l_2.setVisibility(View.VISIBLE);
            }
            viewHolder.t_1.setText(data.get(position).getName());
            viewHolder.t_2.setText(data.get(position).getRprice() + "元");
            viewHolder.t_3.setText("价格:¥" + data.get(position).getOprice() + "元");
            viewHolder.t_3.setPaintFlags(viewHolder.t_3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.t_4.setText("购买");
            viewHolder.i_1.setVisibility(View.GONE);
            viewHolder.i_2.setVisibility(View.GONE);
            viewHolder.i_3.setVisibility(View.GONE);

            List<ShopBuyBean.DataBean.PacksBean.MainsBean> mainsBean = data.get(position).getMains();
            viewHolder.l_2.removeAllViews();
            if (mainsBean.size() > 0) {
                for (int i = 0; i < mainsBean.size(); i++) {
                    if (mainsBean.get(0).getName().contains("卡")) {
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
            LinearLayout l_3;
        }
    }
}