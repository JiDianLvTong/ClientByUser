package com.android.jidian.client.mvp.ui.activity.map;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.MapJwduV5Bean;
import com.android.jidian.client.util.MakerUtils;
import com.android.jidian.client.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChargeSiteMap extends U6BaseActivityByMvp<ChargeSiteMapPresenter> implements ChargeSiteMapContract.View {

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.l_1)
    public LinearLayout l_1;
    @BindView(R.id.l_2)
    public LinearLayout l_2;
    //地图Map相关
    @BindView(R.id.map)
    public MapView mapView;
    private Bundle savedInstanceState;
    private AMap aMap;

    //定位相关
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public MyLocationStyle myLocationStyle;
    public double[] coordinates = new double[]{0,0};
    private ArrayList<Marker> markers = new ArrayList<>();
    private AMapLocation aMapLocation;

    //其他相关
    private boolean hasRequestInternet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_map);
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    //初始化
    @Override
    public void initView() {
        mPresenter = new ChargeSiteMapPresenter();
        mPresenter.attachView(this);
        mapInit();
        mapLocation();
    }

    //地图初始化
    public void mapInit(){
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        //定位相关()
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) ;
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(0x00000000);
        myLocationStyle.strokeWidth(0.0f);
        myLocationStyle.radiusFillColor(0x00000000);
        myLocationStyle.myLocationIcon(new MakerUtils().localMarker(activity));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //参数初始化
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势.
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止倾斜手势.
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false); //显示默认的定位按钮
    }

    private void mapLocation(){
        try {
            mLocationClient = new AMapLocationClient(this);
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
                            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                            coordinates[1] = aMapLocation.getLatitude();//获取纬度
                            coordinates[0] = aMapLocation.getLongitude();//获取经度
                            aMapLocation.getAccuracy();//获取精度信息
                            if (myLocationStyle != null && aMap != null) {
                                aMap.setMyLocationStyle(myLocationStyle);

                            }
                            if(hasRequestInternet == false){
                                hasRequestInternet = true;
                                getInternetData();
                                onClickL_2();
                            }
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError","location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //启动定位
            mLocationClient.startLocation();
        }catch (Exception e){

        }
    }

    //获取网络数据
    private void getInternetData(){
        progressDialog.show();
        mPresenter.requestChargeSite(UserInfoHelper.getInstance().getUid(),coordinates[0]+"",coordinates[1]+"" , "0");
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        activity.finish();
    }

    @OnClick(R.id.l_1)
    public void onClickL_1(){
        hasRequestInternet = false;
        for(int i = 0 ; i < markers.size();i++){
            markers.get(i).remove();
        }
        markers.clear();
        initView();
    }

    @OnClick(R.id.l_2)
    public void onClickL_2(){
        if(aMapLocation!=null){
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(coordinates[1],coordinates[0]),18,0,0));
            aMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mapView!=null){
            mapView.onDestroy();
        }
    }

    @Override
    public void requestChargeSiteSuccess(ChargeSiteMapBean bean) {
        progressDialog.dismiss();
        List<ChargeSiteMapBean.DataBean> dataArrayList = bean.getData();
        for(int i = 0 ; i < dataArrayList.size() ; i++){
            String jingdu = dataArrayList.get(i).getJingdu() + "";
            String weidu = dataArrayList.get(i).getWeidu() + "";

            List<ChargeSiteMapBean.DataBean.ListBean> mapSiteItemArrayList = dataArrayList.get(i).getList();

            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(Double.parseDouble(weidu),Double.parseDouble(jingdu)));
            markerOption.draggable(false);//设置Marker可拖动

            markerOption.icon(new MakerUtils().chargeSiteMarker(activity,mapSiteItemArrayList));
            markerOption.setFlat(true);//设置marker平贴地图效果
            Marker marker = aMap.addMarker(markerOption);
            markers.add(marker);
        }

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                for(int i = 0 ; i < dataArrayList.size() ; i++){
                    String jingdu = dataArrayList.get(i).getJingdu() + "";
                    String weidu = dataArrayList.get(i).getWeidu() + "";
                    if(jingdu.equals(latLng.longitude + "") && weidu.equals(latLng.latitude + "")){
//                        ChargeSiteMapBean.DataBean dataBean = dataArrayList.get(i);
//                        ChargeSiteShowInfo chargeSiteShowInfo = new ChargeSiteShowInfo(activity, dataBean , coordinates[1] , coordinates[0]);
//                        chargeSiteShowInfo.showPopupWindow();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void requestChargeSiteError(String msg) {
        progressDialog.dismiss();
    }




    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
