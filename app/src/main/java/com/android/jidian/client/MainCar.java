package com.android.jidian.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jidian.client.bean.BluetoothEvent;
import com.android.jidian.client.bluetooth.BluetoothSpp;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
@EActivity(R.layout.main_car)
public class MainCar extends BaseActivity {
    @ViewById
    LinearLayout none_panel, data_panel,bottom_panel;
    @ViewById
    TextView imei_code, time, is_online_state, t_4, battery_num, battery_ele, battery_tem;
    @ViewById
    ImageView top_image;
    @ViewById
    RadioGroup radioGroup;
    @ViewById
    RadioButton battery_volLow, battery_volMid, battery_volHig;
    @ViewById
    RelativeLayout rv_main_car_bluetooth_info;
    private String car_id = "";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @AfterViews
    void afterViews() {
        reSetMain();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.battery_volLow == i) {
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("vol",1);
                    message.setData(bundle);
                    BluetoothSpp.BluetoothSetVolHandler.sendMessage(message);
//                    EventBus.getDefault().post(new BluetoothEvent("",BluetoothEvent.BATTERTVOLL));
                } else if (R.id.battery_volMid == i) {
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("vol",2);
                    message.setData(bundle);
                    BluetoothSpp.BluetoothSetVolHandler.sendMessage(message);
//                    EventBus.getDefault().post(new BluetoothEvent("",BluetoothEvent.BATTERTVOLM));
                } else if (R.id.battery_volHig == i) {
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("vol",3);
                    message.setData(bundle);
                    BluetoothSpp.BluetoothSetVolHandler.sendMessage(message);
//                    EventBus.getDefault().post(new BluetoothEvent("",BluetoothEvent.BATTERTVOLH));
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //我的电动车页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MY_ELECTRIC_CAR);
            }
        }, 500);
    }

    @UiThread
    void reSetMain() {
        if (PubFunction.isConnect(activity, true)) {
            HttpMyCarInfo();
            progressDialog.show();
        }
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void panel_1() {
        if (PubFunction.isConnect(activity, true)) {
            Intent intent = new Intent(activity, MainCar_panel_1_.class);
            activity.startActivity(intent);
        }
    }

    @Click
    void panel_2() {
        //点击实时跟踪
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_MY_ELECTRIC_CAR_REAL_TIME_TRACKING);
        if (PubFunction.isConnect(activity, true)) {
            Intent intent = new Intent(activity, MainCar_panel_2_.class);
            intent.putExtra("car_id", car_id);
            activity.startActivity(intent);
        }
    }

    @Click
    void panel_3() {
        //点击轨迹回放
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_MY_ELECTRIC_CAR_TRACK_PLAYBACK);
        if (PubFunction.isConnect(activity, true)) {
            Intent intent = new Intent(activity, MainCar_panel_3_.class);
            intent.putExtra("car_id", car_id);
            activity.startActivity(intent);
        }
    }

    @Click
    void panel_4() {
        HttpGetData(PubFunction.car_ID, "2");
    }

    @Click
    void panel_5() {
        HttpGetData(PubFunction.car_ID, "1");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BluetoothEvent bluetoothEvent){
        String msg = bluetoothEvent.getMsg();
        if (BluetoothEvent.BATTERYNUM == bluetoothEvent.getEventMode()){
            Log.d("xiaoming98", "xiaoming98vent.BATTERYNUM");
            battery_num.setText("电池编号:"+msg);
            if (rv_main_car_bluetooth_info.getVisibility() == View.GONE) {
                rv_main_car_bluetooth_info.setVisibility(View.VISIBLE);
            }
        } else if (BluetoothEvent.BATTERTELE == bluetoothEvent.getEventMode()) {
            Log.d("xiaoming98", "onEvent: BluetoothEvent.BATTERTELE");
            battery_ele.setText(msg+"%");
            if (rv_main_car_bluetooth_info.getVisibility() == View.GONE) {
                rv_main_car_bluetooth_info.setVisibility(View.VISIBLE);
            }
        } else if (BluetoothEvent.BATTERTTEM == bluetoothEvent.getEventMode()) {
            battery_tem.setText(bluetoothEvent.getMsg()+"℃");
            if (rv_main_car_bluetooth_info.getVisibility() == View.GONE) {
                rv_main_car_bluetooth_info.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * http接口：Device/myEcars     从服务器获得绑定车的数据
     */
    @Background
    void HttpMyCarInfo() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "Device/myEcars", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpMyCarInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @SuppressLint("SetTextI18n")
    @UiThread
    void onDataHttpMyCarInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            none_panel.setVisibility(View.VISIBLE);
            data_panel.setVisibility(View.GONE);
            bottom_panel.setVisibility(View.INVISIBLE);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    if ("[]".equals(jsonObject_response.getString("data"))) {
                        none_panel.setVisibility(View.VISIBLE);
                        data_panel.setVisibility(View.GONE);
                        bottom_panel.setVisibility(View.INVISIBLE);
                    } else {
                        JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                        none_panel.setVisibility(View.GONE);
                        data_panel.setVisibility(View.VISIBLE);
                        bottom_panel.setVisibility(View.VISIBLE);
                        car_id = jsonObject.getString("sn");
                        imei_code.setText("设备ID：" + car_id);
                        String title = jsonObject.getString("title");
                        time.setText(title);
                        JSONArray jsonArray = jsonObject.getJSONArray("imgsUrl");
                        Glide.with(activity).load(jsonArray.getString(0)).into(top_image);
                    }
                } else {
                    MyToast.showTheToast(activity, msg);
                    none_panel.setVisibility(View.VISIBLE);
                    data_panel.setVisibility(View.GONE);
                    bottom_panel.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
                none_panel.setVisibility(View.VISIBLE);
                data_panel.setVisibility(View.GONE);
                bottom_panel.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * http接口: Device/unbindCar.html      解绑自己身上的车
     */
    @Background
    void HttpUnBindCar(String imei) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("sn", imei));
        new OkHttpConnect(activity, PubFunction.app + "Device/unbindCar.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpUnBindCar(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpUnBindCar(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * http接口:汽车信息
     */
    @Background
    void HttpGetData(String pid, String type) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("url", "carAction!sendCommand.do"));
        dataList.add(new ParamTypeData("gps", car_id));
        dataList.add(new ParamTypeData("uid", uid));
        if ("1".equals(type)) {
            dataList.add(new ParamTypeData("type", "1"));
            //断电
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commandType", "2");
                jsonObject.put("commandName", "1");
                jsonObject.put("commandValue", "0");
                dataList.add(new ParamTypeData("param", jsonObject.toString()));
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
            System.out.println("关关");
        }
        if ("2".equals(type)) {
            dataList.add(new ParamTypeData("type", "2"));
            //启动
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("commandType", "2");
                jsonObject.put("commandName", "1");
                jsonObject.put("commandValue", "1");
                dataList.add(new ParamTypeData("param", jsonObject.toString()));
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
            System.out.println("开开");
        }

        final String c_type = type;

        new OkHttpConnect(activity, PubFunction.app + "rpc/send.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetData(response, type, c_type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetData(String response, String type, String c_type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                System.out.println(jsonObject_response);
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    if (jsonObject_response.has("data")) {
                        JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                        if (jsonObject.has("res")) {
                            String msg = jsonObject.getString("res");
                            if ("true".equals(msg)) {
                                if ("2".equals(c_type)) {
                                    MyToast.showTheToast(activity, "下发启动指令成功");
                                } else if ("1".equals(c_type)) {
                                    MyToast.showTheToast(activity, "下发刹车指令成功");
                                }
                            } else {
                                if (jsonObject.has("desc")) {
                                    MyToast.showTheToast(activity, jsonObject.getString("desc"));
                                } else {
                                    MyToast.showTheToast(activity, "服务器错误：HttpGetData");
                                }
                            }
                        } else {
                            MyToast.showTheToast(activity, "服务器错误：HttpGetData");
                        }
                    } else {
                        MyToast.showTheToast(activity, "登录太频繁！");
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
}