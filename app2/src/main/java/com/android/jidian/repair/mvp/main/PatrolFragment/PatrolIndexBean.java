package com.android.jidian.repair.mvp.main.PatrolFragment;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/13 11:52
 * description:
 */
public class PatrolIndexBean {
    /**
     * status : 1
     * msg : OK
     * errno : E0000
     * error :
     * data : {"lists":[{"id":"68","name":"北京办公电柜","address":"暂无地址","jingdu":"116.544608","weidu":"39.924109","images":"[]","bgimg":null,"distance":12249.9},{"id":"45","name":"燕山电动车维修部","address":"皇姑区燕山路10号","jingdu":"123.439803","weidu":"41.831002","images":"[\"ape\\/2022\\/12\\/05_05_20221206165308_85577_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221206165311_24576_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221206165339_85637_540x1188.jpg\"]","bgimg":"ape/2022/12/05_05_20221206165308_85577_1040x780.jpg","distance":12717.45},{"id":"43","name":"兔悠便利店","address":"皇姑区鸭绿江街63","jingdu":"123.457890","weidu":"41.849482","images":"[\"ape\\/2022\\/12\\/05_05_20221205164957_81600_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221205165217_70153_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221205165755_20406_540x1188.jpg\"]","bgimg":"ape/2022/12/05_05_20221205164957_81600_1040x780.jpg","distance":12717.99},{"id":"41","name":"乔伊便利店","address":"皇姑区鸭绿江街56-2号10门","jingdu":"123.454625","weidu":"41.846740","images":"[\"ape\\/2022\\/12\\/02_02_20221205180922_35546_1040x780.jpg\",\"ape\\/2022\\/12\\/02_02_20221205180953_87453_540x1188.jpg\",\"ape\\/2022\\/12\\/02_02_20221205181543_18800_1000x750.jpg\"]","bgimg":"ape/2022/12/02_02_20221205180922_35546_1040x780.jpg","distance":12717.86},{"id":"39","name":"快林嘉福超市","address":"皇姑区嘉陵江街63号","jingdu":"123.417244","weidu":"41.821561","images":"[\"ape\\/2022\\/12\\/04_04_20221206161510_70149_1040x780.jpg\",\"ape\\/2022\\/12\\/04_04_20221206161514_67628_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221206161535_92206_540x1188.jpg\"]","bgimg":"ape/2022/12/04_04_20221206161510_70149_1040x780.jpg","distance":12716.16},{"id":"38","name":"优客便利店","address":"皇姑区峨眉山路35号","jingdu":"123.394269","weidu":"41.825121","images":"[\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155559_74855_1040x780.jpg\",\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155602_71855_1000x750.jpg\",\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155605_95725_540x1188.jpg\"]","bgimg":"https://img03.mixiangx.com/ape/2022/12/03_03_20221206155559_74855_1040x780.jpg","distance":12714.25},{"id":"25","name":"鑫宇快餐店","address":"沈河区小北关街96-6号","jingdu":"123.450598","weidu":"41.813573","images":"[\"ape\\/2022\\/12\\/03_03_20221207172934_71595_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221207172938_14684_750x1000.jpg\",\"ape\\/2022\\/12\\/03_03_20221207173016_76994_540x1188.jpg\"]","bgimg":"ape/2022/12/03_03_20221207172934_71595_1000x750.jpg","distance":12719.05},{"id":"15","name":"鼎盛烟酒商行","address":"皇姑区长江街134-2门","jingdu":"123.411422","weidu":"41.834570","images":"[\"ape\\/2022\\/12\\/04_04_20221203153204_33541_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203153208_76986_750x1000.jpg\",\"ape\\/2022\\/12\\/04_04_20221203153213_90155_750x1000.jpg\"]","bgimg":"ape/2022/12/04_04_20221203153204_33541_1000x750.jpg","distance":12715.13},{"id":"12","name":"美吉纯鲜奶坊(三台子店)","address":"皇姑区阳山路23号1门","jingdu":"123.422501","weidu":"41.881473","images":"[\"ape\\/2022\\/12\\/04_04_20221203121510_11333_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203121515_93840_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203121519_55234_1000x750.jpg\"]","bgimg":"ape/2022/12/04_04_20221203121510_11333_1000x750.jpg","distance":12713.87},{"id":"11","name":"电柜自动上传","address":"暂无地址","jingdu":"123.388903","weidu":"41.823092","images":"","bgimg":null,"distance":12713.94},{"id":"10","name":"鼎鑫便利店","address":"沈阳市沈北皇姑区黄河北大街286","jingdu":"123.400920","weidu":"41.905653","images":"[\"ape\\/2022\\/12\\/03_03_20221201144651_70468_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221201144640_7588_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221201144701_74481_1000x750.jpg\"]","bgimg":"ape/2022/12/03_03_20221201144651_70468_1000x750.jpg","distance":12711.15},{"id":"9","name":"于洪区格林豪泰电动车行","address":"沈阳市于洪区黄河大街206号","jingdu":"123.385167","weidu":"41.892416","images":"[\"ape\\/2022\\/12\\/03_03_20221204163516_97691_1040x780.jpg\",\"ape\\/2022\\/12\\/03_03_20221204163543_22548_540x1188.jpg\",\"ape\\/2022\\/12\\/03_03_20221204163547_43325_1040x780.jpg\"]","bgimg":"ape/2022/12/03_03_20221204163516_97691_1040x780.jpg","distance":12710.55},{"id":"8","name":"电柜自动上传","address":"暂无地址","jingdu":"123.447604","weidu":"41.925165","images":"","bgimg":null,"distance":12713.81},{"id":"7","name":"杨国福麻辣烫(文大路店)","address":"皇姑区文大路221-1号19门","jingdu":"123.394091","weidu":"41.901324","images":"[\"ape\\/2022\\/12\\/05_05_20221203174427_22582_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221203174431_16389_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221203174436_76849_1000x750.jpg\"]","bgimg":"ape/2022/12/05_05_20221203174427_22582_1000x750.jpg","distance":12710.82},{"id":"6","name":"格林豪泰电动车行","address":"沈阳市沈北新区正良四路28-6号2门","jingdu":"123.391175","weidu":"41.916284","images":"[\"ape\\/2022\\/12\\/02_02_20221201123500_21265_1000x750.jpg\",\"ape\\/2022\\/12\\/02_02_20221201123510_92313_1000x750.jpg\",\"ape\\/2022\\/12\\/02_02_20221201123522_40434_1000x750.jpg\"]","bgimg":"ape/2022/12/02_02_20221201123500_21265_1000x750.jpg","distance":12709.93}],"page":1}
     */

