package com.android.jidian.client.mvp.ui.activity.main.mainFindFragment;

/**
 * @author : xiaoming
 * date: 2023/2/20 17:55
 * description:
 */
public class MainFindEvent {
    public static int REFRESH = 1;

    public MainFindEvent(int event) {
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
