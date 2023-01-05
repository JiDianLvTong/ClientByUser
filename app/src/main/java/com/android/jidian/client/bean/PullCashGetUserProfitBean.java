package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2021/11/25 下午5:52
 * company: 兴达智联
 * description:
 */
public class PullCashGetUserProfitBean {
    /**
     * status : 1
     * msg : success~
     * data : {"total_profit":70,"one_profit":50,"two_profit":20,"cash":69.7,"sucCash":0.3,"cashRule":["1，收益满100可提现","2，提现必须在哈喽平台实名认证","3，提现后48小时以内到账","4，提现金额大于等于0.1元才可提现"]}
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
         * total_profit : 70
         * one_profit : 50
         * two_profit : 20
         * cash : 69.7
         * sucCash : 0.3
         * cashRule : ["1，收益满100可提现","2，提现必须在哈喽平台实名认证","3，提现后48小时以内到账","4，提现金额大于等于0.1元才可提现"]
         */

        private String total_profit;
        private String one_profit;
        private String two_profit;
        private String cash;
        private String sucCash;
        private List<String> cashRule;

        public String getTotal_profit() {
            return total_profit;
        }

        public void setTotal_profit(String total_profit) {
            this.total_profit = total_profit;
        }

        public String getOne_profit() {
            return one_profit;
        }

        public void setOne_profit(String one_profit) {
            this.one_profit = one_profit;
        }

        public String getTwo_profit() {
            return two_profit;
        }

        public void setTwo_profit(String two_profit) {
            this.two_profit = two_profit;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getSucCash() {
            return sucCash;
        }

        public void setSucCash(String sucCash) {
            this.sucCash = sucCash;
        }

        public List<String> getCashRule() {
            return cashRule;
        }

        public void setCashRule(List<String> cashRule) {
            this.cashRule = cashRule;
        }
    }
}
