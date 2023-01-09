package com.android.jidian.client.widgets;

import android.app.Activity;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import androidx.viewpager.widget.ViewPager;

import com.android.jidian.client.R;
import com.android.jidian.client.base.AbstractXdzlDialog;
import com.android.jidian.client.base.ViewHolder;
import com.android.jidian.client.bean.ChargeSiteMapInfoBean;
import com.android.jidian.client.bean.MapJwduV5Bean;
import com.android.jidian.client.http.BaseHttp;
import com.android.jidian.client.http.BaseHttpParameterFormat;
import com.android.jidian.client.mvp.ui.adapter.ChargeSiteMapViewPagerAdapter;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.UserInfoHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChargeSiteShowInfo extends AbstractXdzlDialog {

    private Activity activity;
    private MapJwduV5Bean.DataBean data;

    private double weidu = 0;
    private double jingdu = 0;

    public ViewPager viewPager;
    public ArrayList<View> views = new ArrayList<View>();

    private OnSubViewClickListener mListener;

    public ChargeSiteShowInfo() {
//        this.activity = activity;
    }

    public ChargeSiteShowInfo init(Activity activity , MapJwduV5Bean.DataBean data , double weidu , double jingdu,OnSubViewClickListener listener) {
//        super(activity.getApplicationContext());
        this.activity = activity;
        this.data = data;
        this.weidu = weidu;
        this.jingdu = jingdu;
        this.mListener = listener;
//        setContentView(R.layout.activity_charge_site_map_show_info);
//        init();
        return this;
    }

//    private void init(){
//        viewPager = this.findViewById(R.id.viewPager);
//        ArrayList<MapJwduV5Bean.DataBean.ListBean> mapSiteItemArrayList = new ArrayList<>(data.getList());
//        for(int i = 0 ; i < mapSiteItemArrayList.size();i++){
//            View view = LayoutInflater.from(activity).inflate(R.layout.activity_charge_site_map_show_info_item,null);
//            views.add(view);
//            List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
//            baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid", UserInfoHelper.getInstance().getUid()));
//            baseHttpParameterFormats.add(new BaseHttpParameterFormat("jingdu", mapSiteItemArrayList.get(i).getJingdu()));
//            baseHttpParameterFormats.add(new BaseHttpParameterFormat("weidu",  mapSiteItemArrayList.get(i).getWeidu()));
//            baseHttpParameterFormats.add(new BaseHttpParameterFormat("cabid",  mapSiteItemArrayList.get(i).getId()));
//            baseHttpParameterFormats.add(new BaseHttpParameterFormat("repair",  mapSiteItemArrayList.get(i).getRepair()));
//            BaseHttp baseHttp = new BaseHttp(activity, PubFunction.app + "Cabinet/detailv3.html", baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
//                @Override
//                public void dataReturn(int code, String errorMessage , String message , String data) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(code == 1){
//
//                                ChargeSiteMapInfoBean chargeSiteMapInfoBean = new Gson().fromJson(data , ChargeSiteMapInfoBean.class);
//
//                                ImageView i_1 = view.findViewById(R.id.i_1);
//                                if(chargeSiteMapInfoBean.getImages().size()>0){
////                                    Picasso.get().load(chargeSiteMapInfoBean.getImages().get(0)).into(i_1);
//                                    Glide.with(activity).load(chargeSiteMapInfoBean.getImages().get(0)).into(i_1);
//                                }
//
//                                TextView t_1 = view.findViewById(R.id.t_1);
//                                TextView t_2 = view.findViewById(R.id.t_2);
//                                TextView t_3 = view.findViewById(R.id.t_3);
//                                TextView t_4 = view.findViewById(R.id.t_4);
//                                TextView t_5 = view.findViewById(R.id.t_5);
//                                TextView t_6 = view.findViewById(R.id.t_6);
//                                TextView t_7 = view.findViewById(R.id.t_7);
//                                LinearLayout l_1 = view.findViewById(R.id.l_1);
//                                LinearLayout l_2 = view.findViewById(R.id.l_2);
//                                t_1.setText(chargeSiteMapInfoBean.getName());
//                                t_2.setText(chargeSiteMapInfoBean.getAddress());
//                                t_3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Poi end = new Poi("" , new LatLng(Double.parseDouble(chargeSiteMapInfoBean.getWeidu()),Double.parseDouble(chargeSiteMapInfoBean.getJingdu())), null);
//                                        AmapNaviParams params = new AmapNaviParams(null, null, end, AmapNaviType.RIDE, AmapPageType.NAVI);
//                                        AmapNaviPage.getInstance().showRouteActivity(activity.getApplicationContext(), params, null);
//                                    }
//                                });
//
//
//                                if(chargeSiteMapInfoBean.getNowline().equals("在线")){
//                                    t_4.setText("在线");
//                                    t_4.setTextColor(0xff169bd5);
//                                }else{
//                                    t_4.setText("离线");
//                                    t_4.setTextColor(0xffcccccc);
//                                }
//
//                                t_5.setText(chargeSiteMapInfoBean.getOtime());
//                                t_6.setText(chargeSiteMapInfoBean.getMphone());
//                                t_7.setText(chargeSiteMapInfoBean.getCabno());
//
//                                for(int i = 0 ; i < chargeSiteMapInfoBean.getBty_rate().size(); i++){
//                                    View view1 = LayoutInflater.from(activity).inflate(R.layout.activity_charge_site_map_show_info_item_item,null);
//                                    TextView t_1_1 = view1.findViewById(R.id.t_1);
//                                    TextView t_1_2 = view1.findViewById(R.id.t_2);
//                                    t_1_1.setText(chargeSiteMapInfoBean.getBty_rate().get(i).getName());
//                                    t_1_2.setText(chargeSiteMapInfoBean.getBty_rate().get(i).getNum() + "块");
//                                    l_1.addView(view1);
//                                }
//                                l_2.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                }
//            });
//            baseHttp.onStart();
//        }
//        ChargeSiteMapViewPagerAdapter adapter = new ChargeSiteMapViewPagerAdapter(activity,views);
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(mapSiteItemArrayList.size());
//        viewPager.setPageTransformer(true, new ScalePageTransformer(true));
//    }

    @Override
    public int intLayoutId() {
        return R.layout.u6_activity_map_show_info;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
//        viewPager = holder.findViewById(R.id.viewPager);
        viewPager = holder.getView(R.id.viewPager);
        ArrayList<MapJwduV5Bean.DataBean.ListBean> mapSiteItemArrayList = new ArrayList<>(data.getList());
        for(int i = 0 ; i < mapSiteItemArrayList.size();i++){
            View view = LayoutInflater.from(activity).inflate(R.layout.u6_activity_map_show_info_item,null);
            views.add(view);
            List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid", UserInfoHelper.getInstance().getUid()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("jingdu", mapSiteItemArrayList.get(i).getJingdu()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("weidu",  mapSiteItemArrayList.get(i).getWeidu()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("cabid",  mapSiteItemArrayList.get(i).getId()));
            baseHttpParameterFormats.add(new BaseHttpParameterFormat("repair",  mapSiteItemArrayList.get(i).getRepair()));
            BaseHttp baseHttp = new BaseHttp(activity, PubFunction.app + "Cabinet/detailv3.html", baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
                @Override
                public void dataReturn(int code, String errorMessage , String message , String data) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(code == 1){

                                ChargeSiteMapInfoBean chargeSiteMapInfoBean = new Gson().fromJson(data , ChargeSiteMapInfoBean.class);

                                ImageView i_1 = view.findViewById(R.id.i_1);
                                if(chargeSiteMapInfoBean.getImages().size()>0){
//                                    Picasso.get().load(chargeSiteMapInfoBean.getImages().get(0)).into(i_1);
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
//                                        Poi end = new Poi("" , new LatLng(Double.parseDouble(chargeSiteMapInfoBean.getWeidu()),Double.parseDouble(chargeSiteMapInfoBean.getJingdu())), null);
//                                        AmapNaviParams params = new AmapNaviParams(null, null, end, AmapNaviType.RIDE, AmapPageType.NAVI);
//                                        AmapNaviPage.getInstance().showRouteActivity(activity.getApplicationContext(), params, null);
                                        if (mListener != null) {
                                            mListener.onClickGuide(chargeSiteMapInfoBean);
                                        }
                                    }
                                });


                                if(chargeSiteMapInfoBean.getNowline().equals("在线")){
                                    t_4.setText("在线");
                                    t_4.setTextColor(0xff169bd5);
                                }else{
                                    t_4.setText("离线");
                                    t_4.setTextColor(0xffcccccc);
                                }

                                t_5.setText(chargeSiteMapInfoBean.getOtime());
                                t_6.setText(chargeSiteMapInfoBean.getMphone());
                                t_7.setText(chargeSiteMapInfoBean.getCabno());

                                for(int i = 0 ; i < chargeSiteMapInfoBean.getBty_rate().size(); i++){
                                    View view1 = LayoutInflater.from(activity).inflate(R.layout.activity_charge_site_map_show_info_item_item,null);
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
        ChargeSiteMapViewPagerAdapter adapter = new ChargeSiteMapViewPagerAdapter(activity,views);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(mapSiteItemArrayList.size());
        viewPager.setPageTransformer(true, new ScalePageTransformer(true));
    }

    public interface OnSubViewClickListener{
        void onClickGuide(ChargeSiteMapInfoBean chargeSiteMapInfoBean);
    }

}
