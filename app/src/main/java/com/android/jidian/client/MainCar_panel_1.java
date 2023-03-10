package com.android.jidian.client;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.android.jidian.client.util.ViewUtil;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.tandong.bottomview.view.BottomView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

import static com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap;

@EActivity(R.layout.main_car_panel_1)
public class MainCar_panel_1 extends BaseActivity implements LocationSource, AMapLocationListener {
    @ViewById
    TextView select_text;
    @ViewById
    MapView map_view;
    @ViewById
    FrameLayout main_panel;
    String[] dataString = new String[]{"1km", "3km", "5km", "10km", "15km", "20km", "30km"};
    int[] dataInt = new int[]{1000, 3000, 5000, 10000, 15000, 20000, 30000};
    private Bundle savedInstanceState;
    private AMap aMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private AMapLocation amapLocation;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Circle circle;
    private NumberPickerView numberPickerView;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    void afterViews() {
        map_view.onCreate(savedInstanceState);// ?????????????????????
        if (aMap == null) {
            aMap = map_view.getMap();
            setUpMap();
        }
        init();
    }

    private void init() {
        if (PubFunction.isConnect(activity, true)) {
            HttpGetFence();
        }
    }

    /**
     * ????????????amap?????????
     */
    private void setUpMap() {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.location_marker);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int newWidth = ViewUtil.dp2px(activity, 29);
        int newHeight = ViewUtil.dp2px(activity, 29);
        // ??????????????????
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // ?????????????????????matrix??????
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // ??????????????????
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        BitmapDescriptor var = fromBitmap(newbm);

        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(var);
        myLocationStyle.strokeColor(0x00000000);// ???????????????????????????
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// ???????????????????????????
        // myLocationStyle.anchor(int,int)//????????????????????????
        myLocationStyle.strokeWidth(0.0f);// ???????????????????????????

        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
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

