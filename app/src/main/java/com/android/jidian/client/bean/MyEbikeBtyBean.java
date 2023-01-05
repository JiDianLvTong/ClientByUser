package com.android.jidian.client.bean;

import java.util.List;

public class MyEbikeBtyBean {


    /**
     * status : 1
     * msg : OK
     * data : {"top":{"labNum":4,"cList":[{"name":"换电币(剩余)","nums":0,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":38,"unit":"天","color":"white"},{"name":"电池(剩余)","nums":14,"unit":"天","color":"white"},{"name":"电动车(剩余)","nums":14,"unit":"天","color":"white"}]},"ebike":[{"devid":"30744","name":"电动车（租赁）","otype":42,"numt":1,"number":"000000000","rprice":"2000.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao4.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":1,"fade_status":"0","end_ts":"2020-05-26 14:06:43","checked":1,"mrent":"0.00","relet":"weekly","create_time":"2020-05-12 14:06:43","renttime":"","bike_num":"","vin":"","is_bike":0,"mtitle":"兴达智联-测试"}],"battery":[{"devid":"140385","name":"电池（租赁） / 电池（购买）","otype":32,"numt":1,"number":"","volt":"","rprice":"1000.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":2,"fade_status":"0","end_ts":"2020-05-26 14:06:43","checked":1,"mrent":"0.00","relet":"weekly","create_time":"2020-05-12 14:06:43","renttime":"","mtitle":"兴达智联-测试"}]}
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
         * top : {"labNum":4,"cList":[{"name":"换电币(剩余)","nums":0,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":38,"unit":"天","color":"white"},{"name":"电池(剩余)","nums":14,"unit":"天","color":"white"},{"name":"电动车(剩余)","nums":14,"unit":"天","color":"white"}]}
         * ebike : [{"devid":"30744","name":"电动车（租赁）","otype":42,"numt":1,"number":"000000000","rprice":"2000.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao4.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":1,"fade_status":"0","end_ts":"2020-05-26 14:06:43","checked":1,"mrent":"0.00","relet":"weekly","create_time":"2020-05-12 14:06:43","renttime":"","bike_num":"","vin":"","is_bike":0,"mtitle":"兴达智联-测试"}]
         * battery : [{"devid":"140385","name":"电池（租赁） / 电池（购买）","otype":32,"numt":1,"number":"","volt":"","rprice":"1000.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":2,"fade_status":"0","end_ts":"2020-05-26 14:06:43","checked":1,"mrent":"0.00","relet":"weekly","create_time":"2020-05-12 14:06:43","renttime":"","mtitle":"兴达智联-测试"}]
         */

        private TopBean top;
        private List<EbikeBean> ebike;
        private List<BatteryBean> battery;

        public TopBean getTop() {
            return top;
        }

        public void setTop(TopBean top) {
            this.top = top;
        }

        public List<EbikeBean> getEbike() {
            return ebike;
        }

        public void setEbike(List<EbikeBean> ebike) {
            this.ebike = ebike;
        }

        public List<BatteryBean> getBattery() {
            return battery;
        }

        public void setBattery(List<BatteryBean> battery) {
            this.battery = battery;
        }

        public static class TopBean {
            /**
             * labNum : 4
             * cList : [{"name":"换电币(剩余)","nums":0,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":38,"unit":"天","color":"white"},{"name":"电池(剩余)","nums":14,"unit":"天","color":"white"},{"name":"电动车(剩余)","nums":14,"unit":"天","color":"white"}]
             */

            private int labNum;
            private List<CListBean> cList;

            public int getLabNum() {
                return labNum;
            }

            public void setLabNum(int labNum) {
                this.labNum = labNum;
            }

            public List<CListBean> getCList() {
                return cList;
            }

            public void setCList(List<CListBean> cList) {
                this.cList = cList;
            }

            public static class CListBean {
                /**
                 * name : 换电币(剩余)
                 * nums : 0
                 * unit : 个
                 * color : white
                 */

                private String name;
                private int nums;
                private String unit;
                private String color;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getNums() {
                    return nums;
                }

                public void setNums(int nums) {
                    this.nums = nums;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }
            }
        }

        public static class EbikeBean {
            /**
             * devid : 30744
             * name : 电动车（租赁）
             * otype : 42
             * numt : 1
             * number : 000000000
             * rprice : 2000.00
             * use_type : 20
             * bg_img : http://img01.halouhuandian.com/shop/qianbao4.png
             * is_bind : 2
             * btips : 待绑定
             * remark : 待绑定
             * shop_type : 1
             * fade_status : 0
             * end_ts : 2020-05-26 14:06:43
             * checked : 1
             * mrent : 0.00
             * relet : weekly
             * create_time : 2020-05-12 14:06:43
             * renttime :
             * bike_num :
             * vin :
             * is_bike : 0
             * mtitle : 兴达智联-测试
             */

            private String devid;
            private String name;
            private int otype;
            private int numt;
            private String number;
            private String rprice;
            private String use_type;
            private String bg_img;
            private String is_bind;
            private String btips;
            private String remark;
            private int shop_type;
            private String fade_status;
            private String end_ts;
            private int checked;
            private String mrent;
            private String relet;
            private String create_time;
            private String renttime;
            private String bike_num;
            private String vin;
            private int is_bike;
            private String mtitle;
            private String ext;

