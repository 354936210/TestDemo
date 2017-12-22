package com.owangwang.easymock.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.owangwang.easymock.R;

/**
 * Created by wangchao on 2017/12/21.
 */

public class MyAlertDialog {
    static AlertDialog alertDialog;
    private Context context;
    View progressView;
    public MyAlertDialog(Context context){
        this.context=context;
    }
    /**
     * 显示dialog
     */
    public void showDialog() {
        if (alertDialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            progressView= View.inflate(context, R.layout.progress_layout,null);
            builder.setView(progressView);
            builder.setCancelable(false);
            alertDialog=builder.create();
        }
        alertDialog.show();
    }

    /**
     * 取消dialog显示
     */
    public void cancleDialog(){
        if (alertDialog!=null) {
            alertDialog.dismiss();
        }
    }
}
