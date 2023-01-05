package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2021/11/27 上午9:36
 * company: 兴达智联
 * description:
 */
public class PullActivityInviteListsBean {
    /**
     * status : 1
     * msg : 请求成功~
     * data : {"type":1,"total":{"num":0,"profit":0},"list":[{"id":"4","cuid":"247443","phone":"333","oid":"252985","order_fee":"200.00","profit":"20.00","level":"2","rate":"0.10","pay_time":"2021-11-24 18:00:57","order_type_name":""},{"id":"3","cuid":"247443","phone":"333","oid":"252986","order_fee":"200.00","profit":"40.00","level":"1","rate":"0.20","pay_time":"2021-11-24 18:14:54","order_type_name":""},{"id":"2","cuid":"247441","phone":"222","oid":"252987","order_fee":"150.00","profit":"30.00","level":"1","rate":"0.20","pay_time":"2021-11-25 11:09:44","order_type_name":""},{"id":"1","cuid":"247440","phone":"111","oid":"252988","order_fee":"100.00","profit":"20.00","level":"1","rate":"0.20","pay_time":"2021-11-25 11:10:16","order_type_name":""}]}
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
         * type : 1
         * total : {"num":0,"profit":0}
         * list : [{"id":"4","cuid":"247443","phone":"333","oid":"252985","order_fee":"200.00","profit":"20.00","level":"2","rate":"0.10","pay_time":"2021-11-24 18:00:57","order_type_name":""},{"id":"3","cuid":"247443","phone":"333","oid":"252986","order_fee":"200.00","profit":"40.00","level":"1","rate":"0.20","pay_time":"2021-11-24 18:14:54","order_type_name":""},{"id":"2","cuid":"247441","phone":"222","oid":"252987","order_fee":"150.00","profit":"30.00","level":"1","rate":"0.20","pay_time":"2021-11-25 11:09:44","order_type_name":""},{"id":"1","cuid":"247440","phone":"111","oid":"252988","order_fee":"100.00","profit":"20.00","level":"1","rate":"0.20","pay_time":"2021-11-25 11:10:16","order_type_name":""}]
         */

        private String type;
        private TotalBean total;
        private List<ListBean> list;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class TotalBean {
            /**
             * num : 0
             * profit : 0
             */

            private String num;
            private String profit;

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }
        }

        public static class ListBean {
            /**
             * id : 4
             * cuid : 247443
             * phone : 333
             * oid : 252985
             * order_fee : 200.00
             * profit : 20.00
             * level : 2
             * rate : 0.10
             * pay_time : 2021-11-24 18:00:57
             * order_type_name :
             */

            private String id;
            private String cuid;
            private String phone;
            private String oid;
            private String order_fee;
            private String profit;
            private String level;
            private String rate;
            private String pay_time;
            private String order_type_name;
            private String reg_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCuid() {
                return cuid;
            }

            public void setCuid(String cuid) {
                this.cuid = cuid;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getOrder_fee() {
                return order_fee;
            }

            public void setOrder_fee(String order_fee) {
                this.order_fee = order_fee;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public String getPay_time() {
                return pay_time;
            }

            public void setPay_time(String pay_time) {
                this.pay_time = pay_time;
            }

            public String getOrder_type_name() {
                return order_type_name;
            }

            public void setOrder_type_name(String order_type_name) {
                this.order_type_name = order_type_name;
            }

            public String getReg_time() {
                return reg_time;
            }

            public void setReg_time(String reg_time) {
                this.reg_time = reg_time;
            }
        }
    }
}
