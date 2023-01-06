package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/6 13:55
 * company: 兴达智联
 * description:
 */
public class UserUOrderBean {
    /**
     * status : 1
     * msg : OK
     * data : {"lists":[{"id":"23","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:26:51","type":"2","order_num":"JD20221130142651EAO1","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:27:01","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"},{"id":"22","gid":"-1","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:09:59","type":"2","order_num":"JD20221130140959I8OE","order_title":"租赁续费（除换电费外）","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:10:09","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车月租","rprice":"0.01"},{"name":"电动车月租","rprice":"0.01"},{"name":"锂电池月租","rprice":"0.01"},{"name":"锂电池月租","rprice":"0.01"}],"select_pack_month":"1"},{"id":"21","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:06:13","type":"2","order_num":"JD202211301406141FMN","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:06:25","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"},{"id":"12","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-29 19:00:00","type":"1","order_num":"JD20221129190000Q5CV","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-29 19:01:52","refund_info":"交易成功","refund_color":"#F94A2C","typer":"支付宝支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"}],"lastid":2}
     * error :
     * errno : E0000
     */

    private String status;
    private String msg;
    private DataBean data;
    private String error;
    private String errno;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public static class DataBean {
        /**
         * lists : [{"id":"23","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:26:51","type":"2","order_num":"JD20221130142651EAO1","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:27:01","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"},{"id":"22","gid":"-1","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:09:59","type":"2","order_num":"JD20221130140959I8OE","order_title":"租赁续费（除换电费外）","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:10:09","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车月租","rprice":"0.01"},{"name":"电动车月租","rprice":"0.01"},{"name":"锂电池月租","rprice":"0.01"},{"name":"锂电池月租","rprice":"0.01"}],"select_pack_month":"1"},{"id":"21","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-30 14:06:13","type":"2","order_num":"JD202211301406141FMN","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-30 14:06:25","refund_info":"交易成功","refund_color":"#F94A2C","typer":"微信支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"},{"id":"12","gid":"1003","auth":"1","auth_time":"0000-00-00 00:00:00","order_time":"2022-11-29 19:00:00","type":"1","order_num":"JD20221129190000Q5CV","order_title":"车+电租赁","order_fee":"0.01","order_status":"1","pay_time":"2022-11-29 19:01:52","refund_info":"交易成功","refund_color":"#F94A2C","typer":"支付宝支付","status_desc":"已支付","goods":[{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}],"select_pack_month":"无"}]
         * lastid : 2
         */

        private List<ListsBean> lists;
        private String lastid;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public String getLastid() {
            return lastid;
        }

        public void setLastid(String lastid) {
            this.lastid = lastid;
        }

        public static class ListsBean {
            /**
             * id : 23
             * gid : 1003
             * auth : 1
             * auth_time : 0000-00-00 00:00:00
             * order_time : 2022-11-30 14:26:51
             * type : 2
             * order_num : JD20221130142651EAO1
             * order_title : 车+电租赁
             * order_fee : 0.01
             * order_status : 1
             * pay_time : 2022-11-30 14:27:01
             * refund_info : 交易成功
             * refund_color : #F94A2C
             * typer : 微信支付
             * status_desc : 已支付
             * goods : [{"name":"电动车","rprice":"0.01"},{"name":"电动车-月租","rprice":"0.01"},{"name":"锂电池","rprice":"0.01"},{"name":"锂电池-月租","rprice":"0.01"}]
             * select_pack_month : 无
             */

            private String id;
            private String gid;
            private String auth;
            private String auth_time;
            private String order_time;
            private String type;
            private String order_num;
            private String order_title;
            private String order_fee;
            private String order_status;
            private String pay_time;
            private String refund_info;
            private String refund_color;
            private String typer;
            private String status_desc;
            private List<GoodsBean> goods;
            private String select_pack_month;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getAuth() {
                return auth;
            }

            public void setAuth(String auth) {
                this.auth = auth;
            }

            public String getAuth_time() {
                return auth_time;
            }

            public void setAuth_time(String auth_time) {
                this.auth_time = auth_time;
            }

            public String getOrder_time() {
                return order_time;
            }

            public void setOrder_time(String order_time) {
                this.order_time = order_time;
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

            public String getOrder_title() {
                return order_title;
            }

            public void setOrder_title(String order_title) {
                this.order_title = order_title;
            }

            public String getOrder_fee() {
                return order_fee;
            }

            public void setOrder_fee(String order_fee) {
                this.order_fee = order_fee;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getPay_time() {
                return pay_time;
            }

            public void setPay_time(String pay_time) {
                this.pay_time = pay_time;
            }

            public String getRefund_info() {
                return refund_info;
            }

            public void setRefund_info(String refund_info) {
                this.refund_info = refund_info;
            }

            public String getRefund_color() {
                return refund_color;
            }

            public void setRefund_color(String refund_color) {
                this.refund_color = refund_color;
            }

            public String getTyper() {
                return typer;
            }

            public void setTyper(String typer) {
                this.typer = typer;
            }

            public String getStatus_desc() {
                return status_desc;
            }

            public void setStatus_desc(String status_desc) {
                this.status_desc = status_desc;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public String getSelect_pack_month() {
                return select_pack_month;
            }

            public void setSelect_pack_month(String select_pack_month) {
                this.select_pack_month = select_pack_month;
            }

            public static class GoodsBean {
                /**
                 * name : 电动车
                 * rprice : 0.01
                 */

                private String name;
                private String rprice;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getRprice() {
                    return rprice;
                }

                public void setRprice(String rprice) {
                    this.rprice = rprice;
                }
            }
        }
    }
}
