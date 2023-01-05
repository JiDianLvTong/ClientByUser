package com.android.jidian.client.bean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EvaluateListsBean {

    /**
     * status : 1
     * msg : 请求成功！
     * page : 1
     * total : 36
     * cabname : 北京演示电柜
     * data : [{"id":"42","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test12345678910111213141516","create_time":"2019-10-30 17:20:22","tags":[{"id":"6","name":"真的很好用","color":"90EE90"},{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"41","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test123456789101112131415","create_time":"2019-10-30 16:47:19","tags":[{"id":"6","name":"真的很好用","color":"90EE90"},{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"40","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test123456789101112131415","create_time":"2019-10-30 16:44:12","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"39","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test1234567891011121314","create_time":"2019-10-30 16:44:10","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"38","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test12345678910111213","create_time":"2019-10-30 16:44:07","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"37","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test123456789101112","create_time":"2019-10-30 16:44:04","tags":[{"id":"3","name":"利民换电","color":"EEEEE23348-23502/com.android.mixiang.client E/LoggingInterceptor: E"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"36","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test1234567891011","create_time":"2019-10-30 16:44:01","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"35","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test12345678910","create_time":"2019-10-30 16:43:59","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"34","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test123456789","create_time":"2019-10-30 16:43:54","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":""},{"id":"33","phone":"152****9701","avater":"http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg","stars":5,"content":"test12345678","create_time":"2019-10-30 16:43:51","tags":[{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}],"tagstr":"很好用"}]
     */
    private String error;
    private int status;
    private String msg;
    private int page;
    private String total;
    private String cabname;
    private List<DataBean> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCabname() {
        return cabname;
    }

    public void setCabname(String cabname) {
        this.cabname = cabname;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 42
         * phone : 152****9701
         * avater : http://img01.halouhuandian.com/avater/2017/2017-08/20170818165044_3912.jpg
         * stars : 5
         * content : test12345678910111213141516
         * create_time : 2019-10-30 17:20:22
         * tags : [{"id":"6","name":"真的很好用","color":"90EE90"},{"id":"3","name":"利民换电","color":"EEEEEE"},{"id":"2","name":"便捷高效","color":"FF3E96"},{"id":"1","name":"换电很快","color":"8B008B"}]
         * tagstr :
         */

        private String id;
        private String phone;
        private String avater;
        private int stars;
        private String content;
        private String create_time;
        private String tagstr;
        private List<TagsBean> tags;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTagstr() {
            return tagstr;
        }

        public void setTagstr(String tagstr) {
            this.tagstr = tagstr;
        }

        @NotNull
        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * id : 6
             * name : 真的很好用
             * color : 90EE90
             */

            private String id;
            private String name;
            private String color;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }
}
