package com.android.jidian.client.mvp.ui.activity.main.mainUserFragment;

/**
 * @author : xiaoming
 * date: 2023/2/20 18:00
 * description:
 */
public class MainUserEvent {
    public static int REFRESH = 1;

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
