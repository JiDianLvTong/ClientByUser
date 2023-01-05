package com.android.jidian.client.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author : xiaoming
 * date: 2020/11/20 上午10:07
 * company: 兴达智联
 * description:
 */
public class BluetoothConnectActivityReceiver extends BroadcastReceiver {
    String strPsw = "1234";

    @Override
    public void onReceive(Context context, Intent intent) {
//        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (btDevice.getName().equals("XD-0001")) {
//                int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
//
//                int pairingKey = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_KEY,
//                        BluetoothDevice.ERROR);
//                try {
//                    BluetoothSocket socket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//                    socket.connect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                // byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");
                // device.setPin(pinBytes);
//            btDevice.setPairingConfirmation(true);
                try {
                    Log.i("tag11111", "ddd  type ");
//                abortBroadcast();
//                    ClsUtils.createBond(btDevice.getClass(), btDevice);
//                    ClsUtils.setPin(btDevice.getClass(), btDevice, "1234"); // 手机和蓝牙采集器配对
//                    ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
                    //打开通知栏
//                MyToast.showTheToast(context,"请点击配对请求进行配对");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expandNotification(context);
                    }
                }, 1500);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("tag11111", "onReceive: " + e.getLocalizedMessage());
                }
            }
        }

//        String action = intent.getAction(); //得到action
//        Log.e("BluetoothConnectActivityReceiveraction1=", action);
//        BluetoothDevice btDevice=null;  //创建一个蓝牙device对象
//        // 从Intent中获取设备对象
//        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//        if(BluetoothDevice.ACTION_FOUND.equals(action)){  //发现设备
//            Log.e("BluetoothConnectActivityReceiver发现设备:", "["+btDevice.getName()+"]"+":"+btDevice.getAddress());
//
//            if(btDevice.getName().contains("XD-0001"))
//            {
//                if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {
//
//                    Log.e("BluetoothConnectActivityReceiverywq", "attemp to bond:"+"["+btDevice.getName()+"]");
//                    try {
//                        //通过工具类ClsUtils,调用createBond方法
//                        ClsUtils.createBond(btDevice.getClass(), btDevice);
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }else
//                Log.e("BluetoothConnectActivityReceivererror", "Is faild");
//        }else if(action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) //再次得到的action，会等于PAIRING_REQUEST
//        {
//            Log.e("BluetoothConnectActivityReceiveraction2=", action);
//            if(btDevice.getName().contains("XD-0001"))
//            {
//                Log.e("BluetoothConnectActivityReceiverhere", "OKOKOK");
//
//                try {
//
//                    //1.确认配对
////                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
//                    //2.终止有序广播
//                    Log.i("BluetoothConnectActivityReceiverorder...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
//                    ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
//                    //3.调用setPin方法进行配对...
//                    boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, "1234");
//                    if (ret){
//                        abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
//                        Log.d("BluetoothConnectActivityReceiversetPin", "onReceive:   success");
//                    }else {
//                        Log.d("BluetoothConnectActivityReceiversetPin", "onReceive:   fail");
//                    }
//
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    Log.i("BluetoothConnectActivityReceiverorder...", e.getLocalizedMessage());
//                    e.printStackTrace();
//                }
//            }else
//                Log.e("BluetoothConnectActivityReceiver提示信息", "这个设备不是目标蓝牙设备");
//
//        }


    }
//
    public static void expandNotification(Context context) {
        Object service = context.getSystemService("statusbar");
        if (null == service)
            return;
        try {
            Class<?> clazz = Class.forName("android.app.StatusBarManager");
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            Method expand = null;
            if (sdkVersion <= 16) {
                expand = clazz.getDeclaredMethod("expand");
            } else {
                /*
                 * Android SDK 16之后的版本展开通知栏有两个接口可以处理
                 * expandNotificationsPanel()
                 * expandSettingsPanel()
                 */
                //expand =clazz.getMethod("expandNotificationsPanel");
                expand = clazz.getDeclaredMethod("expandSettingsPanel");
            }
            expand.setAccessible(true);
            expand.invoke(service);
        } catch (Exception e) {
//       //e.printStackTrace();
        }
    }
}
