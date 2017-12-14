package com.owangwang.easymock.bean;

import java.util.List;

/**
 * Created by wangchao on 2017/12/13.
 */

public class Jsonbean {
    String status;
    String msg;
    List<KuaiDibean> result;

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

    public List<KuaiDibean> getResult() {
        return result;
    }

    public void setResult(List<KuaiDibean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Jsonbean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
