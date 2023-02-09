package com.android.jidian.client.mvp.ui.activity.main.mainEquipmentFragment;

import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.mvp.contract.MainEquipmentContract;
import com.android.jidian.client.mvp.presenter.MainEquipmentPresenter;
import com.android.jidian.client.mvp.ui.activity.main.MainActivity;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.activity.message.AdvicesListsActivity;
import com.android.jidian.client.mvp.ui.activity.login.LoginActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCoinActivity;
import com.android.jidian.client.mvp.ui.activity.pub.ScanCodeActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByReNew;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.ViewUtil;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//todo:: 获取客服电话
//todo:: 点击客服电话 - 拨打电话

public class MainEquipmentFragment extends BaseFragment<MainEquipmentPresenter> implements MainEquipmentContract.View {

    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.equipmentMessage)
    public ImageView equipmentMessage;

    @BindView(R.id.iv_main_top)
    public ImageView iv_main_top;
    @BindView(R.id.tv_level)
    public TextView tv_level;
    @BindView(R.id.tv_main_tip)
    public TextView tv_main_tip;
    @BindView(R.id.tv_custom_phone)
    public TextView tv_custom_phone;
    @BindView(R.id.tv_prioce_unit)
    public TextView tv_prioce_unit;
    @BindView(R.id.tv_prioce_text)
    public TextView tv_prioce_text;

    //登录界面相关
    @BindView(R.id.main_f1)
    public LinearLayout main_f1;
    @BindView(R.id.main_l2)
    public LinearLayout main_l2;//换电币
    @BindView(R.id.main_l1)
    public LinearLayout main_l1;//电池和车
    @BindView(R.id.main_i1)
    public ImageView main_i1;//搞事情图片
    @BindView(R.id.tv_coin)
    public TextView tv_coin;
    //电动车
    @BindView(R.id.ll_bicycle)
    public LinearLayout ll_bicycle;
    @BindView(R.id.iv_bicycle)
    public ImageView iv_bicycle;
    @BindView(R.id.li_bicycle_add)
    public LinearLayout li_bicycle_add;//添加车辆
    @BindView(R.id.cl_bicycle_detail)
    public ConstraintLayout cl_bicycle_detail;
    @BindView(R.id.tv_bike_num)
    public TextView tv_bike_num;
    @BindView(R.id.tv_price)
    public TextView tv_price;
    @BindView(R.id.tv_bicycle_status)
    public TextView tv_bicycle_status;
    //电池
    @BindView(R.id.iv_main_battery_1)
    public ImageView iv_main_battery_1;
    @BindView(R.id.iv_main_battery_2)
    public ImageView iv_main_battery_2;
    @BindView(R.id.cl_battery_detail_1)
    public ConstraintLayout cl_battery_detail_1;
    @BindView(R.id.cl_battery_detail_2)
    public ConstraintLayout cl_battery_detail_2;
    @BindView(R.id.tv_battery_num_1)
    public TextView tv_battery_num_1;
    @BindView(R.id.tv_battery_num_2)
    public TextView tv_battery_num_2;
    @BindView(R.id.li_battery_add_1)
    public LinearLayout li_battery_add_1;
    @BindView(R.id.li_battery_add_2)
    public LinearLayout li_battery_add_2;
    @BindView(R.id.ll_battery_2)
    public LinearLayout ll_battery_2;
    @BindView(R.id.tv_battery_1_price)
    public TextView tv_battery_1_price;
    @BindView(R.id.tv_battery_2_price)
    public TextView tv_battery_2_price;
    @BindView(R.id.tv_battery_status_1)
    public TextView tv_battery_status_1;
    @BindView(R.id.tv_battery_status_2)
    public TextView tv_battery_status_2;
    @BindView(R.id.tv_battery_1_unit)
    public TextView tv_battery_1_unit;
    @BindView(R.id.tv_battery_1_text)
    public TextView tv_battery_1_text;
    @BindView(R.id.tv_battery_2_unit)
    public TextView tv_battery_2_unit;
    @BindView(R.id.tv_battery_2_text)
    public TextView tv_battery_2_text;

    //    @BindView(R.id.cl_battery_package_price)
