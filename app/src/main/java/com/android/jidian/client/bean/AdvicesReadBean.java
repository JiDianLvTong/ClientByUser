package com.android.jidian.client.bean;

public class AdvicesReadBean {
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

    public class DataBean {
        private String no_read_num;

        public String getNo_read_num() {
            return no_read_num;
        }

        public void setNo_read_num(String no_read_num) {
            this.no_read_num = no_read_num;
        }
    }
}