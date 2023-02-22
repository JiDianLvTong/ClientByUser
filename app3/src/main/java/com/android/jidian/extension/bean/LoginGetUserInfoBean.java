package com.android.jidian.extension.bean;

public class LoginGetUserInfoBean {

    private String id;
    private String apptoken;
    private String readlName;
    private String phone;
    private String isfirst;
    private String avater;
    private String qrcode;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
    }

    public String getReadlName() {
        return readlName;
    }

    public void setReadlName(String readlName) {
        this.readlName = readlName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsfirst() {
        return isfirst;
    }

    public void setIsfirst(String isfirst) {
        this.isfirst = isfirst;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }
}
