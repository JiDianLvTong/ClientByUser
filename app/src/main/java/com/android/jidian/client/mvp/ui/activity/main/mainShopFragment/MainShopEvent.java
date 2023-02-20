package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;

/**
 * @author : xiaoming
 * date: 2023/2/20 17:56
 * description:
 */
public class MainShopEvent {
    public static int REFRESH = 1;

    public MainShopEvent(int event) {
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
