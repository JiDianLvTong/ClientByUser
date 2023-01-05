/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.jidian.client.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.jidian.client.bean.BluetoothEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.jidian.client.bean.BluetoothEvent.RECEIVE_DATA;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class BluetoothMain {

    private Activity activity;

    public BluetoothMain(Activity activity) {
        this.activity = activity;
        init();
    }

    private static final String TAG = "BluetoothMain";
    private BluetoothAdapter mBluetoothAdapter;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final long SCAN_PERIOD = 20000;
    private Handler mHandler;
    BluetoothDevice mdevice;
    private BluetoothChatService mChatService = null;
    public static Handler reScanHandler;

    private void init() {
        mHandler = new Handler();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (null == mBluetoothAdapter) {
            Dialog alertDialog = new AlertDialog.Builder(activity).
                    setMessage("This device do not support Bluetooth").
                    create();
            alertDialog.show();
        }
        reScanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ScanLeDevice(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScanLeDevice(true);
                    }
                }, 2000);
            }
        };
        ScanLeDevice(true);
    }

    /**
     * @param enable (扫描使能，true:扫描开始,false:扫描停止)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(扫描蓝牙设备)
     */
    private void ScanLeDevice(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "stop.....................");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//					progressDialog.dismiss();
//					if (target_chara == null) {
//						MyToast.showTheToast(activity, "未发现充电器设备");
//					}
                }
            }, SCAN_PERIOD);
            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
            Log.d(TAG, "begin.....................");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.d(TAG, "stoping................");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    Runnable mStopLeScan = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "le delay time reached");
            // Stop le scan, delay SCAN_PERIOD ms
            ScanLeDevice(false);
        }
    };

    //    BluetoothDevice mDevice;
    @SuppressLint("NewApi")
    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            // TODO Auto-generated method stub
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
            // TODO Auto-generated method stub
            // add the device to the adaper
            Log.d(TAG, "find a device, name is " + device.getName()
                    + ", addr is " + device.getAddress()
                    + "rssi is " + String.valueOf(rssi));
//                    try {
//                        Log.e(TAG, device.getName() + "    " + getHexString(scanRecord, 0, scanRecord.length));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
            if (device.getName() != null) {
                if (device.getName().equals("XD-0001")) {
                    Log.d(TAG, "run: device.getName() " + device.getName());
//                            mDevice = device;
                    mdevice = device;
                    mStopLeScan.run();
                    Toast.makeText(activity, "发现蓝牙", Toast.LENGTH_LONG).show();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Try_Connect();
//                        }
//                    }, 500);

                    Timer delayStart = new Timer();
                    delayStart.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Try_Connect();

                        }
                    }, 1000);

                    if (mChatService == null) setupChat();
                }
            }
//                }
//            });
        }
    };
    static final char[] HEX_CHAR_TABLE = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };

    public static String getHexString(byte[] raw, int offset, int count)
            throws UnsupportedEncodingException {
        StringBuffer hex = new StringBuffer();
        for (int i = offset; i < offset + count; i++) {
            int v = raw[i] & 0xFF;
            hex.append(HEX_CHAR_TABLE[v >>> 4]);
            hex.append(HEX_CHAR_TABLE[v & 0xF]);
            hex.append(" ");
        }
        return hex.toString();
    }

    void Try_Connect() {
        if (mdevice == null) return;
        if (mChatService == null) return;
        Log.d(TAG, "Try_Connect: mChatService.getState()" + mChatService.getState());
        if (mChatService.getState() == 3) {
            mChatService.stop();
            return;
        }
        mChatService.connect(mdevice);
    }

    private void setupChat() {
        Log.d(TAG, "setupConnect()");
        mChatService = new BluetoothChatService(activity, mChatHandler);
    }

    private int bluttooth_num = 0;

    private final Handler mChatHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1 + "  " + msg.arg2);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.d(TAG, "handleMessage: STATE_CONNECTED");
                            Message message = new Message();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putInt("vol", 4);
                            message.setData(bundle);
                            BluetoothSetVolHandler.sendMessage(message);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.d(TAG, "handleMessage: STATE_CONNECTING");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            Log.d(TAG, "handleMessage: STATE_LISTEN");
//                            EventBus.getDefault().post(new BluetoothEvent("", CONNECT_FAILD));
                            break;
                        case BluetoothChatService.STATE_NONE:
                            Log.d(TAG, "handleMessage: STATE_NONE");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    Log.d(TAG, "handleMessage: MESSAGE_WRITE");
                    break;
                case MESSAGE_READ:
                    if (bluttooth_num < 10) {
                        bluttooth_num++;
                    } else {
                        bluttooth_num = 0;
                    }
                    byte[] readBuf = (byte[]) msg.obj;

                    try {
                        Log.d(TAG, "handleMessage: " + getHexString(readBuf, 0, msg.arg1));
                        EventBus.getDefault().post(new BluetoothEvent(getHexString(readBuf, 0, msg.arg1), RECEIVE_DATA));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, "handleMessage: " + e.getLocalizedMessage());
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "handleMessage: MESSAGE_DEVICE_NAME");
                    break;
                case MESSAGE_TOAST:
                    Log.d(TAG, "handleMessage: MESSAGE_TOAST" + msg.getData().getString("toast"));
                    if (msg.getData().getString("toast").equals("连接失败")) {
//                        if (BluetoothDeviceList.reScanHandler != null) {
//                            BluetoothDeviceList.reScanHandler.sendEmptyMessage(1);
//                        }
                        Try_Connect();
                    }
                    break;
                case 100:
                    Log.d(TAG, "handleMessage: STATE_CONNECTED 100");
                    break;
                case 101:
                    Log.d(TAG, "handleMessage: STATE_CONNECTED 101");
                    break;
            }
        }
    };

    private Handler BluetoothSetVolHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int i = msg.getData().getInt("vol");
                byte[] send = null;

                byte[] vol_v1 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xE1, (byte) 0xFE, (byte) 0x01, (byte) 0x0D, (byte) 0x0A};
                byte[] vol_v9 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xE9, (byte) 0x06, (byte) 0x02, (byte) 0x0D, (byte) 0x0A};
                byte[] vol_v15 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xEF, (byte) 0x0C, (byte) 0x02, (byte) 0x0D, (byte) 0x0A};
                byte[] state_connect = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFA, (byte) 0xE1, (byte) 0xFA, (byte) 0x01, (byte) 0x0D, (byte) 0x0A};
                if (i == 1) {
                    send = vol_v1;
                } else if (i == 2) {
                    send = vol_v9;
                } else if (i == 3) {
                    send = vol_v15;
                } else if (i == 4) {
                    send = state_connect;
                }
                if (mChatService != null) {
                    byte[] finalSend = send;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatService.write(finalSend);
                            Log.d(TAG, "handleMessage:mChatService write " + bytesToHex(finalSend));
                        }
                    }, 500);

                }
            } else if (msg.what == 2) {
                int i = msg.getData().getInt("vol");
                if (i == 5) {
                    if (mChatService == null) return;
                    if (mChatService.getState() == 3) {
                        mChatService.stop();
                        return;
                    }
                    mChatService.connect(mdevice);
                }
            }
        }
    };

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
