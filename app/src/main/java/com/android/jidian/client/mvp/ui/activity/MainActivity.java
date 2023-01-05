package com.android.jidian.client.mvp.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.android.jidian.client.BuildConfig;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.bean.MainAppVersionBean;
import com.android.jidian.client.mvp.contract.MainActivityContract;
import com.android.jidian.client.mvp.presenter.MainActivityPresenter;
import com.android.jidian.client.mvp.ui.fragment.mainEquipmentFragment.MainEquipmentFragment;
import com.android.jidian.client.mvp.ui.fragment.mainFindFragment.MainFindFragment;
import com.android.jidian.client.mvp.ui.fragment.mainShopFragment.MainShopFragment;
import com.android.jidian.client.mvp.ui.fragment.mainUserFragment.MainUserFragment;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.util.file.FileManager;
import com.android.jidian.client.widgets.MyToast;
import com.permissionx.guolindev.PermissionX;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

public class MainActivity extends U6BaseActivityByMvp<MainActivityPresenter> implements MainActivityContract.View {

    @BindViews({R.id.t_1, R.id.t_2, R.id.t_3, R.id.t_4})
    public List<TextView> footTextViewList;

    @BindViews({R.id.i_1, R.id.i_2, R.id.i_3, R.id.i_4})
    public List<ImageView> footImageViewList;
    private int[] isSelectIcons = new int[]{R.drawable.foot_electric_bicycle_b69873, R.drawable.foot_find_b69873, R.drawable.foot_shop_b69873, R.drawable.foot_user_b69873};
    private int[] notSelectIcons = new int[]{R.drawable.foot_electric_bicycle_ffffff, R.drawable.foot_find_ffffff, R.drawable.foot_shop_ffffff, R.drawable.foot_user_ffffff};

    @BindView(R.id.mainPanell)
    public LinearLayout mainPanell;

    private int localPage = -1;

    private MainEquipmentFragment mainEquipmentFragment;
    private MainFindFragment mainFindFragment;
    private MainShopFragment mainShopFragment;
    private MainUserFragment mainUserFragment;


