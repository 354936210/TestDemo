package com.owangwang.easymock.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by wangchao on 2017/12/15.
 */

public class SaveEvent extends DataSupport{
    /**
     * 快递公司名
     */
    String type;
    /**
     * 快递单号
     */
    String number;
    /**
     * 快递运送状态
     */
    String deliverystatus;


    public String getType() {
        return type;
    }

    public SaveEvent(String type, String number, String deliverystatus) {
        this.type = type;
        this.number = number;
        this.deliverystatus = deliverystatus;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }
}
