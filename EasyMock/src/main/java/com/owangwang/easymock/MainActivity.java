package com.owangwang.easymock;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.ExpressJson;
import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.RefreshEvent;
import com.owangwang.easymock.bean.SaveEvent;
import com.owangwang.easymock.bean.WoDeKuaiDi;
import com.owangwang.easymock.utils.AppConfig;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    private TextView tv_note;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue mQueue;
    private int count=0;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.express_manager_layout);
        mQueue=AppConfig.myQueue(this);
        tv_note=findViewById(R.id.tv_note);
        refreshLayout=findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAll();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(SaveEvent event) {
        List<MyTypeData> typeData= DataSupport.select("name").where("type=?",event.getType().toUpperCase()).find(MyTypeData.class);
        String name=typeData.get(0).getName();
        WoDeKuaiDi kuaiDi=new WoDeKuaiDi();
        kuaiDi.setName(name);
        kuaiDi.setType(event.getType());
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
    public void onEventMainThread(RefreshEvent event) {
        Log.d("MainActivity----->",count+"");
        if (count==AppConfig.mList.size()) {
            if (event.getMsg().equals("成功")) {
                AppConfig.mList = DataSupport.findAll(WoDeKuaiDi.class);
                AppConfig.adapter.notifyDataSetChanged();
                Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
            } else {
                AppConfig.mList = DataSupport.findAll(WoDeKuaiDi.class);
                AppConfig.adapter.notifyDataSetChanged();
                Toast.makeText(this, "刷新失败", Toast.LENGTH_SHORT).show();
            }
            count=0;
        }

        refreshLayout.setRefreshing(false);
    }

    /**
     * 刷新数据
     */
    private void refreshAll(){
        if (AppConfig.mList.isEmpty()){
            refreshLayout.setRefreshing(false);
        }else {
            //遍历本利保存快递信息，获取快递单号以及type进行网络请求查询
            for (WoDeKuaiDi kuaiDi:AppConfig.mList){
                AppConfig.myQueue(this).add(getRequest(kuaiDi.getNumber(),kuaiDi.getType()));
            }
        }
    }
    /**
     * 查询快递运送状态
     * @param kuaidi_id
     * 快递单号
     * @param type
     * 快递公司的type
     */
    private GsonRequest getRequest(final String kuaidi_id, String type) {
//        http://api.jisuapi.com/express/query?appkey=98e3089a2762b1b2&type=auto&number=469621723142
        String squrl="http://api.jisuapi.com/express/query?appkey=98e3089a2762b1b2&type="+type
                +"&number="+kuaidi_id;
        GsonRequest<ExpressJson> gsonRequest=new GsonRequest<ExpressJson>(squrl, ExpressJson.class,
                new Response.Listener<ExpressJson>() {
                    @Override
                    public void onResponse(ExpressJson expressJson) {
                        if (expressJson.getStatus().equals("0")) {
                            WoDeKuaiDi kuaiDi=new WoDeKuaiDi();
                            kuaiDi.setStatus(expressJson.getResult().getDeliverystatus());
                            int i=kuaiDi.updateAll("number=?",kuaidi_id);
                            if (i==1){
                                EventBus.getDefault().post(new RefreshEvent(count++,"成功"));
                            }
                        }else {
                            EventBus.getDefault().post(new RefreshEvent(count++,"失败"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                EventBus.getDefault().post(new RefreshEvent(count++,"失败"));
            }
        });
//        mQueue.add(gsonRequest);
        return gsonRequest;
    }

    @Override
    public void onBackPressed() {
        if (time!=0){
            long sx=(System.currentTimeMillis()-time)/1000;
            if (sx<=2){
                finish();
            }
        }else {
            Toast.makeText(this,"请再次按返回键退出",Toast.LENGTH_SHORT).show();
            time=System.currentTimeMillis();
        }


    }
}
