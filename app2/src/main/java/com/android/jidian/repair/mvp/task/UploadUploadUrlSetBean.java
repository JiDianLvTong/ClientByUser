package com.android.jidian.repair.mvp.task;

/**
 * @author : xiaoming
 * date: 2023/1/31 10:48
 * description:
 */
public class UploadUploadUrlSetBean {
    /**
     * status : 1
     * msg : 生成成功！
     * data : {"upfurl":"https://uper01.mixiangx.com/Upload/upImage.html?proj=ape&upToken=d22be4cf59a730eded8aaf4ef07192ea-1675148488&companyid=3","servno":"01","domain":"https://uper01.mixiangx.com","path":"/Upload/upImage.html","proj":"ape","upToken":"d22be4cf59a730eded8aaf4ef07192ea-1675148488","companyid":3}
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
         * upfurl : https://uper01.mixiangx.com/Upload/upImage.html?proj=ape&upToken=d22be4cf59a730eded8aaf4ef07192ea-1675148488&companyid=3
         * servno : 01
         * domain : https://uper01.mixiangx.com
         * path : /Upload/upImage.html
         * proj : ape
         * upToken : d22be4cf59a730eded8aaf4ef07192ea-1675148488
         * companyid : 3
         */

        private String upfurl;
        private String servno;
        private String domain;
        private String path;
        private String proj;
        private String upToken;
        private String companyid;

        public String getUpfurl() {
            return upfurl;
        }

        public void setUpfurl(String upfurl) {
            this.upfurl = upfurl;
        }

        public String getServno() {
            return servno;
        }

        public void setServno(String servno) {
            this.servno = servno;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getProj() {
            return proj;
        }

        public void setProj(String proj) {
            this.proj = proj;
        }

        public String getUpToken() {
            return upToken;
        }

        public void setUpToken(String upToken) {
            this.upToken = upToken;
        }

        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }
    }
}
