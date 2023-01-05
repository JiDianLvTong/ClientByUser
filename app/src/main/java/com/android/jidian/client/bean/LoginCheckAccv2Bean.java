package com.android.jidian.client.bean;

/**
 * @author : xiaoming
 * date: 2022/3/7 下午4:47
 * company: 兴达智联
 * description:
 */
public class LoginCheckAccv2Bean {
    /**
     * status : 1
     * msg : 验证通过！
     * data : {"wxappid":"wx3abaaaaf0a0e0d57"}
     */

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
         * wxappid : wx3abaaaaf0a0e0d57
         */

        private String wxappid;

        public String getWxappid() {
            return wxappid;
        }

        public void setWxappid(String wxappid) {
            this.wxappid = wxappid;
        }
    }
}
