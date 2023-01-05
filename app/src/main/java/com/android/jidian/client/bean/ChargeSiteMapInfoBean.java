package com.android.jidian.client.bean;

import java.util.ArrayList;

public class ChargeSiteMapInfoBean {

    private String cabid = "";
    private String cabno = "";
    private String number = "";
    private String merid = "";
    private String gtype = "";
    private String name = "";
    private String otime = "";
    private String address = "";
    private String mphone = "";
    private String jingdu = "";
    private String weidu = "";
    private String usable = "";
    private String is_skip = "";
    private String tips = "";
    private String distance = "";
    private String nowline = "";
    private String type = "";
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<Tags> tags = new ArrayList<>();
    private ArrayList<Bty_rate> bty_rate = new ArrayList<>();

    public String getNowline() {
        return nowline;
    }

    public void setNowline(String nowline) {
        this.nowline = nowline;
    }

    public String getCabid() {
        return cabid;
    }

    public void setCabid(String cabid) {
        this.cabid = cabid;
    }

    public String getCabno() {
        return cabno;
    }

    public void setCabno(String cabno) {
        this.cabno = cabno;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }

    public String getGtype() {
        return gtype;
    }

    public void setGtype(String gtype) {
        this.gtype = gtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(String is_skip) {
        this.is_skip = is_skip;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }

    public ArrayList<Bty_rate> getBty_rate() {
        return bty_rate;
    }

    public void setBty_rate(ArrayList<Bty_rate> bty_rate) {
        this.bty_rate = bty_rate;
    }

    public class Tags{
        private String name = "";
        private String color = "";
        private String icon = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public class Bty_rate{
        private String name = "";
        private String num = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
