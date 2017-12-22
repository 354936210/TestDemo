package com.owangwang.easymock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.ExpressJson;
import com.owangwang.easymock.bean.Jsonbean;
import com.owangwang.easymock.bean.KuaiDibean;
import com.owangwang.easymock.bean.MyExpress;
import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.StatusEvent;
import com.owangwang.easymock.bean.TypeEvent;
import com.owangwang.easymock.utils.AppConfig;
import com.owangwang.easymock.utils.ExitApplication;
import com.owangwang.easymock.views.MyAlertDialog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by wangchao on 2017/12/14.
 */

public class QueryActivity extends AppCompatActivity {
    private List<KuaiDibean> mList;
    private TextView tv_status;
    private TextView tv_name_and_number;
    private LinearLayout layout_show;
    private RequestQueue mQueue;
    private RecyclerView rv_express;
    private ExpressAdapter expressAdapter;
    private Button bt_query;
    private EditText et_id;
    private ArrayAdapter arrayAdapter;
    private Spinner spinner;
    private List<String> nameList;
    private List<String> typeList;
    private List<MyExpress> myExpressesList;
    private String type="auto";
    private String tag="DoMainQueryActivity--------->";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int position;
    private MyAlertDialog dialog;
    private TextView tv_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_layout);
        ExitApplication.addAcrivity(this);
        dialog=new MyAlertDialog(this);
        initdata();
        iniview();
        Intent inten=getIntent();
        String nummber=inten.getStringExtra("mnumber");
        if (nummber!=null){
            et_id.setText(nummber);
            type=inten.getStringExtra("mtype");
            doQuery(nummber,type);
        }
    }

    private void iniview() {

        EventBus.getDefault().register(this);
        tv_title=findViewById(R.id.query_tv_title);
        Typeface mtypeface= Typeface.createFromAsset(getAssets(),"fonts/STHUPO.TTF");
        tv_title.setTypeface(mtypeface);
        layout_show=findViewById(R.id.layout_show1);
        spinner=findViewById(R.id.sp1);
        bt_query=findViewById(R.id.bt_query1);
        rv_express=findViewById(R.id.rv_express1);
        tv_name_and_number=findViewById(R.id.name_and_number1);
        tv_status=findViewById(R.id.status1);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rv_express.setLayoutManager(manager);
        et_id=findViewById(R.id.et_kuaidi_id1);
        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_layout,nameList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout1);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=et_id.getText().toString();
                dialog.showDialog();
                doQuery(id,type);
            }
        });
    }

    private void initdata() {
        preferences=getSharedPreferences("shared",MODE_PRIVATE);
        editor=preferences.edit();

        mQueue= AppConfig.myQueue(this);
        nameList=new ArrayList<>();
        typeList=new ArrayList<>();
        mList=new ArrayList<>();
        myExpressesList=new ArrayList<>();

        if (preferences.getBoolean("isfirst",true)){
            LitePal.getDatabase();
            dialog.showDialog();
            doRequest();
        }else {
            List<MyTypeData> typeData= DataSupport.findAll(MyTypeData.class);
            for (MyTypeData mdata:typeData){
                typeList.add(mdata.getType());
                nameList.add(mdata.getName());
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
    private void doQuery(String kuaidi_id,String type) {
//        http://api.jisuapi.com/express/query?appkey=98e3089a2762b1b2&type=auto&number=469621723142
        String squrl="http://api.jisuapi.com/express/query?appkey=98e3089a2762b1b2&type="+type
                +"&number="+kuaidi_id;
        GsonRequest<ExpressJson> gsonRequest=new GsonRequest<ExpressJson>(squrl, ExpressJson.class,
                new Response.Listener<ExpressJson>() {
                    @Override
                    public void onResponse(ExpressJson expressJson) {
                        if (expressJson.getStatus().equals("0")) {
                            myExpressesList = expressJson.getResult().getList();
                            expressAdapter = new ExpressAdapter(myExpressesList, QueryActivity.this);
                            rv_express.setAdapter(expressAdapter);
                            layout_show.setVisibility(View.VISIBLE);
                            EventBus.getDefault().post(new StatusEvent(expressJson.getResult().getDeliverystatus()));

                            EventBus.getDefault().post(new TypeEvent(expressJson.getResult().getType()));
                        }else {
                            Toast.makeText(QueryActivity.this,expressJson.getMsg(),Toast.LENGTH_LONG).show();
                            myExpressesList.clear();
                            expressAdapter.notifyDataSetChanged();
                            layout_show.setVisibility(View.GONE);
                        }
                        dialog.cancleDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.cancleDialog();
                Toast.makeText(QueryActivity.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
                myExpressesList.clear();
                expressAdapter.notifyDataSetChanged();
                layout_show.setVisibility(View.GONE);
            }
        });
        mQueue.add(gsonRequest);
    }

    /**
     * 查询所有快递公司的基本信息
      */
    public void doRequest(){
        String url="http://api.jisuapi.com/express/type?appkey=98e3089a2762b1b2";
        GsonRequest<Jsonbean> gsonRequest=new GsonRequest<Jsonbean>(url, Jsonbean.class, new Response.Listener<Jsonbean>() {
            @Override
            public void onResponse(Jsonbean jsonbean) {

                    mList=jsonbean.getResult();
                    MyTypeData typeData;
                    if(preferences.getBoolean("isfirst",true)){
                        typeData=new MyTypeData();
                        typeList.add("auto");
                        nameList.add("自动识别");
                        typeData.setType("auto");
                        typeData.setName("自动识别");
                        typeData.save();
                        for (KuaiDibean kuaiDibean:
                             mList) {
                            typeList.add(kuaiDibean.getType());
                            nameList.add(kuaiDibean.getName());
                            typeData=new MyTypeData();
                            typeData.setName(kuaiDibean.getName());
                            typeData.setLetter(kuaiDibean.getLetter());
                            typeData.setType(kuaiDibean.getType());
                            typeData.setTel(kuaiDibean.getTel());
                            typeData.setNumber(kuaiDibean.getNumber());
                            typeData.save();
                        }
                        editor.putBoolean("isfirst",false);
                        editor.commit();
                        EventBus.getDefault().post("1");
                    }
                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        mQueue.add(gsonRequest);
    }
    public void onEventMainThread(String s) {
        dialog.cancleDialog();
        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_layout,nameList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout1);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
    }
    public void onEventMainThread(TypeEvent data) {
        //由于LitePal的BUG所存数据全为大写所以要做大写的处理
        //挺坑的
        String s=data.getType().toUpperCase();
        Log.d("wangchao",s);
        int i=0;
        for (String ss:typeList){
            if (ss.equals(s)){
                tv_name_and_number.setText(nameList.get(i));
                return;
            }
            i++;
        }

    }
    public void onEventMainThread(StatusEvent s) {
        if (s.getStatus().equals("1")){
            tv_status.setText("在途中");
        }else if (s.getStatus().equals("2")){
            tv_status.setText("派送中");
        }else if (s.getStatus().equals("3")){
            tv_status.setText("已签收");
        }else if (s.getStatus().equals("4")) {
            tv_status.setText("派送失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("11111","asfasfasf");
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}