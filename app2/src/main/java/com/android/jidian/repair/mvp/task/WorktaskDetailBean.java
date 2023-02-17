package com.android.jidian.repair.mvp.task;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:27
 * description:
 */
public class WorktaskDetailBean {
    /**
     * status : 1
     * msg : OK
     * errno : E0000
     * error :
     * data : {"lists":{"id":"1","wtype":"10","cabid":"68","lng":"116.544608","lat":"39.924109","address":"北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"2","ustater":null,"create_time":"2023-01-12 14:34:15"}}
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
         * id : 1
         * wtype : 10
         * cabid : 68
         * lng : 116.544608
         * lat : 39.924109
         * address : 北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168
         * content : 这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！
         * ustatus : 2
         * ustater : null
         * create_time : 2023-01-12 14:34:15
         */

        private String id;
        private String wtype;
        private String cabid;
        private String lng;
        private String lat;
        private String address;
        private String content;
        private String ustatus;
        private String ustater;
        private String create_time;
        private String phone;
        private String title;
        private String contentdw;
        private List<String> images;
        private List<String> imagedw;
        private String timedw;

        public String getContentdw() {
            return contentdw;
        }

        public void setContentdw(String contentdw) {
            this.contentdw = contentdw;
        }

        public String getTimedw() {
            return timedw;
        }

        public void setTimedw(String timedw) {
            this.timedw = timedw;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWtype() {
            return wtype;
        }

        public void setWtype(String wtype) {
            this.wtype = wtype;
        }

        public String getCabid() {
            return cabid;
        }

        public void setCabid(String cabid) {
            this.cabid = cabid;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUstatus() {
            return ustatus;
        }

        public void setUstatus(String ustatus) {
            this.ustatus = ustatus;
        }

        public String getUstater() {
            return ustater;
        }

        public void setUstater(String ustater) {
            this.ustater = ustater;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImagedw() {
            return imagedw;
        }

        public void setImagedw(List<String> imagedw) {
            this.imagedw = imagedw;
        }

    }
}
