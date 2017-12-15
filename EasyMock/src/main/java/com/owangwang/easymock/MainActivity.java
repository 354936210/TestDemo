package com.owangwang.easymock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.SaveEvent;
import com.owangwang.easymock.bean.WoDeKuaiDi;
import com.owangwang.easymock.utils.AppConfig;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    TextView tv_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_manager_layout);
        tv_note=findViewById(R.id.tv_note);
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(SaveEvent event) {
        List<MyTypeData> typeData= DataSupport.select("name").where("type=?",event.getType().toUpperCase()).find(MyTypeData.class);
        String name=typeData.get(0).getName();
        WoDeKuaiDi kuaiDi=new WoDeKuaiDi();
        kuaiDi.setName(name);
        kuaiDi.setNumber(event.getNumber());
        kuaiDi.setStatus(event.getDeliverystatus());
        //保存到数据库
        kuaiDi.save();
        //将list也添加此数据
        AppConfig.mList.add(kuaiDi);
        //提醒recyclerview数据更改了
        AppConfig.adapter.notifyDataSetChanged();
        tv_note.setVisibility(View.GONE);
        Log.d("是否执行","111111111");
    }
}
