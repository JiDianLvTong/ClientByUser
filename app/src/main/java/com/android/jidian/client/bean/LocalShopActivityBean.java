package com.android.jidian.client.bean;

import java.util.ArrayList;

public class LocalShopActivityBean {

    private ArrayList<Data> data = new ArrayList<>();

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data{
        private String gid = "";
        private String fname = "";
        private String otype = "";
        private String opt = "";
        private String sname = "";
        private String rprice = "";
        private String oprice = "";
        private String numt = "";
        private String bgurl = "";

        public String getOpt() {
            return opt;
        }

        public void setOpt(String opt) {
            this.opt = opt;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getOtype() {
            return otype;
        }

        public void setOtype(String otype) {
            this.otype = otype;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getRprice() {
            return rprice;
        }

        public void setRprice(String rprice) {
            this.rprice = rprice;
        }

        public String getOprice() {
            return oprice;
        }

        public void setOprice(String oprice) {
            this.oprice = oprice;
        }

        public String getNumt() {
            return numt;
        }

        public void setNumt(String numt) {
            this.numt = numt;
        }

        public String getBgurl() {
            return bgurl;
        }

        public void setBgurl(String bgurl) {
            this.bgurl = bgurl;
        }
    }

}
