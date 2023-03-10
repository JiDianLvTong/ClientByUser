package com.android.jidian.client.mvp.ui.activity.coupon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.android.jidian.client.MainDiscount_2;
import com.android.jidian.client.MainDiscount_2_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.pub.ScanCodeActivity;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.ViewUtil;
import com.android.jidian.client.widgets.MyFragmentPagerAdapter;
import com.android.jidian.client.widgets.MyToast;
import com.timmy.tdialog.TDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponActivity extends U6BaseActivity {

    @BindView(R.id.t_panel)
    LinearLayout t_panel;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.cursor)
    ImageView cursor;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;

    public static Handler handler;
    public static Handler setCurrentPageHandler;

    private int[] colors = new int[]{0xffd7a64a, 0xffcccccc};
    private TextView[] textViews = new TextView[colors.length];
    private int[] positions = new int[colors.length];
    private int now_position = 0;
    private String alter = "";
    private String[] tab_str = new String[]{"?????????", "?????????"};

    //??????????????????
    private int bmpW;
    //??????Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //??????Fragment
    private FragmentManager fragmentManager;


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
                CouponPage1Fragment.reloadHandler.sendMessage(new Message());
                CouponPage2Fragment.reloadHandler.sendMessage(new Message());
                mViewPager.setCurrentItem(page);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_coupon);
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        setHandler();
        //?????????TextView
        InitTextView();
        //?????????ImageView
        InitImageView();
        //?????????Fragment
        InitFragment();
        //?????????ViewPager
        InitViewPager();
    }


    /**
     * ???????????????
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
            textView.setBackgroundColor(0xff303030);
            textView.setGravity(Gravity.CENTER);//??????
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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
     * ????????????????????????
     */
    private void InitViewPager() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager, fragmentArrayList));
        //???ViewPager??????2?????????
        mViewPager.setOffscreenPageLimit(2);
        //???????????????????????????
        mViewPager.setCurrentItem(0);
        //??????viewpager????????????????????????
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * ???????????????
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // ?????????????????????
        int screenW = dm.widthPixels;
        bmpW = (screenW / tab_str.length);
        //????????????????????????
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
     * ?????????Fragment???????????????ArrayList???
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<>();
        CouponPage1Fragment couponPage1Fragment = new CouponPage1Fragment();
        fragmentArrayList.add(couponPage1Fragment);
        CouponPage2Fragment couponPage2Fragment = new CouponPage2Fragment();
        fragmentArrayList.add(couponPage2Fragment);
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * ????????????????????????
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
     * http?????????Coupon/cashv3.html   ???????????????
     */
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

    void onDataHttpDiscount(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if ("0".equals(type)) {
                    MyToast.showTheToast(activity, response);
                } else {
                    try {
                        JSONObject jsonObject_response = new JSONObject(response);
                        String msg = jsonObject_response.getString("msg");
                        int status = jsonObject_response.optInt("status");
                        if (status == 1) {//????????????
                            mViewPager.setCurrentItem(1);
                        }
                        MyToast.showTheToast(activity, msg);
                        CouponPage1Fragment.reloadHandler.sendMessage(new Message());
                        CouponPage2Fragment.reloadHandler.sendMessage(new Message());
                    } catch (Exception e) {
                        MyToast.showTheToast(activity, "JSON???" + e.toString());
                    }
                }
            }
        });
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


    @OnClick(R.id.submit)
    void submit() {
        //??????????????????
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_UNUSED_MY_COUPONS_REDEEM_NOW);
        if (TextUtils.isEmpty(number.getText().toString())) {
            MyToast.showTheToast(activity, "???????????????????????????");
        } else {
            //TODO ???????????????????????????????????????????????????
            confirmAgain();
        }
    }

    @OnClick(R.id.imageView1)
    void imageView1() {
        //???????????????
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_UNUSED_MY_COUPONS_SCAN);
        Toast.makeText(activity, "????????????", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(activity, ScanCodeActivity.class);
        mIntent.putExtra(ScanCodeActivity.SCAN_CODE_IS_INPUT_BOX, "3");
        startActivity(mIntent);
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        activity.finish();
    }

    /**
     * ??????????????????
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
     * ??????????????????
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
            animation.setFillAfter(true);// true:??????????????????????????????
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
}
