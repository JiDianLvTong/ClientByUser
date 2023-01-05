package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2021/11/26 上午11:53
 * company: 兴达智联
 * description:
 */
public class PullCashGetCashRecordBean {
    /**
     * status : 1
     * msg : success~
     * data : [{"cash_type":"支付宝","cash_status":"已申请","cash_time":"","cash_amount":"￥0.1元","cash_account":"920288326@qq.com","cash_name":"刘亚杰"},{"cash_type":"支付宝","cash_status":"已申请","cash_time":"","cash_amount":"￥0.1元","cash_account":"920288326@qq.com","cash_name":"刘亚杰"},{"cash_type":"支付宝","cash_status":"已申请","cash_time":"","cash_amount":"￥0.1元","cash_account":"920288326@qq.com","cash_name":"刘亚杰"}]
     */

    private String status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cash_type : 支付宝
         * cash_status : 已申请
         * cash_time :
         * cash_amount : ￥0.1元
         * cash_account : 920288326@qq.com
         * cash_name : 刘亚杰
         */

        private String cash_type;
        private String cash_status;
        private String cash_time;
        private String cash_amount;
        private String cash_account;
        private String cash_name;

        public String getCash_type() {
            return cash_type;
        }

        public void setCash_type(String cash_type) {
            this.cash_type = cash_type;
        }

        public String getCash_status() {
            return cash_status;
        }

        public void setCash_status(String cash_status) {
            this.cash_status = cash_status;
        }

        public String getCash_time() {
            return cash_time;
        }

        public void setCash_time(String cash_time) {
            this.cash_time = cash_time;
        }

        public String getCash_amount() {
            return cash_amount;
        }

        public void setCash_amount(String cash_amount) {
            this.cash_amount = cash_amount;
        }

        public String getCash_account() {
            return cash_account;
        }

        public void setCash_account(String cash_account) {
            this.cash_account = cash_account;
        }

        public String getCash_name() {
            return cash_name;
        }

        public void setCash_name(String cash_name) {
            this.cash_name = cash_name;
        }
    }
}
