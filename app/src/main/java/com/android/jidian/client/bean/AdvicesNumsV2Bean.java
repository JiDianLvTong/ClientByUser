package com.android.jidian.client.bean;

import java.util.List;

public class AdvicesNumsV2Bean {
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
         * 未读消息数
         */
        private String no_read_num;
        private List<ListBean> list;

        public void setNoReadNum(String no_read_num) {
            this.no_read_num = no_read_num;
        }

        public String getNoReadNum() {
            return no_read_num;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
    }

    public static class ListBean {
        private List<String> ids;
        /**
         * 弹框标题
         */
        private String title;
        /**
         * 文字类型：
         * 10 场景一： 活动消息：文字消息
         * 11 场景二：系统消息： 换电费/电池/车月租即将到期 文字消息
         * 12 场景三：系统消息： 您的电池寿命即将消耗完 文字消息
         * 图片类型：
         * 20 活动图片消息
         * 21 最早的活动图片弹窗
         */
        private String scene;
        /**
         * 内容文字
         */
        private List<String> content;
        /**
         * APP端没有用
         * 展示频率：展示频率：1=仅展示一次；2=每次打开都展示
         */
        private String show_rate;
        /**
         * 0=未读；1=已读
         */
        private String is_read;
        /**
         * 左按钮点击事件
         */
        private ClickBtnBean btn_left;
        /**
         * 右按钮点击事件
         */
        private ClickBtnBean btn_right;
        /**
         * 用于20 活动图片消息
         */
        private String images;
        /**
         * 用于 20活动图片消息 10场景一
         * 0=不跳转；1=跳转
         */
        private String is_jump;
        /**
         * 用于 20活动图片消息 10场景一
         * 跳转地址
         */
        private String jump_to;
        /**
         * 用于20 20活动图片消息 10场景一
         * 跳转动作
         */
        private ParamBean param;
//            /**
//             * 用于21 活动消息
//             */
//            private String big_url;
//            /**
//             * 用于21 活动消息
//             */
//            private int big_show;
//            /**
//             * 用于21 活动消息
//             */
//            private int big_click;
//            /**
//             * 用于21 活动消息
//             */
//            private String big_jump;
//            /**
//             * 用于21 活动消息
//             */
//            private String small_url;
//            /**
//             * 用于21 活动消息
//             */
//            private int small_show;

        public void setIds(List<String> ids) {
            this.ids = ids;
        }

        public List<String> getIds() {
            return ids;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public String getScene() {
            return scene;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getImages() {
            return images;
        }

        public void setIs_jump(String is_jump) {
            this.is_jump = is_jump;
        }

        public String getIs_jump() {
            return is_jump;
        }

        public void setJump_to(String jump_to) {
            this.jump_to = jump_to;
        }

        public String getJump_to() {
            return jump_to;
        }

        public void setShow_rate(String show_rate) {
            this.show_rate = show_rate;
        }

        public String getShow_rate() {
            return show_rate;
        }

        public ParamBean getParam() {
            return param;
        }

        public void setParam(ParamBean param) {
            this.param = param;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        public ClickBtnBean getBtn_left() {
            return btn_left;
        }

        public void setBtn_left(ClickBtnBean btn_left) {
            this.btn_left = btn_left;
        }

        public ClickBtnBean getBtn_right() {
            return btn_right;
        }

        public void setBtn_right(ClickBtnBean btn_right) {
            this.btn_right = btn_right;
        }
    }

    public static class ParamBean {
        private String otype;
        private String froms;
        private String merid;

        public String getOtype() {
            return otype;
        }

        public void setOtype(String otype) {
            this.otype = otype;
        }

        public String getFroms() {
            return froms;
        }

        public void setFroms(String froms) {
            this.froms = froms;
        }

        public String getMerid() {
            return merid;
        }

        public void setMerid(String merid) {
            this.merid = merid;
        }
    }

    public static class ClickBtnBean {
        /**
         * 0=不跳转；1=跳转
         */
        private String is_jump;
        /**
         * 跳转地址
         */
        private String jump_to;
        /**
         * 跳转动作
         */
        private ParamBean param;

        public String getIs_jump() {
            return is_jump;
        }

        public void setIs_jump(String is_jump) {
            this.is_jump = is_jump;
        }

        public String getJump_to() {
            return jump_to;
        }

        public void setJump_to(String jump_to) {
            this.jump_to = jump_to;
        }

        public ParamBean getParam() {
            return param;
        }

        public void setParam(ParamBean param) {
            this.param = param;
        }
    }
}