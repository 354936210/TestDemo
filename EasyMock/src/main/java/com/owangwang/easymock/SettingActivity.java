package com.owangwang.easymock;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.owangwang.easymock.utils.ExitApplication;

/**
 * Created by wangchao on 2017/12/21.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    RelativeLayout set_dianzan;
    RelativeLayout set_fankui;
    RelativeLayout set_product;
    Button bt_tuichu;
    Switch aSwitch;
    TextView tv_title;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ExitApplication.addAcrivity(this);
        preference=getSharedPreferences("setting_config",MODE_PRIVATE);
        editor=preference.edit();
        set_dianzan=findViewById(R.id.setting_dianzan);
        set_fankui=findViewById(R.id.setting_fankui);
        set_product=findViewById(R.id.setting_product);
        bt_tuichu=findViewById(R.id.bt_tuichu);
        aSwitch=findViewById(R.id.switch1);
        tv_title=findViewById(R.id.setting_tv_title);
        Typeface mtypeface= Typeface.createFromAsset(getAssets(),"fonts/STHUPO.TTF");
        if (preference.getBoolean("update",true)){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
        tv_title.setTypeface(mtypeface);
        aSwitch.setOnCheckedChangeListener(this);
        bt_tuichu.setOnClickListener(this);
        set_product.setOnClickListener(this);
        set_fankui.setOnClickListener(this);
        set_dianzan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_product:
                break;
            case R.id.setting_dianzan:
                break;
            case R.id.setting_fankui:
                break;
            case R.id.bt_tuichu:
                ExitApplication.exit();
                break;
            default:
                  break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch1:
                if (isChecked){
                    editor.putBoolean("update",true);
                    editor.commit();
                    Toast.makeText(SettingActivity.this,"开",Toast.LENGTH_SHORT).show();
                }else {
                    editor.putBoolean("update",false);
                    editor.commit();
                    Toast.makeText(SettingActivity.this,"关",Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
