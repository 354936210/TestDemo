package com.owangwang.easymock.bean;

/**
 * Created by wangchao on 2017/12/13.
 */

public class Usersbean {
    String name;
    String url;

    @Override
    public String toString() {
        return "Usersbean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }

    String Id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
