package com.android.jidian.client.bean;

import java.util.List;

public class CabinetDetailBean {

    /**
     * status : 1
     * msg : 请求成功！
     * data : {"cabid":807,"cabno":"00807","number":"00807","merid":"196","gtype":"buy","name":"有车行","otime":"00:00 - 24:00","address":"懋隆11栋","mphone":"18877777777","jingdu":"116.581018000000","weidu":"39.921675000000","usable":0,"images":["https://img01.halouhuandian.com/hello/hello/2020/20200610141652_36536_540x1140.jpg","https://img01.halouhuandian.com/hello/hello/2020/20200610141655_34340_540x1140.jpg","https://img01.halouhuandian.com/hello/hello/2020/20200610141659_14581_540x1140.jpg"],"tags":[{"name":"24小时","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_shijian.png"},{"name":"车行","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_chehang.png"},{"name":"离线","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_offline.png"},{"name":"无电池","color":"CD00CD","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_dianya.png"}],"distance":0.33,"bty_rate":[{"name":"<40%","num":0},{"name":"40%~60%","num":0},{"name":"60%~90%","num":0},{"name":">90%","num":0}]}
     * is_skip : 0
     * tips : 如需购买商品，请联系商家扫描商家二维码购买~
     */

    private int status;
    private String msg;
    private DataBean data;
    private int is_skip;
    private String tips;

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

    public int getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(int is_skip) {
        this.is_skip = is_skip;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public static class DataBean {
        /**
         * cabid : 807
         * cabno : 00807
         * number : 00807
         * merid : 196
         * gtype : buy
         * name : 有车行
         * otime : 00:00 - 24:00
         * address : 懋隆11栋
         * mphone : 18877777777
         * jingdu : 116.581018000000
         * weidu : 39.921675000000
         * usable : 0
         * images : ["https://img01.halouhuandian.com/hello/hello/2020/20200610141652_36536_540x1140.jpg","https://img01.halouhuandian.com/hello/hello/2020/20200610141655_34340_540x1140.jpg","https://img01.halouhuandian.com/hello/hello/2020/20200610141659_14581_540x1140.jpg"]
         * tags : [{"name":"24小时","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_shijian.png"},{"name":"车行","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_chehang.png"},{"name":"离线","color":"EB5628","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_offline.png"},{"name":"无电池","color":"CD00CD","icon":"https://img01.halouhuandian.com/cabinet/map_cab_detail_dianya.png"}]
         * distance : 0.33
         * bty_rate : [{"name":"<40%","num":0},{"name":"40%~60%","num":0},{"name":"60%~90%","num":0},{"name":">90%","num":0}]
         */

        private int cabid;
        private String cabno;
        private String number;
        private String merid;
        private String gtype;
        private String name;
        private String otime;
        private String address;
        private String mphone;
        private String jingdu;
        private String weidu;
        private int usable;
        private double distance;
        private List<String> images;
        private List<TagsBean> tags;
        /**
         * 电池范围
         */
        private List<BtyRateBean> bty_rate;
        /**
         * 2车行 其他换电站
         */
        private int type;

        public int getCabid() {
            return cabid;
        }

        public void setCabid(int cabid) {
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

        public int getUsable() {
            return usable;
        }

        public void setUsable(int usable) {
            this.usable = usable;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<BtyRateBean> getBty_rate() {
            return bty_rate;
        }

        public void setBty_rate(List<BtyRateBean> bty_rate) {
            this.bty_rate = bty_rate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static class TagsBean {
            /**
             * name : 24小时
             * color : EB5628
             * icon : https://img01.halouhuandian.com/cabinet/map_cab_detail_shijian.png
             */
            private String name;
            private String color;
            private String icon;

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

        public static class BtyRateBean {
            /**
             * name : <40%
             * num : 0
             */

            private String name;
            private int num;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}