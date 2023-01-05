package com.android.jidian.client.bean;

import java.util.List;

public class AuthenticationFaceBean {

    /**
     * address : 山西省吕梁市离石区建设北街9号1排9户
     * birth : 19920628
     * card_region : [{"x":231,"y":667},{"x":595,"y":654},{"x":571,"y":11},{"x":208,"y":25}]
     * config_str : {"side":"face"}
     * face_rect : {"angle":-1.1324684619903564,"center":{"x":386.7188415527344,"y":156.33673095703125},"size":{"height":102.50662231445312,"width":119.15633392333984}}
     * face_rect_vertices : [{"x":328.165283203125,"y":208.75753784179688},{"x":326.13934326171875,"y":106.27093505859375},{"x":445.27239990234375,"y":103.91592407226562},{"x":447.29833984375,"y":206.40252685546875}]
     * name : 田彦宇
     * nationality : 汉
     * num : 141102199206280038
     * request_id : 20191115170432_f7014d077bf061388da12c5780db23a2
     * sex : 男
     * success : true
     */

    private String address;
    private String birth;
    private String config_str;
    private FaceRectBean face_rect;
    private String name;
    private String nationality;
    private String num;
    private String request_id;
    private String sex;
    private boolean success;
    private List<CardRegionBean> card_region;
    private List<FaceRectVerticesBean> face_rect_vertices;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public FaceRectBean getFace_rect() {
        return face_rect;
    }

    public void setFace_rect(FaceRectBean face_rect) {
        this.face_rect = face_rect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CardRegionBean> getCard_region() {
        return card_region;
    }

    public void setCard_region(List<CardRegionBean> card_region) {
        this.card_region = card_region;
    }

    public List<FaceRectVerticesBean> getFace_rect_vertices() {
        return face_rect_vertices;
    }

    public void setFace_rect_vertices(List<FaceRectVerticesBean> face_rect_vertices) {
        this.face_rect_vertices = face_rect_vertices;
    }

    public static class FaceRectBean {
        /**
         * angle : -1.1324684619903564
         * center : {"x":386.7188415527344,"y":156.33673095703125}
         * size : {"height":102.50662231445312,"width":119.15633392333984}
         */

        private double angle;
        private CenterBean center;
        private SizeBean size;

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public CenterBean getCenter() {
            return center;
        }

        public void setCenter(CenterBean center) {
            this.center = center;
        }

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public static class CenterBean {
            /**
             * x : 386.7188415527344
             * y : 156.33673095703125
             */

            private double x;
            private double y;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }

        public static class SizeBean {
            /**
             * height : 102.50662231445312
             * width : 119.15633392333984
             */

            private double height;
            private double width;

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }
        }
    }

    public static class CardRegionBean {
        /**
         * x : 231
         * y : 667
         */

        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static class FaceRectVerticesBean {
        /**
         * x : 328.165283203125
         * y : 208.75753784179688
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
