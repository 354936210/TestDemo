package com.owangwang.easymock;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.owangwang.easymock.utils.ExitApplication;
import com.owangwang.easymock.views.BaseActivity;

/**
 * Created by wangchao on 2017/12/22.
 */

public class ProductActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);
        ExitApplication.addAcrivity(this);
    }
}
