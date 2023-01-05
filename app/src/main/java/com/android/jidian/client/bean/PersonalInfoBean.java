package com.android.jidian.client.bean;

/**
 * @author : PTT
 * date: 2020/9/23 15:30
 * company: 兴达智联
 * description:
 */
public class PersonalInfoBean {
    private int status;
    private String msg;
    private DataBean data;

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

    public class DataBean {
        /**
         * 手机号
         */
        private String phone;
        private String real_name;
        private String auth;
        private String id_card;
        /**
         * 头像
         */
        private String avater;
        private String id_auth;
        private String id_auth_tip;
        private String uid;
        private String typer;
        private String front_img;
        private String reverse_img;
        private String audit_status;
        private String ali_face_url;
        private String ali_face_appcode;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getId_card() {
            return id_card;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        public String getId_auth() {
            return id_auth;
        }

        public void setId_auth(String id_auth) {
            this.id_auth = id_auth;
        }

        public String getId_auth_tip() {
            return id_auth_tip;
        }

        public void setId_auth_tip(String id_auth_tip) {
            this.id_auth_tip = id_auth_tip;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTyper() {
            return typer;
        }

        public void setTyper(String typer) {
            this.typer = typer;
        }

        public String getFront_img() {
            return front_img;
        }

        public void setFront_img(String front_img) {
            this.front_img = front_img;
        }

        public String getReverse_img() {
            return reverse_img;
        }

        public void setReverse_img(String reverse_img) {
            this.reverse_img = reverse_img;
        }

        public String getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(String audit_status) {
            this.audit_status = audit_status;
        }

        public String getAli_face_url() {
            return ali_face_url;
        }

        public void setAli_face_url(String ali_face_url) {
            this.ali_face_url = ali_face_url;
        }

        public String getAli_face_appcode() {
            return ali_face_appcode;
        }

        public void setAli_face_appcode(String ali_face_appcode) {
            this.ali_face_appcode = ali_face_appcode;
        }
    }
}