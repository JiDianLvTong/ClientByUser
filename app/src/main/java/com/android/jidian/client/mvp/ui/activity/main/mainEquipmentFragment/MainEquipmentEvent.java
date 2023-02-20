package com.android.jidian.client.mvp.ui.activity.main.mainEquipmentFragment;

/**
 * @author : xiaoming
 * date: 2023/2/20 17:52
 * description:
 */
public class MainEquipmentEvent {
    public static int REFRESH = 1;

    public MainEquipmentEvent(int event) {
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
