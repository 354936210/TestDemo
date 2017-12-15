package com.owangwang.easymock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.owangwang.easymock.bean.ExpressJson;
import com.owangwang.easymock.bean.Jsonbean;
import com.owangwang.easymock.bean.KuaiDibean;
import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.SaveEvent;
import com.owangwang.easymock.utils.AppConfig;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by wangchao on 2017/12/15.
 */

public class AddExpressActivity extends AppCompatActivity implements View.OnClickListener {
    Adapter arrayAdapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    List<KuaiDibean> mList;
    List<String> nameList;
    List<String> typeList;
    RequestQueue mQueue;
    Spinner selectType;

    EditText etSaveNumber;

    Button btSave;
    String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_express_layout);
        mQueue= AppConfig.myQueue(this);
        selectType=findViewById(R.id.select_type);
        etSaveNumber=findViewById(R.id.et_save_number);
        btSave=findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);
        initdata();
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,nameList);
        selectType.setAdapter((SpinnerAdapter) arrayAdapter);
        selectType.setSelection(0);
        type="auto";
        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        if (preferences.getBoolean("isfirst",true)){
            LitePal.getDatabase();
            doRequest();
        }else {
            List<MyTypeData> typeData= DataSupport.findAll(MyTypeData.class);
            for (MyTypeData mdata:typeData){
                typeList.add(mdata.getType());
                nameList.add(mdata.getName());
                Log.d("name",mdata.getName());
                Log.d("type",mdata.getType());
            }
        }
    }
    public void saveExpress(){
        doQuery(etSaveNumber.getText().toString(),type);
        finish();
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

                            Log.d("gggggg","发送成功");
                            EventBus.getDefault().post(new SaveEvent(
                                    expressJson.getResult().getType(),
                                    expressJson.getResult().getNumber(),
                                    expressJson.getResult().getDeliverystatus()
                            ));
                        }else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(etSaveNumber.getText())){
            saveExpress();
        }else {
            etSaveNumber.setError("请填写快递单号!");
        }
    }
}
