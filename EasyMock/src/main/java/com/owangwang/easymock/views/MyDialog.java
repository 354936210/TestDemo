package com.owangwang.easymock.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.owangwang.easymock.R;

/**
 * Created by wangchao on 2017/12/18.
 */

public class MyDialog extends Dialog{
    Context context;
    Button bt_ok;
    Button bt_cancel;
    private View.OnClickListener okOncOnClickListener;
    private View.OnClickListener cancleOncOnClickListener;

    public MyDialog(@NonNull Context context,View.OnClickListener okOncOnClickListener,View.OnClickListener cancleOncOnClickListener) {
        super(context);
        this.context=context;
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.dialog_layout,null);
        bt_cancel=view.findViewById(R.id.bt_cancel);
        bt_ok=view.findViewById(R.id.bt_ok);
        this.okOncOnClickListener=okOncOnClickListener;
        this.cancleOncOnClickListener=cancleOncOnClickListener;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

    }
    public void setOkOnclickListener(){
        bt_ok.setOnClickListener(okOncOnClickListener);
    }
     public void setCancleOnclickListener(){
        bt_cancel.setOnClickListener(cancleOncOnClickListener);
    }

}