    //定位相关
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public MyLocationStyle myLocationStyle;
    public double[] coordinates = new double[]{0, 0};
    private ArrayList<Marker> markers = new ArrayList<>();
    private AMapLocation aMapLocation;
    private boolean mPositioned = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
        statusBars();
    }

    //保证导航栏沉浸 并且 保持底部导航栏不被沉浸
    //需要和 style 配合
    private void statusBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) try {
            Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
            Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
            field.setAccessible(true);
            field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
        } catch (Exception e) {
        }
        //去除灰色遮罩
        //Android5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Android4.4以上,5.0以下
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void initView() {
        mPresenter = new MainActivityPresenter();
        mPresenter.attachView(this);
        //版本检查
        mPresenter.appVerUpgrade(uid);
        //页卡设置
        changeMain(0);
        //广播注册
        registerReceiver();

//        statusBars();

    }

    private void initLocation() {
//        NaviSetting.updatePrivacyShow(getActivity(), true, true);
//        NaviSetting.updatePrivacyAgree(getActivity(), true);
        try {
            mLocationClient = new AMapLocationClient(MainActivity.this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //设置定位监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation mAMapLocation) {
                    if (mAMapLocation != null) {
                        aMapLocation = mAMapLocation;
                        if (aMapLocation.getErrorCode() == 0) {
                            //定位成功回调信息，设置相关消息
                            mPositioned = true;
                            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                            Log.d("xiaoming1223", "aMapLocation.getLocationType(): " + aMapLocation.getLocationType());
                            coordinates[0] = aMapLocation.getLongitude();//获取经度
                            coordinates[1] = aMapLocation.getLatitude();//获取纬度
                            aMapLocation.getAccuracy();//获取精度信息
                            Log.d("xiaoming1223", "aMapLocation.getAccuracy(): " + aMapLocation.getAccuracy());
                            Log.d("xiaoming1223", "onLocationChanged:  aMapLocation.getCity(): " + aMapLocation.getAddress() + "aMapLocation.getCityCode(): " + aMapLocation.getCityCode()
                                    + "aMapLocation.getLatitude() :" + aMapLocation.getLatitude() + "aMapLocation.getLongitude():" + aMapLocation.getLongitude());
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.p_1, R.id.p_2, R.id.p_3, R.id.p_4})
    public void onClickFoot(LinearLayout layout) {
        if (layout.getId() == R.id.p_1) {
            changeMain(0);
        } else if (layout.getId() == R.id.p_2) {
            changeMain(1);
            if (mainFindFragment != null) {
                if (mPositioned) {
                    mainFindFragment.setFragmentPosition(coordinates[0] + "", coordinates[1] + "");
                } else {
                    mainFindFragment.setFragmentPosition("", "");
                }
            }
        } else if (layout.getId() == R.id.p_3) {
            changeMain(2);
            if (mainShopFragment != null) {
                if (mPositioned) {
                    mainShopFragment.setFragmentPosition(1, coordinates[0] + "", coordinates[1] + "");
                } else {
                    mainShopFragment.setFragmentPosition(1, "", "");
                }
            }
        } else if (layout.getId() == R.id.p_4) {
            changeMain(3);
        }
    }

    private void changeMain(int page) {

        if (page == localPage) {
            return;
        }

        if ((page == 2 || page == 3) && UserInfoHelper.getInstance().getUid().isEmpty()) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (mainEquipmentFragment != null) {
            ft.hide(mainEquipmentFragment);
        }
        if (mainFindFragment != null) {
            ft.hide(mainFindFragment);
        }
        if (mainShopFragment != null) {
            ft.hide(mainShopFragment);
        }
        if (mainUserFragment != null) {
            ft.hide(mainUserFragment);
        }

        for (int i = 0; i < footTextViewList.size(); i++) {
            footTextViewList.get(i).setTextColor(0xff999999);
            footImageViewList.get(i).setImageResource(notSelectIcons[i]);
        }
        footTextViewList.get(page).setTextColor(activity.getResources().getColor(R.color.green_33cc66));
        footImageViewList.get(page).setImageResource(isSelectIcons[page]);

        if (page == 0) {
            if (mainEquipmentFragment == null) {
                mainEquipmentFragment = new MainEquipmentFragment();
                ft.add(R.id.mainPanell, mainEquipmentFragment);
            } else {
                ft.show(mainEquipmentFragment);
            }
        } else if (page == 1) {
            if (mainFindFragment == null) {
                mainFindFragment = new MainFindFragment();
                ft.add(R.id.mainPanell, mainFindFragment);
            } else {
                ft.show(mainFindFragment);
            }
        } else if (page == 2) {
            if (mainShopFragment == null) {
                mainShopFragment = new MainShopFragment();
                ft.add(R.id.mainPanell, mainShopFragment);
            } else {
                ft.show(mainShopFragment);
            }
        } else if (page == 3) {
            if (mainUserFragment == null) {
                mainUserFragment = new MainUserFragment();
                ft.add(R.id.mainPanell, mainUserFragment);
            } else {
                ft.show(mainUserFragment);
            }
        }

        ft.commitAllowingStateLoss();
        localPage = page;
    }


    /**
     * 注册广播 广播切换页卡
     */
    private void registerReceiver() {
        broadcastManager.registerMainChangePage(new ChangePageReceiver());
    }

    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastManager.RECEIVER_ACTION_MAIN_CHANGE_PAGE.equals(intent.getAction())) {
                int page = intent.getIntExtra("page", 0);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeMain(page);
                    }
                });
            }
        }
    }

    @Override
    public void requestMainInfoSuccess(MainActiyivyExpenseBean bean) {

    }

    @Override
    public void requestMainInfoFail(String msg) {

    }

    @Override
    public void appVerUpgradeSuccess(MainAppVersionBean bean) {
        MainAppVersionBean.DataBean mainAppVersionBean = bean.getData();
        String url = mainAppVersionBean.getAndroid_url();
        String force = mainAppVersionBean.getAndroid_force();
        boolean isForce = false;
        if (force.equals("1")) {
            isForce = true;
        }
        int netVersion = Integer.parseInt(mainAppVersionBean.getAndroid_code());
        int localVersion = BuildConfig.VERSION_CODE;
        System.out.println("okh -   " + netVersion + "     " + localVersion + "    " + isForce);
        if (url != null && !url.equals("null") && netVersion > localVersion) {

            UpdateConfig updateConfig = new UpdateConfig();
            updateConfig.setCheckWifi(true);
            updateConfig.setNeedCheckMd5(true);
            updateConfig.setNotifyImgRes(R.drawable.logo_update);
            updateConfig.setApkSavePath(FileManager.getInstance().getRootPath());
            updateConfig.setAlwaysShowDownLoadDialog(true);
            updateConfig.setForce(isForce);

            UiConfig uiConfig = new UiConfig();
            uiConfig.setUiType(UiType.CUSTOM);
            uiConfig.setCustomLayoutId(R.layout.dialog_update);

            UpdateAppUtils
                    .getInstance()
                    .apkUrl(url)
                    .updateTitle("发现新版本:" + "Ver_" + mainAppVersionBean.getAndroid_name())
                    .updateContent("1.bug更改")
                    .uiConfig(uiConfig)
                    .updateConfig(updateConfig)
                    .setMd5CheckResultListener(new Md5CheckResultListener() {
                        @Override
                        public void onResult(boolean result) {

                        }
                    })
                    .setUpdateDownloadListener(new UpdateDownloadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDownload(int progress) {

                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                        }
                    }).update();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //检查用户登录信息 以及 使用环境
        boolean isLogin = !UserInfoHelper.getInstance().getUid().isEmpty();
        //获取用户信息
        if (isLogin) {
            if (mPresenter != null) {
                apptoken = sharedPreferences.getString("apptoken", "");
                String appsn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
                if (!TextUtils.isEmpty(apptoken) && !TextUtils.isEmpty(appsn)) {
                    mPresenter.requestCheckAccv2(apptoken, appsn);
                }
//                mPresenter.requestMainInfo(uid);
            }
        }else {
            changeMain(0);
        }
        PermissionX.init(MainActivity.this)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        if (!mPositioned) {
                            initLocation();
                        }
                    } else {
                        MyToast.showTheToast(this, "当前应用缺少必要权限 ");
                    }
                });
    }

    @Override
    public void appVerUpgradeFail(String msg) {

    }

    @Override
    public void requestCheckAccv2Success(LoginCheckAccv2Bean bean) {
        MyToast.showTheToast(this, bean.getMsg());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_avatar", "");
        editor.clear();
        editor.apply();
        startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void requestCheckAccv2Error(String msg) {

    }

    @Override
    public void showProgress() {
        progressDialog.show();
//        LoadingViewUtil.getInstance().show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
//        LoadingViewUtil.getInstance().hide();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
        stop();
    }

    private void stop() {
//        if (srlFragmentInviteDetail != null) {
//            srlFragmentInviteDetail.finishRefresh();
//            srlFragmentInviteDetail.finishLoadMore();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }
}