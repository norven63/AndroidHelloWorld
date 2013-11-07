package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * 由于是FrameLayout布局，所以在onTouch事件里如果对布局下的其他View(那些TextView)进行操作，那么会导致整个布局重绘，从而使得图片再次复位
 * ，效果即是图片原地不动。所以针对此现象，需要将TextView的宽和高设定死。
 */
@ContentView(R.layout.move_view_activity)
public class ViewMoveActivity extends RoboActivity {
  @InjectView(R.id.screen_width)
  private TextView screenWidthView;
  @InjectView(R.id.screen_height)
  private TextView screenHeightView;
  @InjectView(R.id.moveView_left)
  private TextView leftView;
  @InjectView(R.id.moveView_top)
  private TextView topView;
  @InjectView(R.id.moveView_right)
  private TextView rightView;
  @InjectView(R.id.moveView_bottom)
  private TextView bottomView;
  @InjectView(R.id.moveView)
  private View moveView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    moveView.setOnTouchListener(new OnTouchListener() {
      int screenWidth;
      int screenHeight;

      {
        screenWidth = ViewMoveActivity.this.getResources().getDisplayMetrics().widthPixels;
        screenHeight = ViewMoveActivity.this.getResources().getDisplayMetrics().heightPixels - 50;
        screenWidthView.setText("屏宽: " + screenWidth);
        screenHeightView.setText("屏高: " + screenHeight + "+50");
      }

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        // 注意算法
        int vx = v.getWidth() / 2;
        int vy = v.getHeight() / 2;

        int dx = (int) (event.getX() - vx);
        int dy = (int) (event.getY() - vy);

        switch (event.getAction()) {
          case MotionEvent.ACTION_MOVE:
            if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
              int top = v.getTop() + dy;
              int bottom = v.getBottom() + dy;
              int left = v.getLeft() + dx;
              int rigth = v.getRight() + dx;

              // 边界处理
              if (top < 0) {
                top = 0;
                bottom = v.getHeight();
              }

              if (bottom > screenHeight) {
                bottom = screenHeight;
                top = screenHeight - v.getHeight();
              }

              if (left < 0) {
                left = 0;
                rigth = v.getWidth();
              }

              if (rigth > screenWidth) {
                rigth = screenWidth;
                left = screenWidth - v.getWidth();
              }

              v.layout(left, top, rigth, bottom);

              leftView.setText("左: " + moveView.getLeft());
              topView.setText("上: " + moveView.getTop());
              rightView.setText("右: " + moveView.getRight());
              bottomView.setText("下: " + moveView.getBottom());
            }

            break;

          case MotionEvent.ACTION_UP:

            break;
          default:
            break;
        }

        return true;
      }
    });
  }
}
