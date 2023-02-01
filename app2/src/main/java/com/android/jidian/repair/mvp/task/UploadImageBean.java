package com.android.jidian.repair.mvp.task;

/**
 * @author : xiaoming
 * date: 2023/1/31 11:21
 * description:
 */
public class UploadImageBean {
    /**
     * status : 1
     * msg : 上传成功！
     * data : {"big_path":"ape/2023/01/01_20230131162205_45998_1080x2160.jpeg","big_url":"https://img01.mixiangx.com/ape/2023/01/01_20230131162205_45998_1080x2160.jpeg","furl":"https://img01.mixiangx.com/ape/2023/01/01_20230131162205_45998_1080x2160.jpeg","big_w":1080,"big_h":2160,"name":"IMG_CMP_08DDA0419201B97F3A7AE50A5C3A19.jpeg","size":206583,"type":"jpeg","baseDir":"ape/2023/01/","servno":"01","fmd5":"b4d69d251053189044e20ce0756deeda"}
     */

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
        /**
         * big_path : ape/2023/01/01_20230131162205_45998_1080x2160.jpeg
         * big_url : https://img01.mixiangx.com/ape/2023/01/01_20230131162205_45998_1080x2160.jpeg
         * furl : https://img01.mixiangx.com/ape/2023/01/01_20230131162205_45998_1080x2160.jpeg
         * big_w : 1080
         * big_h : 2160
         * name : IMG_CMP_08DDA0419201B97F3A7AE50A5C3A19.jpeg
         * size : 206583
         * type : jpeg
         * baseDir : ape/2023/01/
         * servno : 01
         * fmd5 : b4d69d251053189044e20ce0756deeda
         */

        private String big_path;
        private String big_url;
        private String furl;
        private String big_w;
        private String big_h;
        private String name;
        private String size;
        private String type;
        private String baseDir;
        private String servno;
        private String fmd5;

        public String getBig_path() {
            return big_path;
        }

        public void setBig_path(String big_path) {
            this.big_path = big_path;
        }

        public String getBig_url() {
            return big_url;
        }

        public void setBig_url(String big_url) {
            this.big_url = big_url;
        }

        public String getFurl() {
            return furl;
        }

        public void setFurl(String furl) {
            this.furl = furl;
        }

        public String getBig_w() {
            return big_w;
        }

        public void setBig_w(String big_w) {
            this.big_w = big_w;
        }

        public String getBig_h() {
            return big_h;
        }

        public void setBig_h(String big_h) {
            this.big_h = big_h;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBaseDir() {
            return baseDir;
        }

        public void setBaseDir(String baseDir) {
            this.baseDir = baseDir;
        }

        public String getServno() {
            return servno;
        }

        public void setServno(String servno) {
            this.servno = servno;
        }

        public String getFmd5() {
            return fmd5;
        }

        public void setFmd5(String fmd5) {
            this.fmd5 = fmd5;
        }
    }
}
