package com.android.jidian.repair.mvp.main.PatrolFragment.hasPartol;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/2/3 13:53
 * description:
 */
public class PatrolMyListBean {
    /**
     * status : 1
     * msg : OK
     * errno : E0000
     * error :
     * data : {"lists":[{"cabid":"43","fadmid":"0","fstatus":"0","ftime":"0000-00-00 00:00:00","bgimg":"ape/2022/12/05_05_20221205164957_81600_1040x780.jpg","distance":12717.99,"online":"离线","fstat":"未复核"}],"page":1}
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
         * lists : [{"cabid":"43","fadmid":"0","fstatus":"0","ftime":"0000-00-00 00:00:00","bgimg":"ape/2022/12/05_05_20221205164957_81600_1040x780.jpg","distance":12717.99,"online":"离线","fstat":"未复核"}]
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
             * cabid : 43
             * fadmid : 0
             * fstatus : 0
             * ftime : 0000-00-00 00:00:00
             * bgimg : ape/2022/12/05_05_20221205164957_81600_1040x780.jpg
             * distance : 12717.99
             * online : 离线
             * fstat : 未复核
             */

            private String id;
            private String cabid;
            private String fadmid;
            private String fstatus;
            private String ftime;
            private String bgimg;
            private String distance;
            private String online;
            private String fstat;

            private String cabname;
            private String address;

            public String getCabname() {
                return cabname;
            }

            public void setCabname(String cabname) {
                this.cabname = cabname;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCabid() {
                return cabid;
            }

            public void setCabid(String cabid) {
                this.cabid = cabid;
            }

            public String getFadmid() {
                return fadmid;
            }

            public void setFadmid(String fadmid) {
                this.fadmid = fadmid;
            }

            public String getFstatus() {
                return fstatus;
            }

            public void setFstatus(String fstatus) {
                this.fstatus = fstatus;
            }

            public String getFtime() {
                return ftime;
            }

            public void setFtime(String ftime) {
                this.ftime = ftime;
            }

            public String getBgimg() {
                return bgimg;
            }

            public void setBgimg(String bgimg) {
                this.bgimg = bgimg;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getOnline() {
                return online;
            }

            public void setOnline(String online) {
                this.online = online;
            }

            public String getFstat() {
                return fstat;
            }

            public void setFstat(String fstat) {
                this.fstat = fstat;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
