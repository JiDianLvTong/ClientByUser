package com.android.jidian.client.bean;

import java.util.List;

public class MapBean {

    /**
     * status : 1
     * msg : 请求成功！
     * data : [{"jingdu":"116.316131","weidu":"39.823152","list":[{"id":"95","mer_id":"11","start_time":"08:00","end_time":"22:00","repair":"-1","jingdu":"116.316131","weidu":"39.823152","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"0000-00-00 00:00:00","allow":1,"open_door":0,"open_tip":"已停业","is_24h":0,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.544608","weidu":"39.924109","list":[{"id":"115","mer_id":"10","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.544608","weidu":"39.924109","is_run":"1","battery_num":"7","surplus_num":"0","ontime":"2019-07-30 15:26:53","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0},{"id":"152","mer_id":"10","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.544608","weidu":"39.924109","is_run":"1","battery_num":"8","surplus_num":"0","ontime":"2019-07-12 08:49:52","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0},{"id":"160","mer_id":"28","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.544608","weidu":"39.924109","is_run":"0","battery_num":"1","surplus_num":"0","ontime":"2019-07-06 16:13:23","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0},{"id":"176","mer_id":"11","start_time":"00:00","end_time":"16:37","repair":"-1","jingdu":"116.544608","weidu":"39.924109","is_run":"0","battery_num":"2","surplus_num":"0","ontime":"2019-08-09 18:33:42","allow":1,"open_door":0,"open_tip":"已停业","is_24h":0,"img_idx":2,"btype":-1,"usable":0},{"id":"177","mer_id":"10","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.544608","weidu":"39.924109","is_run":"1","battery_num":"7","surplus_num":"0","ontime":"2019-08-16 14:38:24","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.45206","weidu":"39.95429","list":[{"id":"120","mer_id":"17","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.45206","weidu":"39.95429","is_run":"1","battery_num":"2","surplus_num":"0","ontime":"0000-00-00 00:00:00","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.500560","weidu":"40.024111","list":[{"id":"427","mer_id":"139","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.500560","weidu":"40.024111","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"2019-07-26 20:45:22","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.322460","weidu":"40.042612","list":[{"id":"431","mer_id":"11","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.322460","weidu":"40.042612","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"0000-00-00 00:00:00","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.581152","weidu":"39.921596","list":[{"id":"798","mer_id":"11","start_time":"00:00","end_time":"24:00","repair":"2","jingdu":"116.581152","weidu":"39.921596","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"2019-07-23 13:41:47","allow":1,"open_door":1,"open_tip":"营业中","is_24h":1,"img_idx":5,"btype":2,"usable":0},{"id":"1125","mer_id":"11","start_time":"00:00","end_time":"24:00","repair":"-1","jingdu":"116.581152","weidu":"39.921596","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"2019-07-08 15:43:13","allow":1,"open_door":0,"open_tip":"已停业","is_24h":1,"img_idx":2,"btype":-1,"usable":0}]},{"jingdu":"116.545010","weidu":"39.924063","list":[{"id":"1232","mer_id":"11","start_time":"00:00","end_time":"24:00","repair":"2","jingdu":"116.545010","weidu":"39.924063","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"2019-07-11 15:32:53","allow":1,"open_door":1,"open_tip":"营业中","is_24h":1,"img_idx":5,"btype":2,"usable":0}]}]
     * img_list : ["http://img01.halouhuandian.com/app/2019/site/24_service_site2_v2.png","http://img01.halouhuandian.com/app/2019/site/24_site_v2.png","http://img01.halouhuandian.com/app/2019/site/disable.png","http://img01.halouhuandian.com/app/2019/site/un_24_service_site2_v2.png","http://img01.halouhuandian.com/app/2019/site/un_24_site_v2.png","http://img01.halouhuandian.com/app/2019/site/24_only_repair.png","http://img01.halouhuandian.com/app/2019/site/un_24_only_repair.png","http://img01.halouhuandian.com/app/2019/site/24_only_store.png","http://img01.halouhuandian.com/app/2019/site/un_24_only_store.png"]
     */

    private int status;
    private String msg;
    private List<DataBean> data;
    private List<String> img_list;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    public static class DataBean {
        /**
         * jingdu : 116.316131
         * weidu : 39.823152
         * list : [{"id":"95","mer_id":"11","start_time":"08:00","end_time":"22:00","repair":"-1","jingdu":"116.316131","weidu":"39.823152","is_run":"1","battery_num":"1","surplus_num":"0","ontime":"0000-00-00 00:00:00","allow":1,"open_door":0,"open_tip":"已停业","is_24h":0,"img_idx":2,"btype":-1,"usable":0}]
         */

        private String jingdu;
        private String weidu;
        private List<ListBean> list;

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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 95
             * mer_id : 11
             * start_time : 08:00
             * end_time : 22:00
             * repair : -1
             * jingdu : 116.316131
             * weidu : 39.823152
             * is_run : 1
             * battery_num : 1
             * surplus_num : 0
             * ontime : 0000-00-00 00:00:00
             * allow : 1
             * open_door : 0
             * open_tip : 已停业
             * is_24h : 0
             * img_idx : 2
             * btype : -1
             * usable : 0
             */

            private String id;
            private String mer_id;
            private String start_time;
            private String end_time;
            private String repair;
            private String jingdu;
            private String weidu;
            private String is_run;
            private String battery_num;
            private String surplus_num;
            private String ontime;
            private int allow;
            private String volt_g;
            private int open_door;
            private String open_tip;
            private int is_24h;
            private int img_idx;
            private int btype;
            private int usable;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMer_id() {
                return mer_id;
            }

            public void setMer_id(String mer_id) {
                this.mer_id = mer_id;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getRepair() {
                return repair;
            }

            public void setRepair(String repair) {
                this.repair = repair;
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

            public String getIs_run() {
                return is_run;
            }

            public void setIs_run(String is_run) {
                this.is_run = is_run;
            }

            public String getBattery_num() {
                return battery_num;
            }

            public void setBattery_num(String battery_num) {
                this.battery_num = battery_num;
            }

            public String getSurplus_num() {
                return surplus_num;
            }

            public void setSurplus_num(String surplus_num) {
                this.surplus_num = surplus_num;
            }

            public String getOntime() {
                return ontime;
            }

            public void setOntime(String ontime) {
                this.ontime = ontime;
            }

            public String getVolt_g() {
                return volt_g;
            }

            public void setVolt_g(String volt_g) {
                this.volt_g = volt_g;
            }

            public int getAllow() {
                return allow;
            }

            public void setAllow(int allow) {
                this.allow = allow;
            }

            public int getOpen_door() {
                return open_door;
            }

            public void setOpen_door(int open_door) {
                this.open_door = open_door;
            }

            public String getOpen_tip() {
                return open_tip;
            }

            public void setOpen_tip(String open_tip) {
                this.open_tip = open_tip;
            }

            public int getIs_24h() {
                return is_24h;
            }

            public void setIs_24h(int is_24h) {
                this.is_24h = is_24h;
            }

            public int getImg_idx() {
                return img_idx;
            }

            public void setImg_idx(int img_idx) {
                this.img_idx = img_idx;
            }

            public int getBtype() {
                return btype;
            }

            public void setBtype(int btype) {
                this.btype = btype;
            }

            public int getUsable() {
                return usable;
            }

            public void setUsable(int usable) {
                this.usable = usable;
            }
        }
    }
}
