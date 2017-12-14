package com.owangwang.easymock.bean;

import java.util.List;

/**
 * Created by wangchao on 2017/12/13.
 */

public class Jsonbean {
    boolean success;
    List<Usersbean> users;

    @Override
    public String toString() {
        return "Jsonbean{" +
                "success=" + success +
                ", users=" + users +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Usersbean> getUsers() {
        return users;
    }

    public void setUsers(List<Usersbean> users) {
        this.users = users;
    }
}
