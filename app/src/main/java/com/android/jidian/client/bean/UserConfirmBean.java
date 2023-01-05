package com.android.jidian.client.bean;

public class UserConfirmBean {


    /**
     * status : 2
     * msg : 兴达智联-测试商家向您发送了一条退车指令，退车后您将无法正常使用换电服务，如有疑问请拨打400-6060-137客服咨询
     */

    private int status;
    private String msg;

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
}
