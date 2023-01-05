package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.util.ViewUtil;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap;
import static java.lang.Thread.sleep;

@SuppressLint("Registered")
@EActivity(R.layout.main_car_panel_3)
public class MainCar_panel_3 extends BaseActivity implements LocationSource, AMapLocationListener, AMap.OnMapTouchListener {
    @ViewById
    MapView map_view;
    @ViewById
    LinearLayout page_return;
    @ViewById
    TextView play, start_textView, end_textView;
    @ViewById
    FrameLayout main_panel;
    private Bundle savedInstanceState;
    private String start_time = "";
    private String end_time = "";
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private int thread_code = 0;
    private List<Map<String, Double>> double_list = new ArrayList<>();
    private int cur_code = 1;
    private String car_id = "";
    private TimePickerView pvCustomTime1;
    private TimePickerView pvCustomTime2;
    private boolean hadTouch = false;//用户触摸后停止地图camera移动

    @Background
    void thread_1() {
        while (thread_code == 0) {
            try {
                if (cur_code < double_list.size()) {
                    Map<String, Double> stringDoubleMap_A = double_list.get(cur_code - 1);
                    Map<String, Double> stringDoubleMap_B = double_list.get(cur_code);
                    double jingdu_A = stringDoubleMap_A.get("jingdu");
                    double weidu_A = stringDoubleMap_A.get("weidu");
                    double jingdu_B = stringDoubleMap_B.get("jingdu");
                    double weidu_B = stringDoubleMap_B.get("weidu");
                    LatLng A = new LatLng(weidu_A, jingdu_A);
                    LatLng B = new LatLng(weidu_B, jingdu_B);
                    addPolylinesWithTexture(A, B);
                    //当用户触摸屏幕后，停止地图视图的自动移动
                    if (!hadTouch) {
                        LatLng location = new LatLng(weidu_B, jingdu_B);
                        aMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                    }
                    cur_code = cur_code + 1;
                }
                sleep(1000);
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
//        long timeStamp_a = System.currentTimeMillis();
//        long a = 3 * 24 * 60 * 60 * 1000;
//        long timeStamp_b = timeStamp_a - a;
//        long b = 1 * 24 * 60 * 60 * 1000;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//        Date date_1 = new Date(timeStamp_a);
//        end_time = simpleDateFormat.format(date_1);
//        Date date_2 = new Date(timeStamp_b);
//        start_time = simpleDateFormat.format(date_2);
        car_id = getIntent().getStringExtra("car_id");
        map_view.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = map_view.getMap();
            setUpMap();
        }
//        if (PubFunction.isConnect(activity)) {
//            HttpGetHistoryLocal(start_time, end_time);
//        }
        //初始化自定义PickerView显式年/月
        initCustomTimePicker1();
        initCustomTimePicker2();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //电动车轨迹回放页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_ELECTRIC_VEHICLE_TRACK_PLAYBACK);
            }
        }, 500);
    }

    private void initCustomTimePicker1() {
        /*
         * 注意事项：
         * 1.自定义布局中，id为optionspicker或者timepicker的布局以及其子控件必须要有，否则会报空指针
         * 2.因为系统Calendar的月份是从0-11的，所以如果是调用Calendar的set方法来设置时间，月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器，自定义布局
        pvCustomTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                start_time = format.format(date);
                start_textView.setText(start_time);
            }
        })
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        final TextView tv_cancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime1.returnData();
                                pvCustomTime1.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime1.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(20)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "时", "分", "秒")
                .setLineSpacingMultiplier(1.5f)
                .setTextXOffset(40, -40, 40, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.parseColor("#e6e6e6"))
                .build();
    }

    private void initCustomTimePicker2() {
        /*
         * 注意事项：
         * 1.自定义布局中，id为optionspicker或者timepicker的布局以及其子控件必须要有，否则会报空指针
         * 2.因为系统Calendar的月份是从0-11的，所以如果是调用Calendar的set方法来设置时间，月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器 ，自定义布局
        pvCustomTime2 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                end_time = format.format(date);
                Log.i("052402", end_time);
                end_textView.setText(end_time);
            }
        })
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        final TextView tv_cancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime2.returnData();
                                pvCustomTime2.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime2.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(20)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "时", "分", "秒")
                .setLineSpacingMultiplier(1.5f)
                .setTextXOffset(40, -40, 40, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.parseColor("#e6e6e6"))
                .build();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.location_marker);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int newWidth = ViewUtil.dp2px(activity, 42);
        int newHeight = ViewUtil.dp2px(activity, 50);
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        BitmapDescriptor var = fromBitmap(newbm);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(var);
        myLocationStyle.strokeColor(0x00000000);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMapTouchListener(this);//设置地图触摸事件监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        // 设置收缩按钮
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
    }

    //绘制一条纹理线
    private void addPolylinesWithTexture(LatLng A, LatLng B) {
        //四个点
        //用一个数组来存放纹理
        List<BitmapDescriptor> texTuresList = new ArrayList<>();
        texTuresList.add(BitmapDescriptorFactory.fromResource(R.drawable.map_alr));
        //指定某一段用某个纹理，对应texTuresList的index即可, 四个点对应三段颜色
        List<Integer> texIndexList = new ArrayList<>();
        texIndexList.add(0);//对应上面的第0个纹理
        PolylineOptions options = new PolylineOptions();
        options.width(40);//设置宽度
        //加入四个点
        options.add(A, B);
        //加入对应的颜色,使用setCustomTextureList 即表示使用多纹理；
        options.setCustomTextureList(texTuresList);
        //设置纹理对应的Index
        options.setCustomTextureIndex(texIndexList);
        aMap.addPolyline(options);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map_view.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map_view.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_view.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_view.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        thread_code = 1;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
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
    void play() {
        //点击开始回放
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ELECTRIC_VEHICLE_TRACK_PLAYBACK_PLAYBACK);
        if (start_time.isEmpty() || end_time.isEmpty()) {
            MyToast.showTheToast(activity, "请选择开始时间或结束时间！");
        } else {
            if (PubFunction.isConnect(activity, true)) {
                hadTouch = false;
                HttpGetHistoryLocal(start_time, end_time);
                progressDialog.show();
            }
        }
    }

    /**
     * http接口:汽车信息
     */
    @UiThread
    void HttpGetDataSuccess(String data) {
        aMap.clear();
        setUpMap();
        cur_code = 1;
        double_list.clear();
        progressDialog.dismiss();
        main_panel.setVisibility(View.VISIBLE);
        try {
            JSONTokener jsonTokener = new JSONTokener(data);
            JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
            if (jsonArray.length() == 0) {
                MyToast.showTheToast(activity, "暂无轨迹记录！");
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    System.out.println();
                    String lat = jsonObject.getString("lat");
                    String lng = jsonObject.getString("lng");
                    double lat_D = Double.parseDouble(lat);
                    double lng_D = Double.parseDouble(lng);
                    Map<String, Double> map = new HashMap<>();
                    map.put("jingdu", lng_D);
                    map.put("weidu", lat_D);
                    double_list.add(map);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @UiThread
    void HttpGetDataError(String messageString) {
        MyToast.showTheToast(activity, messageString);
        progressDialog.dismiss();
    }

    @Background
    void HttpGetHistoryLocal(String startTime, String endTime) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("url", "historyAction!findHistory.do"));
        dataList.add(new ParamTypeData("gps", car_id));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("type", "4"));
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mapType", "2");
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            dataList.add(new ParamTypeData("param", jsonObject.toString()));
        } catch (JSONException e) {
            System.out.println(e.getLocalizedMessage());
        }
        new OkHttpConnect(activity, PubFunction.app + "rpc/send.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetHistoryLocal(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetHistoryLocal(String response, String type) {
        if (!type.equals("0")) {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String status = jsonObject_response.getString("status");
                if (status.equals("1")) {
                    if (jsonObject_response.has("data") && !(jsonObject_response.get("data").equals(JSONObject.NULL))) {
                        JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                        String desc = "";
                        if (jsonObject.has("desc")) {
                            desc = jsonObject.getString("desc");
                        }
                        if (jsonObject.has("res") && !jsonObject.get("res").equals(JSONObject.NULL)) {
                            String msg = jsonObject.getString("res");
                            if (msg != null && !msg.isEmpty() && msg.equals("true")) {
                                String data = jsonObject.getString("result");
                                if (data != null && !data.isEmpty()) {
                                    aMap.clear();
//                                    setUpMap();
                                    cur_code = 1;
                                    double_list.clear();
                                    main_panel.setVisibility(View.VISIBLE);
                                    try {
                                        JSONTokener jsonTokener = new JSONTokener(data);
                                        JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                                        if (jsonArray.length() == 0) {
                                            MyToast.showTheToast(activity, "暂无轨迹记录！");
                                        } else {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject_item = jsonArray.getJSONObject(i);
                                                System.out.println();
                                                String lat = jsonObject_item.getString("lat");
                                                String lng = jsonObject_item.getString("lng");
                                                double lat_D = Double.parseDouble(lat);
                                                double lng_D = Double.parseDouble(lng);
                                                Map<String, Double> map = new HashMap<>();
                                                map.put("jingdu", lng_D);
                                                map.put("weidu", lat_D);
                                                double_list.add(map);
                                            }
                                            if (double_list.size() == 0) {
                                                if (start_time.isEmpty() || end_time.isEmpty()) {
                                                    MyToast.showTheToast(activity, "请选择开始时间或结束时间！");
                                                } else {
                                                    MyToast.showTheToast(activity, "暂无轨迹记录！");
                                                }
                                            } else {
                                                if (PubFunction.isConnect(activity, true)) {
                                                    Map<String, Double> stringDoubleMap_B = double_list.get(0);
                                                    double jingdu_B = stringDoubleMap_B.get("jingdu");
                                                    double weidu_B = stringDoubleMap_B.get("weidu");
                                                    LatLng location = new LatLng(weidu_B, jingdu_B);
                                                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 15, 0, 0)), 1000, null);
                                                    Thread.sleep(1500);
                                                    thread_1();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        MyToast.showTheToast(activity, desc);
                                    }
                                }
                            } else {
                                MyToast.showTheToast(activity, desc);
                            }
                        } else {
                            MyToast.showTheToast(activity, "暂无轨迹记录！");
                        }
                    } else {
                        MyToast.showTheToast(activity, "暂无轨迹记录！");
                    }
                } else {
                    String msg = jsonObject_response.getString("msg");
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    @Click
    void start_textView() {
        if (pvCustomTime1 != null) {
            //弹出自定义时间选择器
            pvCustomTime1.show();
        }
    }

    @Click
    void end_textView() {
        if (pvCustomTime2 != null) {
            //弹出自定义时间选择器
            pvCustomTime2.show();
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        hadTouch = true;
    }
}