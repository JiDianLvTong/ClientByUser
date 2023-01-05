package com.android.jidian.client.bean.event;

/**
 * @author : xiaoming
 * date: 2022/12/29 16:39
 * company: 兴达智联
 * description:
 */
public class MainShopEvent {
    public static int LOCATION_LAT_LNG = 1;

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
