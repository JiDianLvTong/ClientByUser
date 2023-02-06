package com.android.jidian.repair.mvp.UserTask.UserTaskList;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/2/3 15:32
 * description:
 */
public class WorktaskMylistsBean {
    /**
     * status : 1
     * msg : OK
     * errno : E0000
     * error :
     * data : {"lists":[{"id":"6","wtype":"30","title":"其他任务","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"5","wtype":"20","phone":"15210079701","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"4","wtype":"10","cabid":"68","lng":"116.544608","lat":"39.924109","address":"北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"1","wtype":"10","cabid":"68","lng":"116.544608","lat":"39.924109","address":"北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"}],"page":1}
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
         * lists : [{"id":"6","wtype":"30","title":"其他任务","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"5","wtype":"20","phone":"15210079701","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"4","wtype":"10","cabid":"68","lng":"116.544608","lat":"39.924109","address":"北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"},{"id":"1","wtype":"10","cabid":"68","lng":"116.544608","lat":"39.924109","address":"北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168","content":"这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！","ustatus":"1","ustater":"已完成","create_time":"2023-01-12 14:34:15"}]
         * page : 1
         */

        private List<ListsBean> lists;
        private String page;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public static class ListsBean {
            /**
             * id : 6
             * wtype : 30
             * title : 其他任务
             * content : 这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！这个电柜自动走路了！
             * ustatus : 1
             * ustater : 已完成
             * create_time : 2023-01-12 14:34:15
             * phone : 15210079701
             * cabid : 68
             * lng : 116.544608
             * lat : 39.924109
             * address : 北京市海淀区常营乡常营路168号北京像素南区4号楼一单元168
             */

            private String id;
            private String wtype;
            private String title;
            private String content;
            private String ustatus;
            private String ustater;
            private String create_time;
            private String phone;
            private String cabid;
            private String lng;
            private String lat;
            private String address;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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
        }
    }
}
