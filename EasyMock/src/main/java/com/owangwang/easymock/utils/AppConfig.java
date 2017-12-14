package com.owangwang.easymock.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wangchao on 2017/12/14.
 */

public class AppConfig {
    static  RequestQueue mQueue;
    public static RequestQueue myQueue(Context context){
        if (mQueue==null) {
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
    }

}
