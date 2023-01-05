package com.android.jidian.client.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.jidian.client.MainBlueTooth_;
import com.android.jidian.client.MainCar_;
import com.android.jidian.client.MainCustomer_;
import com.android.jidian.client.MainDiscount_;
import com.android.jidian.client.MainInfo_;
import com.android.jidian.client.MainMessage_1_;
import com.android.jidian.client.MainSet_;
import com.android.jidian.client.mvp.ui.activity.InviteWallteActivityU6;
import com.android.jidian.client.mvp.ui.activity.MainActivityU6;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.PersonalInfoBean;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.MoreQuestion;
import com.android.jidian.client.net.RetrofitClient;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.CalcUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : PTT
 * date: 2020/9/23 14:04
 * company: 兴达智联
 * description: 个人信息侧拉栏
 */
public class PersonalInfoDrawerMenu extends PopupWindow {
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.iv_headPortrait)
    ImageView ivHeadPortrait;
    @BindView(R.id.tv_app_versionCode)
    TextView tvAppVersionCode;
    @BindView(R.id.lv_left_drawer_list)
    ListView lvList;

    private FragmentActivity mContext;
    private String[] arrarStr = {"我的钱包", "邀请钱包", "我的订单", "我的电动车", "我的充电器", "我的优惠券", "常见问题", "吉电客服", "系统设置"};
    /**
     * "申请贷款"由于骑呗后台不稳定暂时下架，暂删
     */
    private int[] arrarinco = {R.drawable.icon_personal_drawer_menu_wallte, R.drawable.icon_personal_drawer_menu_invite_wallte,
            R.drawable.icon_personal_drawer_menu_order, R.drawable.icon_personal_drawer_menu_bike, R.drawable.icon_personal_drawer_menu_charge,
            R.drawable.icon_personal_drawer_menu_coupon, R.drawable.icon_personal_drawer_menu_question, R.drawable.icon_personal_drawer_menu_custom, R.drawable.icon_personal_drawer_menu_set
    };
    protected SharedPreferences sharedPreferences;
    private String uid, mUserPhoneStr, mHeadPortraitUrl;

    public PersonalInfoDrawerMenu(FragmentActivity context, OnClickItemListener listener) {
        this.mContext = context;
        this.mListener = listener;
        sharedPreferences = mContext.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        mUserPhoneStr = sharedPreferences.getString("phone", "");
        mHeadPortraitUrl = sharedPreferences.getString("user_avatar", "");
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.pw_personal_info_drawer_layout, null);
        ButterKnife.bind(this, rootView);
        initView(rootView);
    }

    private void initView(View rootView) {
        setContentView(rootView);
        //注意此处需要设置ViewGroup的铺满高度,解决部分机型有虚拟导航出现铺不满问题
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setClippingEnabled(false);
        setAnimationStyle(R.style.drawer_animation);
        if (TextUtils.isEmpty(mUserPhoneStr) || TextUtils.isEmpty(mHeadPortraitUrl)) {
            requestPersonalInfo();
        } else {
            tvUserPhone.setText(mUserPhoneStr);
            Glide.with(mContext).load(mHeadPortraitUrl).into(ivHeadPortrait);
        }
        backgroundAlpha(0.5f);
        setOnDismissListener(() -> {
            backgroundAlpha(1f);
        });

        tvAppVersionCode.setText("V " + PubFunction.getLocalVersionName(mContext));
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, getdata(), R.layout.item_pw_personal_info_drawer, new String[]{"text", "icon"}, new int[]{R.id.text1, R.id.icon});
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (CalcUtils.isFastDoubleClick()) {
                    return;
                }
                //骑呗测试
                if (position == 0) {
                    //点击我的钱包
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_WALLET);
                    List<ParamTypeData> dataList1 = new ArrayList<>();
                    dataList1.add(new ParamTypeData("title", "我的钱包访问"));
                    dataList1.add(new ParamTypeData("sourceType", "5"));
                    dataList1.add(new ParamTypeData("sourceId", uid));
                    dataList1.add(new ParamTypeData("type", "110"));
                    dataList1.add(new ParamTypeData("client", "Android"));
                    mContext.startActivity(new Intent(mContext, MainActivityU6.class));
                } else if (position == 1) {
                    //点击邀请钱包
                    mContext.startActivity(new Intent(mContext, InviteWallteActivityU6.class));
                } else if (position == 2) {
                    //点击我的订单
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_MY_ORDER);
                    mContext.startActivity(new Intent(mContext, MainMessage_1_.class));
                } else if (position == 3) {
                    //点击我的电动车
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_MY_ELECTRIC_CAR);
                    mContext.startActivity(new Intent(mContext, MainCar_.class));
                } else if (position == 4) {
                    //点击充电器设置
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_CHARGER_SETTINGS);
                    if (mListener != null) {
                        mListener.onClick(position);
                    }
                    mContext.startActivity(new Intent(mContext, MainBlueTooth_.class));
                } else if (position == 5) {
                    //点击我的优惠券
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_MY_COUPON);
                    mContext.startActivity(new Intent(mContext, MainDiscount_.class));
                } else if (position == 6) {
                    //点击常见问题
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_COMMON_PROBLEM);
                    mContext.startActivity(new Intent(mContext, MoreQuestion.class));
                } else if (position == 7) {
                    //点击HELLO客服
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_CUSTOMER);
                    mContext.startActivity(new Intent(mContext, MainCustomer_.class));
                } else if (position == 8) {
                    //点击系统设置
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_SYSTEM_SETTINGS);
                    mContext.startActivity(new Intent(mContext, MainSet_.class));
                }
                /*由于骑呗后台不稳定暂时下架，暂删 else if (position == 2) {
                    //点击申请贷款
                    if (!TextUtils.isEmpty(getQeiBeiPhone())) {
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_APPLY_FOR_LOAN);
                        QiBei.start(getQeiBeiPhone(), mContext);
                        //骑呗页面访问
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_RIDE);
                    }
                } */
            }
        });
        lvList.setAdapter(simpleAdapter);
        //个人中心页面访问
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_PERSONAL_CENTER);
    }

    /**
     * 左边list的数据
     */
    private List<Map<String, Object>> getdata() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < arrarStr.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", arrarStr[i]);
            map.put("icon", arrarinco[i]);
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }

    /**
     * 调用显示侧滑菜单
     */
    public void showMenu() {
        showAtLocation(mContext.getWindow().getDecorView(), Gravity.START, 0, 0);
    }

    @OnClick({R.id.li_info})
    public void onViewClicked(View v) {
        dismiss();
        //点击头像/手机号
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_CENTER_USER);
        mContext.startActivity(new Intent(mContext, MainInfo_.class));
    }

    /**
     * 请求个人信息
     */
    private void requestPersonalInfo() {
        Disposable disposable = RetrofitClient.getInstance().getApiService()
                .requestPersonalInfo(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(new Consumer<PersonalInfoBean>() {
                    @Override
                    public void accept(PersonalInfoBean bean) throws Exception {
                        if (bean != null) {
                            if (bean.getStatus() == 1) {
                                if (bean.getData() != null) {
                                    try {
                                        mUserPhoneStr = bean.getData().getPhone();
                                        mHeadPortraitUrl = bean.getData().getAvater();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("user_avatar", mHeadPortraitUrl);
                                        editor.putString("phone", mUserPhoneStr);
                                        editor.apply();
                                        tvUserPhone.setText(mUserPhoneStr);
                                        Glide.with(mContext).load(mHeadPortraitUrl).into(ivHeadPortrait);
                                    } catch (Exception e) {
                                        System.out.println(e.getLocalizedMessage());
                                    }
                                }
                            } else {
                                MyToast.showTheToast(mContext, bean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    /**
     * 根据手机号得
     */
    private String getQeiBeiPhone() {
        return "{\n" +
                "\"type\":1,\n" +
                "\"carModels\":\"\",\n" +
                "\"brand\":\"\",\n" +
                "\"guidePrice\":\"\",\n" +
                "\"latitude\":\"\",\n" +
                "\"longitude\":\"\",\n" +
                "\"merchantName\":\"\",\n" +
                "\"orderId\":\"\",\n" +
                "\"userInfo\":{\n" +
                "\"loanAmount\":\"\",\n" +
                "\"phone\":\"" + mUserPhoneStr + "\",\n" +
                "\"userName\":\"\"\n" +
                "}\n" +
                "}";
    }

    public OnClickItemListener mListener;

    public interface OnClickItemListener {
        void onClick(int position);
    }
}