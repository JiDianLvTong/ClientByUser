package com.android.jidian.client.mvp.bean;

public class MainAppVersionBean {

    private String status;
    private String msg;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private String Android_name = "";
        private String Android_code = "";
        private String Android_url = "";
        private String Android_force = "";

        public String getAndroid_name() {
            return Android_name;
        }

        public void setAndroid_name(String android_name) {
            Android_name = android_name;
        }

        public String getAndroid_code() {
            return Android_code;
        }

        public void setAndroid_code(String android_code) {
            Android_code = android_code;
        }

        public String getAndroid_url() {
            return Android_url;
        }

        public void setAndroid_url(String android_url) {
            Android_url = android_url;
        }

        public String getAndroid_force() {
            return Android_force;
        }

        public void setAndroid_force(String android_force) {
            Android_force = android_force;
        }
    }
}
