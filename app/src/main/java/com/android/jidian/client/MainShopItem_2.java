package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import static com.android.jidian.client.MainShop.MSG_FROMS;
import static com.android.jidian.client.MainShop.MSG_MERID;
import static com.android.jidian.client.MainShop.MSG_OTYPE;

@EFragment(R.layout.main_shop_item_2)
public class MainShopItem_2 extends BaseFragment {
    public static Handler handler;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    LinearLayout scan_panel;

    private String url = "";
    private String tip = "";
    private Context context;
    private int local_is_skip;

    private String mOtype, mFroms, mMerid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.main_shop_item_2, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //商家购买页访问（通过扫一扫进入的）
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MERCHANT_PURCHASE);
                }
            }, 500);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @AfterViews
    void afterViews() {
        String cid = "";
        if (getArguments() != null) {
            url = getArguments().getString("url");
            mOtype = getArguments().getString(MSG_OTYPE);
            mFroms = getArguments().getString(MSG_FROMS);
            mMerid = getArguments().getString(MSG_MERID);
            cid = getArguments().getString("id");
        }
        initParam();
        initHandler();

        if ("20".equals(cid)) {
            if (!TextUtils.isEmpty(MainShop.page_2_code)) {
                HttpGetShopInfoItem(url, MainShop.page_2_code, MainShop.mtoken);
            }
        }

        if (!TextUtils.isEmpty(mOtype)) {
            HttpGetShopInfoItem(url, MainShop.page_2_code, MainShop.mtoken);
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
                        if (getActivity() != null) {
                            if (PubFunction.isConnect(getActivity(), true)) {
                                HttpGetShopInfoItem(url, MainShop.page_2_code, MainShop.mtoken);
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

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (getActivity() != null) {
                    scan_panel.setVisibility(View.GONE);
                    HttpGetShopInfoItem(url, MainShop.page_2_code, MainShop.mtoken);
                }
            }
        };
    }

    @Click
    void scan_panel_text() {
        if (getActivity() != null) {
            //点击扫一扫按钮
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HELLO_MALL_SCAN);
            Intent intent = new Intent(getActivity(), ScanCodeNewActivity.class);
            getActivity().startActivityForResult(intent, 0x0005);
        }
    }

    @Background
    void HttpGetShopInfoItem(String path, String merid, String mtoken) {
        List<ParamTypeData> dataList = new ArrayList<>();
        if (!TextUtils.isEmpty(mOtype)) {
            //首页弹框场景三 用户购买的电池寿命少于10次时，用户点击去续费，跳转到电池所属商家商城-购买模块，只展示电池寿命相关的商品
            dataList.add(new ParamTypeData("uid", uid));
            dataList.add(new ParamTypeData("froms", mFroms));
            dataList.add(new ParamTypeData("otype", mOtype));
            dataList.add(new ParamTypeData("merid", mMerid));
            dataList.add(new ParamTypeData("gtype", "buy"));
            path = PubFunction.app + "Goodsv2/cab2Goodsv4.html";
        } else {
            dataList.add(new ParamTypeData("uid", uid));
            dataList.add(new ParamTypeData("merid", merid));
            dataList.add(new ParamTypeData("mtoken", mtoken));
        }
        new OkHttpConnect(getActivity(), path, dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetShopInfoItem(response, type);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetShopInfoItem(String response, String type) {
        if (getActivity() != null) {
            if (refresh != null) {
                refresh.finishRefresh();
            }
            if ("0".equals(type)) {
                MyToast.showTheToast(getActivity(), response);
            } else {
                ShopBuyBean shopBuyBean = new Gson().fromJson(response, ShopBuyBean.class);
                int status = shopBuyBean.getStatus();
                String msg = shopBuyBean.getMsg();
                if (status == 1) {
                    if (getActivity() == null) {
                        return;
                    }
                    scan_panel.setVisibility(View.GONE);
                    local_is_skip = shopBuyBean.getIs_skip();
                    tip = shopBuyBean.getTips();
                    List<ShopBuyBean.DataBean.PacksBean> packsBean = shopBuyBean.getData().getPacks();
                    if (packsBean == null) {
                        MyToast.showTheToast(context, "商家暂无商品");
                    } else {
                        if (packsBean.size() < 1) {
                            MyToast.showTheToast(context, "商家暂无商品");
                        }
                        MyGridAdapter myGridAdapter = new MyGridAdapter(getActivity(), packsBean, R.layout.main_shop_recyclerview_item);
                        listview.setAdapter(myGridAdapter);
                        listview.setDividerHeight(0);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //点击购买按钮
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_MERCHANT_PURCHASE_BUY);
                                String otype = packsBean.get(position).getOprice();
                                String item_id = packsBean.get(position).getId();
                                int is_buy = packsBean.get(position).getIs_buy();
                                int is_sell = packsBean.get(position).getIs_sell();
                                if (is_sell == -1) {
                                    MyToast.showTheToast(context, "如需购买商品，请联系商家扫描商家二维码购买~");
                                } else if (is_sell == 1) {
                                    if (local_is_skip == 0) {
                                        if ("230".equals(otype) || "231".equals(otype) || "232".equals(otype)) {
                                            if (is_buy == 1) {
                                                Intent intent = new Intent(getActivity(), Newshoppay_.class);
                                                intent.putExtra("id", item_id);
                                                intent.putExtra("from", "product");
                                                context.startActivity(intent);
                                            } else {
                                                MyToast.showTheToast(context, "每个用户年卡过期前，仅限购买同类型年卡~");
                                            }
                                        } else {
                                            Intent intent = new Intent(getActivity(), Newshoppay_.class);
                                            intent.putExtra("id", item_id);
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
                    }
                } else {
                    MyToast.showTheToast(requireActivity(), msg);
                }
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
                    if (!TextUtils.isEmpty(mainsBean.get(0).getName())) {
                        if (mainsBean.get(0).getName().contains("卡")) {
                            break;
                        }
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