        // ??????????????????
        circle = aMap.addCircle(new CircleOptions().center(BEIJING).radius(4000).strokeColor(Color.argb(50, 1, 1, 1)).fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(5));
    }

    public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// ??????????????????

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
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
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

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void select_panel() {
        final BottomView bottomView = new BottomView(activity, R.style.BottomViewTheme_Defalut, R.layout.main_car_panel_bottom_view);
        bottomView.setAnimation(R.style.popwin_anim_style);
        View view = bottomView.getView();

        numberPickerView = (NumberPickerView) view.findViewById(R.id.picker);
        numberPickerView.setDisplayedValues(dataString);
        numberPickerView.setMinValue(0);
        numberPickerView.setMaxValue(dataString.length - 1);

        TextView submit = (TextView) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PubFunction.isConnect(activity, true)) {
                    select_text.setText(numberPickerView.getContentByCurrValue());
                    int a = numberPickerView.getPickedIndexRelativeToRaw();
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(13 - a));
                    circle.setRadius(dataInt[a]);
                    LatLng start = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    circle.setCenter(start);
                    HttpSetFence(amapLocation.getLongitude() + "", amapLocation.getLatitude() + "", dataInt[a] + "");
                    bottomView.dismissBottomView();

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("jingdu", amapLocation.getLongitude() + "");
                    bundle.putString("weidu", amapLocation.getLatitude() + "");
                    bundle.putString("radius", dataInt[a] + "");
                    message.setData(bundle);
                }
            }
        });
        bottomView.showBottomView(true);
    }

    /**
     * http?????????Device/fence     ?????????????????????  ??????
     */
    @UiThread
    void HttpGetFenceSuccess(String messageString, String data) {
        progressDialog.dismiss();
        if (TextUtils.isEmpty(data)) {
            MyToast.showTheToast(activity, "??????????????????");
        } else {
            try {
                JSONTokener jsonTokener = new JSONTokener(data);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                String id = jsonObject.getString("id");
                String jingdu = jsonObject.getString("jingdu");
                String weidu = jsonObject.getString("weidu");
                String radius = jsonObject.getString("radius");

                int radius_int = Integer.parseInt(radius);
                int radius_int_small = radius_int / 1000;
                double jingdu_doublr = Double.parseDouble(jingdu);
                double weidu_doublr = Double.parseDouble(weidu);
                circle.setRadius(radius_int);
                LatLng start = new LatLng(weidu_doublr, jingdu_doublr);
                circle.setCenter(start);
                select_text.setText(radius_int_small + "km");

                Resources res_0 = getResources();
                Bitmap bmp_0 = BitmapFactory.decodeResource(res_0, R.drawable.map_c_local);
                int width = bmp_0.getWidth();
                int height = bmp_0.getHeight();
                // ?????????????????????
                int newWidth = ViewUtil.dp2px(activity, 10);
                int newHeight = ViewUtil.dp2px(activity, 17);
                // ??????????????????
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // ?????????????????????matrix??????
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap newbm_0 = Bitmap.createBitmap(bmp_0, 0, 0, width, height, matrix, true);
                bmp_0.recycle();
                bmp_0 = null;
                BitmapDescriptor bitmapDescriptor_0 = fromBitmap(newbm_0);

                LatLng marker = new LatLng(weidu_doublr, jingdu_doublr);
                markerOptions = new MarkerOptions().icon(bitmapDescriptor_0).anchor(0.5f, 0.5f).position(marker).draggable(true);
                aMap.addMarker(markerOptions);
                main_panel.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @UiThread
    void HttpGetFenceError(String messageString) {
        MyToast.showTheToast(activity, messageString);
        progressDialog.dismiss();
    }

    @Background
    void HttpGetFence() {
        String path = PubFunction.app + "Device/fence.html";
        HttpPost httpPost = new HttpPost(path);
        httpPost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        PubFunction.setHandlerWhit_APTK_APUD(activity, "app", uid, httpPost);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("uid", uid));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONTokener jsonTokener = new JSONTokener(result);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                System.out.println(jsonObject);
                String code = jsonObject.getString("status");
                String messageString = jsonObject.getString("msg");

                if ("1".equals(code)) {
                    if (jsonObject.has("data")) {
                        String data = jsonObject.getString("data");
                        HttpGetFenceSuccess(messageString, data);
                    } else {
                        HttpGetFenceSuccess(messageString, "");
                    }
                } else {
                    HttpGetFenceError(messageString);
                }
            } else {
                HttpGetFenceError("??????????????????HttpGetFence");
            }
        } catch (Exception e) {
            HttpGetFenceError("json???????????????HttpGetFence");
        }
    }

    /**
     * http?????????Device/setFence     ????????????????????????
     */
    @UiThread
    void HttpSetFenceSuccess(String messageString, String data) {
        progressDialog.dismiss();
        MyToast.showTheToast(activity, messageString);
        HttpGetFence();
    }

    @UiThread
    void HttpSetFenceError(String messageString) {
        MyToast.showTheToast(activity, messageString);
        progressDialog.dismiss();
    }

    @Background
    void HttpSetFence(String jingdu, String weidu, String radius) {
        String path = PubFunction.app + "Device/setFence.html";
        HttpPost httpPost = new HttpPost(path);
        httpPost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

        PubFunction.setHandlerWhit_APTK_APUD(activity, "app", uid, httpPost);

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("uid", uid));
        list.add(new BasicNameValuePair("jingdu", jingdu));
        list.add(new BasicNameValuePair("weidu", weidu));
        list.add(new BasicNameValuePair("radius", radius));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONTokener jsonTokener = new JSONTokener(result);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();

                System.out.println(jsonObject);
                String code = jsonObject.getString("status");
                String messageString = jsonObject.getString("msg");
                if ("1".equals(code)) {
                    if (jsonObject.has("data")) {
                        String data = jsonObject.getString("data");
                        HttpSetFenceSuccess(messageString, data);
                    } else {
                        HttpSetFenceSuccess(messageString, "");
                    }
                } else {
                    HttpSetFenceError(messageString);
                }
            } else {
                HttpSetFenceError("??????????????????HttpSetFence");
            }
        } catch (Exception e) {
            HttpSetFenceError("json???????????????HttpSetFence");
        }
    }
}