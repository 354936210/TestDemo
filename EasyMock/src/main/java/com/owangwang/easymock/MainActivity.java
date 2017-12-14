package com.owangwang.easymock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.Jsonbean;
import com.owangwang.easymock.bean.Usersbean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btRequest;

    RecyclerView rv;
    List<Usersbean> mList;
//    RequestQueue mQueue;
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
//        mQueue = Volley.newRequestQueue(this);

    }



    public void doRequest(String url){
        GsonRequest<Jsonbean> gsonRequest=new GsonRequest<Jsonbean>(url, Jsonbean.class, new Response.Listener<Jsonbean>() {
            @Override
            public void onResponse(Jsonbean jsonbean) {
                Log.d("MainActivity",jsonbean.toString());
                if (jsonbean.isSuccess()){
                     mList=jsonbean.getUsers();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

//        mQueue.add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
//        doRequest("https://easy-mock.com/mock/5a2de725e9ee5f7c09d8e60c/example/sfaaa#!method=GET&queryParameters=%5B%5D&body=&headers=%5B%5D");
        init();
        myAdapter=new MyAdapter(mList);
        rv.setAdapter(myAdapter);
    }
    public void init() {
        Usersbean usersbean;

        for (int i = 0; i < 20; i++) {
            usersbean = new Usersbean();
            usersbean.setId("YZ000" + i);
            usersbean.setName("Ying丶Zi" + i);
            if (i % 4 == 0) {
                usersbean.setUrl("http://upload.jianshu.io/users/upload_avatars/579463/a54a6aa2bcf9.jpeg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240");
            } else if (i % 3 == 0) {
                usersbean.setUrl("http://cdn2.jianshu.io/assets/default_avatar/13-394c31a9cb492fcb39c27422ca7d2815.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/114/h/114");
            } else if (i % 2 == 0) {
                usersbean.setUrl("http://upload.jianshu.io/users/upload_avatars/2223071/606c47a7cec8?imageMogr2/auto-orient/strip|imageView2/1/w/114/h/114");

            }else
                {
                    usersbean.setUrl("http://upload.jianshu.io/users/upload_avatars/1094787/6f0f0fd7-3fb8-42c2-ba2a-37f4892c39c8.JPG?imageMogr2/auto-orient/strip|imageView2/1/w/114/h/114");
                }
            mList.add(usersbean);
        }

    }
}
