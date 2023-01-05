package com.android.jidian.client.bean;

import java.util.List;

public class CouponBefBeans {

    /**
     * status : 1
     * msg : 获取成功！
     * data : [{"id":"138533","merid":"11","type":"44","name":"电动车租金立减券","code":"HL-327919--UCOKRA","fvalue":"200","cost":"5.00","rules":"使用规则：请在优惠劵有效期内使用，以免过期失效。","start_ts":"2020-07-10","end_ts":"2020-07-31","ustatus":"2","lock_uid":"125794","use_time":"0000-00-00 00:00:00","expire":"有效期：2020-07-10 ~ 2020-07-31","unit":"元","bg_img":"https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png","is_use":0,"is_choose":0,"reason":"下单商品中没有车租金哦~","urules":"券功能：使用成功后续租电动车立减200元"},{"id":"138531","merid":"11","type":"43","name":"电动车押金立减券","code":"HL-705430--FWQIYS","fvalue":"100","cost":"5.00","rules":"使用规则：请在优惠劵有效期内使用，以免过期失效。","start_ts":"2020-07-10","end_ts":"2020-07-31","ustatus":"2","lock_uid":"125794","use_time":"0000-00-00 00:00:00","expire":"有效期：2020-07-10 ~ 2020-07-31","unit":"元","bg_img":"https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png","is_use":0,"is_choose":0,"reason":"下单商品中没有车押金哦~","urules":"券功能：使用成功后租赁电动车立减100元"},{"id":"138530","merid":"27","type":"34","name":"电池租金立减券","code":"HL-931498--BXQWIN","fvalue":"3","cost":"5.00","rules":"使用规则：请在优惠劵有效期内使用，以免过期失效。","start_ts":"2020-07-10","end_ts":"2020-07-31","ustatus":"2","lock_uid":"125794","use_time":"0000-00-00 00:00:00","expire":"有效期：2020-07-10 ~ 2020-07-31","unit":"元","bg_img":"https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png","is_use":1,"is_choose":1,"reason":"","urules":"券功能：使用成功后续租电池立减3元"},{"id":"138528","merid":"11","type":"33","name":"电池押金立减券","code":"HL-949916--CIVQFZ","fvalue":"40","cost":"5.00","rules":"使用规则：请在优惠劵有效期内使用，以免过期失效。","start_ts":"2020-07-10","end_ts":"2020-07-31","ustatus":"2","lock_uid":"125794","use_time":"0000-00-00 00:00:00","expire":"有效期：2020-07-10 ~ 2020-07-31","unit":"元","bg_img":"https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png","is_use":0,"is_choose":0,"reason":"下单商品中没有电池押金哦~","urules":"券功能：使用成功后租赁电池立减40元"},{"id":"138526","merid":"11","type":"90","name":"包月换电立减券","code":"HL-593034-LJQ-TFPQVY","fvalue":"50","cost":"50.00","rules":"使用规则：请在优惠劵有效期内使用，以免过期失效。","start_ts":"2020-07-10","end_ts":"2020-07-31","ustatus":"2","lock_uid":"125794","use_time":"2020-07-10 15:52:19","expire":"有效期：2020-07-10 ~ 2020-07-31","unit":"元","bg_img":"https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png","is_use":1,"is_choose":1,"reason":"","urules":"券功能：使用成功后购买包月套餐立减50元"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 138533
         * merid : 11
         * type : 44
         * name : 电动车租金立减券
         * code : HL-327919--UCOKRA
         * fvalue : 200
         * cost : 5.00
         * rules : 使用规则：请在优惠劵有效期内使用，以免过期失效。
         * start_ts : 2020-07-10
         * end_ts : 2020-07-31
         * ustatus : 2
         * lock_uid : 125794
         * use_time : 0000-00-00 00:00:00
         * expire : 有效期：2020-07-10 ~ 2020-07-31
         * unit : 元
         * bg_img : https://img01.halouhuandian.com/hello/adm/2020/20200615151546_28994_716x241.png
         * is_use : 0
         * is_choose : 0
         * reason : 下单商品中没有车租金哦~
         * urules : 券功能：使用成功后续租电动车立减200元
         */

        private String id;
        private String merid;
        private String type;
        private String name;
        private String code;
        private String fvalue;
        private int cost;
        private String rules;
        private String start_ts;
        private String end_ts;
        private String ustatus;
        private String lock_uid;
        private String use_time;
        private String expire;
        private String unit;
        private String bg_img;
        private int is_use;
        private int is_choose;
        private String reason;
        private String urules;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMerid() {
            return merid;
        }

        public void setMerid(String merid) {
            this.merid = merid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFvalue() {
            return fvalue;
        }

        public void setFvalue(String fvalue) {
            this.fvalue = fvalue;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        public String getStart_ts() {
            return start_ts;
        }

        public void setStart_ts(String start_ts) {
            this.start_ts = start_ts;
        }

        public String getEnd_ts() {
            return end_ts;
        }

        public void setEnd_ts(String end_ts) {
            this.end_ts = end_ts;
        }

        public String getUstatus() {
            return ustatus;
        }

        public void setUstatus(String ustatus) {
            this.ustatus = ustatus;
        }

        public String getLock_uid() {
            return lock_uid;
        }

        public void setLock_uid(String lock_uid) {
            this.lock_uid = lock_uid;
        }

        public String getUse_time() {
            return use_time;
        }

        public void setUse_time(String use_time) {
            this.use_time = use_time;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getBg_img() {
            return bg_img;
        }

        public void setBg_img(String bg_img) {
            this.bg_img = bg_img;
        }

        public int getIs_use() {
            return is_use;
        }

        public void setIs_use(int is_use) {
            this.is_use = is_use;
        }

        public int getIs_choose() {
            return is_choose;
        }

        public void setIs_choose(int is_choose) {
            this.is_choose = is_choose;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getUrules() {
            return urules;
        }

        public void setUrules(String urules) {
            this.urules = urules;
        }
    }
}
