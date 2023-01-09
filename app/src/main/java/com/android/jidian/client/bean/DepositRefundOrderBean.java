package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : PTT
 * date: 9/23/21 2:39 PM
 * company: 兴达智联
 * description:
 */
public class DepositRefundOrderBean {
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
         * 主订单id
         */
        private String id;
        /**
         * 支付方式(type1支付宝2微信 10信用免押100线下支付)
         */
        private String type;
        /**
         * 主订单号
         */
        private String order_num;
        /**
         * 订单退款金额
         */
        private String refprice;
        /**
         * 包含商品
         */
        private List<String> goods;
        private String apply_status;
        /**
         * 退款状态(0 => '已申请', 1 => '审核通过', 2 => '驳回', -1 无)
         */
        private String apply;
        /**
         * 申请记录id
         */
        private String refund_id;
        /**
         * 驳回原因
         */
        private String reject_reason;
        /**
         * 支付方式
         */
        private String typer;
        /**
         * 提示信息
         */
        private List<String> note;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getRefprice() {
            return refprice;
        }

        public void setRefprice(String refprice) {
            this.refprice = refprice;
        }

        public List<String> getGoods() {
            return goods;
        }

        public void setGoods(List<String> goods) {
            this.goods = goods;
        }

        public String getApply_status() {
            return apply_status;
        }

        public void setApply_status(String apply_status) {
            this.apply_status = apply_status;
        }

        public String getRefund_id() {
            return refund_id;
        }

        public void setRefund_id(String refund_id) {
            this.refund_id = refund_id;
        }

        public String getReject_reason() {
            return reject_reason;
        }

        public void setReject_reason(String reject_reason) {
            this.reject_reason = reject_reason;
        }

        public String getTyper() {
            return typer;
        }

        public void setTyper(String typer) {
            this.typer = typer;
        }

        public List<String> getNote() {
            return note;
        }

        public void setNote(List<String> note) {
            this.note = note;
        }

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }
    }
}