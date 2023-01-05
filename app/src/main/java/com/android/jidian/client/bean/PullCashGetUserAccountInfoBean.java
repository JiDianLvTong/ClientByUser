package com.android.jidian.client.bean;

/**
 * @author : xiaoming
 * date: 2021/12/8 上午9:33
 * company: 兴达智联
 * description:
 */
public class PullCashGetUserAccountInfoBean {
    /**
     * status : 1
     * msg : 请求成功~
     * data : {"isAliPay":1,"isCash":1,"appid":"*******************************","partner":"*******************************","app_private_key":"*******************************"}
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
         * isAliPay : 1
         * isCash : 1
         * appid : *******************************
         * partner : *******************************
         * app_private_key : *******************************
         */

        private String isAliPay;//支付是否授权1-已授权0-未授权
        private String isCash;//	是否可以提现1-可以0-不可以
        private String appid;
        private String partner;
        private String app_private_key;

        public String getIsAliPay() {
            return isAliPay;
        }

        public void setIsAliPay(String isAliPay) {
            this.isAliPay = isAliPay;
        }

        public String getIsCash() {
            return isCash;
        }

        public void setIsCash(String isCash) {
            this.isCash = isCash;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getApp_private_key() {
            return app_private_key;
        }

        public void setApp_private_key(String app_private_key) {
            this.app_private_key = app_private_key;
        }
    }
}
