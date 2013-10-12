package com.myAndroid.helloworld.view;

import com.myAndroid.helloworld.R;
import android.view.LayoutInflater;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class MyLayout extends LinearLayout {

  public MyLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    layoutInflater.inflate(R.layout.mylayout, this);
  }

}
