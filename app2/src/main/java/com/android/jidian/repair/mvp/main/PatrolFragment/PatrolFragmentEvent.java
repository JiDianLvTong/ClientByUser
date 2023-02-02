package com.android.jidian.repair.mvp.main.PatrolFragment;

/**
 * @author : xiaoming
 * date: 2023/2/2 11:42
 * description:
 */
public class PatrolFragmentEvent {
    public static int LOCATION_SUCCESS = 1;

    public PatrolFragmentEvent(int event, String lng, String lat) {
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
