package com.android.jidian.client.bean;

/**
 * @author : xiaoming
 * date: 2021/11/27 下午1:37
 * company: 兴达智联
 * description:
 */
public class PullActivityIndexBean {
    /**
     * status : 1
     * msg : 请求成功~
     * data : []
     * activity : {"big_url":"https://img01.halouhuandian.com/activity/2019/eject3.png","big_show":"1","big_click":"1","big_jump":"https://testh5.halouhuandian.com/Polite/index.html","small_url":"https://img01.halouhuandian.com/activity/2019/small_img3.png","small_show":"1","show_rate":"1","dtime":"2","aid":"2264525"}
     */

    private String status;
    private String msg;
    private ActivityBean activity;

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

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public static class ActivityBean {
        /**
         * big_url : https://img01.halouhuandian.com/activity/2019/eject3.png
         * big_show : 1
         * big_click : 1
         * big_jump : https://testh5.halouhuandian.com/Polite/index.html
         * small_url : https://img01.halouhuandian.com/activity/2019/small_img3.png
         * small_show : 1
         * show_rate : 1
         * dtime : 2
         * aid : 2264525
         */

        private String big_url;
        private String big_show;
        private String big_click;
        private String big_jump;
        private String small_url;
        private String small_show;
        private String show_rate;
        private String dtime;
        private String aid;

        public String getBig_url() {
            return big_url;
        }

        public void setBig_url(String big_url) {
            this.big_url = big_url;
        }

        public String getBig_show() {
            return big_show;
        }

        public void setBig_show(String big_show) {
            this.big_show = big_show;
        }

        public String getBig_click() {
            return big_click;
        }

        public void setBig_click(String big_click) {
            this.big_click = big_click;
        }

        public String getBig_jump() {
            return big_jump;
        }

        public void setBig_jump(String big_jump) {
            this.big_jump = big_jump;
        }

        public String getSmall_url() {
            return small_url;
        }

        public void setSmall_url(String small_url) {
            this.small_url = small_url;
        }

        public String getSmall_show() {
            return small_show;
        }

        public void setSmall_show(String small_show) {
            this.small_show = small_show;
        }

        public String getShow_rate() {
            return show_rate;
        }

        public void setShow_rate(String show_rate) {
            this.show_rate = show_rate;
        }

        public String getDtime() {
            return dtime;
        }

        public void setDtime(String dtime) {
            this.dtime = dtime;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }
    }
}
