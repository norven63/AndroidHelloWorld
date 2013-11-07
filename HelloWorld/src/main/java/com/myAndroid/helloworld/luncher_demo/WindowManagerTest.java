package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.luncher_test)
public class WindowManagerTest extends RoboActivity implements OnLongClickListener {
  private float mPosX;
  private float mPosY;

  private Bitmap bitmap;
  private WindowManager.LayoutParams mWindowParams;
  private WindowManager mWindowManager;
  // 这个ImageView是直接加进windowManager里面的
  private ImageView v;

  private OnDragListener dragListener;

  @InjectView(R.id.luncherLayout_1)
  private ViewGroup luncherLayout_1;

  @InjectView(R.id.luncherLayout_2)
  private ViewGroup luncherLayout_2;

  @InjectView(R.id.luncherImage_1)
  private ImageView luncherImage_1;

  @InjectView(R.id.luncherImage_2)
  private ImageView luncherImage_2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mWindowManager = (WindowManager) this.getSystemService("window");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_button);
    addViewToWindowManage(bitmap, 200, 200, 80, 80);

    // 为两张图片设置长按事件,长按后即会启动拖动效果
    luncherImage_1.setOnLongClickListener(this);
    luncherImage_2.setOnLongClickListener(this);

    // 你懂的
    dragListener = new OnDragListener() {

      @Override
      public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        ViewGroup newViewGroup = (ViewGroup) v;

        switch (action) {
        // 拖入事件
          case DragEvent.ACTION_DRAG_ENTERED:
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_enter));

            break;
          // 拖出事件
          case DragEvent.ACTION_DRAG_EXITED:
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_normal));

            break;
          // 完成拖入事件
          case DragEvent.ACTION_DROP:
            // 先把原来的给删除
            View dragView = (View) event.getLocalState();
            ViewGroup formerViewGroupview = (ViewGroup) dragView.getParent();
            formerViewGroupview.removeView(dragView);

            // 再加入到新的地方
            newViewGroup.addView(dragView);
            newViewGroup.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_normal));
            // 最后还要记得把本尊显示出来
            dragView.setVisibility(View.VISIBLE);

            break;
          default:
            break;
        }

        return true;
      }
    };
    luncherLayout_1.setOnDragListener(dragListener);
    luncherLayout_2.setOnDragListener(dragListener);
  }

  @Override
  public boolean onLongClick(View view) {
    // 设置数据,会被拖动目标ViewGroup所接收,即用来传递信息
    ClipData data = ClipData.newPlainText("", "");

    // 设置拖动阴影,即你所拖动的那个图标(这也同时说明,你拖动的不过是一个影分身,本尊其实并没有移动)
    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

    view.startDrag(data, shadowBuilder, view, 0);

    // 将本尊隐身
    view.setVisibility(View.INVISIBLE);

    return true;
  }

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