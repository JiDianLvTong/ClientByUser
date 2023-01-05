package com.android.jidian.client.bean;

/**
 * @author : xiaoming
 * date: 2021/11/25 下午3:09
 * company: 兴达智联
 * description:
 */
public class PullActivityPoliteBean {
    /**
     * share : {"title":"分享标题","desc":"分享描述","link":"https://testh5.halouhuandian.com/Activity/invite.html?code=V0h2YmNaWVlTbzl3UWdNMkFiWmo5cHNLZ0pWSDlFK3F2c0xLa0NETXAvND0=","imgUrl":"","type":"link","dataUrl":"","config":{"appId":"","timestamp":"","nonceStr":"","signature":""}}
     */

    /**
     * title : 分享标题
     * desc : 分享描述
     * link : https://testh5.halouhuandian.com/Activity/invite.html?code=V0h2YmNaWVlTbzl3UWdNMkFiWmo5cHNLZ0pWSDlFK3F2c0xLa0NETXAvND0=
     * imgUrl :
     * type : link
     * dataUrl :
     * config : {"appId":"","timestamp":"","nonceStr":"","signature":""}
     */

    private String title;
    private String desc;
    private String link;
    private String imgUrl;
    private String type;
    private String dataUrl;
    private ConfigBean config;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public static class ConfigBean {
        /**
         * appId :
         * timestamp :
         * nonceStr :
         * signature :
         */

        private String appId;
        private String timestamp;
        private String nonceStr;
        private String signature;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

}
