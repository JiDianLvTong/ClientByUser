package com.android.jidian.client.bean;

import java.util.List;

public class MapTopTabBean {

    /**
     * status : 1
     * msg : OK~
     * topTab : [{"tab":1,"name":"换电","icon":"https://img01.halouhuandian.com/hello/adm/2020/20200603164845_20121_186x186.jpg"},{"tab":2,"name":"租车","icon":"https://img01.halouhuandian.com/hello/adm/2020/20200603164909_6572_186x186.jpg"},{"tab":3,"name":"买车","icon":"https://img01.halouhuandian.com/hello/adm/2020/20200603164936_84803_186x186.jpg"},{"tab":4,"name":"还车","icon":"https://img01.halouhuandian.com/hello/adm/2020/20200603164958_57851_186x186.jpg"},{"tab":5,"name":"维修","icon":"https://img01.halouhuandian.com/hello/adm/2020/20200603165015_38632_186x186.jpg"}]
     */

    private int status;
    private String msg;
    private List<TopTabBean> topTab;

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

    public List<TopTabBean> getTopTab() {
        return topTab;
    }

    public void setTopTab(List<TopTabBean> topTab) {
        this.topTab = topTab;
    }

    public static class TopTabBean {
        /**
         * tab : 1
         * name : 换电
         * icon : https://img01.halouhuandian.com/hello/adm/2020/20200603164845_20121_186x186.jpg
         */

        private int tab;
        private String name;
        private String icon;

        public int getTab() {
            return tab;
        }

        public void setTab(int tab) {
            this.tab = tab;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
