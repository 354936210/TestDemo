package com.owangwang.easymock.bean;

/**
 * Created by wangchao on 2017/12/13.
 */

public class KuaiDibean {
    String name;
    String number;
    String type;
    String letter;
    String tel;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Kuaidibean{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", letter='" + letter + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
