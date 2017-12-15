package com.owangwang.easymock.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.owangwang.easymock.MyAdapter;
import com.owangwang.easymock.bean.WoDeKuaiDi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class AppConfig {
    public static List<WoDeKuaiDi> mList=new ArrayList<>();
    public static MyAdapter adapter;
    static  RequestQueue mQueue;
    public static RequestQueue myQueue(Context context){
        if (mQueue==null) {
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
    }

}
