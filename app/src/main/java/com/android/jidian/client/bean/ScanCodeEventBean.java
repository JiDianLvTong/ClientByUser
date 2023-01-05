package com.android.jidian.client.bean;

/**
 * @author : PTT
 * date: 2020/12/8 下午5:33
 * company: 兴达智联
 * description:
 */
public class ScanCodeEventBean {
    public static final int REFRESH_E_BIKE = 1;
    public static final int REFRESH_DISCOUNT = 2;

    private int eventMode;
    private String msg;

    public ScanCodeEventBean(int eventMode) {
        this.eventMode = eventMode;
    }

    public ScanCodeEventBean(String message, int eventMode) {
        this.msg = message;
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
}