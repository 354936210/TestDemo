package com.owangwang.easymock.bean;

/**
 * Created by wangchao on 2017/12/14.
 */

public class ExpressJson {
    String status;
    String msg;
    ExpressForm result;

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

    public ExpressForm getResult() {
        return result;
    }

    public void setResult(ExpressForm result) {
        this.result = result;
    }
}
