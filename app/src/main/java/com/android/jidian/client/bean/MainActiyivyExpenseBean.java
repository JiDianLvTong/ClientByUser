package com.android.jidian.client.bean;

import java.util.List;

public class MainActiyivyExpenseBean {

    /**
     * status : 1
     * msg : OK
     * data : {"top":{"labNum":3,"cList":[{"name":"换电币(剩余)","nums":6579,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":2108,"unit":"天","color":"white"},{"name":"电池(过期)","nums":41,"unit":"天","color":"red"}]},"ebike":[{"devid":"30660","name":"电动车（购买）","otype":42,"numt":1,"number":"0","rprice":"3.00","use_type":"10","bg_img":"http://img01.halouhuandian.com/shop/qianbao4.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":1,"fade_status":"0","end_ts":"2020-04-21 10:15:18","checked":0,"mrent":"0.00","relet":"","create_time":"2020-04-21 10:15:18","renttime":"","bike_num":"","vin":"","is_bike":0,"mtitle":""}],"battery":[{"devid":"140194","name":"电池（剩余1061天）","otype":32,"numt":1,"number":"MDBBBGK12AMM0006.450","volt":"60","rprice":"1.20","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"1","btips":"已领取","remark":"已租赁","shop_type":2,"fade_status":"0","end_ts":"2023-04-21 10:25:41","checked":0,"mrent":"6.00","relet":"daily","create_time":"2020-05-11 10:11:23","renttime":"","mtitle":"兴达智联-测试"},{"devid":"140145","name":"电池（过期41天）","otype":32,"numt":1,"number":"","volt":"","rprice":"800.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":2,"fade_status":"0","end_ts":"2020-04-14 15:47:31","checked":2,"mrent":"8.80","relet":"daily","create_time":"2020-04-14 15:47:31","renttime":"","mtitle":"2二级测试商家"}],"umonth":{"packets":{"gid":"1163","merid":"10","otype":"20","name":"包月换电","rprice":"199.00","packet":"10","expire":"2020-11-11 10:16:45","days":170,"bg_img":"http://img01.halouhuandian.com/shop/rent_459.png","tips":"有效期内无限次换电，欢迎使用。","cut":{"order_num":"","froms":"","coid":"","cut_status":"","button":"","is_show":"0","is_show_renew":"1"},"checked":0,"mains":[]},"ucard":[{"gid":"9994","otype":"241","name":"c端循环卡","rprice":"0.00","start_ts":"2020-04-28 11:13:14","end_ts":"2021-03-31 23:59:59","expire":"2021-03-31 23:59:59","content":"","remark":"","rule":"有效期内可免费换电150次，如需再次换电可使用换电币","ltimes":"150.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/month_times_card.png","days":311}]}}
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
         * top : {"labNum":3,"cList":[{"name":"换电币(剩余)","nums":6579,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":2108,"unit":"天","color":"white"},{"name":"电池(过期)","nums":41,"unit":"天","color":"red"}]}
         * ebike : [{"devid":"30660","name":"电动车（购买）","otype":42,"numt":1,"number":"0","rprice":"3.00","use_type":"10","bg_img":"http://img01.halouhuandian.com/shop/qianbao4.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":1,"fade_status":"0","end_ts":"2020-04-21 10:15:18","checked":0,"mrent":"0.00","relet":"","create_time":"2020-04-21 10:15:18","renttime":"","bike_num":"","vin":"","is_bike":0,"mtitle":""}]
         * battery : [{"devid":"140194","name":"电池（剩余1061天）","otype":32,"numt":1,"number":"MDBBBGK12AMM0006.450","volt":"60","rprice":"1.20","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"1","btips":"已领取","remark":"已租赁","shop_type":2,"fade_status":"0","end_ts":"2023-04-21 10:25:41","checked":0,"mrent":"6.00","relet":"daily","create_time":"2020-05-11 10:11:23","renttime":"","mtitle":"兴达智联-测试"},{"devid":"140145","name":"电池（过期41天）","otype":32,"numt":1,"number":"","volt":"","rprice":"800.00","use_type":"20","bg_img":"http://img01.halouhuandian.com/shop/qianbao5.png","is_bind":"2","btips":"待绑定","remark":"待绑定","shop_type":2,"fade_status":"0","end_ts":"2020-04-14 15:47:31","checked":2,"mrent":"8.80","relet":"daily","create_time":"2020-04-14 15:47:31","renttime":"","mtitle":"2二级测试商家"}]
         * umonth : {"packets":{"gid":"1163","merid":"10","otype":"20","name":"包月换电","rprice":"199.00","packet":"10","expire":"2020-11-11 10:16:45","days":170,"bg_img":"http://img01.halouhuandian.com/shop/rent_459.png","tips":"有效期内无限次换电，欢迎使用。","cut":{"order_num":"","froms":"","coid":"","cut_status":"","button":"","is_show":"0","is_show_renew":"1"},"checked":0,"mains":[]},"ucard":[{"gid":"9994","otype":"241","name":"c端循环卡","rprice":"0.00","start_ts":"2020-04-28 11:13:14","end_ts":"2021-03-31 23:59:59","expire":"2021-03-31 23:59:59","content":"","remark":"","rule":"有效期内可免费换电150次，如需再次换电可使用换电币","ltimes":"150.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/month_times_card.png","days":311}]}
         */

