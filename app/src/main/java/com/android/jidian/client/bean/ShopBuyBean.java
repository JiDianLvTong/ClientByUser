package com.android.jidian.client.bean;

import java.util.List;

public class ShopBuyBean {

    /**
     * status : 1
     * msg : OK
     * data : {"merchant":{"title":"3三级测试商家"},"category":[{"id":"20","name":"购买","urls":"https://testapp.halouhuandian.com/Goodsv2/buyv3.html"},{"id":"30","name":"租赁","urls":"https://testapp.halouhuandian.com/Goodsv2/rentv3.html"}],"packs":[{"id":"8589","name":"299年卡","otype":"230","rprice":"299.00","oprice":"299.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_299.png","is_sell":1,"is_buy":0,"mains":[{"name":"299年卡","oprice":"299","rprice":"299","size":"-"}]},{"id":"8590","name":"499年卡","otype":"231","rprice":"499.00","oprice":"499.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_499.png","is_sell":1,"is_buy":0,"mains":[{"name":"499年卡","oprice":"499","rprice":"499","size":"-"}]},{"id":"8591","name":"699年卡","otype":"232","rprice":"699.00","oprice":"699.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_699.png","is_sell":1,"is_buy":0,"mains":[{"name":"699年卡","oprice":"699","rprice":"699","size":"-"}]},{"id":"8602","name":"购车套餐B1三级测试商家","otype":"50","oprice":"2400.00","rprice":"2400.00","bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":[{"name":"电动车单售","oprice":"0","rprice":"0","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"}]},{"id":"8604","name":"购车套餐A1","otype":"50","oprice":"1200.00","rprice":"1200.00","bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":[{"name":"电动车单售","oprice":"0","rprice":"0","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"}]},{"id":"4812","name":"充电器单售","otype":"120","oprice":"200.00","rprice":"200.00","bg_img":"http://img01.halouhuandian.com/hello/adm/2019/2019-02/20190228140933_62588_1920x1200.jpg","is_sell":1,"mains":[{"name":"充电器单售","oprice":"200","rprice":"200","size":"-"}]},{"id":"8594","name":"充电器单售","otype":"120","oprice":"260.00","rprice":"260.00","bg_img":"http://img01.halouhuandian.com/hello/app/2018/2018-10/charger.png","is_sell":1,"mains":[{"name":"充电器单售","oprice":"260","rprice":"260","size":"-"}]}]}
     * is_skip : 2
     * tips : 如需购买商品，请联系商家扫描商家二维码购买~
     * errno : E0000
     * error :
     */

    private int status;
    private String msg;
    private DataBean data;
    private int is_skip;
    private String tips;
    private String errno;
    private String error;

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

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * merchant : {"title":"3三级测试商家"}
         * category : [{"id":"20","name":"购买","urls":"https://testapp.halouhuandian.com/Goodsv2/buyv3.html"},{"id":"30","name":"租赁","urls":"https://testapp.halouhuandian.com/Goodsv2/rentv3.html"}]
         * packs : [{"id":"8589","name":"299年卡","otype":"230","rprice":"299.00","oprice":"299.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_299.png","is_sell":1,"is_buy":0,"mains":[{"name":"299年卡","oprice":"299","rprice":"299","size":"-"}]},{"id":"8590","name":"499年卡","otype":"231","rprice":"499.00","oprice":"499.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_499.png","is_sell":1,"is_buy":0,"mains":[{"name":"499年卡","oprice":"499","rprice":"499","size":"-"}]},{"id":"8591","name":"699年卡","otype":"232","rprice":"699.00","oprice":"699.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/goods_699.png","is_sell":1,"is_buy":0,"mains":[{"name":"699年卡","oprice":"699","rprice":"699","size":"-"}]},{"id":"8602","name":"购车套餐B1三级测试商家","otype":"50","oprice":"2400.00","rprice":"2400.00","bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":[{"name":"电动车单售","oprice":"0","rprice":"0","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"}]},{"id":"8604","name":"购车套餐A1","otype":"50","oprice":"1200.00","rprice":"1200.00","bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":[{"name":"电动车单售","oprice":"0","rprice":"0","size":"-"},{"name":"电池单售单价","oprice":"1200","rprice":"1200","size":"-"}]},{"id":"4812","name":"充电器单售","otype":"120","oprice":"200.00","rprice":"200.00","bg_img":"http://img01.halouhuandian.com/hello/adm/2019/2019-02/20190228140933_62588_1920x1200.jpg","is_sell":1,"mains":[{"name":"充电器单售","oprice":"200","rprice":"200","size":"-"}]},{"id":"8594","name":"充电器单售","otype":"120","oprice":"260.00","rprice":"260.00","bg_img":"http://img01.halouhuandian.com/hello/app/2018/2018-10/charger.png","is_sell":1,"mains":[{"name":"充电器单售","oprice":"260","rprice":"260","size":"-"}]}]
         */

