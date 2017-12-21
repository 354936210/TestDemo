package com.owangwang.easymock.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/12/21.
 */

/**
 * 管理所有Activity退出的工具类
 */
public class ExitApplication {
    static List<Activity> mlist;
    public ExitApplication() {
        if (mlist==null){
            mlist=new ArrayList<>();
        }
    }

    public  static void addAcrivity(Activity activity){
        if (mlist==null){
           mlist=new ArrayList<>();
        }mlist.add(activity);
    }
    public static void exit()
    {
        if (mlist!=null) {
            for (Activity act : mlist) {
                act.finish();
            }

        }
        System.exit(0);
    }
}
