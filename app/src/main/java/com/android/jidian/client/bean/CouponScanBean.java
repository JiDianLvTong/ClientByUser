package com.android.jidian.client.bean;

public class CouponScanBean {

    /**
     * status : 1
     * msg : success~
     * data : {"name":"换电币兑换券","type":"换电币兑换券","fval":"券面值：50币","expire":"有效期：2020-05-28 ~ 2020-11-26","code":"HL-392008-HLB-YVFLNO","code_info":"优惠码：HL-392008-HLB-YVFLNO","rule":"券规则：","alert":"是否兑换？"}
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
         * name : 换电币兑换券
         * type : 换电币兑换券
         * fval : 券面值：50币
         * expire : 有效期：2020-05-28 ~ 2020-11-26
         * code : HL-392008-HLB-YVFLNO
         * code_info : 优惠码：HL-392008-HLB-YVFLNO
         * rule : 券规则：
         * alert : 是否兑换？
         */

        private String name;
        private String type;
        private String fval;
        private String expire;
        private String code;
        private String code_info;
        private String rule;
        private String alert;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFval() {
            return fval;
        }

        public void setFval(String fval) {
            this.fval = fval;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode_info() {
            return code_info;
        }

        public void setCode_info(String code_info) {
            this.code_info = code_info;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }
    }
}
