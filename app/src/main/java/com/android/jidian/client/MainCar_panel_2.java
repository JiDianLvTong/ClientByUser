package com.android.jidian.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.pub.gaode.AMapUtil;
import com.android.jidian.client.pub.gaode.DrivingRouteOverlay;
import com.android.jidian.client.util.BuryingPointManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap;
import static java.lang.Thread.sleep;

@EActivity(R.layout.main_car_panel_2)
public class MainCar_panel_2 extends BaseActivity implements AMap.CancelableCallback, LocationSource, AMapLocationListener, AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {

    private final int ROUTE_TYPE_DRIVE = 2;
    @ViewById
    LinearLayout page_return;
    @ViewById
    MapView map_view;
    @ViewById
    TextView find_car;
    @ViewById
    RelativeLayout main_panel;
    private Bundle savedInstanceState;
    private AMap aMap;
    private UiSettings mUiSettings;
    private GeocodeSearch geocoderSearch;
    private MyLocationStyle myLocationStyle;
    private AMapLocation amapLocation;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    //    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//?????????39.942295,116.335891
//    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//?????????39.995576,116.481288
    private LatLonPoint mStartPoint = null;
    private LatLonPoint mEndPoint = null;
    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ProgressDialog progDialog = null;// ??????????????????

    private int is_local = 0;
    private int is_local_code = 0;

    private String car_id = "";


    @Background
    void setLocal() {
        while (is_local_code == 0) {
            try {
                sleep(10);
                if (is_local == 1) {
                    setUpMap();
                    is_local_code = 1;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    void afterViews() {
        car_id = getIntent().getStringExtra("car_id");
        map_view.onCreate(savedInstanceState);// ?????????????????????
        if (aMap == null) {
            aMap = map_view.getMap();
            setUpMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //?????????????????????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_ELECTRIC_VEHICLE_REAL_TIME_TRACKING);
            }
        }, 500);
    }

    private void setfromandtoMarker() {
//        aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(mEndPoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_c_local)));
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                .anchor(0.5f, 0.5f)
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .draggable(false);
        Marker marker = aMap.addMarker(markerOptions);
        marker.setInfoWindowEnable(false);
    }

    /**
     * ????????????
     */
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }


    /**
     * ????????????amap?????????
     */
    private void setUpMap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_c_local);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float newWidthSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        float newHeightSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23, metrics);
        //??????????????????
        float scaleWidth = newWidthSize / width;
        float scaleHeight = newHeightSize / height;
        //?????????????????????matrix??????
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // ??????????????????
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        myLocationStyle = new MyLocationStyle();
        BitmapDescriptor var = fromBitmap(newbm);
        myLocationStyle.myLocationIcon(var);
        myLocationStyle.anchor(1.0f, 1.0f);
        myLocationStyle.strokeColor(0x00000000);// ???????????????????????????
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// ???????????????????????????
        myLocationStyle.strokeWidth(0.0f);// ???????????????????????????
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        aMap.setLocationSource(this);// ??????????????????
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// ????????????????????????????????????
        aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        // ???????????????????????????????????? ??????????????????????????????????????????????????????????????????
        aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        // ??????????????????
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onResume() {
        super.onResume();
        map_view.onResume();
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onPause() {
        super.onPause();
        map_view.onPause();
        deactivate();
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_view.onSaveInstanceState(outState);
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_view.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }


    /**
     * ???????????????????????????
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        this.amapLocation = amapLocation;
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                is_local = 1;
                mListener.onLocationChanged(amapLocation);// ?????????????????????
            } else {
                String errText = "????????????," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * ????????????
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //??????????????????
            mlocationClient.setLocationListener(this);
            //??????????????????????????????
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //??????????????????
            mlocationClient.setLocationOption(mLocationOption);
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????2000ms?????????????????????????????????stopLocation()???????????????????????????
            // ???????????????????????????????????????????????????onDestroy()??????
            // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
            mlocationClient.startLocation();
        }
    }

    /**
     * ????????????
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * ??????????????????????????????
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            MyToast.showTheToast(activity, "????????????????????????...");
            return;
        }
        if (mEndPoint == null) {
            MyToast.showTheToast(activity, "???????????????");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// ??????????????????
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "");// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            mRouteSearch.calculateDriveRouteAsyn(query);// ????????????????????????????????????
        }
    }

    /**
     * ???????????????
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("????????????");
        progDialog.show();
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// ?????????????????????????????????
        setfromandtoMarker();
        aMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(AMapUtil.convertToLatLng(mEndPoint), 12, 0, 0)),
                888,
                MainCar_panel_2.this);
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    if (drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(activity, aMap, drivePath, mDriveRouteResult.getStartPos(), mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//????????????marker????????????
                    drivingRouteOverlay.setIsColorfulline(true);//????????????????????????????????????????????????true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("?????????" + taxiCost + "???");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MainCar_panel_2.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result", mDriveRouteResult);
                            startActivity(intent);
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    MyToast.showTheToast(activity, "??????");
                }

            } else {
                MyToast.showTheToast(activity, "??????");
            }
        } else {
            MyToast.showTheToast(activity, errorCode + "");
        }
    }

    /**
     * ???????????????
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }


    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void find_car() {
        //??????????????????
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ELECTRIC_VEHICLE_RT_SEARCH_ROUTE);
        if (PubFunction.isConnect(activity, true)) {
            if (PubFunction.isConnect(activity, true)) {
                HttpGetLocal();
                progressDialog.show();
            }
        }
    }


    /**
     * http??????:??????????????????
     */
    @Background
    void HttpGetLocal() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("url", "carAction!getPositionByID.do"));
        dataList.add(new ParamTypeData("gps", car_id));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "3"));
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mapType", "2");
            dataList.add(new ParamTypeData("param", jsonObject.toString()));
        } catch (JSONException e) {
            System.out.println(e.getLocalizedMessage());
        }
        new OkHttpConnect(activity, PubFunction.app + "rpc/send.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetLocal(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetLocal(String response, String type) {
        if (type.equals("0")) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                System.out.println(jsonObject_response);
                String status = jsonObject_response.getString("status");
                if (status.equals("1")) {
                    if (jsonObject_response.has("data")) {
                        JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                        String desc = "";
                        if (jsonObject.has("desc")) {
                            desc = jsonObject.getString("desc");
                        }
                        if (jsonObject.has("res")) {
                            String msg = jsonObject.getString("res");
                            if (msg.equals("true")) {
                                String data = jsonObject.getString("result");
                                try {
                                    JSONTokener jsonTokener = new JSONTokener(data);
                                    JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                                    JSONObject jsonObject_small = jsonArray.getJSONObject(0);
                                    String lat = jsonObject_small.getString("lat");
                                    String lng = jsonObject_small.getString("lng");
                                    double lat_D = Double.parseDouble(lat);
                                    double lng_D = Double.parseDouble(lng);
                                    mEndPoint = new LatLonPoint(lat_D, lng_D);
//                                    setfromandtoMarker();
                                } catch (Exception e) {
                                    MyToast.showTheToast(activity, desc);
                                }
                            } else {
                                MyToast.showTheToast(activity, desc);
                            }
                        } else {
                            MyToast.showTheToast(activity, desc);
                        }
                    } else {
                        MyToast.showTheToast(activity, "???????????????");
                    }
                } else {
                    String msg = jsonObject_response.getString("msg");
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON???" + e.toString());
            }
        }
        if (mEndPoint != null) {
            mStartPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
            searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {
        aMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(AMapUtil.convertToLatLng(mEndPoint), 12, 0, 0)),
                888,
                MainCar_panel_2.this);
    }
}
