package com.android.jidian.repair.mvp.main;

import static com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolFragmentEvent.LOCATION_SUCCESS;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jidian.repair.BuildConfig;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.base.permissionManager.PermissionManager;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.keepAlive.KeepLiveManager;
import com.android.jidian.repair.service.LongLinkService;
import com.android.jidian.repair.utils.file.FileManager;
import com.android.jidian.repair.widgets.dialog.DialogByEnter;
import com.android.jidian.repair.mvp.main.FailureFragment.FailureEvent;
import com.android.jidian.repair.mvp.main.PatrolFragment.MainPartolFragment;
import com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolFragmentEvent;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.TimeLimitTaskFragment;
import com.android.jidian.repair.mvp.main.UserFragment.UserFragment;
import com.android.jidian.repair.utils.WebSocketLongLink;
import com.android.jidian.repair.widgets.NoScrollViewPager;
import com.android.jidian.repair.widgets.ViewPagerAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.permissionx.guolindev.PermissionX;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

@BindEventBus
public class MainActivity extends BaseActivityByMvp<MainPresenter> implements MainContract.View {

    @BindView(R.id.vp_content)
    public NoScrollViewPager vpContent;
    @BindView(R.id.tab_layout_main_page)
    public CommonTabLayout tabLayoutMainPage;

    private List<Fragment> mFragments;

    //定位相关
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public double[] coordinates = new double[]{0, 0};
    private AMapLocation aMapLocation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPresenter.requestAppUpdateVersion(UserInfoSp.getInstance().getId());
        //1像素且透明Activity提升App进程优先级
//        KeepLiveManager.getInstance().registerKeepLiveReceiver(this);
        ArrayList<CustomTabEntity> mMainTabEntities = new ArrayList<>();
        String[] mTitles = {"即时任务", "巡检", "我的"};
        int[] mIconUnSelectIds = {
                R.drawable.main_count_gray, R.drawable.main_check_gray,
                R.drawable.main_user_gray
        };//R.drawable.main_broken_gray,
        int[] mIconSelectIds = {
                R.drawable.main_count_yellow, R.drawable.main_check_yellow,
                R.drawable.main_user_yellow
        };//R.drawable.main_broken_yellow,

        for (int i = 0; i < mIconSelectIds.length; i++) {
            mMainTabEntities.add(new MainTabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        tabLayoutMainPage.setTabData(mMainTabEntities);
        tabLayoutMainPage.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpContent.setCurrentItem(position, false);
                tabLayoutMainPage.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        //添加fragment
        mFragments = new ArrayList<>();
        mFragments.add(TimeLimitTaskFragment.newInstance("", ""));
        mFragments.add(MainPartolFragment.newInstance("", ""));
        mFragments.add(new UserFragment());

        vpContent.setOffscreenPageLimit(mTitles.length - 1);
        vpContent.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        vpContent.setScrollEnable(false);
        PermissionX.init(MainActivity.this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        initLocation();
                    } else {
                        DialogByEnter dialog = new DialogByEnter(activity, "当前应用缺少必要权限,会影响部分功能使用！");
                        dialog.showPopupWindow();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainEvent event) {
        if (event != null) {
            if (event.getEvent() == MainEvent.RECEIVE_LONG_LINK) {
                Log.d(TAG, "onEvent: " + event.getData());

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    if (media != null) {
                        String compressPath = media.getCompressPath();
                        EventBus.getDefault().post(new FailureEvent(FailureEvent.FAILURE_TAKE_PHOTO, media));
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null && !TextUtils.isEmpty(uid) && coordinates[0] != 0 && coordinates[1] != 0) {
            mPresenter.requestLoginCheckAcc(uid, apptoken, coordinates[0] + "", coordinates[1] + "");
        }
        if (0 == tabLayoutMainPage.getCurrentTab()) {
            if (mFragments.get(0) != null) {
                ((TimeLimitTaskFragment) mFragments.get(0)).setFragmentRefresh();
            }
        }else if (1 == tabLayoutMainPage.getCurrentTab()){
            if (mFragments.get(1) != null) {
                ((MainPartolFragment) mFragments.get(1)).setFragmentRefresh();
            }
        }
//        startService(new Intent(MainActivity.this, LongLinkService.class));
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
                            coordinates[0] = aMapLocation.getLongitude();//获取经度
                            coordinates[1] = aMapLocation.getLatitude();//获取纬度
                            EventBus.getDefault().postSticky(new PatrolFragmentEvent(LOCATION_SUCCESS, coordinates[0] + "", coordinates[1] + ""));
                            Log.d(TAG, "onLocationChanged:  aMapLocation.getCity(): " + aMapLocation.getAddress() + "aMapLocation.getCityCode(): " + aMapLocation.getCityCode() + "aMapLocation.getLatitude() :" + aMapLocation.getLatitude() + "aMapLocation.getLongitude():" + aMapLocation.getLongitude());
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                            Log.e(TAG, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
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

//    private void onTabIconSelected(int position) {
//        vpContent.setCurrentItem(position, false);
//        tabLayoutMainPage.setCurrentTab(position);
//        if (position == 0 || position == 2) {
//            //首页刷新数据
////            mMainWalletFragment.refreshData(true);
////            switchStatusBar(R.color.transparent, false, false);
//            if (position == 2) {
////                ((MainWalletFragment) mFragments.get(2)).onFragmentShow();
//            }
//        } else {
////            switchStatusBar(R.color.color_FFFFFF, true, true);
//        }
//    }

    @Override
    public void requestLoginCheckAccSuccess() {

    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestAppUpdateVersionSuccess(UpdateVersionBean.DataBean bean) {
        String url = bean.getAndroid_url();
        String force = bean.getAndroid_force();
        boolean isForce = false;
        if (force.equals("1")) {
            isForce = true;
        }
        int netVersion = Integer.parseInt(bean.getAndroid_code());
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
                    .updateTitle("发现新版本:" + "Ver_" + bean.getAndroid_name())
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

    /**
     * 嵌套类
     */
    private static final class MainTabEntity implements CustomTabEntity {
        public String title;
        int selectedIcon;
        int unSelectedIcon;

        MainTabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }
}