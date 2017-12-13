package com.owangwang.easymock;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by wangchao on 2017/12/13.
 */

public class GsonRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;
    private Gson mGson;
    private Class<T> mClass;
    public GsonRequest(int method, String url, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
    }
    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        // 这里可以设置post和get两种，post保密点
        this(Method.POST, url, clazz, listener, errorListener);
    }
    // 此方式可通过post方式传递String类型参数
    public GsonRequest(String url, Map<String, String> params, Class<T> clazz,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        // 这里可以设置post和get两种，post保密点
        this(Method.POST, url, clazz, listener, errorListener);
    }
    /**
     * HTTP请求报错处理
     */
    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        // HTTP状态码信息=volleyError.networkResponse.statusCode
        // AppContext.setHTTP_REPONSE_RESULT(String.valueOf(volleyError.networkResponse.statusCode));//将HTTP错误状态码存入全局http返回结果变量
        // AppContext.setHTTP_REPONSE_RESULT("服务器异常，请联系系统管理员！");
        return super.parseNetworkError(volleyError);
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.i("JSONRESULT", jsonString); // 打印返回结果
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}