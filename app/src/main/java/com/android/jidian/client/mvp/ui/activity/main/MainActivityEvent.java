package com.android.jidian.client.mvp.ui.activity.main;

/**
 * @author : xiaoming
 * date: 2023/2/9 15:25
 * description:
 */
public class MainActivityEvent {
    public static int CHANGEMAIN = 1;

    public MainActivityEvent(int event, int index) {
        this.event = event;
        this.index = index;
    }

    private int event;
    private int index;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
