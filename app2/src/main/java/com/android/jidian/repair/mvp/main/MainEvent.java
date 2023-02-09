package com.android.jidian.repair.mvp.main;

import com.luck.picture.lib.entity.LocalMedia;

/**
 * @author : xiaoming
 * date: 2023/2/8 10:43
 * description:
 */
public class MainEvent {
    public static int RECEIVE_LONG_LINK = 1;
    private int event;
    private String data;

    public MainEvent(int event, String data) {
        this.event = event;
        this.data = data;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
