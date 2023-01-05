package com.android.jidian.client.mvp.ui.fragment.mainEquipmentFragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.Newpay_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.mvp.contract.MainEquipmentContract;
import com.android.jidian.client.mvp.presenter.MainEquipmentPresenter;
import com.android.jidian.client.mvp.ui.activity.AdvicesListsActivity;
import com.android.jidian.client.mvp.ui.activity.LoginActivityU6;
import com.android.jidian.client.mvp.ui.activity.ScanCodeNewActivityU6;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainEquipmentFragment extends BaseFragment<MainEquipmentPresenter> implements MainEquipmentContract.View {

    @BindView(R.id.shSwipeRefreshLayout)
    public SmartRefreshLayout shSwipeRefreshLayout;

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
    @BindView(R.id.root_view)
    public ConstraintLayout root_view;
    @BindView(R.id.tv_message)
    public TextView tv_message;

    //未登录界面相关
//    @BindView(R.id.logoutPanel)
//    public LinearLayout logoutPanel;
//    @BindView(R.id.logoutAddBicycle)
//    public FrameLayout logoutAddBicycle;
//    @BindView(R.id.logoutUserPanel)
//    public LinearLayout logoutUserPanel;
//    @BindView(R.id.logoutUserInfoPanel)
//    public LinearLayout logoutUserInfoPanel;
    //登录界面相关
    @BindView(R.id.main_f1)
    public LinearLayout main_f1;
    @BindView(R.id.main_l2)
    public LinearLayout main_l2;//换电币
    @BindView(R.id.main_t_9)
    public TextView main_t_9;
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
    @BindView(R.id.tv_package_name)
    public TextView tv_package_name;
    @BindView(R.id.tv_battery_price)
    public TextView tv_battery_price;
    @BindView(R.id.tv_battery_status_1)
    public TextView tv_battery_status_1;
    @BindView(R.id.tv_battery_status_2)
    public TextView tv_battery_status_2;
    @BindView(R.id.cl_battery_package_price)
    public ConstraintLayout cl_battery_package_price;
    //续费
    @BindView(R.id.tv_expire_text)
    public TextView tv_expire_text;
    @BindView(R.id.ll_repay)
    public LinearLayout ll_repay;
    @BindView(R.id.ll_main_re_pay)
    public LinearLayout ll_main_re_pay;
    @BindView(R.id.tv_package_price)
    public TextView tv_package_price;

    private Unbinder unbinder;

    private String gid = "";
    private String opt = "";
    private List<Integer> batteryStatus; //两个电池 0=已绑定 1=未绑定

    private MainActiyivyExpenseBean mExpenseBean;


    @Override
    public void onResume() {
        super.onResume();
        //显示哪种情况
        //登录状态
        if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
            getEquipmentInfo();
        }
        //未登录状态
        else {
            tv_level.setText("请登录");
            main_f1.setVisibility(View.VISIBLE);
            main_l2.setVisibility(View.INVISIBLE);
            main_t_9.setVisibility(View.INVISIBLE);
            main_l1.setVisibility(View.GONE);
            main_i1.setVisibility(View.VISIBLE);
        }
    }

    private void getEquipmentInfo() {
        mPresenter.requestWalletInfo(UserInfoHelper.getInstance().getUid());
    }

    @OnClick(R.id.main_i1)
    public void onClickMainI1() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        } else {
//            getActivity().startActivity(new Intent(getActivity(), BusinessShopActivity.class));
        }
    }

    @OnClick(R.id.tv_message)
    public void onClickMessage() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        } else {
            getActivity().startActivity(new Intent(getActivity(), AdvicesListsActivity.class));
        }
    }

    @OnClick(R.id.tv_level)
    public void onClicktvLevel() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        }
    }

    @OnClick(R.id.tv_main_tip)
    public void onClickMainTip() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        }
    }

    //登录界面相关
    @OnClick(R.id.li_bicycle_add)
    public void onClickLoginAddBicycle() {
//        getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class), 00);
    }

    @OnClick(R.id.li_battery_add_1)
    public void onCliclBatteryAdd1() {
        if (batteryStatus.size() > 0) {
            if (batteryStatus.get(0) == 0) {
//                getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class), 01);
            }
        }
    }

    @OnClick(R.id.li_battery_add_2)
    public void onCliclBatteryAdd2() {
        if (batteryStatus.size() > 1) {
            if (batteryStatus.get(1) == 0) {
//                getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class), 02);
            }
        }

    }

    //    @OnClick(R.id. loginHasBicyclePanelScan)
