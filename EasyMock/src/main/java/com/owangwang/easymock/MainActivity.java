package com.owangwang.easymock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.Jsonbean;
import com.owangwang.easymock.bean.KuaiDibean;
import com.owangwang.easymock.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btRequest;
    RecyclerView rv;
    List<KuaiDibean> mList;
    RequestQueue mQueue;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList=new ArrayList<>();

        btRequest=findViewById(R.id.bt_request);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(manager);
        btRequest.setOnClickListener(this);
        mQueue = AppConfig.myQueue(this);

    }



    public void doRequest(String url){
        GsonRequest<Jsonbean> gsonRequest=new GsonRequest<Jsonbean>(url, Jsonbean.class, new Response.Listener<Jsonbean>() {
            @Override
            public void onResponse(Jsonbean jsonbean) {
                mList=jsonbean.getResult();
                myAdapter=new MyAdapter(mList,MainActivity.this);
                rv.setAdapter(myAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        mQueue.add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
        doRequest("http://api.jisuapi.com/express/type?appkey=98e3089a2762b1b2");
    }


}
