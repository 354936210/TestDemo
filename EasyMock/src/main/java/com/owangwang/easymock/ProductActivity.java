package com.owangwang.easymock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.owangwang.easymock.utils.ExitApplication;

/**
 * Created by wangchao on 2017/12/22.
 */

public class ProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);
        ExitApplication.addAcrivity(this);
    }
}
