package com.android.jidian.repair.mvp.user.userMyPatrol.patrolDetail;

/**
 * @author : xiaoming
 * date: 2023/2/3 14:45
 * description:
 */
public class PatrolDetailBean {
    /**
     * status : 1
     * msg : OK
     * errno : E0000
     * error :
     * data : {"id":"1","company_id":"3","uid":"108","cabid":"43","fadmid":"0","fstatus":"0","ftime":"0000-00-00 00:00:00","img1":"https://img02.mixiangx.com/ape/2023/02/02_20230202112835_13142_1728x2304.jpeg","img2":"https://img02.mixiangx.com/ape/2023/02/02_20230202112843_16709_1728x2304.jpeg","img3":"https://img02.mixiangx.com/ape/2023/02/02_20230202112850_12266_1728x2304.jpeg","img4_1":"https://img02.mixiangx.com/ape/2023/02/02_20230202112859_68810_1728x2304.jpeg","img4_2":"https://img02.mixiangx.com/ape/2023/02/02_20230202112906_53180_1728x2304.jpeg","img4_3":"https://img02.mixiangx.com/ape/2023/02/02_20230202112912_74786_1728x2304.jpeg","img4_4":"https://img02.mixiangx.com/ape/2023/02/02_20230202112918_43411_1728x2304.jpeg","img5_1":"https://img02.mixiangx.com/ape/2023/02/02_20230202112924_48571_1728x2304.jpeg","img5_2":"https://img02.mixiangx.com/ape/2023/02/02_20230202112930_28154_1728x2304.jpeg","img5_3":"https://img02.mixiangx.com/ape/2023/02/02_20230202112936_59440_1728x2304.jpeg","img6_1":"https://img02.mixiangx.com/ape/2023/02/02_20230202112942_39903_1728x2304.jpeg","isnetdbm":"0","isdixian":"1","isopenbtn":"1","day":"20230202","isdraft":"1","create_uid":"108","create_time":"2023-02-02 11:30:00","update_time":"0000-00-00 00:00:00","update_uid":"0","status":"1","name":"兔悠便利店","address":"皇姑区鸭绿江街63","fstat":"未复核"}
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
         * company_id : 3
         * uid : 108
         * cabid : 43
         * fadmid : 0
         * fstatus : 0
         * ftime : 0000-00-00 00:00:00
         * img1 : https://img02.mixiangx.com/ape/2023/02/02_20230202112835_13142_1728x2304.jpeg
         * img2 : https://img02.mixiangx.com/ape/2023/02/02_20230202112843_16709_1728x2304.jpeg
         * img3 : https://img02.mixiangx.com/ape/2023/02/02_20230202112850_12266_1728x2304.jpeg
         * img4_1 : https://img02.mixiangx.com/ape/2023/02/02_20230202112859_68810_1728x2304.jpeg
         * img4_2 : https://img02.mixiangx.com/ape/2023/02/02_20230202112906_53180_1728x2304.jpeg
         * img4_3 : https://img02.mixiangx.com/ape/2023/02/02_20230202112912_74786_1728x2304.jpeg
         * img4_4 : https://img02.mixiangx.com/ape/2023/02/02_20230202112918_43411_1728x2304.jpeg
         * img5_1 : https://img02.mixiangx.com/ape/2023/02/02_20230202112924_48571_1728x2304.jpeg
         * img5_2 : https://img02.mixiangx.com/ape/2023/02/02_20230202112930_28154_1728x2304.jpeg
         * img5_3 : https://img02.mixiangx.com/ape/2023/02/02_20230202112936_59440_1728x2304.jpeg
         * img6_1 : https://img02.mixiangx.com/ape/2023/02/02_20230202112942_39903_1728x2304.jpeg
         * isnetdbm : 0
         * isdixian : 1
         * isopenbtn : 1
         * day : 20230202
         * isdraft : 1
         * create_uid : 108
         * create_time : 2023-02-02 11:30:00
         * update_time : 0000-00-00 00:00:00
         * update_uid : 0
         * status : 1
         * name : 兔悠便利店
         * address : 皇姑区鸭绿江街63
         * fstat : 未复核
         */

        private String id;
        private String company_id;
        private String uid;
        private String cabid;
        private String fadmid;
        private String fstatus;
        private String ftime;
        private String img1;
        private String img2;
        private String img3;
        private String img4_1;
        private String img4_2;
        private String img4_3;
        private String img4_4;
        private String img5_1;
        private String img5_2;
        private String img5_3;
        private String img6_1;
        private String isnetdbm;
        private String isdixian;
        private String isopenbtn;
        private String day;
        private String isdraft;
        private String create_uid;
        private String create_time;
        private String update_time;
        private String update_uid;
        private String status;
        private String name;
        private String address;
        private String fstat;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getImg1() {
            return img1;
        }

        public void setImg1(String img1) {
            this.img1 = img1;
        }

        public String getImg2() {
            return img2;
        }

        public void setImg2(String img2) {
            this.img2 = img2;
        }

        public String getImg3() {
            return img3;
        }

        public void setImg3(String img3) {
            this.img3 = img3;
        }

        public String getImg4_1() {
            return img4_1;
        }

        public void setImg4_1(String img4_1) {
            this.img4_1 = img4_1;
        }

        public String getImg4_2() {
            return img4_2;
        }

        public void setImg4_2(String img4_2) {
            this.img4_2 = img4_2;
        }

        public String getImg4_3() {
            return img4_3;
        }

        public void setImg4_3(String img4_3) {
            this.img4_3 = img4_3;
        }

        public String getImg4_4() {
            return img4_4;
        }

        public void setImg4_4(String img4_4) {
            this.img4_4 = img4_4;
        }

        public String getImg5_1() {
            return img5_1;
        }

        public void setImg5_1(String img5_1) {
            this.img5_1 = img5_1;
        }

        public String getImg5_2() {
            return img5_2;
        }

        public void setImg5_2(String img5_2) {
            this.img5_2 = img5_2;
        }

        public String getImg5_3() {
            return img5_3;
        }

        public void setImg5_3(String img5_3) {
            this.img5_3 = img5_3;
        }

        public String getImg6_1() {
            return img6_1;
        }

        public void setImg6_1(String img6_1) {
            this.img6_1 = img6_1;
        }

        public String getIsnetdbm() {
            return isnetdbm;
        }

        public void setIsnetdbm(String isnetdbm) {
            this.isnetdbm = isnetdbm;
        }

        public String getIsdixian() {
            return isdixian;
        }

        public void setIsdixian(String isdixian) {
            this.isdixian = isdixian;
        }

        public String getIsopenbtn() {
            return isopenbtn;
        }

        public void setIsopenbtn(String isopenbtn) {
            this.isopenbtn = isopenbtn;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getIsdraft() {
            return isdraft;
        }

        public void setIsdraft(String isdraft) {
            this.isdraft = isdraft;
        }

        public String getCreate_uid() {
            return create_uid;
        }

        public void setCreate_uid(String create_uid) {
            this.create_uid = create_uid;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getUpdate_uid() {
            return update_uid;
        }

        public void setUpdate_uid(String update_uid) {
            this.update_uid = update_uid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFstat() {
            return fstat;
        }

        public void setFstat(String fstat) {
            this.fstat = fstat;
        }
    }
}
