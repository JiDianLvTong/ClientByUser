package com.android.jidian.repair.mvp.UserAuditCab;

import java.util.List;

public class AuditCabListBean {
    /**
     * "status": 1,
     * "msg": "请求成功！",
     * "data": [
     * {
     * "id": "00353",
     * "name": "滨江村饿了么站",
     * "address": "滨江村3组5号",
     * "phone": "18582580516",
     * "image": "https://img01.halouhuandian.com/hello/hello/2019/2019-10/20191015105126_76163_992x744.jpg",
     * "bty_num": "0",
     * "jingdu": "105.457008",
     * "weidu": "28.882122",
     * "distance": 0,
     * "open_door": 0,
     * "open_tip": "已停业"
     * }
     * ],
     * "page": 2,
     * "errno": "E0000",
     * "error": "",
     * "show": 0
     */
    private String status;
    private String msg;
    private String page;
    private List<DataBean> data;
    private String errno;
    private String error;
    private String show;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
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

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public static class DataBean {
        /**
         * 站点id
         */
        private String id;
        /**
         * 站点编号
         */
        private String number;
        /**
         * 站点名称
         */
        private String name;
        /**
         * 地址
         */
        private String address;
        /**
         * 电话
         */
        private String phone;
        /**
         * 站点图片
         */
        private String image;
        /**
         * 电池数量
         */
        private String bty_num;
        /**
         * 经度
         */
        private String jingdu;
        /**
         * 纬度
         */
        private String weidu;
        /**
         * 距离
         */
        private String distance;
        /**
         * 是否停运电柜
         */
        private String is_run;
        /**
         * 是否停运电柜-文本
         */
        private String tips;
        /**
         * 故障数
         */
        private String ftotal;
        /**
         * 管理仓门权限
         */
        private String mdoor;
        /**
         * 停用启用电柜
         */
        private String mcab;
        /**
         * 打开电柜后台权限
         */
        private String open_andr;
        /**
         * 打开电柜后门权限
         */
        private String open_cab_door;
        /**
         * 重启安卓板权限
         */
        private String restart_andr;
        /**
         * 0审核中 2被驳回
         */
        private String audit_status;
        /**
         * 驳回原因
         */
        private String audit_remarks;
        /**
         * 巡检状态 0 待巡检 1 已巡检
         */
        private String patrol;
        /**
         * 是否营业 1-营业,0-已停业
         */
        private String open_door;
        /**
         * 是否营业 文字描述
         */
        private String open_tip;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBty_num() {
            return bty_num;
        }

        public void setBty_num(String bty_num) {
            this.bty_num = bty_num;
        }

        public String getJingdu() {
            return jingdu;
        }

        public void setJingdu(String jingdu) {
            this.jingdu = jingdu;
        }

        public String getWeidu() {
            return weidu;
        }

        public void setWeidu(String weidu) {
            this.weidu = weidu;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getIs_run() {
            return is_run;
        }

        public void setIs_run(String is_run) {
            this.is_run = is_run;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getFtotal() {
            return ftotal;
        }

        public void setFtotal(String ftotal) {
            this.ftotal = ftotal;
        }

        public String getMdoor() {
            return mdoor;
        }

        public void setMdoor(String mdoor) {
            this.mdoor = mdoor;
        }

        public String getMcab() {
            return mcab;
        }

        public void setMcab(String mcab) {
            this.mcab = mcab;
        }

        public String getOpen_andr() {
            return open_andr;
        }

        public void setOpen_andr(String open_andr) {
            this.open_andr = open_andr;
        }

        public String getOpen_cab_door() {
            return open_cab_door;
        }

        public void setOpen_cab_door(String open_cab_door) {
            this.open_cab_door = open_cab_door;
        }

        public String getRestart_andr() {
            return restart_andr;
        }

        public void setRestart_andr(String restart_andr) {
            this.restart_andr = restart_andr;
        }

        public String getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(String audit_status) {
            this.audit_status = audit_status;
        }

        public String getAudit_remarks() {
            return audit_remarks;
        }

        public void setAudit_remarks(String audit_remarks) {
            this.audit_remarks = audit_remarks;
        }

        public String getPatrol() {
            return patrol;
        }

        public void setPatrol(String patrol) {
            this.patrol = patrol;
        }

        public String getOpen_door() {
            return open_door;
        }

        public void setOpen_door(String open_door) {
            this.open_door = open_door;
        }

        public String getOpen_tip() {
            return open_tip;
        }

        public void setOpen_tip(String open_tip) {
            this.open_tip = open_tip;
        }
    }
}
