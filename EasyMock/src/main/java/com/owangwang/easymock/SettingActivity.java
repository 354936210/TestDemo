package com.owangwang.easymock;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
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
        if (preference.getBoolean("update",false)){
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
                Intent intent_product=new Intent(SettingActivity.this,ProductActivity.class);
                startActivity(intent_product);
                break;
            case R.id.setting_dianzan:
                break;
            case R.id.setting_fankui:
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=354936210";//uin是发送过去的qq号码
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SettingActivity.this,"请检查是否安装QQ！",Toast.LENGTH_SHORT).show();
                }
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

                    verifyStoragePermissions(this);
                }else {
                    editor.putBoolean("update",false);
                    editor.commit();

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
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode==REQUEST_EXTERNAL_STORAGE){
           if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
               editor.putBoolean("update",true);
               editor.commit();
           }else {
               Toast.makeText(SettingActivity.this,"开启自动更新功能需要内存读写权限！",Toast.LENGTH_SHORT)
                       .show();
               aSwitch.setChecked(false);
           }
           return;
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
