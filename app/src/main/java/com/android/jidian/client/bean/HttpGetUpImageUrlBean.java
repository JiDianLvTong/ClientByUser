package com.android.jidian.client.bean;

/**
 * @author : PTT
 * date: 9/28/21 10:11 AM
 * company: 兴达智联
 * description:
 */
public class HttpGetUpImageUrlBean {
    private String status;
    private String msg;
    private String error;
    private String errno;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private String upfurl;
        private String servno;

        public String getUpfurl() {
            return upfurl;
        }

        public void setUpfurl(String upfurl) {
            this.upfurl = upfurl;
        }

        public String getServno() {
            return servno;
        }

        public void setServno(String servno) {
            this.servno = servno;
        }
    }
}