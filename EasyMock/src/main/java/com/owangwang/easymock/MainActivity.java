package com.owangwang.easymock;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.owangwang.easymock.bean.ExpressJson;
import com.owangwang.easymock.bean.MyTypeData;
import com.owangwang.easymock.bean.RefreshEvent;
import com.owangwang.easymock.bean.SaveEvent;
import com.owangwang.easymock.bean.UpdateEvent;
import com.owangwang.easymock.bean.WoDeKuaiDi;
import com.owangwang.easymock.utils.AppConfig;
import com.owangwang.easymock.utils.ExitApplication;
import com.owangwang.easymock.utils.GsonRequest;
import com.owangwang.easymock.views.BaseActivity;

import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {
    private ImageView iv_open;
    private TextView tv_note;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue mQueue;
    private int count=0;
    private long time;
    private NavigationView navigationView;
    private TextView tv_version;
    DrawerLayout drawerLayout;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    int serviceVersionCOde=3;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.express_manager_layout);
        //设置左滑菜单中显示的版本号
        tv_version=findViewById(R.id.tv_version);
        try {
            tv_version.setText(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mQueue=AppConfig.myQueue(this);
        ExitApplication.addAcrivity(this);

        preference=getSharedPreferences("setting_config",MODE_PRIVATE);
        editor=preference.edit();
        if (preference.getBoolean("update",false)){
            Log.d("MainActivity---->","执行检测版本功能");
            getNewVersion();
        }
        navigationView=findViewById(R.id.na_v);
        drawerLayout=findViewById(R.id.dl_swipe);
        iv_open=findViewById(R.id.iv_open);
        iv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.it_search:
                        intent=new Intent(MainActivity.this,QueryActivity.class );
                        startActivity(intent);
                        break;
                    case R.id.it_call:
                        try {
                            //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=354936210";//uin是发送过去的qq号码
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"请检查是否安装QQ！",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.it_set:
                        intent=new Intent(MainActivity.this,SettingActivity.class );
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

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
        AppConfig.myQueue(this).stop();
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
    public void onEventMainThread(UpdateEvent event) {
     checkVersion();
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
        //如果左滑菜单是开启状态首先关闭左滑菜单
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }
        //如果左滑菜单没开启那么执行检测多次点击才退出程序的逻辑
        else {

            if (time != 0) {
                long sx = (System.currentTimeMillis() - time) / 1000;
                if (sx <= 2) {
                    finish();
                } else {
                    time = System.currentTimeMillis();
                    Toast.makeText(this, "请再次按返回键退出", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "请再次按返回键退出", Toast.LENGTH_LONG).show();
                time = System.currentTimeMillis();
            }
        }

    }


    /*
 * 获取当前程序的版本名
 */
    private String getVersionName() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG","版本号"+packInfo.versionCode);
        Log.e("TAG","版本名"+packInfo.versionName);
        return packInfo.versionName;
    }
    /*
 * 获取当前程序的版本号
 */
    private int getVersionCode() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG","版本号"+packInfo.versionCode);
        Log.e("TAG","版本名"+packInfo.versionName);
        return packInfo.versionCode;
    }


    //对比本程序的版本号和最新程序的版本号
    public void checkVersion() {//按钮！
        //如果检测本程序的版本号小于服务器的版本号，那么提示用户更新

        try {
            if (getVersionCode() < serviceVersionCOde) {
                showDialogUpdate();//弹出提示版本更新的对话框

            }else{
                //否则吐司，说现在是最新的版本
//                Toast.makeText(this,"当前已经是最新的版本",Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate() {

        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置提示框的标题
        builder.setTitle("版本升级").
                // 设置提示框的图标
                        setIcon(R.mipmap.ic_launcher).
                // 设置要显示的信息
                        setMessage("发现新版本！请及时更新").
                // 设置确定按钮
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, "选择确定哦", 0).show();

                        loadNewVersionProgress();//下载最新的版本程序
                    }
                }).

                // 设置取消按钮,null是什么都不做，并关闭对话框
                        setNegativeButton("取消", null);

        // 生产对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();


    }
    /**
     * 下载新版本程序，需要子线程
     */
    private void loadNewVersionProgress() {
//        final   String uri="http://www.apk.anzhi.com/data3/apk/201703/14/4636d7fce23c9460587d602b9dc20714_88002100.apk";
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        //启动子线程下载任务
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(mUrl, pd);
                    sleep(3000);
                    Log.d("---","---------------------");
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                    Log.d("更新调试","失败");
//                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }}.start();
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public File getFileFromServer(String uri, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(uri);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time= System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(getExternalFilesDir("upgrade_apk") +
                    File.separator +
                    getPackageName() +
                    ".apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }
    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Log.d("是否执行","是");
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri apkUri =
//                FileProvider.getUriForFile(MainActivity.this,"com.owangwang.easymock.fileprovider", file);
//        // 由于没有在Activity环境下启动Activity,设置下面的标签
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //添加这一句表示对目标应用临时授权该Uri所代表的文件
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        //执行的数据类型
//        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        startActivity(intent);
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断android版本是否是7.0以上
        //对于7.0版本以上要特殊处理
        if (Build.VERSION.SDK_INT>=24){
            installApkIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), "com.owangwang.easymock.fileprovider", file), "application/vnd.android.package-archive");
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        if (getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            startActivity(installApkIntent);
        }

    }

    public void getNewVersion() {
        //从网络获取版本号
        StringRequest stringRequest = new StringRequest("http://www.owangwang.com/version/version.html",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        serviceVersionCOde=Integer.parseInt(response);
                        Log.d("最新版本","版本号----"+response);
                        EventBus.getDefault().post(new UpdateEvent("1"));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        //从网络获取最新版本下载地址
        StringRequest stringRequest1 = new StringRequest("http://www.owangwang.com/version/versionurl.html",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mUrl=response;
                        Log.d("最新版本","版本地址-----"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
        mQueue.add(stringRequest1);

    }
}
