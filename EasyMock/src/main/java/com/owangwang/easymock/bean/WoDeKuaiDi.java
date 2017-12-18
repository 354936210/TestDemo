package com.owangwang.easymock.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by wangchao on 2017/12/15.
 */

public class WoDeKuaiDi extends DataSupport{
    @Override
    public String toString() {
        return "WoDeKuaiDi{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * 快递公司名
     */
    String name;
    /**
     * 快递单号
     */
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String number;
    /**
     * 快递运送状态
     */
    String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