        private TopBean top;
        private UmonthBean umonth;
        private List<EbikeBean> ebike;
        private List<BatteryBean> battery;
        private String servtel;
        private String rentFee;

        public TopBean getTop() {
            return top;
        }

        public void setTop(TopBean top) {
            this.top = top;
        }

        public UmonthBean getUmonth() {
            return umonth;
        }

        public void setUmonth(UmonthBean umonth) {
            this.umonth = umonth;
        }

        public List<EbikeBean> getEbike() {
            return ebike;
        }

        public void setEbike(List<EbikeBean> ebike) {
            this.ebike = ebike;
        }

        public List<BatteryBean> getBattery() {
            return battery;
        }

        public void setBattery(List<BatteryBean> battery) {
            this.battery = battery;
        }

        public String getServtel() {
            return servtel;
        }

        public void setServtel(String servtel) {
            this.servtel = servtel;
        }

        public String getRentFee() {
            return rentFee;
        }

        public void setRentFee(String rentFee) {
            this.rentFee = rentFee;
        }

        public static class TopBean {
            /**
             * labNum : 3
             * cList : [{"name":"换电币(剩余)","nums":6579,"unit":"个","color":"white"},{"name":"包月卡(剩余)","nums":2108,"unit":"天","color":"white"},{"name":"电池(过期)","nums":41,"unit":"天","color":"red"}]
             */

            private int labNum;
            private List<CListBean> cList;

            public int getLabNum() {
                return labNum;
            }

            public void setLabNum(int labNum) {
                this.labNum = labNum;
            }

            public List<CListBean> getCList() {
                return cList;
            }

            public void setCList(List<CListBean> cList) {
                this.cList = cList;
            }

            public static class CListBean {
                /**
                 * name : 换电币(剩余)
                 * nums : 6579
                 * unit : 个
                 * color : white
                 */

                private String name;
                private int nums;
                private String unit;
                private String color;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getNums() {
                    return nums;
                }

                public void setNums(int nums) {
                    this.nums = nums;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }
            }
        }

        public static class UmonthBean {
            /**
             * packets : {"gid":"1163","merid":"10","otype":"20","name":"包月换电","rprice":"199.00","packet":"10","expire":"2020-11-11 10:16:45","days":170,"bg_img":"http://img01.halouhuandian.com/shop/rent_459.png","tips":"有效期内无限次换电，欢迎使用。","cut":{"order_num":"","froms":"","coid":"","cut_status":"","button":"","is_show":"0","is_show_renew":"1"},"checked":0,"mains":[]}
             * ucard : [{"gid":"9994","otype":"241","name":"c端循环卡","rprice":"0.00","start_ts":"2020-04-28 11:13:14","end_ts":"2021-03-31 23:59:59","expire":"2021-03-31 23:59:59","content":"","remark":"","rule":"有效期内可免费换电150次，如需再次换电可使用换电币","ltimes":"150.00","bg_img":"http://img01.halouhuandian.com/app/2019/goods/month_times_card.png","days":311}]
             */

            private PacketsBean packets;
            private List<UcardBean> ucard;

            public PacketsBean getPackets() {
                return packets;
            }

            public void setPackets(PacketsBean packets) {
                this.packets = packets;
            }

            public List<UcardBean> getUcard() {
                return ucard;
            }

            public void setUcard(List<UcardBean> ucard) {
                this.ucard = ucard;
            }

