package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.Timer;
import java.util.TimerTask;

import android.widget.Toast;

import android.os.Message;

import android.os.Handler;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.luncher_demo)
public class LuncherDemoActivity extends RoboActivity implements OnLongClickListener {
  @SuppressLint("HandlerLeak")
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      View view = (View) msg.obj;
      view.setAlpha((float) 0.3);
    }
  };

  private OnDragListener viewGroupDragListener;
  private OnDragListener viewDragListener;

  // 为了防止ViewGroup和View同时监听drag事件而起冲突:true-ViewGroup可监听;false-View监听
  private boolean isItemOnDragListener;

  @InjectView(R.id.luncherParent)
  private ViewGroup luncherParent;

  @SuppressLint("NewApi")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 每个单独View的拖动监听
    viewDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(final View view, DragEvent event) {
        Timer timer = (Timer) view.getTag(R.id.timer);

        final View dragView = (View) event.getLocalState();

        final GridLayout newViewGroup = (GridLayout) view.getParent();

        final float x = view.getX();
        final float y = view.getY();

        switch (event.getAction()) {
          case DragEvent.ACTION_DRAG_ENTERED:
            // 开启计时器,只有当拖动图标在目标图标处停留1秒后才会有效果
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                handler.post(new Runnable() {
                  @Override
                  public void run() {
                    Message message = new Message();
                    message.obj = view;
                    handler.sendMessage(message);
                  }
                });
              }
            }, 1000);

            break;

          case DragEvent.ACTION_DRAG_EXITED:
            timer.cancel();
            view.setTag(R.id.timer, new Timer());
            view.setAlpha((float) 1.0);

            break;

          case DragEvent.ACTION_DROP:
            if (!view.equals(dragView)) {
              if (view.getAlpha() == (float) 0.3) {
                final int index = newViewGroup.indexOfChild(view);

                // 保持本尊的原位置坐标
                int l = dragView.getLeft();
                int t = dragView.getTop();
                int r = dragView.getRight();
                int b = dragView.getBottom();

                ((ViewGroup) dragView.getParent()).removeView(dragView);
                newViewGroup.addView(dragView, index);

                // 在ViewGroup操作完毕后,先将本尊重新布局至原位置,然后再开启从原位置移动至新位置的动画
                dragView.layout(l, t, r, b);
                dragView.animate().x(x).y(y);

                // 设置标记位,不让ViewGroup作出将本尊移回原位的响应操作
                isItemOnDragListener = false;
              }
            }

            dragView.setAlpha((float) 1.0);

            timer.cancel();
            view.setTag(R.id.timer, new Timer());
            view.setAlpha((float) 1.0);

            // 此处返回false是为了让ViewGroup能够捕获到DragEvent.ACTION_DROP事件!!!
            return false;

          default:

            break;
        }

        return true;
      }
    };

    // 为图片设置长按事件,长按后即会启动拖动效果;为图片设置拖动监听,当被其他图片拖动遮挡时,触发换位效果
    for (int i = 0; i < luncherParent.getChildCount(); i++) {
      View view = luncherParent.getChildAt(i);
      view.setOnDragListener(viewDragListener);
      view.setOnLongClickListener(this);

      // 注意此处key的定义,是在res/values/string.xml文件中定义的一个<item>表情
      view.setTag(R.id.timer, new Timer());
    }

    // ViewGroup的拖动监听
    viewGroupDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(View v, DragEvent event) {
        View dragView = (View) event.getLocalState();
        ViewGroup dragViewGroup = ((ViewGroup) dragView.getParent());

        switch (event.getAction()) {
          case DragEvent.ACTION_DRAG_ENTERED:
            Toast.makeText(LuncherDemoActivity.this, "ViewGroup - ENTERED", Toast.LENGTH_SHORT).show();
            break;

          case DragEvent.ACTION_DROP:
            Toast.makeText(LuncherDemoActivity.this, "ViewGroup - DROP", Toast.LENGTH_SHORT).show();

            if (isItemOnDragListener) {
              dragView.setAlpha((float) 1.0);

              // 从拖动点移回至原位的动画
              float x = dragView.getX();
              float y = dragView.getY();

              dragView.setX(event.getX() - dragView.getWidth() / 2);
              dragView.setY(event.getY() - dragView.getHeight() / 2);

              dragView.animate().x(x).y(y);

            } else {
              // // 先从原来的ViewGroup中删除
              // dragViewGroup.removeView(dragView);
              //
              // // 再加入到新的ViewGroup
              // ((ViewGroup) v).addView(dragView);
            }

          default:
            break;
        }

        return true;
      }
    };
    luncherParent.setOnDragListener(viewGroupDragListener);
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
    view.setAlpha((float) 0.5);

    isItemOnDragListener = true;
    return true;
  }

}