//    public ConstraintLayout cl_battery_package_price;
    //续费
    @BindView(R.id.tv_expire_text)
    public TextView tv_expire_text;
    @BindView(R.id.ll_repay)
    public LinearLayout ll_repay;
    @BindView(R.id.ll_main_re_pay)
    public LinearLayout ll_main_re_pay;
    @BindView(R.id.tv_package_price)
    public TextView tv_package_price;
    @BindView(R.id.main_l2_charge)
    public TextView main_l2_charge;

    private String gid = "";
    private String opt = "";

    private MainActiyivyExpenseBean mExpenseBean;

    //设置页面布局
    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_equipment;
    }

    //初始化页面
    @Override
    public void initView(View view) {
        //mvp初始化
        mPresenter = new MainEquipmentPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
                    getEquipmentInfo();
                } else {
                    smartRefreshLayout.finishRefresh();
                }
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //显示哪种情况
        //登录状态
        if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
            showProgress();
            getEquipmentInfo();
        }
        //未登录状态
        else {
            tv_level.setText("请登录");
            main_f1.setVisibility(View.VISIBLE);
            main_l2.setVisibility(View.INVISIBLE);
            main_l1.setVisibility(View.GONE);
            main_i1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//不可见
            if (mPresenter != null && !UserInfoHelper.getInstance().getUid().isEmpty()) {
                mPresenter.requestWalletInfo(UserInfoHelper.getInstance().getUid());
            }
        }
    }

    //请求设备信息
    private void getEquipmentInfo() {
        mPresenter.requestWalletInfo(UserInfoHelper.getInstance().getUid());
    }

    //请求设备信息 - 成功
    @Override
    public void requestWalletInfoSuccess(MainActiyivyExpenseBean bean) {
        smartRefreshLayout.finishRefresh();
        hideProgress();
        if (bean.getData() != null) {
            BigDecimal total_price = new BigDecimal(0);//一键续费显示的钱
            mExpenseBean = bean;
            MainActiyivyExpenseBean.DataBean dataBean = bean.getData();
            //车辆信息更新
//        UserLoginBean userLoginBean = SpUser.getInstance().getUserLoginBean();
            main_l2.setVisibility(View.VISIBLE);
            tv_level.setText("青铜会员");
            tv_main_tip.setText("享有7项会员权益，点击查看详情");
            tv_custom_phone.setText(dataBean.getServtel());
            if (dataBean.getTop().getCList().size() > 0) {
                tv_coin.setText(dataBean.getTop().getCList().get(0).getNums() + "");
            }

            //套餐信息更新
            boolean hasOrder = dataBean.getEbike().size() > 0 || dataBean.getBattery().size() > 0;
            //没有套餐
            if (!hasOrder) {
                main_l1.setVisibility(View.GONE);
                main_i1.setVisibility(View.VISIBLE);
                //loginHasNotOrderSubmitInfo.setText("");
            }
            //有套餐
            else {
                main_l1.setVisibility(View.VISIBLE);
                main_i1.setVisibility(View.GONE);
//            loginHasOrderSubmitInfo.setText(userEquipmentBean.getTipstr());
                gid = dataBean.getUmonth().getPackets().getGid();
//            opt = dataBean.getUmonth().getPackets().getOpt();
                if (dataBean.getTop().getCList().size() > 1) {
                    tv_expire_text.setText("您的设备还有" + dataBean.getUmonth().getPackets().getDays() + "天到期");
                }
                total_price = total_price.add(new BigDecimal(dataBean.getUmonth().getPackets().getRprice()));
            }
            if (dataBean.getEbike().size() > 0) {
                //没有绑定车辆
                if ("20".equals(dataBean.getEbike().get(0).getUse_type())) {//租
                    total_price = total_price.add(new BigDecimal(dataBean.getEbike().get(0).getRprice()));
                }
                if (dataBean.getEbike().get(0).getIs_bind().equals("2") || dataBean.getEbike().get(0).getIs_bind().equals("0")) {
                    iv_bicycle.setImageResource(R.drawable.main_bicycle_gray);
                    tv_bicycle_status.setText("扫码绑定");
                    li_bicycle_add.setVisibility(View.VISIBLE);
                    cl_bicycle_detail.setVisibility(View.GONE);
                } else if (dataBean.getEbike().get(0).getIs_bind().equals("1")) {//绑定车辆
                    iv_bicycle.setImageResource(R.drawable.main_bicycle);
                    li_bicycle_add.setVisibility(View.GONE);
                    cl_bicycle_detail.setVisibility(View.VISIBLE);
                    if ("10".equals(dataBean.getEbike().get(0).getUse_type())) {//买
                        tv_price.setVisibility(View.GONE);
                        tv_prioce_unit.setVisibility(View.GONE);
                        tv_prioce_text.setVisibility(View.GONE);
                    } else {//租
                        tv_price.setVisibility(View.VISIBLE);
                        tv_prioce_unit.setVisibility(View.VISIBLE);
                        tv_prioce_text.setVisibility(View.VISIBLE);
                        tv_price.setText(dataBean.getEbike().get(0).getMrent());
                    }
//            loginHasBicyclePanelID.setText(userEquipmentBean.getEbike().getImei());
                }
            } else {
                iv_bicycle.setImageResource(R.drawable.main_bicycle_gray);
                li_bicycle_add.setVisibility(View.VISIBLE);
                cl_bicycle_detail.setVisibility(View.GONE);
                tv_bicycle_status.setText("添加车辆");
            }
            //电池信息更新
            int batteryCount = dataBean.getBattery().size();
            if (batteryCount > 0) {
                ll_battery_2.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.connect(R.id.main_l1, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(R.id.main_l1, ConstraintSet.TOP, R.id.main_f1, ConstraintSet.BOTTOM, 0);
                constraintSet.constrainHeight(R.id.main_l1, ViewUtil.dp2px(getActivity(), 240));
                MainActiyivyExpenseBean.DataBean.BatteryBean battery = dataBean.getBattery().get(0);
                String battery1IsBind = battery.getIs_bind();
                if (battery1IsBind.equals("1")) {
                    cl_battery_detail_1.setVisibility(View.VISIBLE);
                    li_battery_add_1.setVisibility(View.GONE);
                    iv_main_battery_1.setImageResource(R.drawable.main_battery);
                    tv_battery_num_1.setText(battery.getNumber());
                    if ("10".equals(battery.getUse_type())) {//买
                        tv_battery_1_price.setText(battery.getMrent() + "");
                        tv_battery_1_price.setVisibility(View.VISIBLE);
                        tv_battery_1_unit.setVisibility(View.VISIBLE);
                        tv_battery_1_text.setVisibility(View.VISIBLE);
                    } else {
                        total_price = total_price.add(new BigDecimal(battery.getRprice()));
                        tv_battery_1_price.setVisibility(View.GONE);
                        tv_battery_1_unit.setVisibility(View.GONE);
                        tv_battery_1_text.setVisibility(View.GONE);
                    }
                } else if (battery1IsBind.equals("2")) {
                    tv_battery_status_1.setText("扫码绑定");
                    li_battery_add_1.setVisibility(View.VISIBLE);
                    cl_battery_detail_1.setVisibility(View.GONE);
                }
            } else {
                tv_battery_status_1.setText("添加电池");
                li_battery_add_1.setVisibility(View.VISIBLE);
                cl_battery_detail_1.setVisibility(View.GONE);
            }
            if (batteryCount > 1) {
                ll_battery_2.setVisibility(View.VISIBLE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.connect(R.id.main_l1, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(R.id.main_l1, ConstraintSet.TOP, R.id.main_f1, ConstraintSet.BOTTOM, 0);
                constraintSet.constrainHeight(R.id.main_l1, ViewUtil.dp2px(getActivity(), 300));
                MainActiyivyExpenseBean.DataBean.BatteryBean battery = dataBean.getBattery().get(1);
                String battery2IsBind = battery.getIs_bind();
                if ("10".equals(battery.getUse_type())) {//买
                    tv_battery_2_price.setText(battery.getMrent() + "");
                    tv_battery_2_price.setVisibility(View.VISIBLE);
                    tv_battery_2_unit.setVisibility(View.VISIBLE);
                    tv_battery_2_text.setVisibility(View.VISIBLE);
                } else {
                    total_price = total_price.add(new BigDecimal(battery.getRprice()));
                    tv_battery_2_price.setVisibility(View.GONE);
                    tv_battery_2_unit.setVisibility(View.GONE);
                    tv_battery_2_text.setVisibility(View.GONE);
                }
                if (battery2IsBind.equals("1")) {
                    cl_battery_detail_2.setVisibility(View.VISIBLE);
                    li_battery_add_2.setVisibility(View.GONE);
                    iv_main_battery_2.setImageResource(R.drawable.main_battery);
                    tv_battery_num_2.setText(battery.getNumber());
                } else if (battery2IsBind.equals("2")) {
                    tv_battery_status_2.setText("扫码绑定");
                    li_battery_add_2.setVisibility(View.VISIBLE);
                    cl_battery_detail_2.setVisibility(View.GONE);
                }
            }
            tv_package_price.setText(total_price.toString());
        }
    }

    //请求设备信息 - 失败
    @Override
    public void requestWalletInfoFail(String msg) {
        smartRefreshLayout.finishRefresh();
        hideProgress();
        showMessage(msg);
    }


    @OnClick(R.id.main_i1)
    public void onClickMainI1() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            Intent intent = new Intent();
            intent.putExtra("page", 2);
            intent.setAction(BroadcastManager.RECEIVER_ACTION_MAIN_CHANGE_PAGE);
            getActivity().sendBroadcast(intent);
        }
    }

    @OnClick(R.id.equipmentMessage)
    public void onClickMessage() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            getActivity().startActivity(new Intent(getActivity(), AdvicesListsActivity.class));
        }
    }

    @OnClick(R.id.tv_level)
    public void onClicktvLevel() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @OnClick(R.id.tv_main_tip)
    public void onClickMainTip() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

