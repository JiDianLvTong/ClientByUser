package com.android.jidian.repair.mvp.main;

/**
 * @author : xiaoming
 * date: 2023/2/16 10:22
 * description:
 */
public class UpdateVersionBean {
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

    public static class DataBean {
        private String Android_code;
        private String Android_url;
        private String Android_force;
        private String Android_name;

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

        public String getAndroid_name() {
            return Android_name;
        }

        public void setAndroid_name(String android_name) {
            Android_name = android_name;
        }
    }
}
