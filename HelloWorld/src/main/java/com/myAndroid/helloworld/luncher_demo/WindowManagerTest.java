package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class WindowManagerTest extends RoboActivity implements OnLongClickListener {
  private static final String TAG = "GRAGTEST";

  private float mPosX;
  private float mPosY;

  private Bitmap bitmap;
  private WindowManager.LayoutParams mWindowParams;
  private WindowManager mWindowManager;
  // 这个ImageView是直接加进windowManager里面的
  private ImageView v;

  private OnDragListener viewGroupDragListener;
  private OnDragListener viewDragListener;

  // 为了防止ViewGroup和View同时监听drap而起冲突:true-ViewGroup可监听;false-View监听
  private boolean isItemOnDragListener;

  @InjectView(R.id.luncherLayout_1)
  private ViewGroup luncherLayout_1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.luncher_test);

    mWindowManager = (WindowManager) this.getSystemService("window");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_button1);
    addViewToWindowManage(bitmap, 200, 200, 80, 80);

    viewDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(View view, DragEvent event) {
        View dragView = (View) event.getLocalState();
        dragView.setAlpha((float) 0.3);
        GridLayout newViewGroup = (GridLayout) view.getParent();

        switch (event.getAction()) {
        // 拖入事件
          case DragEvent.ACTION_DRAG_ENTERED:
            if (view.equals(dragView)) {
              return true;
            }

            newViewGroup.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_enter));

            int index = newViewGroup.indexOfChild(view);
            ((ViewGroup) dragView.getParent()).removeView(dragView);

            newViewGroup.addView(dragView, index);

            break;

          case DragEvent.ACTION_DROP:
            // 此处只为修正GridLayout位置错乱问题
            int index2 = newViewGroup.indexOfChild(dragView);
            newViewGroup.removeView(dragView);
            newViewGroup.addView(dragView, index2);

            break;
          default:
            break;
        }

        // 设置标记位,不让ViewGroup作出响应操作
        isItemOnDragListener = false;

        return true;
      }
    };

    // 为图片设置长按事件,长按后即会启动拖动效果;为图片设置拖动监听,当被其他图片拖动遮挡时,触发换位效果
    for (int i = 0; i < luncherLayout_1.getChildCount(); i++) {
      View view = luncherLayout_1.getChildAt(i);
      view.setOnDragListener(viewDragListener);
      view.setOnLongClickListener(this);
    }

    // 你懂的
    viewGroupDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(View v, DragEvent event) {
        View dragView = (View) event.getLocalState();

        switch (event.getAction()) {
        // 拖入事件
          case DragEvent.ACTION_DRAG_ENTERED:
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_enter));

            break;
          // 拖出事件
          case DragEvent.ACTION_DRAG_EXITED:
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_normal));

            break;
          // 放入事件
          case DragEvent.ACTION_DROP:
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_normal));

            if (isItemOnDragListener) {
              // 先把原来的给删除
              ((ViewGroup) dragView.getParent()).removeView(dragView);

              // 再加入到新的地方
              ((ViewGroup) v).addView(dragView);
            }

            return false;
          default:
            break;
        }

        return true;
      }
    };
    luncherLayout_1.setOnDragListener(viewGroupDragListener);
  }

  @Override
  public boolean onLongClick(View view) {
    // 设置数据,会被拖动目标ViewGroup所接收,即用来传递信息
    ClipData data = ClipData.newPlainText("", "");

    // 设置拖动阴影,即你所拖动的那个图标(这也同时说明,你拖动的不过是一个影分身,本尊其实并没有移动)
    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

    view.startDrag(data, shadowBuilder, view, 0);

    // 将本尊隐身(其实是淡化处理,为什么不View.INVISIBLE而是采取淡化呢?是因为防止在其隐身后,该拖动事件被其ViewGroup捕获,从而造成"The specified child already has a parent"的异常)
    // view.setVisibility(View.INVISIBLE);
    view.setAlpha((float) 0.3);

    isItemOnDragListener = true;
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

    v.setVisibility(View.GONE);
    mWindowManager.addView(v, mWindowParams);
  }

}