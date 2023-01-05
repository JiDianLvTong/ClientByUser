package com.android.jidian.client.pub.zhifubao;

/**
 * @author : xiaoming
 * date: 2021/12/6 下午1:43
 * company: 兴达智联
 * description:
 */
public class AuthResultBean {

    /**
     * alipay_user_info_share_response : {"code":"10000","msg":"Success","user_id":"2088102104794936","avatar":"http://tfsimg.alipay.com/images/partner/T1uIxXXbpXXXXXXXX","province":"安徽省","city":"安庆","nick_name":"支付宝小二","gender":"F"}
     * sign : ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE
     */

    private AlipayUserInfoShareResponseBean alipay_user_info_share_response;
    private String sign;

    public AlipayUserInfoShareResponseBean getAlipay_user_info_share_response() {
        return alipay_user_info_share_response;
    }

    public void setAlipay_user_info_share_response(AlipayUserInfoShareResponseBean alipay_user_info_share_response) {
        this.alipay_user_info_share_response = alipay_user_info_share_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class AlipayUserInfoShareResponseBean {
        /**
         * code : 10000
         * msg : Success
         * user_id : 2088102104794936
         * avatar : http://tfsimg.alipay.com/images/partner/T1uIxXXbpXXXXXXXX
         * province : 安徽省
         * city : 安庆
         * nick_name : 支付宝小二
         * gender : F
         */

        private String code;
        private String msg;
        private String user_id;
        private String avatar;
        private String province;
        private String city;
        private String nick_name;
        private String gender;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
