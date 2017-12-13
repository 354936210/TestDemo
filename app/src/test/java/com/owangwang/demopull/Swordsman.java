package com.owangwang.demopull;

/**
 * Created by wangchao on 2017/12/13.
 */

public class Swordsman {
    String name;
    String level;

    public Swordsman(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
