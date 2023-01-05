package com.android.jidian.client.bean;

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
    public int status;
    public String msg;
    public T data;

    public boolean isOk() {
        return status == 1;
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