            public String getDevid() {
                return devid;
            }

            public void setDevid(String devid) {
                this.devid = devid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOtype() {
                return otype;
            }

            public void setOtype(int otype) {
                this.otype = otype;
            }

            public int getNumt() {
                return numt;
            }

            public void setNumt(int numt) {
                this.numt = numt;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getRprice() {
                return rprice;
            }

            public void setRprice(String rprice) {
                this.rprice = rprice;
            }

            public String getUse_type() {
                return use_type;
            }

            public void setUse_type(String use_type) {
                this.use_type = use_type;
            }

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public String getIs_bind() {
                return is_bind;
            }

            public void setIs_bind(String is_bind) {
                this.is_bind = is_bind;
            }

            public String getBtips() {
                return btips;
            }

            public void setBtips(String btips) {
                this.btips = btips;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public String getFade_status() {
                return fade_status;
            }

            public void setFade_status(String fade_status) {
                this.fade_status = fade_status;
            }

            public String getEnd_ts() {
                return end_ts;
            }

            public void setEnd_ts(String end_ts) {
                this.end_ts = end_ts;
            }

            public int getChecked() {
                return checked;
            }

            public void setChecked(int checked) {
                this.checked = checked;
            }

            public String getMrent() {
                return mrent;
            }

            public void setMrent(String mrent) {
                this.mrent = mrent;
            }

            public String getRelet() {
                return relet;
            }

            public void setRelet(String relet) {
                this.relet = relet;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getRenttime() {
                return renttime;
            }

            public void setRenttime(String renttime) {
                this.renttime = renttime;
            }

            public String getBike_num() {
                return bike_num;
            }

            public void setBike_num(String bike_num) {
                this.bike_num = bike_num;
            }

            public String getVin() {
                return vin;
            }

            public void setVin(String vin) {
                this.vin = vin;
            }

            public int getIs_bike() {
                return is_bike;
            }

            public void setIs_bike(int is_bike) {
                this.is_bike = is_bike;
            }

            public String getMtitle() {
                return mtitle;
            }

            public void setMtitle(String mtitle) {
                this.mtitle = mtitle;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }
        }

        public static class BatteryBean {
            /**
             * devid : 140385
             * name : 电池（租赁） / 电池（购买）
             * otype : 32
             * numt : 1
             * number :
             * volt :
             * rprice : 1000.00
             * use_type : 20
             * bg_img : http://img01.halouhuandian.com/shop/qianbao5.png
             * is_bind : 2
             * btips : 待绑定
             * remark : 待绑定
             * shop_type : 2
             * fade_status : 0
             * end_ts : 2020-05-26 14:06:43
             * checked : 1
             * mrent : 0.00
             * relet : weekly
             * create_time : 2020-05-12 14:06:43
             * renttime :
             * mtitle : 兴达智联-测试
             */

            private String devid;
            private String name;
            private int otype;
            private int numt;
            private String number;
            private String volt;
            private String rprice;
            private String use_type;
            private String bg_img;
            private String is_bind;
            private String btips;
            private String remark;
            private int shop_type;
            private String fade_status;
            private String end_ts;
            private int checked;
            private String mrent;
            private String relet;
            private String create_time;
            private String renttime;
            private String mtitle;
            private String ext;

            public String getDevid() {
                return devid;
            }

            public void setDevid(String devid) {
                this.devid = devid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOtype() {
                return otype;
            }

            public void setOtype(int otype) {
                this.otype = otype;
            }

            public int getNumt() {
                return numt;
            }

            public void setNumt(int numt) {
                this.numt = numt;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getVolt() {
                return volt;
            }

            public void setVolt(String volt) {
                this.volt = volt;
            }

            public String getRprice() {
                return rprice;
            }

            public void setRprice(String rprice) {
                this.rprice = rprice;
            }

            public String getUse_type() {
                return use_type;
            }

            public void setUse_type(String use_type) {
                this.use_type = use_type;
            }

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public String getIs_bind() {
                return is_bind;
            }

            public void setIs_bind(String is_bind) {
                this.is_bind = is_bind;
            }

            public String getBtips() {
                return btips;
            }

            public void setBtips(String btips) {
                this.btips = btips;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public String getFade_status() {
                return fade_status;
            }

            public void setFade_status(String fade_status) {
                this.fade_status = fade_status;
            }

            public String getEnd_ts() {
                return end_ts;
            }

            public void setEnd_ts(String end_ts) {
                this.end_ts = end_ts;
            }

            public int getChecked() {
                return checked;
            }

            public void setChecked(int checked) {
                this.checked = checked;
            }

            public String getMrent() {
                return mrent;
            }

            public void setMrent(String mrent) {
                this.mrent = mrent;
            }

            public String getRelet() {
                return relet;
            }

            public void setRelet(String relet) {
                this.relet = relet;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getRenttime() {
                return renttime;
            }

            public void setRenttime(String renttime) {
                this.renttime = renttime;
            }

            public String getMtitle() {
                return mtitle;
            }

            public void setMtitle(String mtitle) {
                this.mtitle = mtitle;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }
        }
    }
}
