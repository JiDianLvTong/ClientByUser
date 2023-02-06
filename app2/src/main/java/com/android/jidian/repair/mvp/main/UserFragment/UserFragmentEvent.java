package com.android.jidian.repair.mvp.main.UserFragment;

/**
 * @author : xiaoming
 * date: 2023/2/3 14:23
 * description:
 */
public class UserFragmentEvent {
    public static int LOCATION_SUCCESS = 1;

    public UserFragmentEvent(int event, String lng, String lat) {
        this.event = event;
        this.lng = lng;
        this.lat = lat;
    }

    private int event;
    private String lng;
    private String lat;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
