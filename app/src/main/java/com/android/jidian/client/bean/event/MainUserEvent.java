package com.android.jidian.client.bean.event;

/**
 * @author : xiaoming
 * date: 2023/1/30 14:53
 * description:
 */
public class MainUserEvent {
    public static int COUPON_USE = 1;

    public MainUserEvent(int event) {
        this.event = event;
    }

    private int event;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