            public static class PacketsBean {
                /**
                 * gid : 1163
                 * merid : 10
                 * otype : 20
                 * name : 包月换电
                 * rprice : 199.00
                 * packet : 10
                 * expire : 2020-11-11 10:16:45
                 * days : 170
                 * bg_img : http://img01.halouhuandian.com/shop/rent_459.png
                 * tips : 有效期内无限次换电，欢迎使用。
                 * cut : {"order_num":"","froms":"","coid":"","cut_status":"","button":"","is_show":"0","is_show_renew":"1"}
                 * checked : 0
                 * mains : []
                 */

                private String gid;
                private String merid;
                private String otype;
                private String name;
                private String rprice;
                private String packet;
                private String expire;
                private int days;
                private String bg_img;
                private String tips;
                private CutBean cut;
                private int checked;
                private String show_type;
                private int ischecked;
                private List<?> mains;
                private String isexpire;//0=未过期，1=已过期

                public String getGid() {
                    return gid;
                }

                public void setGid(String gid) {
                    this.gid = gid;
                }

                public String getMerid() {
                    return merid;
                }

                public void setMerid(String merid) {
                    this.merid = merid;
                }

                public String getOtype() {
                    return otype;
                }

                public void setOtype(String otype) {
                    this.otype = otype;
                }

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

                public String getPacket() {
                    return packet;
                }

                public void setPacket(String packet) {
                    this.packet = packet;
                }

                public String getExpire() {
                    return expire;
                }

                public void setExpire(String expire) {
                    this.expire = expire;
                }

                public int getDays() {
                    return days;
                }

                public void setDays(int days) {
                    this.days = days;
                }

                public String getBg_img() {
                    return bg_img;
                }

                public void setBg_img(String bg_img) {
                    this.bg_img = bg_img;
                }

                public String getTips() {
                    return tips;
                }

                public void setTips(String tips) {
                    this.tips = tips;
                }

                public CutBean getCut() {
                    return cut;
                }

                public void setCut(CutBean cut) {
                    this.cut = cut;
                }

                public int getChecked() {
                    return checked;
                }

                public void setChecked(int checked) {
                    this.checked = checked;
                }

                public int getIschecked() {
                    return ischecked;
                }

                public void setIschecked(int ischecked) {
                    this.ischecked = ischecked;
                }

                public List<?> getMains() {
                    return mains;
                }

                public void setMains(List<?> mains) {
                    this.mains = mains;
                }

                public String getShow_type() {
                    return show_type;
                }

                public void setShow_type(String show_type) {
                    this.show_type = show_type;
                }

                public String getIsexpire() {
                    return isexpire;
                }

                public void setIsexpire(String isexpire) {
                    this.isexpire = isexpire;
                }

                public static class CutBean {
                    /**
                     * order_num :
                     * froms :
                     * coid :
                     * cut_status :
                     * button :
                     * is_show : 0
                     * is_show_renew : 1
                     */

                    private String order_num;
                    private String froms;
                    private String coid;
                    private String cut_status;
                    private String button;
                    private String is_show;
                    private String is_show_renew;

                    public String getOrder_num() {
                        return order_num;
                    }

                    public void setOrder_num(String order_num) {
                        this.order_num = order_num;
                    }

                    public String getFroms() {
                        return froms;
                    }

                    public void setFroms(String froms) {
                        this.froms = froms;
                    }

                    public String getCoid() {
                        return coid;
                    }

                    public void setCoid(String coid) {
                        this.coid = coid;
                    }

                    public String getCut_status() {
                        return cut_status;
                    }

                    public void setCut_status(String cut_status) {
                        this.cut_status = cut_status;
                    }

                    public String getButton() {
                        return button;
                    }

                    public void setButton(String button) {
                        this.button = button;
                    }

                    public String getIs_show() {
                        return is_show;
                    }

                    public void setIs_show(String is_show) {
                        this.is_show = is_show;
                    }

                    public String getIs_show_renew() {
                        return is_show_renew;
                    }

                    public void setIs_show_renew(String is_show_renew) {
                        this.is_show_renew = is_show_renew;
                    }
                }
            }

            public static class UcardBean {
                /**
                 * gid : 9994
                 * otype : 241
                 * name : c端循环卡
                 * rprice : 0.00
                 * start_ts : 2020-04-28 11:13:14
                 * end_ts : 2021-03-31 23:59:59
                 * expire : 2021-03-31 23:59:59
                 * content :
                 * remark :
                 * rule : 有效期内可免费换电150次，如需再次换电可使用换电币
                 * ltimes : 150.00
                 * bg_img : http://img01.halouhuandian.com/app/2019/goods/month_times_card.png
                 * days : 311
                 */