//    public void onClickLoginHasBicyclePanelScan(){
//        getActivity().startActivityForResult(new Intent(getActivity(), QrCode.class),03);
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
                    jsonObject.put("devid", ebikeBeans.get(i).getDevid());
                    jsonObject.put("otype", ebikeBeans.get(i).getOtype());
                    jsonObject.put("relet", ebikeBeans.get(i).getRelet());
                    jsonObject.put("numt", ebikeBeans.get(i).getNumt());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        if (batteryBeans != null) {
            for (int i = 0; i < batteryBeans.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("devid", batteryBeans.get(i).getDevid());
                    jsonObject.put("otype", batteryBeans.get(i).getOtype());
                    jsonObject.put("relet", batteryBeans.get(i).getRelet());
                    jsonObject.put("numt", batteryBeans.get(i).getNumt());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        if (ucardBeans != null) {
            for (int i = 0; i < ucardBeans.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("gid", ucardBeans.get(i).getGid());
                    jsonObject.put("otype", ucardBeans.get(i).getOtype());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        if (umonthBean != null) {
            for (int i = 0; i < umonthBean.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("gid", umonthBean.get(i).getGid());
                    jsonObject.put("otype", umonthBean.get(i).getOtype());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }

        //from=40
        Intent intent = new Intent(getActivity(), Newpay_.class);
        intent.putExtra("mjson", jsonArray.toString());
        intent.putExtra("type", "40");
        requireActivity().startActivity(intent);
    }

    @OnClick(R.id.tv_custom_phone)
    public void onClickLoginUserPanelPhonePanel() {

//        Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + SpSystem.getInstance().getCustomerServiceBean().get_contact()));
//        startActivity(Intent);

    }

    //电动车
    @OnClick(R.id.li_bicycle_add)
    public void onClickliBicycleAdd() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        } else {
            Intent mIntent = new Intent(getActivity(), ScanCodeNewActivityU6.class);
            mIntent.putExtra(ScanCodeNewActivityU6.SCAN_CODE_IS_INPUT_BOX, "4");
            startActivity(mIntent);
        }
    }

    //电池1相关
    @OnClick(R.id.li_battery_add_1)
    public void onClickBattery1AddPanel() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        } else {
            Intent mIntent = new Intent(getActivity(), ScanCodeNewActivityU6.class);
            mIntent.putExtra(ScanCodeNewActivityU6.SCAN_CODE_IS_INPUT_BOX, "2");
            startActivity(mIntent);
        }
    }
//    @OnClick(R.id.battery1InfoPanel)
//    public void onClickBattery1InfoPanel(){
//        getActivity().startActivity(new Intent(getActivity(), MyExchangeLog.class));
//    }


    //电池2相关
    @OnClick(R.id.li_battery_add_2)
    public void onClickBattery2AddPanel() {
        String uid = UserInfoHelper.getInstance().getUid();
        if (TextUtils.isEmpty(uid)) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivityU6.class));
        } else {
            Intent mIntent = new Intent(getActivity(), ScanCodeNewActivityU6.class);
            mIntent.putExtra(ScanCodeNewActivityU6.SCAN_CODE_IS_INPUT_BOX, "2");
            startActivity(mIntent);
        }
    }
//    @OnClick(R.id.battery2InfoPanel)
//    public void onClickBattery2InfoPanel(){
//        getActivity().startActivity(new Intent(getActivity(), MyExchangeLog.class));
//    }


    public void onRefresh() {
        onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_fragment_equipment;
    }

    @Override
    public void initView(View view) {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
//            dialogLoading.showPopupWindow();
        }
        mPresenter = new MainEquipmentPresenter();
        mPresenter.attachView(this);
        shSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
                    getEquipmentInfo();
                } else {
                    shSwipeRefreshLayout.finishRefresh();
                }
            }
        });
        shSwipeRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }

    @Override
    public void requestWalletInfoSuccess(MainActiyivyExpenseBean bean) {
        shSwipeRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            mExpenseBean = bean;
            MainActiyivyExpenseBean.DataBean dataBean = bean.getData();
            //车辆信息更新
//        UserLoginBean userLoginBean = SpUser.getInstance().getUserLoginBean();
            main_l2.setVisibility(View.VISIBLE);
            main_t_9.setVisibility(View.VISIBLE);
            tv_level.setText("青铜会员");
            tv_main_tip.setText("享有7项会员权益，点击查看详情");
            tv_custom_phone.setText("400-4004-4354");
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
                tv_package_price.setText(dataBean.getUmonth().getPackets().getRprice());
            }
            if (dataBean.getEbike().size() > 0) {
                //没有绑定车辆
                if (dataBean.getEbike().get(0).getIs_bind().equals("2") || dataBean.getEbike().get(0).getIs_bind().equals("0")) {
                    iv_bicycle.setImageResource(R.drawable.main_bicycle_gray);
                    tv_bicycle_status.setText("扫码绑定");
                    li_bicycle_add.setVisibility(View.VISIBLE);
                    cl_bicycle_detail.setVisibility(View.GONE);
                } else if (dataBean.getEbike().get(0).getIs_bind().equals("1")) {//绑定车辆
                    iv_bicycle.setImageResource(R.drawable.main_bicycle);
                    li_bicycle_add.setVisibility(View.GONE);
                    cl_bicycle_detail.setVisibility(View.VISIBLE);
//            loginHasBicyclePanelID.setText(userEquipmentBean.getEbike().getImei());
                }
                if ("10".equals(dataBean.getEbike().get(0).getUse_type())) {//买
                    tv_price.setVisibility(View.GONE);
                    tv_prioce_unit.setVisibility(View.GONE);
                    tv_prioce_text.setVisibility(View.GONE);
                } else {//租
                    tv_price.setVisibility(View.VISIBLE);
                    tv_prioce_unit.setVisibility(View.VISIBLE);
                    tv_prioce_text.setVisibility(View.VISIBLE);
                    tv_price.setText(dataBean.getEbike().get(0).getUse_type() + "");
                }
            }
