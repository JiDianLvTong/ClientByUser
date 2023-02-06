package com.android.jidian.repair.mvp.UserLog;

import java.util.List;

/**
 * @author : PTT
 * date: 2020/8/10 17:50
 * company: 兴达智联
 * description:
 */
public class OperationLogBean {
    /*{
        "status": 1,
            "msg": "查询成功",
            "data": [
        {
            "uname": "周云娇",
                "uphone": "15765465860",
                "title": "停用/启用仓门",
                "diftxt": "停用了电柜id为4472仓门id为3的仓门",
                "remark": "电柜",
                "create_time": "2020-07-24 11:10:18"
        }
    ]
    }*/
    private String status;
    private String msg;
    private int lastid;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public int getLastid() {
        return lastid;
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
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
         * 用户名
         */
        private String uname;
        /**
         * 手机号
         */
        private String uphone;
        /**
         * 操作行为
         */
        private String title;
        /**
         * 操作内容
         */
        private String diftxt;
        private String remark;
        /**
         * 操作时间
         */
        private String create_time;

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUphone() {
            return uphone;
        }

        public void setUphone(String uphone) {
            this.uphone = uphone;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDiftxt() {
            return diftxt;
        }

        public void setDiftxt(String diftxt) {
            this.diftxt = diftxt;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
