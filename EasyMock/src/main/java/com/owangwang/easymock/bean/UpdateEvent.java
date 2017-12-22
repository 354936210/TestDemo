package com.owangwang.easymock.bean;

/**
 * Created by wangchao on 2017/12/22.
 */

public class UpdateEvent {
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UpdateEvent(String msg) {
        this.msg = msg;
    }
}
