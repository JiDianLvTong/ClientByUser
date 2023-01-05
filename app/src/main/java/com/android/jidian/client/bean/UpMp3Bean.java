package com.android.jidian.client.bean;

public class UpMp3Bean {

    /**
     * status : 1
     * msg : 上传成功！
     * data : {"fpath":"hello/mp3/2020/20200611141145_0502.mp3","url":"https://img01.halouhuandian.com/hello/mp3/2020/20200611141145_0502.mp3","fname":"test.mp3","fsize":42768,"ftype":"mp3"}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public static class DataBean {
        /**
         * fpath : hello/mp3/2020/20200611141145_0502.mp3
         * url : https://img01.halouhuandian.com/hello/mp3/2020/20200611141145_0502.mp3
         * fname : test.mp3
         * fsize : 42768
         * ftype : mp3
         */

        private String fpath;
        private String url;
        private String fname;
        private int fsize;
        private String ftype;

        public String getFpath() {
            return fpath;
        }

        public void setFpath(String fpath) {
            this.fpath = fpath;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }
    }
}
