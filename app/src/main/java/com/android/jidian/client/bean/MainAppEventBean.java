package com.android.jidian.client.bean;

/**
 * @author : PTT
 * date: 2020/10/16 15:39
 * company: 兴达智联
 * description:
 */
public class MainAppEventBean {
    public static int PAYSUCCESS = 1;
    public static int PAYSUCCESSCLOSEORDER = 2;
    public static int PAYSUCCESSCLOSEORDERLIST = 3;
    public static int PAYSUCCESSCLOSEORDERLISTPAGE = 4;
    public static int PAYSUCCESSCLOSESHOP = 5;

    /**
     * 1:支付成功 关闭支付订单倒计时界面
     * 2:支付成功 关闭支付提交订单界面
     * 3:支付成功 关闭支付提交订单列表界面 订单倒计时界面 4:关闭订单列表界面
     * 5:支付成功 关闭商城界面
     */
    private int event;
    private String msg;

    public MainAppEventBean(int event) {
        this.event = event;
    }

    public MainAppEventBean(int event, String msg) {
        this.event = event;
        this.msg = msg;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
