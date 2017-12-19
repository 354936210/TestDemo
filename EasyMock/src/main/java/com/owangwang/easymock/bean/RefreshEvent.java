package com.owangwang.easymock.bean;

/**
 * Created by wangchao on 2017/12/19.
 */

public class RefreshEvent {
    int count;

    public RefreshEvent(int count, String msg) {
        this.count = count;
        this.msg = msg;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    String msg;

    public String getMsg() {
        return msg;
    }

    public RefreshEvent(String msg) {
        this.msg = msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }
}
