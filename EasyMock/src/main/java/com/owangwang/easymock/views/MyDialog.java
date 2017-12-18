package com.owangwang.easymock.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by wangchao on 2017/12/18.
 */

public class MyDialog extends Dialog{

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
