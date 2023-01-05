package com.android.jidian.client.bean;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2022/12/30 17:46
 * company: 兴达智联
 * description:
 */
public class FindIndexBean {
    /**
     * status : 1
     * msg : OK.
     * errno : E0000
     * error :
     * data : {"toplist":[{"imgno":1,"fname":"img","fangx":"-","imgurl":"https://appx.mixiangx.com/static/tools/images/gdmap.png","target":""}],"imglist":[{"imgno":1,"fname":"img","fangx":"|","imgurl":"https://appx.mixiangx.com/static/app/images/find1.png","target":""},{"imgno":2,"fname":"img","fangx":"|","imgurl":"https://appx.mixiangx.com/static/app/images/find2.png","target":""},{"imgno":3,"fname":"img","fangx":"-","imgurl":"https://appx.mixiangx.com/static/app/images/find3.png","target":""}]}
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
        private List<ToplistBean> toplist;
        private List<ImglistBean> imglist;

        public List<ToplistBean> getToplist() {
            return toplist;
        }

        public void setToplist(List<ToplistBean> toplist) {
            this.toplist = toplist;
        }

        public List<ImglistBean> getImglist() {
            return imglist;
        }

        public void setImglist(List<ImglistBean> imglist) {
            this.imglist = imglist;
        }

        public static class ToplistBean {
            /**
             * imgno : 1
             * fname : img
             * fangx : -
             * imgurl : https://appx.mixiangx.com/static/tools/images/gdmap.png
             * target :
             */

            private String imgno;
            private String fname;
            private String fangx;
            private String imgurl;
            private String target;

            public String getImgno() {
                return imgno;
            }

            public void setImgno(String imgno) {
                this.imgno = imgno;
            }

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getFangx() {
                return fangx;
            }

            public void setFangx(String fangx) {
                this.fangx = fangx;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }
        }

        public static class ImglistBean {
            /**
             * imgno : 1
             * fname : img
             * fangx : |
             * imgurl : https://appx.mixiangx.com/static/app/images/find1.png
             * target :
             */

            private String imgno;
            private String fname;
            private String fangx;
            private String imgurl;
            private String target;

            public String getImgno() {
                return imgno;
            }

            public void setImgno(String imgno) {
                this.imgno = imgno;
            }

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getFangx() {
                return fangx;
            }

            public void setFangx(String fangx) {
                this.fangx = fangx;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }
        }
    }
}
