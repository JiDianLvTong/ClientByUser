package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.MainActivityU6;
import com.android.jidian.client.mvp.ui.activity.ScanCodeNewActivityU6;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.LogUtils;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.ViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSESHOP;

/**
 * modified by tianyanyu 20190325
 */
@SuppressLint("Registered")
@EActivity(R.layout.main_shop1)
public class MainShop extends BaseFragmentActivity {
    public static String page_2_code = "";
    public static String page_3_code = "";
    public static String mtoken = "";
    @ViewById
    LinearLayout scan_panel;
    @ViewById
    SlidingTabLayout stb_main_shop;
    @ViewById
    ViewPager mViewPager;
    @ViewById
    TextView scan_panel_text, tv_title;

    private String[] tab_url;
    private String[] tab_id;
    private String code;
    private String[] mTabs;

    public static final String MSG_OTYPE = "otype";
    public static final String MSG_FROMS = "froms";
    public static final String MSG_MERID = "merid";
    private String otype, froms, merid;

    private void initView() {
        //购买
        List<Fragment> fragments = new ArrayList<>();
        MainShopItem_2_ mainShopBuy = new MainShopItem_2_();
        Bundle bundleBuy = new Bundle();
        if (!TextUtils.isEmpty(otype)) {
            //首页弹框场景三 用户购买的电池寿命少于10次时，用户点击去续费，跳转到电池所属商家商城-购买模块，只展示电池寿命相关的商品
            bundleBuy.putString(MSG_OTYPE, otype);
            bundleBuy.putString(MSG_FROMS, froms);
            bundleBuy.putString(MSG_MERID, merid);
        } else {
            bundleBuy.putString("url", tab_url[0]);
            bundleBuy.putString("id", tab_id[0]);
            bundleBuy.putString("code", code);
        }
        mainShopBuy.setArguments(bundleBuy);
        fragments.add(mainShopBuy);
        //租赁
        if (mTabs.length > 1) {
            MainShopItem_3_ mainShopLease = new MainShopItem_3_();
            Bundle bundleLease = new Bundle();
            bundleLease.putString("url", tab_url[1]);
            bundleLease.putString("id", tab_id[1]);
            bundleLease.putString("code", code);
            mainShopLease.setArguments(bundleLease);
            fragments.add(mainShopLease);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTabs);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mTabs.length);
        stb_main_shop.setViewPager(mViewPager, mTabs);
        mViewPager.setCurrentItem(0, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    MainShopItem_2.handler.sendMessage(new Message());
                } else {
                    MainShopItem_3.handler.sendMessage(new Message());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @AfterViews
    void afterViews() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tv_title.setText("换电商城");
        if (getIntent() != null) {
            otype = getIntent().getStringExtra(MSG_OTYPE);
            froms = getIntent().getStringExtra(MSG_FROMS);
            merid = getIntent().getStringExtra(MSG_MERID);
            if (!TextUtils.isEmpty(otype)) {
                //首页弹框场景三 用户购买的电池寿命少于10次时，用户点击去续费，跳转到电池所属商家商城-购买模块，只展示电池寿命相关的商品
                requestHttpGetShopInfoBuyItem();
            } else {
                if (getIntent().hasExtra("code")) {
                    code = getIntent().getStringExtra("code");
                    if (!TextUtils.isEmpty(code)) {
                        initInfo(code);
                    }
                }
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //HELLO商城页访问（点击HELLO商城进入）
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_HELLO_MALL);
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainAppEventBean event) {
        if (event != null) {
            if (event.getEvent() == PAYSUCCESSCLOSESHOP) {
                if (Util.isTopActivity("MainShop", this)) {
                    startActivity(new Intent(this, MainActivityU6.class));
                    finish();
                }
            }
        }
    }

    private void initInfo(String content) {
        try {
            String sourceStr = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
            int count = 0;
            for (int i = 0; i < sourceStr.length(); i++) {
                //char 类型转String
                String s = String.valueOf(sourceStr.charAt(i));
                if ("#".equals(s)) {
                    count++;
                }
            }
            if (count >= 4) {
                String[] sourceStrArray = sourceStr.split("##");
                if (PubFunction.isConnect(activity, false)) {
                    scan_panel.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    stb_main_shop.setVisibility(View.VISIBLE);
                    page_3_code = sourceStrArray[0];
                    page_2_code = sourceStrArray[0];
                    mtoken = sourceStrArray[1];
                    HttpGetShopInfo();
                }
            } else {
                MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
            }
        } catch (Exception e) {
            MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
        }
    }

    @Click
    void iv_back() {
        this.finish();
    }

    @Click
    void scan_panel_text() {
        //点击扫一扫按钮
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HELLO_MALL_SCAN);
        Intent intent = new Intent(activity, ScanCodeNewActivityU6.class);
        activity.startActivityForResult(intent, 0x0005);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (PubFunction.isConnect(activity, true)) {
            if (requestCode == 0x0005 && resultCode == RESULT_OK) {
                if (data != null) {
                    String content = data.getStringExtra("codedContent");
                    Util.d("12341234111", content);
                    System.out.println(content);
                    initInfo(content);
                }
            }

            if (requestCode == 0x0010 && resultCode == RESULT_OK) {
                if (data != null) {
                    String content = data.getStringExtra("codedContent");
                    try {
                        String sourceStr = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
                        int count = 0;
                        for (int i = 0; i < sourceStr.length(); i++) {
                            //char 类型转String
                            String s = String.valueOf(sourceStr.charAt(i));
                            if ("#".equals(s)) {
                                count++;
                            }
                        }
                        if (count >= 4) {
                            String[] sourceStrArray = sourceStr.split("##");
                            if (PubFunction.isConnect(activity, false)) {
                                scan_panel.setVisibility(View.GONE);
                                mViewPager.setVisibility(View.VISIBLE);
                                stb_main_shop.setVisibility(View.VISIBLE);
                                page_3_code = sourceStrArray[0];
                                page_2_code = sourceStrArray[0];
                                mtoken = sourceStrArray[1];
                                MainShopItem_2.handler.sendMessage(new Message());
                                MainShopItem_3.handler.sendMessage(new Message());
                            }
                        } else {
                            MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
                        }
                    } catch (Exception e) {
                        MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
                    }
                }
            }
        }
    }