                private String gid;
                private String otype;
                private String name;
                private String rprice;
                private String start_ts;
                private String end_ts;
                private String expire;
                private String content;
                private String remark;
                private int checked;
                private int ischecked;
                private String show_type;
                private String rule;
                private String ltimes;
                private String bg_img;
                private String days;

                public String getGid() {
                    return gid;
                }

                public void setGid(String gid) {
                    this.gid = gid;
                }

                public String getOtype() {
                    return otype;
                }

                public int getIschecked() {
                    return ischecked;
                }

                public void setIschecked(int ischecked) {
                    this.ischecked = ischecked;
                }

                public int getChecked() {
                    return checked;
                }

                public void setChecked(int checked) {
                    this.checked = checked;
                }

                public void setOtype(String otype) {
                    this.otype = otype;
                }

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

                public String getStart_ts() {
                    return start_ts;
                }

                public void setStart_ts(String start_ts) {
                    this.start_ts = start_ts;
                }

                public String getEnd_ts() {
                    return end_ts;
                }

                public void setEnd_ts(String end_ts) {
                    this.end_ts = end_ts;
                }

                public String getExpire() {
                    return expire;
                }

                public void setExpire(String expire) {
                    this.expire = expire;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getRule() {
                    return rule;
                }

                public void setRule(String rule) {
                    this.rule = rule;
                }

                public String getLtimes() {
                    return ltimes;
                }

                public void setLtimes(String ltimes) {
                    this.ltimes = ltimes;
                }

                public String getBg_img() {
                    return bg_img;
                }

                public void setBg_img(String bg_img) {
                    this.bg_img = bg_img;
                }

                public String getDays() {
                    return days;
                }

                public void setDays(String days) {
                    this.days = days;
                }

                public String getShow_type() {
                    return show_type;
                }

                public void setShow_type(String show_type) {
                    this.show_type = show_type;
                }
            }
        }

        public static class EbikeBean {
            /**
             * devid : 30660
             * name : 电动车（购买）
             * otype : 42
             * numt : 1
             * number : 0
             * rprice : 3.00
             * use_type : 10
             * bg_img : http://img01.halouhuandian.com/shop/qianbao4.png
             * is_bind : 2
             * btips : 待绑定
             * remark : 待绑定
             * shop_type : 1
             * fade_status : 0
             * end_ts : 2020-04-21 10:15:18
             * checked : 0
             * mrent : 0.00
             * relet :
             * create_time : 2020-04-21 10:15:18
             * renttime :
             * bike_num :
             * vin :
             * is_bike : 0
             * mtitle :
             */

            private String devid;
            private String name;
            private int otype;
            private int numt;
            private String number;
            private String rprice;
            private String use_type;
            private String bg_img;
            private String is_bind;
            private String btips;
            private String remark;
            private int shop_type;
            private String fade_status;
            private String end_ts;
            private int checked;
            private int ischecked;
            private String mrent;
            private String relet;
            private String create_time;
            private String renttime;
            private String bike_num;
            private String vin;
            private int is_bike;
            private String mtitle;
            private String ext;

            public String getDevid() {
                return devid;
            }

            public void setDevid(String devid) {
                this.devid = devid;
            }

            public int getIschecked() {
                return ischecked;
            }