        private MerchantBean merchant;
        private List<CategoryBean> category;
        private List<PacksBean> packs;

        public MerchantBean getMerchant() {
            return merchant;
        }

        public void setMerchant(MerchantBean merchant) {
            this.merchant = merchant;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<PacksBean> getPacks() {
            return packs;
        }

        public void setPacks(List<PacksBean> packs) {
            this.packs = packs;
        }

        public static class MerchantBean {
            /**
             * title : 3三级测试商家
             */

            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class CategoryBean {
            /**
             * id : 20
             * name : 购买
             * urls : https://testapp.halouhuandian.com/Goodsv2/buyv3.html
             */

            private String id;
            private String name;
            private String urls;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrls() {
                return urls;
            }

            public void setUrls(String urls) {
                this.urls = urls;
            }
        }

        public static class PacksBean {
            /**
             * id : 8589
             * name : 299年卡
             * otype : 230
             * rprice : 299.00
             * oprice : 299.00
             * bg_img : http://img01.halouhuandian.com/app/2019/goods/goods_299.png
             * is_sell : 1
             * is_buy : 0
             * mains : [{"name":"299年卡","oprice":"299","rprice":"299","size":"-"}]
             */

            private String id;
            private String name;
            private String otype;
            private String rprice;
            private String oprice;
            private String bg_img;
            private String fcolor;
            private int is_sell;
            private int is_buy;
            private List<MainsBean> mains;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOtype() {
                return otype;
            }

            public void setOtype(String otype) {
                this.otype = otype;
            }

            public String getRprice() {
                return rprice;
            }

            public void setRprice(String rprice) {
                this.rprice = rprice;
            }

            public String getOprice() {
                return oprice;
            }

            public void setOprice(String oprice) {
                this.oprice = oprice;
            }

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public int getIs_sell() {
                return is_sell;
            }

            public void setIs_sell(int is_sell) {
                this.is_sell = is_sell;
            }

            public int getIs_buy() {
                return is_buy;
            }

            public void setIs_buy(int is_buy) {
                this.is_buy = is_buy;
            }

            public List<MainsBean> getMains() {
                return mains;
            }

            public void setMains(List<MainsBean> mains) {
                this.mains = mains;
            }

            public String getFcolor() {
                return fcolor;
            }

            public void setFcolor(String fcolor) {
                this.fcolor = fcolor;
            }

            public static class MainsBean {
                /**
                 * name : 299年卡
                 * oprice : 299
                 * rprice : 299
                 * size : -
                 */

                private String name;
                private String oprice;
                private String rprice;
                private String size;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getOprice() {
                    return oprice;
                }

                public void setOprice(String oprice) {
                    this.oprice = oprice;
                }

                public String getRprice() {
                    return rprice;
                }

                public void setRprice(String rprice) {
                    this.rprice = rprice;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }
            }
        }
    }
}
