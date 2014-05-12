package com.myAndroid.helloworld.customView;

import com.myAndroid.helloworld.R;

import android.graphics.Paint;

import android.graphics.Color;

import android.graphics.Canvas;

import android.view.LayoutInflater;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class MyLayout extends LinearLayout {
  private final Paint paint;

  public MyLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    paint = new Paint();// 如果不再此处定义Paint,onDraw()等方法无法正常工作

    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    layoutInflater.inflate(R.layout.mylayout, this);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    canvas.drawColor(Color.GREEN);

    paint.setColor(Color.RED);
    paint.setStrokeWidth((float) 2.0);

    canvas.drawLine(5, 5, 800, 5, paint);

    super.dispatchDraw(canvas);
  }
}
