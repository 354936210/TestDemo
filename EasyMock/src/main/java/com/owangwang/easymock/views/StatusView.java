package com.owangwang.easymock.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangchao on 2017/12/19.
 */

public class StatusView extends View{
    private int mWith;
    private int mHeight;
    private String statusText;
    private int radius;
    Paint paint;
    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mHeight=getHeight();
        mWith=getWidth();
        if (mWith>mHeight){
            radius=mHeight/2;
        }else {
            radius=mWith/2;
        }
        paint=new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.STROKE);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setStatusText(String statusText){
        this.statusText=statusText;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWith/2,mHeight/2,radius,paint);
    }
}