    private String status;
    private String msg;
    private String errno;
    private String error;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lists : [{"id":"68","name":"北京办公电柜","address":"暂无地址","jingdu":"116.544608","weidu":"39.924109","images":"[]","bgimg":null,"distance":12249.9},{"id":"45","name":"燕山电动车维修部","address":"皇姑区燕山路10号","jingdu":"123.439803","weidu":"41.831002","images":"[\"ape\\/2022\\/12\\/05_05_20221206165308_85577_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221206165311_24576_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221206165339_85637_540x1188.jpg\"]","bgimg":"ape/2022/12/05_05_20221206165308_85577_1040x780.jpg","distance":12717.45},{"id":"43","name":"兔悠便利店","address":"皇姑区鸭绿江街63","jingdu":"123.457890","weidu":"41.849482","images":"[\"ape\\/2022\\/12\\/05_05_20221205164957_81600_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221205165217_70153_1040x780.jpg\",\"ape\\/2022\\/12\\/05_05_20221205165755_20406_540x1188.jpg\"]","bgimg":"ape/2022/12/05_05_20221205164957_81600_1040x780.jpg","distance":12717.99},{"id":"41","name":"乔伊便利店","address":"皇姑区鸭绿江街56-2号10门","jingdu":"123.454625","weidu":"41.846740","images":"[\"ape\\/2022\\/12\\/02_02_20221205180922_35546_1040x780.jpg\",\"ape\\/2022\\/12\\/02_02_20221205180953_87453_540x1188.jpg\",\"ape\\/2022\\/12\\/02_02_20221205181543_18800_1000x750.jpg\"]","bgimg":"ape/2022/12/02_02_20221205180922_35546_1040x780.jpg","distance":12717.86},{"id":"39","name":"快林嘉福超市","address":"皇姑区嘉陵江街63号","jingdu":"123.417244","weidu":"41.821561","images":"[\"ape\\/2022\\/12\\/04_04_20221206161510_70149_1040x780.jpg\",\"ape\\/2022\\/12\\/04_04_20221206161514_67628_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221206161535_92206_540x1188.jpg\"]","bgimg":"ape/2022/12/04_04_20221206161510_70149_1040x780.jpg","distance":12716.16},{"id":"38","name":"优客便利店","address":"皇姑区峨眉山路35号","jingdu":"123.394269","weidu":"41.825121","images":"[\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155559_74855_1040x780.jpg\",\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155602_71855_1000x750.jpg\",\"https:\\/\\/img03.mixiangx.com\\/ape\\/2022\\/12\\/03_03_20221206155605_95725_540x1188.jpg\"]","bgimg":"https://img03.mixiangx.com/ape/2022/12/03_03_20221206155559_74855_1040x780.jpg","distance":12714.25},{"id":"25","name":"鑫宇快餐店","address":"沈河区小北关街96-6号","jingdu":"123.450598","weidu":"41.813573","images":"[\"ape\\/2022\\/12\\/03_03_20221207172934_71595_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221207172938_14684_750x1000.jpg\",\"ape\\/2022\\/12\\/03_03_20221207173016_76994_540x1188.jpg\"]","bgimg":"ape/2022/12/03_03_20221207172934_71595_1000x750.jpg","distance":12719.05},{"id":"15","name":"鼎盛烟酒商行","address":"皇姑区长江街134-2门","jingdu":"123.411422","weidu":"41.834570","images":"[\"ape\\/2022\\/12\\/04_04_20221203153204_33541_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203153208_76986_750x1000.jpg\",\"ape\\/2022\\/12\\/04_04_20221203153213_90155_750x1000.jpg\"]","bgimg":"ape/2022/12/04_04_20221203153204_33541_1000x750.jpg","distance":12715.13},{"id":"12","name":"美吉纯鲜奶坊(三台子店)","address":"皇姑区阳山路23号1门","jingdu":"123.422501","weidu":"41.881473","images":"[\"ape\\/2022\\/12\\/04_04_20221203121510_11333_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203121515_93840_1000x750.jpg\",\"ape\\/2022\\/12\\/04_04_20221203121519_55234_1000x750.jpg\"]","bgimg":"ape/2022/12/04_04_20221203121510_11333_1000x750.jpg","distance":12713.87},{"id":"11","name":"电柜自动上传","address":"暂无地址","jingdu":"123.388903","weidu":"41.823092","images":"","bgimg":null,"distance":12713.94},{"id":"10","name":"鼎鑫便利店","address":"沈阳市沈北皇姑区黄河北大街286","jingdu":"123.400920","weidu":"41.905653","images":"[\"ape\\/2022\\/12\\/03_03_20221201144651_70468_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221201144640_7588_1000x750.jpg\",\"ape\\/2022\\/12\\/03_03_20221201144701_74481_1000x750.jpg\"]","bgimg":"ape/2022/12/03_03_20221201144651_70468_1000x750.jpg","distance":12711.15},{"id":"9","name":"于洪区格林豪泰电动车行","address":"沈阳市于洪区黄河大街206号","jingdu":"123.385167","weidu":"41.892416","images":"[\"ape\\/2022\\/12\\/03_03_20221204163516_97691_1040x780.jpg\",\"ape\\/2022\\/12\\/03_03_20221204163543_22548_540x1188.jpg\",\"ape\\/2022\\/12\\/03_03_20221204163547_43325_1040x780.jpg\"]","bgimg":"ape/2022/12/03_03_20221204163516_97691_1040x780.jpg","distance":12710.55},{"id":"8","name":"电柜自动上传","address":"暂无地址","jingdu":"123.447604","weidu":"41.925165","images":"","bgimg":null,"distance":12713.81},{"id":"7","name":"杨国福麻辣烫(文大路店)","address":"皇姑区文大路221-1号19门","jingdu":"123.394091","weidu":"41.901324","images":"[\"ape\\/2022\\/12\\/05_05_20221203174427_22582_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221203174431_16389_1000x750.jpg\",\"ape\\/2022\\/12\\/05_05_20221203174436_76849_1000x750.jpg\"]","bgimg":"ape/2022/12/05_05_20221203174427_22582_1000x750.jpg","distance":12710.82},{"id":"6","name":"格林豪泰电动车行","address":"沈阳市沈北新区正良四路28-6号2门","jingdu":"123.391175","weidu":"41.916284","images":"[\"ape\\/2022\\/12\\/02_02_20221201123500_21265_1000x750.jpg\",\"ape\\/2022\\/12\\/02_02_20221201123510_92313_1000x750.jpg\",\"ape\\/2022\\/12\\/02_02_20221201123522_40434_1000x750.jpg\"]","bgimg":"ape/2022/12/02_02_20221201123500_21265_1000x750.jpg","distance":12709.93}]
         * page : 1
         */

        private List<ListsBean> lists;
        private String page;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public static class ListsBean {
            /**
             * id : 68
             * name : 北京办公电柜
             * address : 暂无地址
             * jingdu : 116.544608
             * weidu : 39.924109
             * images : []
             * bgimg : null
             * distance : 12249.9
             */

            private String id;
            private String name;
            private String address;
            private String jingdu;
            private String weidu;
            private String images;
            private Object bgimg;
            private double distance;

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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getJingdu() {
                return jingdu;
            }

            public void setJingdu(String jingdu) {
                this.jingdu = jingdu;
            }

            public String getWeidu() {
                return weidu;
            }

            public void setWeidu(String weidu) {
                this.weidu = weidu;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public Object getBgimg() {
                return bgimg;
            }

            public void setBgimg(Object bgimg) {
                this.bgimg = bgimg;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }
        }
    }
}
