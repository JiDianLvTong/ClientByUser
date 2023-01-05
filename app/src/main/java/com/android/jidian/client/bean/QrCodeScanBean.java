package com.android.jidian.client.bean;

public class QrCodeScanBean {
    /**
     * status : 0
     * msg : GPS已绑定其他用户~
     * errno : E1003
     * error :
     * show : 1
     */
    private int status;
    private String msg;
    private String errno;
    private String error;
    private int show;
    private String sync_msg;
    private String sync_btn;
    private String type;
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

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getSync_msg() {
        return sync_msg;
    }

    public void setSync_msg(String sync_msg) {
        this.sync_msg = sync_msg;
    }

    public String getSync_btn() {
        return sync_btn;
    }

    public void setSync_btn(String sync_btn) {
        this.sync_btn = sync_btn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String type;
        private String fval;
        private String expire;
        private String code;
        private String code_info;
        private String rule;
        private String alert;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFval() {
            return fval;
        }

        public void setFval(String fval) {
            this.fval = fval;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode_info() {
            return code_info;
        }

        public void setCode_info(String code_info) {
            this.code_info = code_info;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }
    }
}