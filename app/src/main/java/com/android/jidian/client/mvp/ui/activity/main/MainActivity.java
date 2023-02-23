package com.android.jidian.client.mvp.ui.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.Marker;
import com.android.jidian.client.BuildConfig;
import com.android.jidian.client.R;
import com.android.jidian.client.base.PermissionManager.PermissionManager;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.base.broadcastManage.BroadcastManager;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.mvp.bean.MainAppVersionBean;
import com.android.jidian.client.mvp.contract.MainActivityContract;
import com.android.jidian.client.mvp.presenter.MainActivityPresenter;
import com.android.jidian.client.mvp.ui.activity.login.LoginActivity;
import com.android.jidian.client.mvp.ui.activity.main.mainEquipmentFragment.MainEquipmentEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainEquipmentFragment.MainEquipmentFragment;
import com.android.jidian.client.mvp.ui.activity.main.mainFindFragment.MainFindEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainFindFragment.MainFindFragment;
import com.android.jidian.client.mvp.ui.activity.main.mainShopFragment.MainShopEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainShopFragment.MainShopFragment;
import com.android.jidian.client.mvp.ui.activity.main.mainUserFragment.MainUserEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainUserFragment.MainUserFragment;
import com.android.jidian.client.mvp.ui.dialog.DialogByEnter;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.util.file.FileManager;
import com.android.jidian.client.widgets.MyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private int[] isSelectIcons = new int[]{R.drawable.u6_main_foot_electric_bicycle_d7a64a, R.drawable.u6_main_foot_find_d7a64a, R.drawable.u6_main_foot_shop_d7a64a, R.drawable.u6_main_foot_user_d7a64a};
    private int[] notSelectIcons = new int[]{R.drawable.u6_main_foot_electric_bicycle_ffffff, R.drawable.u6_main_foot_find_ffffff, R.drawable.u6_main_foot_shop_ffffff, R.drawable.u6_main_foot_user_ffffff};

    @BindView(R.id.mainPanel)
    public LinearLayout mainPanel;

    private int localPage = -1;

    private MainEquipmentFragment mainEquipmentFragment;
    private MainFindFragment mainFindFragment;
    private MainShopFragment mainShopFragment;
    private MainUserFragment mainUserFragment;

    //定位相关
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public double[] coordinates = new double[]{0, 0};
    private ArrayList<Marker> markers = new ArrayList<>();
    private AMapLocation aMapLocation;
    private boolean mPositioned = false;

    //权限相关
    private boolean permissionShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_main);
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查用户登录信息 以及 使用环境
        boolean isLogin = !UserInfoHelper.getInstance().getUid().isEmpty();
        //检查权限
        if (permissionShow == false) {
            permissionShow = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    permissionShow = false;
                }
            }, 10 * 1000);
            PermissionManager.getInstance().getLocalAndWrite(activity, new PermissionManager.PermissionListener() {
                @Override
                public void granted(List<String> grantedList) {
                    for (int i = 0; i < grantedList.size(); i++) {
                        if (grantedList.get(i).equals(PermissionManager.getInstance().local_1)) {
                            initLocation();
                        }
                        if (grantedList.get(i).equals(PermissionManager.getInstance().write)) {
                            if (isLogin) {
                                apptoken = sharedPreferences.getString("apptoken", "");
                                String appSn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
                                if (!TextUtils.isEmpty(apptoken) && !TextUtils.isEmpty(appSn)) {
                                    mPresenter.requestCheckAccv2(apptoken, appSn);
                                }
                            }
                        }
                    }
                }

                @Override
                public void refused(List<String> deniedList) {
                    DialogByEnter dialog = new DialogByEnter(activity, "当前应用缺少必要权限,会影响部分功能使用！");
                    dialog.showPopupWindow();
                }
            });
        }

        //获取用户信息
        if (isLogin) {
            refreshCurrentPage();
        }
    }

    private void initLocation() {
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
                            coordinates[0] = aMapLocation.getLongitude();//获取经度
                            coordinates[1] = aMapLocation.getLatitude();//获取纬度
                            Log.d(Tag, "onLocationChanged:  aMapLocation.getCity(): " + aMapLocation.getAddress() + "aMapLocation.getCityCode(): " + aMapLocation.getCityCode() + "aMapLocation.getLatitude() :" + aMapLocation.getLatitude() + "aMapLocation.getLongitude():" + aMapLocation.getLongitude());
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                            Log.e(Tag, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            Log.e(Tag, e.toString());
            e.printStackTrace();
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
                    mainShopFragment.setFragmentPosition(coordinates[0] + "", coordinates[1] + "");
                } else {
                    mainShopFragment.setFragmentPosition("", "");
                }
            }
        } else if (layout.getId() == R.id.p_4) {
            changeMain(3);
        }
    }

    private void refreshCurrentPage() {
        if (0 == localPage) {
            mainEquipmentFragment.setFragmentRefresh();
        } else if (1 == localPage) {
            mainFindFragment.setFragmentRefresh();
        } else if (2 == localPage) {
            mainShopFragment.setFragmentRefresh();
        } else if (3 == localPage) {
            mainUserFragment.setFragmentRefresh();
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
            footTextViewList.get(i).setTextColor(0xffffffff);
            footImageViewList.get(i).setImageResource(notSelectIcons[i]);
        }
        footTextViewList.get(page).setTextColor(activity.getResources().getColor(R.color.yellow_B69873));
        footImageViewList.get(page).setImageResource(isSelectIcons[page]);
        if (page == 0) {
            if (mainEquipmentFragment == null) {
                mainEquipmentFragment = new MainEquipmentFragment();
                ft.add(R.id.mainPanel, mainEquipmentFragment);
            } else {
                ft.show(mainEquipmentFragment);
            }
            mainEquipmentFragment.setFragmentRefresh();
//            EventBus.getDefault().postSticky(new MainEquipmentEvent(MainEquipmentEvent.REFRESH));
        } else if (page == 1) {
            if (mainFindFragment == null) {
                mainFindFragment = new MainFindFragment();
                ft.add(R.id.mainPanel, mainFindFragment);
            } else {
                ft.show(mainFindFragment);
            }
//            EventBus.getDefault().postSticky(new MainFindEvent(MainFindEvent.REFRESH));
            mainFindFragment.setFragmentRefresh();
        } else if (page == 2) {
            if (mainShopFragment == null) {
                mainShopFragment = new MainShopFragment();
                ft.add(R.id.mainPanel, mainShopFragment);
            } else {
                ft.show(mainShopFragment);
            }
//            EventBus.getDefault().postSticky(new MainShopEvent(MainShopEvent.REFRESH));
            mainShopFragment.setFragmentRefresh();
        } else if (page == 3) {
            if (mainUserFragment == null) {
                mainUserFragment = new MainUserFragment();
                ft.add(R.id.mainPanel, mainUserFragment);
            } else {
                ft.show(mainUserFragment);
            }
//            EventBus.getDefault().postSticky(new MainUserEvent(MainUserEvent.REFRESH));
            mainUserFragment.setFragmentRefresh();
        }
        ft.commitAllowingStateLoss();
        localPage = page;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainActivityEvent event) {
        if (event != null) {
            if (event.getEvent() == MainActivityEvent.CHANGEMAIN) {
                changeMain(event.getIndex());
            }else if (event.getEvent() == MainActivityEvent.LOGIN_SUCCESS){
                if (localPage == 0) {
                    if (mainEquipmentFragment != null) {
                        mainEquipmentFragment.setFragmentRefresh();
                    }
                }else {
                    changeMain(0);
                }
            }
        }
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
        if (url != null && !url.equals("null") && netVersion > localVersion) {

            UpdateConfig updateConfig = new UpdateConfig();
            updateConfig.setCheckWifi(true);
            updateConfig.setNeedCheckMd5(true);
            updateConfig.setNotifyImgRes(R.drawable.u6_pub_dialog_update);
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
    public void requestCheckAccv2Success(LoginCheckAccv2Bean bean) {
        MyToast.showTheToast(this, bean.getMsg());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_avatar", "");
        editor.clear();
        editor.apply();
        startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByDoubleClick();
        }
        return false;
    }

    private boolean isExit;

    private void exitByDoubleClick() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            MyToast.showTheToast(activity, "再按一次退出程序!");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }
}