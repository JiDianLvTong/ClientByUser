package com.android.jidian.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.mvp.ui.dialog.DialogByLoading;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.widgets.MyAlertDialog;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.service.BluetoothLeService;
import com.android.jidian.client.util.BuryingPointManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2017/6/6.
 */
@SuppressLint("Registered")
@EActivity(R.layout.main_bluetooth)
public class MainBlueTooth extends BaseActivity {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    // ??????????????????
    private static final long SCAN_PERIOD = 10000;
    private final static String TAG = MainBlueTooth.class.getSimpleName();
    //????????????
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static int show_update = -1;
    public static int show_download = -1;
    private boolean check_connect = true;
    private boolean need_interrupt = false;
    private boolean isCheckedVer = false;
    //    private byte[] bytes = send_coonect();
    //???????????????handler
    public static Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                show_update = 1;
            } else {
                show_update = 0;
            }
        }
    };
    //??????service,???????????????????????????
    private static BluetoothLeService mBluetoothLeService;
    //???????????????
    private static BluetoothGattCharacteristic target_chara = null;

    private int zibo = 0;

    private String download_url;
    @ViewById
    LinearLayout page_return, count_panel;
    @ViewById
    TextView send, coin_1, updata, download, wait_text, info,title;
    @ViewById
    TextView focusview;

//    private List<Map<String, String>> urldatalist = new ArrayList<>();

    // ???????????????
    BluetoothAdapter mBluetoothAdapter;

    DialogByLoading update_dialog;

    private int useable_time = -1;
    /**
     * @param @param rev_string(???????????????)
     * @return void
     * @throws
     * @Title: displayData
     * @Description: TODO(?????????????????????scrollview?????????)
     */
    int out_time_count = 30;
    String countString = "0";
    //    private int useable_time = -1;
    private Handler mHandler;
    //?????????????????????
    private int bp_cycles1;
    //????????????
    private String mDeviceName;
    //????????????
    private String mDeviceAddress;

    //    public static int show_update = -1;
    private String download_bin;
    private String site_version;
    private int blue_return_version;

    //???????????????handler
    public Handler downloadReturnHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyToast.showTheToast(activity, "???????????????????????????");
            if (msg.what == 1) {
                show_update = 1;
            } else {
                show_update = -1;
            }
        }
    };

    @Click
    void title() {
        HttpGetBinInfo("Charger/upgradeSeatCharger.html", "3");
    }

    /* BluetoothLeService????????????????????? */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            // ?????????????????????????????????
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    //    private String download_bin;
    private String use_life_time;
    //??????????????????
    private boolean mConnected = false;
    private String status = "disconnected";
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private DialogByLoading progressDialog;
    //    private DialogByLoading reprogressDialog;
    private Handler mhandler = new Handler();
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        // 2.????????????????????????
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // ?????????????????????
                case 1: {
                    // ??????View
                    wait_text.setVisibility(View.GONE);
//                    reprogressDialog.dismiss();
                    String state = msg.getData().getString("connect_state");
                    progressDialog = new DialogByLoading(activity);
                    progressDialog.setText(state);
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                            progressDialog.dismiss();
                        }
                    });
                    thread.start();
