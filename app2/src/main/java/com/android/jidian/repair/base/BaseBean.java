package com.android.jidian.repair.base;

/**
 * @author : PTT
 * date: 2020/7/27 15:22
 * company: 兴达智联
 * description:
 */
public class BaseBean<T> {
    /**
     * status 1是请求成功 0是参数错误
     */
    public String status;
    public String msg;
    public T data;

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

    @Override
    public String toString() {
        return "BaseBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}