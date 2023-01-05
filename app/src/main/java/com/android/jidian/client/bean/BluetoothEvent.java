package com.android.jidian.client.bean;

import android.bluetooth.BluetoothDevice;

/**
 * @author : xiaoming
 * date: 2020/9/8 下午3:39
 * company: 兴达智联
 * description:
 */
public class BluetoothEvent {
    public static final int BATTERYNUM = 1;
    public static final int BATTERTELE = 2;
    public static final int BATTERTTEM = 3;
    public static final int BATTERTVOLL = 4;
    public static final int BATTERTVOLM = 5;
    public static final int BATTERTVOLH = 6;
    public static final int RECEIVE_DATA = 7;
    public static final int CONNECT_FAILD = 8;
    public static final int BLUETOOTH_FOUND = 9;

    private int eventMode;
    private String msg;
    private BluetoothDevice device;

    public BluetoothEvent(String message,int eventMode) {
        this.msg = message;
        this.eventMode = eventMode;
    }

    public BluetoothEvent(BluetoothDevice device, int eventMode) {
        this.device = device;
        this.eventMode = eventMode;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getEventMode() {
        return eventMode;
    }

    public void setEventMode(int eventMode) {
        this.eventMode = eventMode;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
