package com.android.jidian.client.bean;

public class CouponCashv2Bean {

    /**
     * status : 1
     * msg : success~
     * data : {"name":"换电币券","time":"2019-12-24--2019-12-24","code":"HL-741940-HLB-CZLVPS"}
     * errno : E0000
     * show : 1
     */

    private int status;
    private String msg;
    private DataBean data;
    private String errno;
    private int show;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public static class DataBean {
        /**
         * name : 换电币券
         * time : 2019-12-24--2019-12-24
         * code : HL-741940-HLB-CZLVPS
         */

        private String name;
        private String time;
        private String code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
