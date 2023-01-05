package com.android.jidian.client.bean;

import java.util.List;

public class ContactV2Bean {
    /**
     * status : 1
     * msg : 获取成功！
     * data : [{"name":"客服电话","is_show":1,"contact":"400-6060-137","_contact":"4006060137"},{"name":"客服电话2","is_show":1,"contact":"400-6060-138","_contact":"4006060138"}]
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 客服电话
         * is_show : 1
         * contact : 400-6060-137
         * _contact : 4006060137
         */

        private String name;
        private int is_show;
        private String contact;
        private String _contact;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String get_contact() {
            return _contact;
        }

        public void set_contact(String _contact) {
            this._contact = _contact;
        }
    }
}