    /**
     * 点击首页弹框
     * 接口返回商品（购买）
     */
    @Background
    void requestHttpGetShopInfoBuyItem() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("froms", froms));
        dataList.add(new ParamTypeData("otype", otype));
        dataList.add(new ParamTypeData("merid", merid));
        dataList.add(new ParamTypeData("gtype", "buy"));
        new OkHttpConnect(activity, PubFunction.app + "Goodsv2/cab2Goodsv4.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), (response, type) -> {
            onDataHttpGetShopInfo(response, type);
            progressDialog.dismiss();
        }).startHttpThread();
    }

    /**
     * 点击首页HELLO商城
     * 接口返回商品（购买、租赁）
     */
    @Background
    void HttpGetShopInfo() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("merid", page_2_code));
        dataList.add(new ParamTypeData("mtoken", mtoken));
        new OkHttpConnect(activity, PubFunction.app + "Goodsv2/buyv3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetShopInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetShopInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                String msg = jsonObjectResponse.getString("msg");
                String status = jsonObjectResponse.getString("status");
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonObjectResponse.getJSONObject("data");
                    //标题
                    String title = jsonObject.getJSONObject("merchant").getString("title");
                    tv_title.setText(title);
                    //tab
                    JSONArray jsonArray = jsonObject.getJSONArray("category");
                    tab_id = new String[jsonArray.length()];
                    mTabs = new String[jsonArray.length()];
                    tab_url = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                        tab_id[i] = jsonObjectItem.getString("id");
                        mTabs[i] = jsonObjectItem.getString("name");
                        tab_url[i] = jsonObjectItem.getString("urls");
                    }
                    initView();
                    if (!TextUtils.isEmpty(code)) {
                        decodeCode(code);
                    }
                    scan_panel.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    stb_main_shop.setVisibility(View.VISIBLE);
                    MainShopItem_2.handler.sendMessage(new Message());
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
    }

    private void decodeCode(String code) {
        try {
            String sourceStr = new String(Base64.decode(code.getBytes(), Base64.DEFAULT));
            int count = 0;
            for (int i = 0; i < sourceStr.length(); i++) {
                //char 类型转String
                String s = String.valueOf(sourceStr.charAt(i));
                if ("#".equals(s)) {
                    count++;
                }
            }
            if (count >= 4) {
                String[] sourceStrArray = sourceStr.split("##");
                if (PubFunction.isConnect(activity, false)) {
                    page_3_code = sourceStrArray[0];
                    mtoken = sourceStrArray[1];
                    page_2_code = sourceStrArray[0];
                }
            } else {
                MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
            }
        } catch (Exception e) {
            MyToast.showTheToast(activity, "请扫描正确的商家二维码！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        page_2_code = "";
        page_3_code = "";
        mtoken = "";
    }
}