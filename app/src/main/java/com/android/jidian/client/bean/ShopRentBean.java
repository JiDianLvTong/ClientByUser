package com.android.jidian.client.bean;

import java.util.List;

public class ShopRentBean {

    /**
     * status : 1
     * msg : OK
     * data : {"merchant":{"title":"3三级测试商家"},"category":[{"id":"20","name":"购买","urls":"https://testapp.halouhuandian.com/Goodsv2/buyv3.html"},{"id":"30","name":"租赁","urls":"https://testapp.halouhuandian.com/Goodsv2/rentv3.html"}],"packs":[{"id":"4815","name":"三级0元设置分成--套餐","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车","oprice":"900.00","rprice":"900.00","size":"-"},{"name":"锂电池","oprice":"800.00","rprice":"800.00","size":"60v"}]},"oprice":"1700.00","rprice":"1700.00"},{"id":"8603","name":"租车押金套餐B1","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车押金","oprice":"0.00","rprice":"0.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"}]},"oprice":"1200.00","rprice":"1200.00"},{"id":"8605","name":"租车套餐A","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车押金","oprice":"0.00","rprice":"0.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"}]},"oprice":"600.00","rprice":"600.00"},{"id":"9917","name":"三级0元设置分成--单租车","otype":"41","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车","oprice":"0","rprice":"0","size":"-"}]},"oprice":"0.00","rprice":"0.00"}]}
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
         * packs : [{"id":"4815","name":"三级0元设置分成--套餐","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车","oprice":"900.00","rprice":"900.00","size":"-"},{"name":"锂电池","oprice":"800.00","rprice":"800.00","size":"60v"}]},"oprice":"1700.00","rprice":"1700.00"},{"id":"8603","name":"租车押金套餐B1","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike2.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车押金","oprice":"0.00","rprice":"0.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"}]},"oprice":"1200.00","rprice":"1200.00"},{"id":"8605","name":"租车套餐A","otype":"60","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车押金","oprice":"0.00","rprice":"0.00","size":"-"},{"name":"电池单租押金","oprice":"600.00","rprice":"600.00","size":"-"}]},"oprice":"600.00","rprice":"600.00"},{"id":"9917","name":"三级0元设置分成--单租车","otype":"41","type":1,"bg_img":"http://img01.halouhuandian.com/shop/buy_bike1.png","is_sell":1,"mains":{"right":[],"left":[{"name":"电动车","oprice":"0","rprice":"0","size":"-"}]},"oprice":"0.00","rprice":"0.00"}]
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
             * id : 4815
             * name : 三级0元设置分成--套餐
             * otype : 60
             * type : 1
             * bg_img : http://img01.halouhuandian.com/shop/buy_bike1.png
             * is_sell : 1
             * mains : {"right":[],"left":[{"name":"电动车","oprice":"900.00","rprice":"900.00","size":"-"},{"name":"锂电池","oprice":"800.00","rprice":"800.00","size":"60v"}]}
             * oprice : 1700.00
             * rprice : 1700.00
             */

            private String id;
            private String name;
            private String otype;
            private int type;
            private String bg_img;
            private int is_sell;
            private String fcolor;
            private MainsBean mains;
            private String oprice;
            private String rprice;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public MainsBean getMains() {
                return mains;
            }

            public void setMains(MainsBean mains) {
                this.mains = mains;
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

            public String getFcolor() {
                return fcolor;
            }

            public void setFcolor(String fcolor) {
                this.fcolor = fcolor;
            }

            public static class MainsBean {
                private List<?> right;
                private List<LeftBean> left;

                public List<?> getRight() {
                    return right;
                }

                public void setRight(List<?> right) {
                    this.right = right;
                }

                public List<LeftBean> getLeft() {
                    return left;
                }

                public void setLeft(List<LeftBean> left) {
                    this.left = left;
                }

                public static class LeftBean {
                    /**
                     * name : 电动车
                     * oprice : 900.00
                     * rprice : 900.00
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
}
