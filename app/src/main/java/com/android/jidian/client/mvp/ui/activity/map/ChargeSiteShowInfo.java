package com.android.jidian.client.mvp.ui.activity.map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.android.jidian.client.Main;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.ChargeSiteMapInfoBean;
import com.android.jidian.client.http.BaseHttp;
import com.android.jidian.client.http.BaseHttpParameterFormat;
import com.android.jidian.client.mvp.bean.ChargeSiteMapBean;
import com.android.jidian.client.util.MapUtil;
import com.android.jidian.client.util.UserInfoHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ChargeSiteShowInfo extends BasePopupWindow {

    private Activity activity;
    private ChargeSiteMapBean.DataBean data;

    private double weidu = 0;
    private double jingdu = 0;

    public ViewPager viewPager;
    public ArrayList<View> views = new ArrayList<View>();
    private OnClickItemViewListener mListener;

    interface OnClickItemViewListener {
        void OnClickNavigation(String name, String weidu, String jingdu);
    }


    public ChargeSiteShowInfo(Activity activity, ChargeSiteMapBean.DataBean data, double weidu, double jingdu, OnClickItemViewListener listener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.data = data;
        this.weidu = weidu;
        this.jingdu = jingdu;
        this.mListener = listener;
        setContentView(R.layout.u6_activity_map_show_info);
        init();
    }

    private void init() {
        viewPager = this.findViewById(R.id.viewPager);
        List<ChargeSiteMapBean.DataBean.ListBean> mapSiteItemArrayList = data.getList();
        for (int i = 0; i < mapSiteItemArrayList.size(); i++) {

            View view = LayoutInflater.from(activity).inflate(R.layout.u6_activity_map_show_info_item, null);
            views.add(view);

            List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid", UserInfoHelper.getInstance().getUid()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("jingdu", mapSiteItemArrayList.get(i).getJingdu()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("weidu", mapSiteItemArrayList.get(i).getWeidu()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("cabid", mapSiteItemArrayList.get(i).getId()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("repair", mapSiteItemArrayList.get(i).getRepair()));
            BaseHttp baseHttp = new BaseHttp(activity, "https://appx.mixiangx.com/Cabinet/detailv3", baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
                @Override
                public void dataReturn(int code, String errorMessage, String message, String data) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (code == 1) {

                                ChargeSiteMapInfoBean chargeSiteMapInfoBean = new Gson().fromJson(data, ChargeSiteMapInfoBean.class);

                                ImageView i_1 = view.findViewById(R.id.i_1);
                                if (chargeSiteMapInfoBean.getImages().size() > 0) {
                                    Glide.with(activity).load(chargeSiteMapInfoBean.getImages().get(0)).into(i_1);
                                }

                                TextView t_1 = view.findViewById(R.id.t_1);
                                TextView t_2 = view.findViewById(R.id.t_2);
                                TextView t_3 = view.findViewById(R.id.t_3);
                                TextView t_4 = view.findViewById(R.id.t_4);
                                TextView t_5 = view.findViewById(R.id.t_5);
                                TextView t_6 = view.findViewById(R.id.t_6);
                                TextView t_7 = view.findViewById(R.id.t_7);
                                LinearLayout l_1 = view.findViewById(R.id.l_1);
                                LinearLayout l_2 = view.findViewById(R.id.l_2);
                                t_1.setText(chargeSiteMapInfoBean.getName());
                                t_2.setText(chargeSiteMapInfoBean.getAddress());
                                t_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mListener != null) {
                                            mListener.OnClickNavigation(chargeSiteMapInfoBean.getName() + chargeSiteMapInfoBean.getCabno(), chargeSiteMapInfoBean.getWeidu(), chargeSiteMapInfoBean.getJingdu());
                                        }
//                                        Poi end = new Poi("", new LatLng(Double.parseDouble(chargeSiteMapInfoBean.getWeidu()), Double.parseDouble(chargeSiteMapInfoBean.getJingdu())), null);
//                                        AmapNaviParams params = new AmapNaviParams(null, null, end, AmapNaviType.RIDE, AmapPageType.NAVI);
//                                        AmapNaviPage.getInstance().showRouteActivity(activity.getApplicationContext(), params, null);
//                                        MapUtil.showNavigationDialog(activity, getFragmentManager()
//                                                , chargeSiteMapInfoBean.getName() + chargeSiteMapInfoBean.getCabno()
//                                                , chargeSiteMapInfoBean.getWeidu(), chargeSiteMapInfoBean.getJingdu());
//                                        MapUtil.showNavigationDialog(activity, "目的地", chargeSiteMapInfoBean.getWeidu() + "", chargeSiteMapInfoBean.getJingdu() + "");

                                    }
                                });


                                if (chargeSiteMapInfoBean.getNowline().equals("在线")) {
                                    t_4.setText("在线");
                                    t_4.setTextColor(0xff169bd5);
                                } else {
                                    t_4.setText("离线");
                                    t_4.setTextColor(0xffcccccc);
                                }

                                t_5.setText(chargeSiteMapInfoBean.getOtime());
                                t_6.setText(chargeSiteMapInfoBean.getMphone());
                                t_7.setText(chargeSiteMapInfoBean.getCabno());

                                for (int i = 0; i < chargeSiteMapInfoBean.getBty_rate().size(); i++) {
                                    View view1 = LayoutInflater.from(activity).inflate(R.layout.activity_charge_site_map_show_info_item_item, null);
                                    TextView t_1_1 = view1.findViewById(R.id.t_1);
                                    TextView t_1_2 = view1.findViewById(R.id.t_2);
                                    t_1_1.setText(chargeSiteMapInfoBean.getBty_rate().get(i).getName());
                                    t_1_2.setText(chargeSiteMapInfoBean.getBty_rate().get(i).getNum() + "块");
                                    l_1.addView(view1);
                                }
                                l_2.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
            baseHttp.onStart();
        }


        ChargeSiteMapViewPagerAdapter adapter = new ChargeSiteMapViewPagerAdapter(activity, views);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(mapSiteItemArrayList.size());
        viewPager.setPageTransformer(true, new ScalePageTransformer(true));
    }

}
