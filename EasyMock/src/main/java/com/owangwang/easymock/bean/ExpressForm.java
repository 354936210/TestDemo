package com.owangwang.easymock.bean;

import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class ExpressForm {
    List<MyExpress> list;
    String number;
    String type;
    String deliverystatus;//物流状态 1在途中 2派件中 3已签收 4派送失败(拒签等)
    String issign;

    public List<MyExpress> getList() {
        return list;
    }

    public void setList(List<MyExpress> list) {
        this.list = list;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }

}