//    @OnClick(R.id.li_bicycle_add)
//    public void onClickLoginAddBicycle() {
////        getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class), 00);
//        EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.CHANGEMAIN, 2));
//    }

//    @OnClick(R.id.li_battery_add_1)
//    public void onCliclBatteryAdd1() {
//        EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.CHANGEMAIN, 2));
//    }

//    @OnClick(R.id.li_battery_add_2)
//    public void onCliclBatteryAdd2() {
//        if (batteryStatus.size() > 1) {
//            if (batteryStatus.get(1) == 0) {
////                getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class), 02);
//            }
//        }
//
//    }

    @OnClick(R.id.ll_repay)
    public void onClickLoginHasOrderSubmit() {
        List<MainActiyivyExpenseBean.DataBean.EbikeBean> ebikeBeans = mExpenseBean.getData().getEbike();
        List<MainActiyivyExpenseBean.DataBean.BatteryBean> batteryBeans = mExpenseBean.getData().getBattery();
        List<MainActiyivyExpenseBean.DataBean.UmonthBean.UcardBean> ucardBeans = mExpenseBean.getData().getUmonth().getUcard();
        List<MainActiyivyExpenseBean.DataBean.UmonthBean.PacketsBean> umonthBean = new ArrayList<>();
        umonthBean.add(mExpenseBean.getData().getUmonth().getPackets());
        JSONArray jsonArray = new JSONArray();
        if (ebikeBeans != null) {
            for (int i = 0; i < ebikeBeans.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    if (!"10".equals(ebikeBeans.get(i).getUse_type())) {
                        jsonObject.put("devid", ebikeBeans.get(i).getDevid());
                        jsonObject.put("otype", ebikeBeans.get(i).getOtype());
                        jsonObject.put("relet", ebikeBeans.get(i).getRelet());
                        jsonObject.put("numt", ebikeBeans.get(i).getNumt());
                        jsonArray.put(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (batteryBeans != null) {
            for (int i = 0; i < batteryBeans.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    if (!"10".equals(batteryBeans.get(i).getUse_type())) {
                        jsonObject.put("devid", batteryBeans.get(i).getDevid());
                        jsonObject.put("otype", batteryBeans.get(i).getOtype());
                        jsonObject.put("relet", batteryBeans.get(i).getRelet());
                        jsonObject.put("numt", batteryBeans.get(i).getNumt());
                        jsonArray.put(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ucardBeans != null) {
            for (int i = 0; i < ucardBeans.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("gid", ucardBeans.get(i).getGid());
                    jsonObject.put("otype", ucardBeans.get(i).getOtype());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (umonthBean != null) {
            for (int i = 0; i < umonthBean.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("gid", umonthBean.get(i).getGid());
                    jsonObject.put("otype", umonthBean.get(i).getOtype());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //from=40
        Intent intent = new Intent(getActivity(), PayByReNew.class);
        intent.putExtra("mjson", jsonArray.toString());
        intent.putExtra("type", "40");
        requireActivity().startActivity(intent);
    }

    @OnClick(R.id.tv_custom_phone)
    public void onClickLoginUserPanelPhonePanel() {
        String s = tv_custom_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(s)) {
            Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + s));
            startActivity(Intent);
        }
    }

    //电动车
    @OnClick(R.id.li_bicycle_add)
    public void onClickliBicycleAdd() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            if (mExpenseBean != null) {
                if (mExpenseBean.getData().getEbike().size() == 0) {
                    EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.CHANGEMAIN, 2));
                }else {
                    Intent mIntent = new Intent(getActivity(), ScanCodeActivity.class);
                    mIntent.putExtra(ScanCodeActivity.SCAN_CODE_IS_INPUT_BOX, "4");
                    startActivity(mIntent);
                }
            }
        }
    }

    //电池1相关
    @OnClick(R.id.li_battery_add_1)
    public void onClickBattery1AddPanel() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            Intent mIntent = new Intent(getActivity(), ScanCodeActivity.class);
            mIntent.putExtra(ScanCodeActivity.SCAN_CODE_IS_INPUT_BOX, "2");
            startActivity(mIntent);
        }
    }

    //电池2相关
    @OnClick(R.id.li_battery_add_2)
    public void onClickBattery2AddPanel() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            Intent mIntent = new Intent(getActivity(), ScanCodeActivity.class);
            mIntent.putExtra(ScanCodeActivity.SCAN_CODE_IS_INPUT_BOX, "2");
            startActivity(mIntent);
        }
    }

    @OnClick(R.id.main_l2_charge)
    public void onClickMain_l2_charge() {
        getActivity().startActivity(new Intent(getActivity(), PayByCoinActivity.class));
    }


    @Override
    public void showProgress() {
        dialogByLoading.show();
    }

    @Override
    public void hideProgress() {
        dialogByLoading.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }

}
