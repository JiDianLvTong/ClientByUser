package com.android.jidian.client.bean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EvaluateLabsBean {

    /**
     * status : 1
     * msg : OK
     * data : [{"id":"1","name":"换电很快","color":"8B008B"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"4","name":"电动车续航保障","color":"0000FF"},{"id":"5","name":"妈妈再也不用担心我的电动车跑不回家了","color":"555555"},{"id":"6","name":"真的很好用","color":"90EE90"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    @NotNull
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 换电很快
         * color : 8B008B
         */

        private String id;
        private String name;
        private String color;

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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
