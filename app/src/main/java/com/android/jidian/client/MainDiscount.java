package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.bean.ScanCodeEventBean;
import com.android.jidian.client.mvp.ui.activity.ScanCodeNewActivity;
import com.android.jidian.client.util.ViewUtil;
import com.android.jidian.client.widgets.MyFragmentPagerAdapter;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.mvp.ui.activity.CouponCashv2Activity;
import com.timmy.tdialog.TDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/6/6.
 * 我的优惠券
 */
@EActivity(R.layout.main_discount)
public class MainDiscount extends BaseFragmentActivity {
    public static Handler handler;
    public static Handler setCurrentPageHandler;

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String alter_ = (String) msg.obj;
                if (alter_ != null) {
                    alter = alter_;
                }
            }
        };
        setCurrentPageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int page = msg.arg1;
                MainDiscount_1.reloadHandler.sendMessage(new Message());
                MainDiscount_2.reloadHandler.sendMessage(new Message());
                mViewPager.setCurrentItem(page);
            }
        };
    }

    @ViewById
    LinearLayout t_panel;
    @ViewById
    EditText number;
    @ViewById
    ViewPager mViewPager;
    @ViewById
    ImageView cursor;
    @ViewById
    TextView tv_title;

    private int[] colors = new int[]{0xff2ba245, 0xff333333};
    private TextView[] textViews = new TextView[colors.length];
    private int[] positions = new int[colors.length];
    private int now_position = 0;

    private String[] tab_str = new String[]{"未使用", "已使用"};

    //动画图片宽度
    private int bmpW;
    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScanCodeEventBean bean) {
        if (bean != null) {
            if (ScanCodeEventBean.REFRESH_DISCOUNT == bean.getEventMode()) {
                if (!TextUtils.isEmpty(bean.getMsg())) {
                    Intent intent = new Intent(this, CouponCashv2Activity.class);
                    intent.putExtra("code", bean.getMsg());
                    startActivity(intent);
                }
            }
        }
    }

    @AfterViews
    void afterVoids() {
        //初始化TextView
        InitTextView();
        //初始化ImageView
        InitImageView();
        //初始化Fragment
        InitFragment();
        //初始化ViewPager
        InitViewPager();
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void imageView1() {
        //点击扫一扫
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_UNUSED_MY_COUPONS_SCAN);
        Toast.makeText(activity, "打开扫描", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(activity, ScanCodeNewActivity.class);
        mIntent.putExtra(ScanCodeNewActivity.SCAN_CODE_IS_INPUT_BOX, "3");
        startActivity(mIntent);
    }

    @Click
    void submit() {
        //点击立即兑换
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_UNUSED_MY_COUPONS_REDEEM_NOW);
        if (TextUtils.isEmpty(number.getText().toString())) {
            MyToast.showTheToast(activity, "请输入您的优惠码！");
        } else {
            //TODO 二次确认是否需要请求优惠券列表页？
            confirmAgain();
        }
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        LinearLayout.LayoutParams params_t = (LinearLayout.LayoutParams) t_panel.getLayoutParams();
        params_t.height = ViewUtil.dp2px(activity, 40);
        params_t.width = width;
        t_panel.setLayoutParams(params_t);

        for (int i = 0; i < tab_str.length; i++) {
            TextView textView = new TextView(activity);
            textView.setHeight(ViewUtil.dp2px(activity, 40));
            textView.setWidth(width / tab_str.length);
            textView.setText(tab_str[i]);
            textView.setBackgroundColor(0xffffffff);
            textView.setGravity(Gravity.CENTER);//居中
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            textView.setOnClickListener(new MyOnClickListener(i));
            if (i == 0) {
                textView.setTextColor(colors[0]);
            } else {
                textView.setTextColor(colors[1]);
            }
            textViews[i] = textView;
            t_panel.addView(textView);
        }
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager, fragmentArrayList));
        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);
        //设置默认打开第一页
        mViewPager.setCurrentItem(0);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 获取分辨率宽度
        int screenW = dm.widthPixels;
        bmpW = (screenW / tab_str.length);
        //设置动画图片宽度
        setBmpW(cursor, bmpW);
        for (int i = 0; i < positions.length; i++) {
            if (i == 0) {
                positions[i] = 0;
            } else if (i == 1) {
                positions[i] = (int) (screenW / tab_str.length);
            } else {
                positions[i] = positions[1] * (i);
            }
        }
    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<>();
        MainDiscount_1_ MainDiscount_1 = new MainDiscount_1_();
        fragmentArrayList.add(MainDiscount_1);
        MainDiscount_2_ MainDiscount_2 = new MainDiscount_2_();
        fragmentArrayList.add(MainDiscount_2);
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    /**
     * http接口：Coupon/cashv3.html   使用优惠卷
     */
    @Background
    void HttpDiscount(String code_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("code", code_str));
        new OkHttpConnect(activity, PubFunction.app + "Coupon/cashv3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpDiscount(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpDiscount(String response, String type) {
        progressDialog.dismiss();
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                int status = jsonObject_response.optInt("status");
                if (status == 1) {//兑换成功
                    mViewPager.setCurrentItem(1);
                }
                MyToast.showTheToast(activity, msg);
                MainDiscount_1.reloadHandler.sendMessage(new Message());
                MainDiscount_2.reloadHandler.sendMessage(new Message());
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * 头标点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            for (int i = 0; i < textViews.length; i++) {
                if (position == i) {
                    animation = new TranslateAnimation(now_position, positions[i], 0, 0);
                    now_position = positions[i];
                    textViews[i].setTextColor(colors[0]);
                } else {
                    textViews[i].setTextColor(colors[1]);
                }
            }
            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public void confirmAgain() {
        View dialog_view = View.inflate(this, R.layout.cashv2_confirm, null);
        TextView textView = dialog_view.findViewById(R.id.textView);
        textView.setText(alter);
        new TDialog.Builder(getSupportFragmentManager())
                .setDialogView(dialog_view)
                .setScreenWidthAspect(this, 0.7f)
                .addOnClickListener(R.id.textView2, R.id.textView3)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.textView2:
                            tDialog.dismiss();
                            break;
                        case R.id.textView3:
                            progressDialog.show();
                            HttpDiscount(number.getText().toString());
                            number.setText("");
                            tDialog.dismiss();
                            break;
                    }
                })
                .create()
                .show();
    }

    private String alter = "";
}