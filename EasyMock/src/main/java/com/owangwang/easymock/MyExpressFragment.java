package com.owangwang.easymock;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.SaveEvent;
import com.owangwang.easymock.bean.WoDeKuaiDi;
import com.owangwang.easymock.utils.AppConfig;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/12/15.
 */

public class MyExpressFragment extends Fragment{
    Context context;
    List<WoDeKuaiDi> mList;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    TextView tv_note;
    FloatingActionButton bt_add;
    MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_main,container,false);
        context=view.getContext();
        mList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.rv);

        tv_note=view.findViewById(R.id.tv_note);
        bt_add=view.findViewById(R.id.bt_add);
        init();



        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddExpressActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void init(){
        //创建数据库
        LitePal.getDatabase();
        List<WoDeKuaiDi> kuaiDisList;
        kuaiDisList=DataSupport.findAll(WoDeKuaiDi.class);
        //如果没有保存的快递则显示“暂无快递”，否则不显示并将kuaiDisList复制给mlist
            if (kuaiDisList.isEmpty()){
                Log.d("S是否为空","是");
                tv_note.setVisibility(View.VISIBLE);
            }else {
                tv_note.setVisibility(View.GONE);
                Log.d("S是否为空","否");
                Log.d("不是空那值是多少",kuaiDisList.get(0).toString());
            }
            mList=kuaiDisList;
            AppConfig.mList=mList;
            adapter=new MyAdapter(AppConfig.mList);
            AppConfig.adapter=adapter;
            manager=new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(AppConfig.adapter);

    }
    public void onEventMainThread(SaveEvent event) {
        List<MyTypeData> typeData=DataSupport.select("name").where("type=?",event.getType().toUpperCase()).find(MyTypeData.class);
        String name=typeData.get(0).getName();
        WoDeKuaiDi kuaiDi=new WoDeKuaiDi();
            kuaiDi.setName(name);
            kuaiDi.setNumber(event.getNumber());
            kuaiDi.setStatus(event.getDeliverystatus());
            //保存到数据库
            kuaiDi.save();
            //将list也添加此数据
        mList.add(kuaiDi);
        //提醒recyclerview数据更改了
        adapter.notifyDataSetChanged();
    }


}
