package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
      ViewGroup parentView = (ViewGroup) msg.obj;
      View view = parentView.getChildAt(msg.arg1);
      final View dragView = parentView.getChildAt(msg.arg2);

      // 监听View的坐标
      float x = view.getX();
      float y = view.getY();

      // 拖动View的原位置坐标
      float x_ = dragView.getX();
      float y_ = dragView.getY();

      ((ViewGroup) dragView.getParent()).removeView(dragView);
      parentView.addView(dragView, msg.arg1);

      // 在ViewGroup对拖动View的新增&删除操作完毕后,先将拖动View重新布局至原位置,然后再开启从原位置移动至新位置的动画
      dragView.setX(x_);
      dragView.setY(y_);
      dragView.animate().x(x).y(y).setDuration(620).setListener(new AnimatorListener() {

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
          // 恢复标记位,允许ViewGroup作出将本尊移回原位的响应操作
          isItemOnDragListener = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
          // 设置标记位,不让ViewGroup作出将本尊移回原位的响应操作
          isItemOnDragListener = false;
        }
      });

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
        final View dragView = (View) event.getLocalState();

        // 如果拖动事件遮蔽自己则直接return
        if (view.equals(dragView)) {
          return false;
        }

        Timer timer = (Timer) view.getTag(R.id.timer);

        final GridLayout parentView = (GridLayout) view.getParent();
        final int index = parentView.indexOfChild(view);

        switch (event.getAction()) {
          case DragEvent.ACTION_DRAG_ENTERED:
            // 开启计时器,在只有当拖动View在监听View处停留0.7秒后才会有效果
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                handler.post(new Runnable() {
                  @Override
                  public void run() {
                    // 发送Message以触发Handle处理,其中包含了ViewGroup对象,监听View和拖动View的索引
                    Message message = new Message();
                    message.obj = parentView;
                    message.arg1 = index;
                    message.arg2 = parentView.indexOfChild(dragView);

                    handler.sendMessage(message);
                  }
                });
              }
            }, 700);

            break;

          case DragEvent.ACTION_DRAG_EXITED:
            // 取消定时器,并重置
            timer.cancel();
            view.setTag(R.id.timer, new Timer());

            break;

          case DragEvent.ACTION_DROP:
            if (view.getAlpha() == (float) 0.3) {

            }

            dragView.setAlpha((float) 1.0);

            // 取消定时器,并重置
            timer.cancel();
            view.setTag(R.id.timer, new Timer());

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

      // 注意此处key的定义,是在res/values/string.xml文件中定义的一个<item>标签
      view.setTag(R.id.timer, new Timer());
    }

    // ViewGroup的拖动监听
    viewGroupDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(View v, DragEvent event) {
        View dragView = (View) event.getLocalState();

        switch (event.getAction()) {
          case DragEvent.ACTION_DROP:
            dragView.setAlpha((float) 1.0);

            float e_x = event.getX() - dragView.getWidth() / 2;
            float e_y = event.getY() - dragView.getHeight() / 2;

            if (isItemOnDragListener) {
              // 从拖动点移回至原位的动画
              float x = dragView.getX();
              float y = dragView.getY();

              dragView.setX(e_x);
              dragView.setY(e_y);

              dragView.animate().x(x).y(y);
            } else {
              // 两个View的移位动画未结束时就拦截住DROP事件处理方案
              // TODO
            }

            break;

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