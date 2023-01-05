package com.android.jidian.client.bean;

import java.io.Serializable;
import java.util.List;

public class AdvicesLists2Bean implements Serializable {
    private static final long serialVersionUID = 1111L;
    /**
     * status : 1
     * error : 2
     * msg : 请求成功~
     * data : [{"msg_id":"160","mtype":"1","to_uid":"117131","title":"全体竖图消息","is_all":"1","show_type":"2","is_jump":"1","show_rate":"2","start_ts":"2020-03-24 18:34:52","end_ts":"2020-03-31 00:00:00","content":"","url":"https://www.halouhuandian.com/","images":"https://img01.halouhuandian.com/hello/testadm/2020/20200324183511_77638_747x921.png","type":"2","create_time":"2020-03-24 18:35:58","is_read":"0","read_time":"0000-00-00 00:00:00","expire":0,"format_date":"2020-03-24"},{"msg_id":"159","mtype":"1","to_uid":"117131","title":"测试全体消息666","is_all":"1","show_type":"2","is_jump":"0","show_rate":"2","start_ts":"2020-03-24 18:08:29","end_ts":"2020-03-31 00:00:00","content":"","url":"","images":"https://img01.halouhuandian.com/hello/testadm/2020/20200324180855_96699_1920x1080.jpg","type":"2","create_time":"2020-03-24 18:09:03","is_read":"0","read_time":"0000-00-00 00:00:00","expire":0,"format_date":"2020-03-24"},{"msg_id":"158","mtype":"1","to_uid":"117131","title":"测试全体消息123","is_all":"1","show_type":"2","is_jump":"0","show_rate":"2","start_ts":"2020-03-24 17:59:24","end_ts":"2020-03-31 00:00:00","content":"","url":"","images":"https://img01.halouhuandian.com/hello/testadm/2020/20200324175955_85542_1920x1080.png","type":"2","create_time":"2020-03-24 18:00:07","is_read":"0","read_time":"0000-00-00 00:00:00","expire":0,"format_date":"2020-03-24"},{"msg_id":"153","mtype":"1","to_uid":"117131","title":"测试全体消息","is_all":"1","show_type":"2","is_jump":"0","show_rate":"1","start_ts":"2020-03-24 00:00:00","end_ts":"2020-03-31 00:00:00","content":"这个是通知内容","url":"","images":"https://img01.halouhuandian.com/hello/testadm/2020/20200323185649_53365_1600x1000.jpg","type":"2","create_time":"2020-03-23 18:56:59","is_read":"0","read_time":"0000-00-00 00:00:00","expire":0,"format_date":"2020-03-23"}]
     */

    private int status;
    private int error;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
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

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 2222L;
        /**
         * "msg_id":"1096",
         * "mtype":"1",
         * "to_uid":"6666",
         * "title":"文字消息",
         * "is_all":"0",
         * "show_type":"1",
         * "is_jump":"0",
         * "show_rate":"1",
         * "start_ts":"2020-07-14 00:00:00",
         * "end_ts":"2020-07-18 00:00:00",
         * "content":"通知内容123",
         * "url":"",
         * "images":"",
         * "type":"2",
         * "create_time":"2020-07-14 14:48:25",
         * "is_read":"0",
         * "read_time":"2020-07-14 14:57:20",
         * "status":"1",
         * "detail_images":"",
         * "expire":1,
         * "format_date":"2020-07-14"
         */
        private String msg_id;
        private String mtype;
        private String to_uid;
        private String title;
        private String is_all;
        private String show_type;
        private String is_jump;
        private String show_rate;
        private String start_ts;
        private String end_ts;
        private String content;
        private String url;
        /**
         * 列表图
         */
        private String images;
        private String type;
        private String create_time;
        private String is_read;
        private String read_time;
        private String status;
        /**
         * 详情图
         */
        private String detail_images;
        private int expire;
        private String format_date;

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getMtype() {
            return mtype;
        }

        public void setMtype(String mtype) {
            this.mtype = mtype;
        }

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIs_all() {
            return is_all;
        }

        public void setIs_all(String is_all) {
            this.is_all = is_all;
        }

        public String getShow_type() {
            return show_type;
        }

        public void setShow_type(String show_type) {
            this.show_type = show_type;
        }

        public String getIs_jump() {
            return is_jump;
        }

        public void setIs_jump(String is_jump) {
            this.is_jump = is_jump;
        }

        public String getShow_rate() {
            return show_rate;
        }

        public void setShow_rate(String show_rate) {
            this.show_rate = show_rate;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getRead_time() {
            return read_time;
        }

        public void setRead_time(String read_time) {
            this.read_time = read_time;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public String getFormat_date() {
            return format_date;
        }

        public void setFormat_date(String format_date) {
            this.format_date = format_date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDetail_images() {
            return detail_images;
        }

        public void setDetail_images(String detail_images) {
            this.detail_images = detail_images;
        }
    }
}