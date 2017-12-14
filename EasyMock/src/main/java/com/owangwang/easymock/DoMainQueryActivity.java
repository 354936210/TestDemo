package com.owangwang.easymock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.ExpressJson;
import com.owangwang.easymock.bean.MyExpress;
import com.owangwang.easymock.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class DoMainQueryActivity extends AppCompatActivity {
    TextView tv_status;
    TextView tv_name_and_number;
    LinearLayout layout_show;
    RequestQueue mQueue;
    RecyclerView rv_express;
    ExpressAdapter expressAdapter;
    Button bt_query;
    EditText et_id;
    Adapter arrayAdapter;
    Spinner spinner;
    List<String> nameList;
    List<String> typeList;
    List<MyExpress> myExpressesList;
    String type="auto";
    String tag="DoMainQueryActivity--------->";

    int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_kuaidi);
        mQueue= AppConfig.myQueue(this);
        nameList=new ArrayList<>();
        typeList=new ArrayList<>();
        myExpressesList=new ArrayList<>();
        typeList=getIntent().getStringArrayListExtra("typelist");
        nameList=getIntent().getStringArrayListExtra("namelist");
        position=getIntent().getIntExtra("position",0);
        type=typeList.get(position);
        layout_show=findViewById(R.id.layout_show);
        spinner=findViewById(R.id.sp);
        bt_query=findViewById(R.id.bt_query);
        rv_express=findViewById(R.id.rv_express);
        tv_name_and_number=findViewById(R.id.name_and_number);
        tv_status=findViewById(R.id.status);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rv_express.setLayoutManager(manager);
        et_id=findViewById(R.id.et_kuaidi_id);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,nameList);
        spinner.setAdapter((SpinnerAdapter) arrayAdapter);
        spinner.setSelection(position);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=typeList.get(position);
                Log.d(tag,"type:"+type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=et_id.getText().toString();
                doQuery(id,type);
            }
        });
    }

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
                            expressAdapter = new ExpressAdapter(myExpressesList, DoMainQueryActivity.this);
                            rv_express.setAdapter(expressAdapter);
                            layout_show.setVisibility(View.VISIBLE);

                            for (String s:typeList
                                 ) {
                                if (s.equals(expressJson.getResult().getType())){
                                    tv_name_and_number.setText(nameList.get(typeList.indexOf(s))+":"+expressJson.getResult().getNumber());
                                }
                            }
                            if (expressJson.getResult().getDeliverystatus().equals("1")){
                                tv_status.setText("在途中");
                            }else if (expressJson.getResult().getDeliverystatus().equals("2")){
                                tv_status.setText("派送中");
                            }else if (expressJson.getResult().getDeliverystatus().equals("3")){
                                tv_status.setText("已签收");
                            }else if (expressJson.getResult().getDeliverystatus().equals("4")) {
                                tv_status.setText("派送失败");
                            }

                        }else {
                            Toast.makeText(DoMainQueryActivity.this,expressJson.getMsg(),Toast.LENGTH_LONG).show();
                            myExpressesList.clear();
                            expressAdapter.notifyDataSetChanged();
                            layout_show.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DoMainQueryActivity.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
                myExpressesList.clear();
                expressAdapter.notifyDataSetChanged();
                layout_show.setVisibility(View.GONE);
            }
        });
        mQueue.add(gsonRequest);
    }
}
