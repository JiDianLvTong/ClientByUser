package com.android.jidian.repair.mvp.user.userLog;

import java.util.List;

/**
 * @author : PTT
 * date: 2020/10/22 15:21
 * company: 兴达智联
 * description:
 */
public class BatteryInquiryBean {
    /*{
        "status": 1,
            "msg": "请求成功！",
            "lastid": 0,
            "data": [
        {
            "cabid": "0",
                "uid": "126664",
                "create_time": "2020-08-25 16:33:19",
                "is_bind": "-1",
                "city": "",
                "battry": "NDRRRHB17ANN0001",
                "uname": "Hello用户(15555555519)"
        }
    ]
    }*/
    private String status;
    private String msg;
    private int lastid;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public int getLastid() {
        return lastid;
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String cabid;
        private String uid;
        private String create_time;
        private String is_bind;
        private String city;
        private String battery;
        private String uname;
        private String get_type;

        public String getCabid() {
            return cabid;
        }

        public void setCabid(String cabid) {
            this.cabid = cabid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(String is_bind) {
            this.is_bind = is_bind;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getBattery() {
            return battery;
        }

        public void setBattery(String battery) {
            this.battery = battery;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getGet_type() {
            return get_type;
        }

        public void setGet_type(String get_type) {
            this.get_type = get_type;
        }
    }
}