//                    MyToast.showTheToast(activity, state);
                    break;
                }
            }
        }
    };
    private Thread Life_frame;
    //???????????????????????????
    @SuppressLint("HandlerLeak")
    private Handler myHandler1 = new Handler() {
        // 2.????????????????????????
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Life_frame = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (check_connect && !need_interrupt) {
                            try {
                                Thread.sleep(1000);
                                if (target_chara != null && activity != null) {
                                    target_chara.setValue(bluetooth_life);
                                    if (mBluetoothLeService != null) {
                                        mBluetoothLeService.writeCharacteristic(target_chara);
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                Life_frame.start();
            }
        }
    };

    private int is_charge = 0;
    private int is_update = 0;
    private ArrayList<byte[]> dataList = null;
    /**
     * ??????????????????????????????BluetoothLeService??????????????????
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {//Gatt????????????
                mConnected = true;
                status = "???????????????";
                //??????????????????
                updateConnectionState(status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {//Gatt????????????
                mConnected = false;
                status = "???????????????";
                //??????????????????
                updateConnectionState(status);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //??????GATT?????????
                // Show all the supported services and characteristics on the
                // user interface.
                //?????????????????????????????????
                if (mBluetoothLeService != null) {
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //????????????
                //???????????????????????????
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    /**
     * ???????????????????????? ???????????????????????????????????????BluetoothDevice???????????????name MAC?????????
     **/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String device_name = "";
                    if (device.getName() != null) {
                        device_name = device.getName();
                    }
                    if (device_name.equals("HC-08")) {
                        scanLeDevice(false);
                        progressDialog.dismiss();
                        MyToast.showTheToast(activity, "?????????????????????");
                        String device_address = device.getAddress();
                        mDeviceAddress = device_address;
                        mDeviceName = device_name;
                        ConnectBlueTooth();
                    }
                }
            });
        }
    };

    /* ??????????????? */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @AfterViews
    void afterViews() {
        init();
//        HttpGetBinInfo();
        if (PubFunction.isConnect(this, true)) {
            HttpLifeUtimes();
        } else {
            MyToast.showTheToast(this, "?????????????????????????????????????????????????????????");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //?????????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CHARGER);
            }
        }, 500);
    }


    @Background
    void HttpGetBinInfo(String url, String h_version) {
        List<ParamTypeData> dataList = new ArrayList<>();
        if (!"-1".equals(h_version)) {
            dataList.add(new ParamTypeData("gen", h_version));
        }
        new OkHttpConnect(activity, PubFunction.app + url, dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGeBinInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }


    void onDataHttpGeBinInfo(String response, String type) {
        if (type.equals("0")) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if (status.equals("1")) {
                    JSONObject jsonArray = jsonObject_response.getJSONObject("data");
                    download_bin = jsonArray.getString("url");
                    site_version = jsonArray.getString("version");
                    download.setVisibility(View.VISIBLE);
                } else {
                    download.setVisibility(View.GONE);
                    MyToast.showTheToast(activity, msg);
                }
                isCheckedVer = true;
            } catch (Exception e) {
                System.out.println(e.toString());
//                MyToast.showTheToast(activity, "JSON???" + e.toString());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //?????????????????????
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
        //???????????????????????????
        check_connect = false;
        if (Life_frame != null) {
            Life_frame.interrupt();
        }

    }

    // Activity?????????????????????????????????????????????????????????????????????????????????
    @Override
    protected void onResume() {
        super.onResume();
        //?????????????????????
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            //?????????????????????????????????
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    private void init() {
        //????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        progressDialog = new DialogByLoading(activity);
        progressDialog.setText("??????????????????,?????????...");
        progressDialog.show();
        mHandler = new Handler();
        init_ble();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: init_ble
     * @Description: TODO(???????????????)
     */
    private void init_ble() {
        // ????????????????????????
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "?????????BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        // ????????????????????????????????????
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // ??????????????????
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            scanLeDevice(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "afterview: ??????????????????");
//                MyToast.showTheToast(activity,"??????????????????");
                scanLeDevice(true);
            } else {
//                MyToast.showTheToast(activity,"??????????????????");
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    /**
     * @param enable (???????????????true:????????????,false:????????????)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(??????????????????)
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("SCAN", "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    progressDialog.dismiss();
                    if (target_chara == null) {
                        MyToast.showTheToast(activity, "????????????????????????");
                    }
                }
            }, SCAN_PERIOD);
            /* ??????????????????????????????mLeScanCallback ???????????? */
            Log.i("SCAN", "begin.....................");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.i("Stop", "stoping................");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * ????????????
     **/
    private void ConnectBlueTooth() {
//        MyToast.showTheToast(activity, "??????????????????????????????...");
        /* ????????????service */
        wait_text.setVisibility(View.VISIBLE);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    /* ?????????????????? */
    private void updateConnectionState(String status) {
        Message msg = new Message();
        msg.what = 1;
        Bundle b = new Bundle();
        b.putString("connect_state", status);
        msg.setData(b);
        //????????????????????????UI???textview???
        myHandler.sendMessage(msg);
        //???????????????????????????
//        myHandler1.sendEmptyMessage(1);
        System.out.println("connect_state:" + status);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(??????????????????)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";
        // ????????????,???????????????????????????????????????
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
        // ??????????????????????????????????????????????????????????????????
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
        // ????????????????????????????????????
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            // ??????????????????
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            // ??????????????????uuid??????????????????????????????SampleGattAttributes???????????????????????????
            gattServiceData.add(currentServiceData);
            System.out.println("Service uuid:" + uuid);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            // ?????????????????????????????????????????????????????????
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();
            // Loops through available Characteristics.
            // ????????????????????????????????????????????????????????????
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString()
                        .equals(HEART_RATE_MEASUREMENT)) {
                    // ??????????????????Characteristic??????????????????mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService
                                    .readCharacteristic(gattCharacteristic);
                        }
                    }, 200);
                    // ??????Characteristic???????????????,???????????????????????????????????????mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(
                            gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // ??????????????????
                    // ???????????????????????????
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:"
                            + descriptor.getUuid());
                    // ????????????????????????
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // ?????????????????????????????????????????????????????????????????????
            mGattCharacteristics.add(charas);
            // ?????????????????????????????????????????????????????????
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    private byte[] bluetooth_life = new byte[]{(byte) 0xA5, (byte) 0x0A, (byte) 0x09, (byte) 0x09, (byte) 0x02, (byte) 0x55, (byte) 0x03, (byte) 0xEF, (byte) 0xAC, (byte) 0x5A};

    private void displayData(String returnStr) {
        //?????????????????????
        String str = returnStr;
        int count = str.length() / 2;
        String[] RX = new String[count];
        for (int i = 0; i < count; i++) {
            RX[i] = str.substring(i * 2, (i + 1) * 2);
        }
        System.out.println("bluetooth_return : " + returnStr);
        //??????????????????
        if (RX.length == 25) {
            if (RX[0].equals("A5") && RX[1].equals("19") && RX[2].equals("01")) {
                byte[] a = new byte[]{(byte) 0xA5, (byte) 0x0A, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x80, (byte) 0x03, (byte) 0x16, (byte) 0x9D, (byte) 0x5A};
                target_chara.setValue(a);
                mBluetoothLeService.writeCharacteristic(target_chara);
                System.out.println("???????????????");
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (is_charge == 0) {
                            if (target_chara == null) {
                                MyToast.showTheToast(activity, "?????????????????????");
                            } else {
                                progressDialog = new DialogByLoading(activity);
                                progressDialog.setText("????????????????????????????????????......");
                                progressDialog.setCanCancel(false);
                                progressDialog.show();
                                is_charge = 1;
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        while (out_time_count > 0) {
                                            out_time_count = out_time_count - 1;
                                            try {
                                                sleep(1000);
                                            } catch (InterruptedException e) {
                                                System.out.println(e.getLocalizedMessage());
                                            }
                                            if (out_time_count == 1) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }
                                };
                                thread.start();
                            }
                        } else {
                            MyToast.showTheToast(activity, "?????????????????????????????????????????????");
                        }
                    }
                });
                send.setBackgroundResource(R.drawable.button_corners_red_radius_5);
                send.setTextColor(Color.parseColor("#ffffff"));
            }
        }
        //?????????????????????
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("05") && RX[3].equals("00")) {
                if (is_charge == 1) {
                    //???????????????????????????0??????????????????????????????
                    if (countString.equals("0")) {
                        MyToast.showTheToast(activity, "???????????????????????????0?????????????????????");
                    } else {//??????????????????????????????countString???

                        int countString_int = Integer.parseInt(countString);
                        String countString_hex = Integer.toHexString(countString_int);
                        target_chara.setValue(send_count(countString_hex));
                        mBluetoothLeService.writeCharacteristic(target_chara);
                        progressDialog.dismiss();
                        is_charge = 0;
                        out_time_count = -1;
                    }
                }
            }
        }
        //?????????????????????
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("07")) {
                MyToast.showTheToast(activity, "????????????????????????");
                HttpLifeAffirm();
            }
        }
        //????????????
        // A5 0A 03 00 ?? 64 ?? ?? ?? ??
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("03") && RX[3].equals("00")) {
                if (!isCheckedVer){
                    HttpGetBinInfo("Charger/upgradeHandCharger.html", "-1");
                }
//                download.setVisibility(View.VISIBLE);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (download_bin != null) {
                            new DownLoadAndInstallBluetooth(activity, download_bin, "hlhdapp.bin", downloadReturnHandler);
                        } else {
                            MyToast.showTheToast(activity, "??????????????????????????????????????????");
                        }
                    }
                });
                String count_str = RX[5];
                int count_int = Integer.parseInt(count_str, 16);
                count_panel.setVisibility(View.VISIBLE);
                coin_1.setText(count_int + "");
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyToast.showTheToast(activity, "??????????????????????????????????????????????????????");
                    }
                });
                //??????????????????
                if (show_update == 1) {
                    download.setVisibility(View.GONE);
                    updata.setVisibility(View.VISIBLE);
                    //????????????
                    updata.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (is_update == 0) {
                                if (target_chara == null) {
                                    MyToast.showTheToast(activity, "?????????????????????");
                                } else {
                                    //                                updata.setVisibility(View.GONE);
                                    progressDialog = new DialogByLoading(activity);
                                    progressDialog.setText("?????????????????????????????????......");
                                    progressDialog.setCanCancel(false);
                                    progressDialog.show();
                                    try {
                                        Thread.sleep(1000);
                                        need_interrupt = true;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    is_update = 1;
                                }
                            } else {
                                MyToast.showTheToast(activity, "?????????????????????????????????????????????");
                            }
                        }
                    });
                    updata.setBackgroundResource(R.drawable.button_corners_red_radius_5);
                    if (is_update == 0) {

                    } else if (is_update == 1) {
                        is_update = 0;
                        String filePath = Environment.getExternalStorageDirectory() + "/Download/hlhdapp.bin";
                        File file = new File(filePath);
                        long filesize = file.length();
                        long a_s = filesize / (256);
                        long b_s = filesize % (256);
                        dataList = new ArrayList<>();
                        try {
                            FileInputStream is = new FileInputStream(file);
                            byte[] buffer = new byte[64];
                            int length = 0;
                            while ((length = is.read(buffer)) > 0) {

                                byte[] hexData = new byte[length];
                                for (int i = 0; i < length; i++) {
                                    hexData[i] = buffer[i];
                                }
                                dataList.add(hexData);
                                Arrays.fill(buffer, (byte) 0x00);
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getLocalizedMessage());
                        } catch (IOException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        byte[] a = send_filesize((int) a_s, (int) b_s);
                        System.out.println("bluetooth_send : " + bytesToHex(a));
                        target_chara.setValue(a);
                        mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                }
            } else if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("03") && RX[3].equals("02")) {
                updata.setVisibility(View.GONE);
                send.setBackgroundResource(R.drawable.button_corners_red_radius_5);

                //???????????????   ???????????????   ??????255
//                String b = RX[5]+RX[6];
//                int a_int = Integer.parseInt(b,16);
                String count_str = RX[5];
                final int count_int = Integer.parseInt(count_str, 16);
                count_panel.setVisibility(View.VISIBLE);
                coin_1.setText(count_int + "");


                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        LayoutInflater inflater = LayoutInflater.from(activity);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        final AlertDialog mAlertDialog = builder.create();
                        View view1 = inflater.inflate(R.layout.alertdialog_bluetooth, null);


                        TextView alertDialogTitle = (TextView) view1.findViewById(R.id.alertDialogTitle);
                        final EditText alert_text = (EditText) view1.findViewById(R.id.alert_text);

                        if (bp_cycles1 >= (255 - count_int)) {
                            useable_time = 255 - count_int;
                            alertDialogTitle.setText("????????????????????????????????????" + useable_time + "????????????????????????????????????:");
                        } else {
                            useable_time = bp_cycles1;
                            if (useable_time <= 0) {
                                useable_time = 0;
                            }
                            alertDialogTitle.setText("????????????????????????????????????" + useable_time + "????????????????????????????????????:");
                        }

//                        Integer.toHexString(Integer.parseInt(alert_text.getText().toString()));


                        TextView success_t = (TextView) view1.findViewById(R.id.success);
                        success_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (PubFunction.isConnect(activity, true)) {
                                    if (alert_text.getText().length() != 0 && alert_text.getText() != null) {
                                        if (Integer.parseInt(alert_text.getText().toString()) <= useable_time && alert_text.getText() != null && !alert_text.getText().toString().equals("0") && Integer.parseInt(alert_text.getText().toString()) != 0) {
                                            countString = alert_text.getText().toString();
                                            target_chara.setValue(send_count(Integer.toHexString(Integer.parseInt(alert_text.getText().toString()))));
                                            mBluetoothLeService.writeCharacteristic(target_chara);
                                            progressDialog.dismiss();
                                            is_charge = 0;
                                            out_time_count = -1;
                                            mAlertDialog.dismiss();
                                            InputMethodManager imm22222 = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            if (null == imm22222)
                                                return;
                                            if (getCurrentFocus() != null) {

                                                //???????????????
                                                imm22222.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                            } else {
                                                //???????????????
                                                imm22222.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                            }
                                        } else if (Integer.parseInt(alert_text.getText().toString()) == 0 || alert_text.getText().length() == 0 || Integer.parseInt(alert_text.getText().toString()) == 0) {
                                            MyToast.showTheToast(activity, "?????????????????????");
                                        } else if (Integer.parseInt(alert_text.getText().toString()) > (255 - count_int)) {
                                            MyToast.showTheToast(activity, "????????????????????????");
                                        } else {
                                            MyToast.showTheToast(activity, "????????????????????????");
                                        }
                                    } else if (alert_text.getText().length() == 0) {
                                        MyToast.showTheToast(activity, "?????????????????????");
                                    }
                                }
                            }
                        });
                        TextView error_t = (TextView) view1.findViewById(R.id.error);
                        error_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                                InputMethodManager imm11111 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                                if (null == imm11111)
                                    return;
                                if (getCurrentFocus() != null) {
                                    //???????????????
                                    imm11111.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                } else {
                                    //???????????????
                                    imm11111.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            }

                        });

                        mAlertDialog.setCancelable(false);
                        mAlertDialog.show();
                        mAlertDialog.getWindow().setContentView(view1);
                        //????????????????????? alertdialog??????????????????
                        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    }
                });
            } else if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("03") && RX[3].equals("01")) {
                updata.setVisibility(View.GONE);
            }
        }
        //???????????? ???????????? ????????????06
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("09") && RX[3].equals("00")) {
                need_interrupt = false;
                MyToast.showTheToast(activity, "???????????????????????????");
                updata.setBackgroundResource(R.drawable.button_corners_red_radius_5);
                updata.setClickable(true);
            }
        }

        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("11") && RX[3].equals("00")) {
                need_interrupt = false;
                MyToast.showTheToast(activity, "?????????????????????");
                updata.setVisibility(View.GONE);
            }
        }

        //????????????02?????? ??????2000???
        if (RX.length == 11) {
            if (RX[0].equals("A5") && RX[1].equals("0B") && RX[2].equals("03") && RX[3].equals("01") && RX[4].equals("02")) {
                String b = RX[5] + RX[6];
                int a_int = Integer.parseInt(b, 16);
                count_panel.setVisibility(View.VISIBLE);
                coin_1.setText(a_int + "");
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyToast.showTheToast(activity, "??????????????????????????????????????????????????????");
                    }
                });

                //????????????
                updata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (is_update == 0) {
                            if (target_chara == null) {
                                MyToast.showTheToast(activity, "?????????????????????");
                            } else {
                                progressDialog = new DialogByLoading(activity);
                                progressDialog.setText("?????????????????????????????????......");
                                progressDialog.setCanCancel(false);
                                progressDialog.show();
                                try {
                                    Thread.sleep(1000);
                                    need_interrupt = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                is_update = 1;
                            }
                        } else {
                            MyToast.showTheToast(activity, "?????????????????????????????????????????????");
                        }
                    }
                });
                updata.setVisibility(View.GONE);
                if (is_update == 0) {
                } else if (is_update == 1) {
                    is_update = 0;
                    String filePath = Environment.getExternalStorageDirectory() + "/jidian/app.bin";
                    File file = new File(filePath);
                    long filesize = file.length();
                    long a_s = filesize / (256);
                    long b_s = filesize % (256);
                    dataList = new ArrayList<>();
                    try {
                        FileInputStream is = new FileInputStream(file);
                        byte[] buffer = new byte[64];
                        int length = 0;
                        while ((length = is.read(buffer)) > 0) {

                            byte[] hexData = new byte[length];
                            for (int i = 0; i < length; i++) {
                                hexData[i] = buffer[i];
                            }
                            dataList.add(hexData);
                            Arrays.fill(buffer, (byte) 0x00);
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getLocalizedMessage());
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    byte[] a = send_filesize((int) a_s, (int) b_s);
                    System.out.println("bluetooth_send : " + bytesToHex(a));
                    target_chara.setValue(a);
                    mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        }

        //???????????????????????????bin????????????09???0B????????????06??????50ms-200ms????????????
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("0B") && RX[3].equals("00")) {
                updata.setBackgroundResource(R.drawable.button_corners_gray_radius_5);
//                updata.setClickable(false);

                updata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyToast.showTheToast(activity, "?????????????????????????????????...");
                    }
                });
            }
        }

        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("0F") && RX[3].equals("00")) {
                MyToast.showTheToast(activity, "?????????????????????");
                updata.setBackgroundResource(R.drawable.button_corners_red_radius_5);
                updata.setClickable(true);
            }
        }


        //??????0B??????50ms-150ms??????????????????????????????????????????????????????
        if (RX.length == 10) {
            if (RX[0].equals("A5") && RX[1].equals("0A") && RX[2].equals("0D") && RX[3].equals("02")) {
                final String c_h = RX[4];
                final String c_l = RX[5];
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        byte[] a = send_filedata(c_h, c_l);
                        System.out.println("bluetooth_send : " + bytesToHex(a));
                        int item_order = (a.length / 20) + 1;
                        for (int i = 0; i < item_order; i++) {
                            if (i == item_order - 1) {
                                byte[] item_a = new byte[14];
                                for (int j = 0; j < item_a.length; j++) {
                                    item_a[j] = a[(i * 20) + j];
                                }
                                target_chara.setValue(item_a);
                                mBluetoothLeService.writeCharacteristic(target_chara);
                            } else {
                                byte[] item_a = new byte[20];
                                for (int j = 0; j < item_a.length; j++) {
                                    item_a[j] = a[(i * 20) + j];
                                }
                                target_chara.setValue(item_a);
                                mBluetoothLeService.writeCharacteristic(target_chara);
                            }
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        }
                    }
                };
                thread.start();
            }
        }


        /**
         * ?????????????????????
         * */
        // A5 14 32 32 02 4F 00 00 F5 E3 00 00 00 00 01 03 03 E3 BA 5A
        if (RX.length == 20) {
            if (RX[0].equals("A5") && RX[1].equals("14") && RX[2].equals("32") && RX[3].equals("32")) {

                String soc = RX[5];
                String dianya = RX[6] + RX[7] + RX[8] + RX[9];
                String dianliu = RX[10] + RX[11] + RX[12] + RX[13];
                String s_version = RX[14];
                String h_version = RX[15];

                String text = "?????????" + Integer.parseInt(soc, 16) + "    ?????????" + Integer.parseInt(dianya, 16) + "    ?????????" + Integer.parseInt(dianliu, 16) + "    ???????????????" + Integer.parseInt(s_version, 16) + "    ???????????????" + Integer.parseInt(h_version, 16);
                info.setText(text);
                blue_return_version = Integer.parseInt(s_version, 16);
                if (site_version != null) {
                    if (blue_return_version < Integer.parseInt(site_version)) {
                        show_download = 1;
                    } else {
                        show_download = 2;
                    }
                }
                if (4 == Integer.parseInt(h_version, 16)) {
                    if (!isCheckedVer) {
                        HttpGetBinInfo("Charger/upgradeSeatCharger.html", "4");
                    }
                } else {
                    if (!isCheckedVer) {
                        HttpGetBinInfo("Charger/upgradeSeatCharger.html", "3");
                    }
                }
                myHandler1.sendEmptyMessage(1);
            }
        }

        //????????????
        //??????
        // A5 0A 03 00 ?? 64 ?? ?? ?? ??
        if (RX.length == 11) {
            if (RX[0].equals("A5") && RX[1].equals("0B") && RX[2].equals("03") && RX[3].equals("00")) {
                info.setVisibility(View.VISIBLE);

                //??????????????????
                String count_str = RX[5] + RX[6];
                final int count_int = Integer.parseInt(count_str, 16);
                count_panel.setVisibility(View.VISIBLE);
                coin_1.setText(count_int + "");

                //????????????
                if (show_download == 1) {
                    if (show_update == 1) {
                        download.setVisibility(View.GONE);
                        updata.setVisibility(View.VISIBLE);
                        //????????????
                        updata.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (is_update == 0) {
                                    if (target_chara == null) {
                                        MyToast.showTheToast(activity, "?????????????????????");
                                    } else {
                                        //                                updata.setVisibility(View.GONE);
                                        progressDialog = new DialogByLoading(activity);
                                        progressDialog.setText("?????????????????????????????????......");
                                        progressDialog.setCanCancel(false);
                                        progressDialog.show();
                                        try {
                                            Thread.sleep(1000);
                                            need_interrupt = true;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        is_update = 1;
                                    }
                                } else {
                                    MyToast.showTheToast(activity, "?????????????????????????????????????????????");
                                }
                            }
                        });
                        updata.setBackgroundResource(R.drawable.button_corners_red_radius_5);

                        if (is_update == 0) {

                        } else if (is_update == 1) {
                            is_update = 0;
                            String filePath = Environment.getExternalStorageDirectory() + "/Download/hlhdapp.bin";
                            File file = new File(filePath);
                            long filesize = file.length();
                            long a_s = filesize / (256);
                            long b_s = filesize % (256);
                            dataList = new ArrayList<>();
                            try {
                                FileInputStream is = new FileInputStream(file);
                                byte[] buffer = new byte[64];
                                int length = 0;
                                while ((length = is.read(buffer)) > 0) {

                                    byte[] hexData = new byte[length];
                                    for (int i = 0; i < length; i++) {
                                        hexData[i] = buffer[i];
                                    }
                                    dataList.add(hexData);
                                    Arrays.fill(buffer, (byte) 0x00);
                                }
                            } catch (FileNotFoundException e) {
                                System.out.println(e.getLocalizedMessage());
                            } catch (IOException e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                            byte[] a = send_filesize((int) a_s, (int) b_s);
                            zibo++;
                            System.out.println("bluetooth_send : " + zibo + "        " + bytesToHex(a));
                            target_chara.setValue(a);
                            mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        download.setVisibility(View.VISIBLE);
                        //                    download_bin = urldatalist.get(1).get("url");
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DownLoadAndInstallBluetooth(activity, download_bin, "hlhdapp.bin", downloadReturnHandler);
                            }
                        });
                    }
                } else if (show_download == 2) {
                    download.setVisibility(View.GONE);
                }

                //????????????????????????
                send.setBackgroundResource(R.drawable.button_corners_red_radius_5);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        need_interrupt = true;
                        MyAlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        final AlertDialog mAlertDialog = builder.create();
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        View view1 = inflater.inflate(R.layout.alertdialog_bluetooth, null);
                        TextView alertDialogTitle = (TextView) view1.findViewById(R.id.alertDialogTitle);
                        final EditText alert_text = (EditText) view1.findViewById(R.id.alert_text);
                        if (bp_cycles1 >= (255 - count_int)) {
                            useable_time = 255 - count_int;
                            alertDialogTitle.setText("????????????????????????????????????" + useable_time + "????????????????????????????????????:");
                        } else {
                            useable_time = bp_cycles1;
                            if (useable_time <= 0) {
                                useable_time = 0;
                            }
                            alertDialogTitle.setText("????????????????????????????????????" + useable_time + "????????????????????????????????????:");
                        }

                        TextView success_t = (TextView) view1.findViewById(R.id.success);
                        success_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (PubFunction.isConnect(activity, true)) {
                                    if (alert_text.getText().length() != 0 && alert_text.getText() != null) {
                                        if (Integer.parseInt(alert_text.getText().toString()) <= useable_time && alert_text.getText() != null && !alert_text.getText().toString().equals("0") && Integer.parseInt(alert_text.getText().toString()) != 0) {
                                            countString = alert_text.getText().toString();
                                            target_chara.setValue(send_count_double(Integer.toHexString(Integer.parseInt(alert_text.getText().toString()))));
                                            mBluetoothLeService.writeCharacteristic(target_chara);
                                            progressDialog.dismiss();
                                            is_charge = 0;
                                            out_time_count = -1;
                                            mAlertDialog.dismiss();
                                            InputMethodManager imm22222 = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            if (null == imm22222)
                                                return;
                                            if (getCurrentFocus() != null) {

                                                //???????????????
                                                imm22222.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                            } else {
                                                //???????????????
                                                imm22222.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                            }
                                        } else if (Integer.parseInt(alert_text.getText().toString()) == 0 || alert_text.getText().length() == 0 || Integer.parseInt(alert_text.getText().toString()) == 0) {
                                            MyToast.showTheToast(activity, "?????????????????????");
                                        } else if (Integer.parseInt(alert_text.getText().toString()) > (255 - count_int)) {
                                            MyToast.showTheToast(activity, "????????????????????????");
                                        } else {
                                            MyToast.showTheToast(activity, "????????????????????????");
                                        }
                                    } else if (alert_text.getText().length() == 0) {
                                        MyToast.showTheToast(activity, "?????????????????????");
                                    }
                                }
                            }
                        });

                        TextView error_t = (TextView) view1.findViewById(R.id.error);
                        error_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                focusview.requestFocus();
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            sleep(300);
                                        } catch (InterruptedException e) {
                                            System.out.println(e.getLocalizedMessage());
                                        }
                                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputMethodManager.hideSoftInputFromWindow(focusview.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                };
                                thread.start();
                                mAlertDialog.dismiss();
                            }
                        });
                        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                try {
                                    Thread.sleep(1000);
                                    need_interrupt = false;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        mAlertDialog.setCancelable(false);
                        mAlertDialog.show();
                        mAlertDialog.getWindow().setContentView(view1);
                        //????????????????????? alertdialog??????????????????
                        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    }
                });
            }
        }
    }

    @Click
    void send() {
        //?????????????????????
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_CHARGER_SETTINGS);
        if (is_charge == 0) {
            if (target_chara == null) {
                MyToast.showTheToast(activity, "?????????????????????");
            } else {
                MyToast.showTheToast(activity, "?????????????????????????????????...");
            }
        } else {
            MyToast.showTheToast(activity, "?????????????????????????????????????????????");
        }
    }

    @Click
    void updata() {
        if (is_charge == 0) {
            if (target_chara == null) {
                MyToast.showTheToast(activity, "?????????????????????");
            } else {
                MyToast.showTheToast(activity, "?????????????????????????????????...");
            }
        } else {
            MyToast.showTheToast(activity, "??????????????????????????????????????????");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    @Click
    void page_return() {
        this.finish();
    }

    //A5 0A 04 00 02 64 03 XX XX 5A
    private byte[] send_count(String count) {
        byte[] message_b = new byte[]{0, 0, 0, 0, 0, 0, 0};
        message_b[0] = (byte) 0xA5;
        message_b[1] = (byte) 0x0A;
        message_b[2] = (byte) 0x04;
        message_b[3] = (byte) 0x00;
        message_b[4] = (byte) 0x02;
        message_b[5] = (byte) Integer.parseInt(count, 16);
        message_b[6] = (byte) 0x03;
        String str = Util.getCRC(message_b);
        while (true) {
            if (str.length() < 4) {
                str = "0" + str;
            } else {
                break;
            }
        }
        String str_06 = str.substring(0, 2);
        String str_07 = str.substring(2, 4);
        byte[] message_c = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        message_c[0] = (byte) 0xA5;
        message_c[1] = (byte) 0x0A;
        message_c[2] = (byte) 0x04;
        message_c[3] = (byte) 0x00;
        message_c[4] = (byte) 0x02;
        message_c[5] = (byte) Integer.parseInt(count, 16);
        message_c[6] = (byte) 0x03;
        message_c[7] = (byte) Integer.parseInt(str_07, 16);
        message_c[8] = (byte) Integer.parseInt(str_06, 16);
        message_c[9] = (byte) 0x5A;
        return message_c;
    }

    //A5 0A 04 00 02 64 03 XX XX 5A
    private byte[] send_count_double(String count) {

        int count_l = 4 - count.length();
        for (int i = 0; i < count_l; i++) {
            count = "0" + count;
        }

        String a = count.substring(0, 2);
        String b = count.substring(2, 4);

        byte[] message_b = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        message_b[0] = (byte) 0xA5;
        message_b[1] = (byte) 0x0B;
        message_b[2] = (byte) 0x04;
        message_b[3] = (byte) 0x00;
        message_b[4] = (byte) 0x02;
        message_b[5] = (byte) Integer.parseInt(a, 16);
        message_b[6] = (byte) Integer.parseInt(b, 16);
        message_b[7] = (byte) 0x03;
        String str = Util.getCRC(message_b);
        while (true) {
            if (str.length() < 4) {
                str = "0" + str;
            } else {
                break;
            }
        }
        String str_06 = str.substring(0, 2);
        String str_07 = str.substring(2, 4);
        byte[] message_c = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        message_c[0] = (byte) 0xA5;
        message_c[1] = (byte) 0x0B;
        message_c[2] = (byte) 0x04;
        message_c[3] = (byte) 0x00;
        message_c[4] = (byte) 0x02;
        message_c[5] = (byte) Integer.parseInt(a, 16);
        message_c[6] = (byte) Integer.parseInt(b, 16);
        message_c[7] = (byte) 0x03;
        message_c[8] = (byte) Integer.parseInt(str_07, 16);
        message_c[9] = (byte) Integer.parseInt(str_06, 16);
        message_c[10] = (byte) 0x5A;

        return message_c;
    }

    private byte[] send_filesize(int size_h, int size_l) {
        byte[] message_b = new byte[]{0, 0, 0, 0, 0, 0, 0};
        message_b[0] = (byte) 0xA5;
        message_b[1] = (byte) 0x0A;
        message_b[2] = (byte) 0x06;
        message_b[3] = (byte) 0x02;
        message_b[4] = (byte) size_h;
        message_b[5] = (byte) size_l;
        message_b[6] = (byte) 0x03;
        String str = Util.getCRC(message_b);
        while (true) {
            if (str.length() < 4) {
                str = "0" + str;
            } else {
                break;
            }
        }
        String str_06 = str.substring(0, 2);
        String str_07 = str.substring(2, 4);
        byte[] message_c = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        message_c[0] = (byte) 0xA5;
        message_c[1] = (byte) 0x0A;
        message_c[2] = (byte) 0x06;
        message_c[3] = (byte) 0x02;
        message_c[4] = (byte) size_h;
        message_c[5] = (byte) size_l;
        message_c[6] = (byte) 0x03;
        message_c[7] = (byte) Integer.parseInt(str_07, 16);
        message_c[8] = (byte) Integer.parseInt(str_06, 16);
        message_c[9] = (byte) 0x5A;
        return message_c;
    }

    private byte[] send_filedata(String count_h, String count_l) {
        //????????????????????????
        String count_str = count_h + count_l;
        int count_int = Integer.parseInt(count_str, 16);
        //??????????????????
        byte[] byteData = dataList.get((count_int / 64));
        //????????????????????????
        int dataSize = byteData.length;
        zibo++;
        System.out.println("bluetooth_send :" + zibo + "             " + bytesToHex(byteData) + "     " + dataSize);
        byte[] message_b = new byte[71];
        Arrays.fill(message_b, (byte) 0xff);
        message_b[0] = (byte) 0xa5;
        message_b[1] = (byte) dataSize;
        message_b[2] = (byte) 0x08;
        message_b[3] = (byte) Integer.parseInt(count_h, 16);
        message_b[4] = (byte) Integer.parseInt(count_l, 16);
        message_b[5] = (byte) 0x02;
        for (int i = 0; i < dataSize; i++) {
            message_b[6 + i] = byteData[i];
        }
        message_b[70] = (byte) 0x03;
        String str = Util.getCRC(message_b);
        while (true) {
            if (str.length() < 4) {
                str = "0" + str;
            } else {
                break;
            }
        }
        String str_06 = str.substring(0, 2);
        String str_07 = str.substring(2, 4);
        byte[] message_c = new byte[74];
        Arrays.fill(message_c, (byte) 0xff);
        message_c[0] = (byte) 0xa5;
        message_c[1] = (byte) dataSize;
        message_c[2] = (byte) 0x08;
        message_c[3] = (byte) Integer.parseInt(count_h, 16);
        message_c[4] = (byte) Integer.parseInt(count_l, 16);
        message_c[5] = (byte) 0x02;
        for (int i = 0; i < dataSize; i++) {
            message_c[6 + i] = byteData[i];
        }
        message_c[70] = (byte) 0x03;
        message_c[71] = (byte) Integer.parseInt(str_07, 16);
        message_c[72] = (byte) Integer.parseInt(str_06, 16);
        message_c[73] = (byte) 0x5A;
        return message_c;
    }

    /**
     * http?????????https://app.halouhuandian.com/Life/utimes.html   ?????????????????????
     */
    @Background
    void HttpLifeUtimes() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "Life/utimes.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpLifeUtimes(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpLifeUtimes(String response, String type) {
        if (type.equals("0")) {
            MyToast.showTheToast(this, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("msg");
                if (status.equals("1")) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
//                    try {
                    String ss = dataObject.getString("bp_cycles");
                    bp_cycles1 = Integer.parseInt(ss);
//                    }catch (NumberFormatException e){
//                        bp_cycles1 = 0;
//                    }
                    String bp_cycles = dataObject.getString("bp_cycles");
                    int count = Integer.parseInt(bp_cycles);
                    //??????uid????????????????????????100??????????????????????????????100???
//                    countString = use_life_time;
                    if (count >= 100) {
                        countString = "100";
                    } else if (count > 0 && count < 100) {
                        countString = count + "";
                    } else {
                        countString = "0";
                    }
                } else if (status.equals("0")) {
                    bp_cycles1 = 0;
                } else {
                    MyToast.showTheToast(this, msg);
                }
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    /**
     * http?????????https://app.halouhuandian.com/Life/affirm.html   ?????????????????????
     */
    @Background
    void HttpLifeAffirm() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("times", countString));
        new OkHttpConnect(activity, PubFunction.app + "Life/affirm.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpLifeAffirm(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    private void onDataHttpLifeAffirm(String response, String type) {
        System.out.println(response);
    }

}