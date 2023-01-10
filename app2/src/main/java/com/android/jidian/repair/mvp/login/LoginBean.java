package com.android.jidian.repair.mvp.login;

/**
 * @author : xiaoming
 * date: 2023/1/10 17:10
 * description:
 */
public class LoginBean {
    /**
     * status : 1
     * msg : 登录成功！
     * data : {"id":"3","dwrole":"20","apptoken":"426d0108Bw8FCFINUlAPBxpUUBtfUw9T","adname":"吉电出行.超管","phone":"15210079701","is_dwfirst":"0","avater":""}
     * errno : E0000
     * error :
     */

    private String status;
    private String msg;
    private DataBean data;
    private String errno;
    private String error;

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

    public static class DataBean {
        /**
         * id : 3
         * dwrole : 20
         * apptoken : 426d0108Bw8FCFINUlAPBxpUUBtfUw9T
         * adname : 吉电出行.超管
         * phone : 15210079701
         * is_dwfirst : 0
         * avater :
         */

        private String id;
        private String dwrole;
        private String apptoken;
        private String adname;
        private String phone;
        private String is_dwfirst;
        private String avater;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDwrole() {
            return dwrole;
        }

        public void setDwrole(String dwrole) {
            this.dwrole = dwrole;
        }

        public String getApptoken() {
            return apptoken;
        }

        public void setApptoken(String apptoken) {
            this.apptoken = apptoken;
        }

        public String getAdname() {
            return adname;
        }

        public void setAdname(String adname) {
            this.adname = adname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIs_dwfirst() {
            return is_dwfirst;
        }

        public void setIs_dwfirst(String is_dwfirst) {
            this.is_dwfirst = is_dwfirst;
        }

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }
    }
}
