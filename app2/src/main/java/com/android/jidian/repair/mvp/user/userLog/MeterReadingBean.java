package com.android.jidian.repair.mvp.user.userLog;

import java.util.List;

/**
 * @author : PTT
 * date: 2020/10/22 15:21
 * company: 兴达智联
 * description:
 */
public class MeterReadingBean {
    /*{
        "status": 1,
            "msg": "请求成功！",
            "lastid": 0,
            "data": [
        {
            "cab_id": "1246",
                "electric": 0,
                "now_electric": "20",
                "date": "2019-11-25",
                "ex_nums": 0,
                "city":'北京'
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
        /**
         * 电柜编号
         */
        private String cab_id;
        /**
         * 当天使用电量
         */
        private String electric;
        private String now_electric;
        /**
         * 时间
         */
        private String date;
        /**
         * 换电次数
         */
        private String ex_nums;
        /**
         * 城市
         */
        private String city;

        public String getCab_id() {
            return cab_id;
        }

        public void setCab_id(String cab_id) {
            this.cab_id = cab_id;
        }

        public String getElectric() {
            return electric;
        }

        public void setElectric(String electric) {
            this.electric = electric;
        }

        public String getNow_electric() {
            return now_electric;
        }

        public void setNow_electric(String now_electric) {
            this.now_electric = now_electric;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getEx_nums() {
            return ex_nums;
        }

        public void setEx_nums(String ex_nums) {
            this.ex_nums = ex_nums;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
