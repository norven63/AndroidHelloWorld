package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import roboguice.activity.RoboActivity;

public class WindowManagerActivity extends RoboActivity {
  private float mPosX;
  private float mPosY;

  private Bitmap bitmap;
  private ImageView v;
  private WindowManager.LayoutParams mWindowParams;
  private WindowManager mWindowManager;

  @Override
  public boolean onTouchEvent(MotionEvent motion) {
    mPosX = motion.getX();
    mPosY = motion.getY();

    switch (motion.getAction()) {
      case MotionEvent.ACTION_DOWN:

        break;
      case MotionEvent.ACTION_MOVE:
        mWindowParams.alpha = 1.0f;
        mWindowParams.x = (int) mPosX;
        mWindowParams.y = (int) mPosY;

        mWindowManager.updateViewLayout(v, mWindowParams);

        break;
      case MotionEvent.ACTION_UP:
        // mWindowManager.removeView(v);

        break;
    }

    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mWindowManager = (WindowManager) this.getSystemService("window");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_button1);
    addViewToWindowManage(bitmap, 200, 200, 80, 80);
  }

  void addViewToWindowManage(Bitmap bm, int x, int y, int height, int width) {
    mWindowParams = new WindowManager.LayoutParams();

    mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
    mWindowParams.x = x;
    mWindowParams.y = y;
    // mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    // mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    mWindowParams.height = height;
    mWindowParams.width = width;
    mWindowParams.flags =
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
    mWindowParams.format = PixelFormat.TRANSLUCENT;
    mWindowParams.windowAnimations = 0;

    v = new ImageView(this);
    v.setImageBitmap(bm);
    mWindowManager.addView(v, mWindowParams);
  }
}
