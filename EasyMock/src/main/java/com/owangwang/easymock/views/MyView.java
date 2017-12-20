package com.owangwang.easymock.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangchao on 2017/12/14.
 */

public class MyView extends View {
    Paint paint=new Paint();
    Paint paint1=new Paint();
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.parseColor("#aba7a7"));
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        paint1.setColor(Color.parseColor("#aba7a7"));
        paint1.setStyle(Paint.Style.FILL);
        paint1.setDither(true);

    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawCircle(canvas);
    }

    /**
     * 画实心圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {

        canvas.drawCircle(getWidth()/2,getHeight()/2,10,paint1);
    }

    /**
     * 画竖直的线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Path path=new Path();
        path.moveTo(getWidth()/2,0);
        path.lineTo(getWidth()/2,getHeight());
        canvas.drawPath(path,paint);
    }

}