//
////        loginUserPanelPhone.setText(SpSystem.getInstance().getCustomerServiceBean().get_contact());
////        loginUserPanelName.setText(SpUser.getInstance().getUserInfoBean().getUname());

            //电池信息更新
            int batteryCount = dataBean.getBattery().size();
            batteryStatus = new ArrayList<>();
            if (batteryCount > 0) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.connect(R.id.main_l1, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(R.id.main_l1, ConstraintSet.TOP, R.id.main_f1, ConstraintSet.BOTTOM, 0);
                constraintSet.constrainHeight(R.id.main_l1, ViewUtil.dp2px(getActivity(), 240));
                constraintSet.applyTo(root_view);
                cl_battery_package_price.setVisibility(View.VISIBLE);
                String battery1IsBind = dataBean.getBattery().get(0).getIs_bind();
                if ("10".equals(dataBean.getEbike().get(0).getUse_type())) {//买
                    cl_battery_package_price.setVisibility(View.GONE);
                } else {//租
                    cl_battery_package_price.setVisibility(View.VISIBLE);
                    tv_battery_price.setText(dataBean.getBattery().get(0).getMrent() + "");
                }
                if (battery1IsBind.equals("1")) {
                    batteryStatus.add(1);
                    cl_battery_detail_1.setVisibility(View.VISIBLE);
                    li_battery_add_1.setVisibility(View.GONE);
                    MainActiyivyExpenseBean.DataBean.BatteryBean battery = dataBean.getBattery().get(0);
                    iv_main_battery_1.setImageResource(R.drawable.main_battery);
                    tv_battery_num_1.setText(battery.getNumber());
                } else if (battery1IsBind.equals("2")) {
                    batteryStatus.add(0);
                    tv_battery_status_1.setText("扫码绑定");
                    li_battery_add_1.setVisibility(View.VISIBLE);
                    cl_battery_detail_1.setVisibility(View.GONE);
                }
            } else {
                li_battery_add_1.setVisibility(View.VISIBLE);
                cl_battery_detail_1.setVisibility(View.GONE);
            }
            if (batteryCount > 1) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.connect(R.id.main_l1, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(R.id.main_l1, ConstraintSet.TOP, R.id.main_f1, ConstraintSet.BOTTOM, 0);
                constraintSet.constrainHeight(R.id.main_l1, ViewUtil.dp2px(getActivity(), 300));
                constraintSet.applyTo(root_view);
                ll_battery_2.setVisibility(View.VISIBLE);
                String battery2IsBind = dataBean.getBattery().get(1).getIs_bind();
                if ("10".equals(dataBean.getBattery().get(1).getUse_type())) {//买
//                    cl_battery_package_price.setVisibility(View.GONE);
                } else {//租
                    cl_battery_package_price.setVisibility(View.VISIBLE);
                    if ("20".equals(dataBean.getBattery().get(0).getUse_type())) {
//                        if ("20".equals(dataBean.getBattery().get(1).getUse_type())) {//租
                        double i1 = Double.parseDouble(dataBean.getBattery().get(0).getMrent());
                        double i2 = Double.parseDouble(dataBean.getBattery().get(1).getMrent());
                        tv_battery_price.setText((i1 + i2) + "");
//                        }
                    } else {
//                        if ("20".equals(dataBean.getBattery().get(1).getUse_type())) {//租
                        tv_battery_price.setText(dataBean.getBattery().get(1).getMrent() + "");
//                        }
                    }
                }
                if (battery2IsBind.equals("1")) {
                    batteryStatus.add(1);
                    cl_battery_detail_2.setVisibility(View.VISIBLE);
                    li_battery_add_2.setVisibility(View.GONE);
                    MainActiyivyExpenseBean.DataBean.BatteryBean battery = dataBean.getBattery().get(1);
                    iv_main_battery_2.setImageResource(R.drawable.main_battery);
                    tv_battery_num_2.setText(battery.getNumber());
                } else if (battery2IsBind.equals("2")) {
                    batteryStatus.add(0);
                    tv_battery_status_1.setText("扫码绑定");
                    li_battery_add_2.setVisibility(View.VISIBLE);
                    cl_battery_detail_2.setVisibility(View.GONE);
                }
            } else {
                li_battery_add_2.setVisibility(View.VISIBLE);
                cl_battery_detail_2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void requestWalletInfoFail(String msg) {
        shSwipeRefreshLayout.finishRefresh();
        showMessage(msg);
    }
}
