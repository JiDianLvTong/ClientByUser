package com.android.jidian.repair.mvp.message.fragment;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:27
 * description:
 */
public class MessageFragmentEvent {

    public static int MESSAGE_ALL_READ = 1;

    public MessageFragmentEvent(int event) {
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