            public void setIschecked(int ischecked) {
                this.ischecked = ischecked;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOtype() {
                return otype;
            }

            public void setOtype(int otype) {
                this.otype = otype;
            }

            public int getNumt() {
                return numt;
            }

            public void setNumt(int numt) {
                this.numt = numt;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getRprice() {
                return rprice;
            }

            public void setRprice(String rprice) {
                this.rprice = rprice;
            }

            public String getUse_type() {
                return use_type;
            }

            public void setUse_type(String use_type) {
                this.use_type = use_type;
            }

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public String getIs_bind() {
                return is_bind;
            }

            public void setIs_bind(String is_bind) {
                this.is_bind = is_bind;
            }

            public String getBtips() {
                return btips;
            }

            public void setBtips(String btips) {
                this.btips = btips;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public String getFade_status() {
                return fade_status;
            }

            public void setFade_status(String fade_status) {
                this.fade_status = fade_status;
            }

            public String getEnd_ts() {
                return end_ts;
            }

            public void setEnd_ts(String end_ts) {
                this.end_ts = end_ts;
            }

            public int getChecked() {
                return checked;
            }

            public void setChecked(int checked) {
                this.checked = checked;
            }

            public String getMrent() {
                return mrent;
            }

            public void setMrent(String mrent) {
                this.mrent = mrent;
            }

            public String getRelet() {
                return relet;
            }

            public void setRelet(String relet) {
                this.relet = relet;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getRenttime() {
                return renttime;
            }

            public void setRenttime(String renttime) {
                this.renttime = renttime;
            }

            public String getBike_num() {
                return bike_num;
            }

            public void setBike_num(String bike_num) {
                this.bike_num = bike_num;
            }

            public String getVin() {
                return vin;
            }

            public void setVin(String vin) {
                this.vin = vin;
            }

            public int getIs_bike() {
                return is_bike;
            }

            public void setIs_bike(int is_bike) {
                this.is_bike = is_bike;
            }

            public String getMtitle() {
                return mtitle;
            }

            public void setMtitle(String mtitle) {
                this.mtitle = mtitle;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }
        }

        public static class BatteryBean {
            /**
             * devid : 140194
             * name : 电池（剩余1061天）
             * otype : 32
             * numt : 1
             * number : MDBBBGK12AMM0006.450
             * volt : 60
             * rprice : 1.20
             * use_type : 20
             * bg_img : http://img01.halouhuandian.com/shop/qianbao5.png
             * is_bind : 1
             * btips : 已领取
             * remark : 已租赁
             * shop_type : 2
             * fade_status : 0
             * end_ts : 2023-04-21 10:25:41
             * checked : 0
             * mrent : 6.00
             * relet : daily
             * create_time : 2020-05-11 10:11:23
             * renttime :
             * mtitle : 兴达智联-测试
             */

            private String devid;
            private String name;
            private int otype;
            private int numt;
            private String number;
            private String volt;
            private String rprice;
            private String use_type;
            private String bg_img;
            private String is_bind;
            private String btips;
            private String remark;
            private int shop_type;
            private String fade_status;
            private String end_ts;
            private int checked;
            private int ischecked;
            private String mrent;
            private String relet;
            private String create_time;
            private String renttime;
            private String mtitle;
            private String ext;

            public String getDevid() {
                return devid;
            }

            public void setDevid(String devid) {
                this.devid = devid;
            }

            public int getIschecked() {
                return ischecked;
            }

            public void setIschecked(int ischecked) {
                this.ischecked = ischecked;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOtype() {
                return otype;
            }

            public void setOtype(int otype) {
                this.otype = otype;
            }

            public int getNumt() {
                return numt;
            }

            public void setNumt(int numt) {
                this.numt = numt;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getVolt() {
                return volt;
            }

            public void setVolt(String volt) {
                this.volt = volt;
            }

            public String getRprice() {
                return rprice;
            }

            public void setRprice(String rprice) {
                this.rprice = rprice;
            }

            public String getUse_type() {
                return use_type;
            }

            public void setUse_type(String use_type) {
                this.use_type = use_type;
            }

            public String getBg_img() {
                return bg_img;
            }

            public void setBg_img(String bg_img) {
                this.bg_img = bg_img;
            }

            public String getIs_bind() {
                return is_bind;
            }

            public void setIs_bind(String is_bind) {
                this.is_bind = is_bind;
            }

            public String getBtips() {
                return btips;
            }

            public void setBtips(String btips) {
                this.btips = btips;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public String getFade_status() {
                return fade_status;
            }

            public void setFade_status(String fade_status) {
                this.fade_status = fade_status;
            }

            public String getEnd_ts() {
                return end_ts;
            }

            public void setEnd_ts(String end_ts) {
                this.end_ts = end_ts;
            }

            public int getChecked() {
                return checked;
            }

            public void setChecked(int checked) {
                this.checked = checked;
            }

            public String getMrent() {
                return mrent;
            }

            public void setMrent(String mrent) {
                this.mrent = mrent;
            }

            public String getRelet() {
                return relet;
            }

            public void setRelet(String relet) {
                this.relet = relet;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getRenttime() {
                return renttime;
            }

            public void setRenttime(String renttime) {
                this.renttime = renttime;
            }

            public String getMtitle() {
                return mtitle;
            }

            public void setMtitle(String mtitle) {
                this.mtitle = mtitle;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }
        }
    }
}
