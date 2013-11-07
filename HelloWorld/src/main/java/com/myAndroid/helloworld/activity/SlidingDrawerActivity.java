package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.myAndroid.helloworld.R;

public class SlidingDrawerActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.slidingdrawer_layout);
    SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);

    slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
      @Override
      public void onDrawerClosed() {
        Toast.makeText(SlidingDrawerActivity.this, "onDrawerClosed()", Toast.LENGTH_SHORT).show();
      }
    });

    slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
      @Override
      public void onDrawerOpened() {
        Toast.makeText(SlidingDrawerActivity.this, "onDrawerOpened()", Toast.LENGTH_SHORT).show();
      }
    });

    slidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {
      @Override
      public void onScrollEnded() {
        Toast.makeText(SlidingDrawerActivity.this, "onScrollEnded()", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onScrollStarted() {
        Toast.makeText(SlidingDrawerActivity.this, "onScrollStarted()", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
