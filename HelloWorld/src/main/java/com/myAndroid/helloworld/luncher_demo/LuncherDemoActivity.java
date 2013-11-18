package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.luncher_demo)
public class LuncherDemoActivity extends RoboActivity implements OnLongClickListener {
  @SuppressLint("HandlerLeak")
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      ViewGroupAddAndRemove viewGroupAddAndRemove = (ViewGroupAddAndRemove) msg.obj;
      ViewGroup parentView = viewGroupAddAndRemove.getViewGroup();
      final View view = viewGroupAddAndRemove.getListenerView();
      final View dragView = viewGroupAddAndRemove.getDragView();

      /**
       * 能够触发位移操作两种条件: 1.停留超过指定秒数后并继续拖动,根据左移还是右移判断当坐标与(监听View的宽度/2)的值 2.拖动View是一个CellView
       */
      if (msg.what == 1 || dragView instanceof CellView) {
        // 为了防止监听View在执行布局调整动画时仍然继续监听onDrag事件,并然后在0.35秒后恢复其监听功能
        view.setTag(R.id.isMove, true);
        new Timer().schedule(new TimerTask() {
          @Override
          public void run() {
            view.setTag(R.id.isMove, false);

            handler.post(new Runnable() {
              @Override
              public void run() {
                view.setAlpha((float) 1.0);
              }
            });
          }
        }, 350);

        // 删除&添加
        if (null != dragView.getTag()) {
          CellView cellView = (CellView) dragView.getTag();
          cellView.removeChildView(dragView);
          dragView.setTag(null);
        }

        if (null != ((ViewGroup) dragView.getParent())) {
          ((ViewGroup) dragView.getParent()).removeView(dragView);
        }
        parentView.addView(dragView, msg.arg1);

        // 强制执行一次布局动画,使子元素能够正常排列(防止错乱现象)
        ((ViewGroup) dragView.getParent()).scheduleLayoutAnimation();

      } else {
        view.setAlpha((float) 0.3);
      }
    }
  };

  private float currentX = 0;
  private boolean isLeft = true;

  private LuncherDemoFragment luncherDemoFragment;
  @InjectView(R.id.cellLayout)
  private LinearLayout cellLayout;
  @InjectView(R.id.middleLayout)
  private LinearLayout middleLayout;

  private OnDragListener viewDragListener;
  private OnDragListener viewGroupDragListener;

  // 为了防止ViewGroup和View同时监听drag事件而起冲突:true-ViewGroup可监听;false-View监听
  private boolean isItemOnDragListener = true;

  @InjectView(R.id.luncherParent)
  private ViewGroup luncherParent;

  @SuppressLint("NewApi")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (null == luncherDemoFragment) {
      luncherDemoFragment = new LuncherDemoFragment();
      FragmentTransaction fragmentTransaction = LuncherDemoActivity.this.getFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.cellLayout, luncherDemoFragment);
      fragmentTransaction.commitAllowingStateLoss();
    }

    middleLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        hiddenLayout();
      }
    });

    cellLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // 只为拦截middleLayout的点击事件
      }
    });

    // 每个单独View的拖动监听
    viewDragListener = new OnDragListener() {
      @Override
      public boolean onDrag(final View view, DragEvent event) {
        // 如果监听View正在执行一个移动动画则直接return
        if (null != view.getTag(R.id.isMove) && (Boolean) view.getTag(R.id.isMove)) {
          return false;
        }

        final View dragView = (View) event.getLocalState();

        // 如果拖动事件遮蔽自己则直接return
        if (view.equals(dragView)) {
          return false;
        }

        final GridLayout parentView = (GridLayout) view.getParent();
        if (null == parentView) {
          return false;
        }

        final int index = parentView.indexOfChild(view);

        Timer timer = (Timer) view.getTag(R.id.timer);

        switch (event.getAction()) {
          case DragEvent.ACTION_DRAG_ENTERED:
            // 开启计时器,在只有当拖动View在监听View处停留0.25秒后才会有效果
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                handler.post(new Runnable() {
                  @Override
                  public void run() {
                    ViewGroupAddAndRemove viewGroupAddAndRemove = new ViewGroupAddAndRemove();
                    viewGroupAddAndRemove.setViewGroup(parentView);
                    viewGroupAddAndRemove.setListenerView(view);
                    viewGroupAddAndRemove.setDragView(dragView);

                    // 发送Message以触发Handle处理,其中封装了ViewGroup、监听View、拖动View三个对象以及监听View的索引
                    Message message = new Message();
                    message.obj = viewGroupAddAndRemove;
                    message.arg1 = index;

                    handler.sendMessage(message);
                  }
                });
              }
            }, 250);

            break;

          case DragEvent.ACTION_DRAG_LOCATION:
            boolean canMove = false;
            if (isLeft) {
              canMove = event.getX() >= view.getWidth() * 3 / 4;
            } else {
              canMove = event.getX() <= view.getWidth() / 4;
            }

            if (view.getAlpha() != 1.0 && canMove) {
              ViewGroupAddAndRemove viewGroupAddAndRemove = new ViewGroupAddAndRemove();
              viewGroupAddAndRemove.setViewGroup(parentView);
              viewGroupAddAndRemove.setListenerView(view);
              viewGroupAddAndRemove.setDragView(dragView);

              Message message = new Message();
              message.obj = viewGroupAddAndRemove;
              message.arg1 = index;
              message.what = 1;

              handler.sendMessage(message);
            }

            break;

          case DragEvent.ACTION_DRAG_EXITED:
            // 取消定时器,并重置
            timer.cancel();
            view.setTag(R.id.timer, new Timer());
            view.setAlpha((float) 1.0);

            break;

          case DragEvent.ACTION_DROP:
            if (view.getAlpha() == (float) 0.3) {
              if (view instanceof CellView) {
                ((CellView) view).addChildView(dragView);
                dragView.setTag(view);

                view.setAlpha((float) 1.0);
              } else {
                // 新建一个CellView,并初始化相应数据
                CellView cellView = new CellView(LuncherDemoActivity.this);
                cellView.addChildView(view);
                view.setTag(cellView);
                cellView.addChildView(dragView);
                dragView.setTag(cellView);
                cellView.setOnDragListener(viewDragListener);
                cellView.setOnLongClickListener(LuncherDemoActivity.this);
                cellView.setTag(R.id.timer, new Timer());
                cellView.setOnClickListener(new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    CellView cellView = (CellView) v;

                    luncherDemoFragment.setCellView(cellView);

                    cellLayout.animate().y(((View) cellLayout.getParent()).getHeight() - cellLayout.getHeight());
                    middleLayout.animate().y(0);
                  }
                });

                GridLayout.LayoutParams vLayoutParams = new GridLayout.LayoutParams(new LayoutParams(140, 140));
                vLayoutParams.leftMargin = 10;
                vLayoutParams.topMargin = 10;
                vLayoutParams.rightMargin = 10;
                vLayoutParams.bottomMargin = 10;

                parentView.removeView(view);
                parentView.removeView(dragView);

                if (index >= parentView.getChildCount()) {
                  parentView.addView(cellView, vLayoutParams);
                } else {
                  parentView.addView(cellView, index, vLayoutParams);
                }
              }

              isItemOnDragListener = false;
            }

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
        final View dragView = (View) event.getLocalState();
        switch (event.getAction()) {
          case DragEvent.ACTION_DROP:
            dragView.setBackground((Drawable) dragView.getTag(R.id.bg));
            dragView.setOnDragListener(viewDragListener);

            float e_x = event.getX() - dragView.getWidth() / 2;
            float e_y = event.getY() - dragView.getHeight() / 2;

            if (isItemOnDragListener) {
              if (null != dragView.getTag()) {
                CellView cellView = (CellView) dragView.getTag();
                cellView.removeChildView(dragView);
                dragView.setTag(null);

                if (null != ((ViewGroup) dragView.getParent())) {
                  ((ViewGroup) dragView.getParent()).removeView(dragView);
                }

                ((ViewGroup) v).addView(dragView);

                dragView.setOnDragListener(viewDragListener);
                dragView.setOnLongClickListener(LuncherDemoActivity.this);

              } else {
                // 从拖动点移回至原位的动画
                float x = dragView.getX();
                float y = dragView.getY();

                dragView.setX(e_x);
                dragView.setY(e_y);

                dragView.animate().x(x).y(y).setDuration(200);
              }
            } else {
              // 两个View的移位动画未结束时就拦截住DROP事件处理方案
              // TODO
            }

            break;
          case DragEvent.ACTION_DRAG_ENTERED:
            // 记录拖动开始位置坐标,并隐藏文件夹界面
            currentX = event.getX();
            hiddenLayout();

            break;

          case DragEvent.ACTION_DRAG_LOCATION:
            // 判断是左移还是右移
            if (event.getX() > currentX) {
              isLeft = true;
            } else {
              isLeft = false;
            }

            currentX = event.getX();

            break;

          default:
            break;
        }

        return true;
      }
    };
    luncherParent.setOnDragListener(viewGroupDragListener);
  }

  @SuppressLint("NewApi")
  @Override
  public boolean onLongClick(View view) {
    // 将本尊隐身
    view.setTag(R.id.bg, view.getBackground());

    // 设置数据,会被拖动目标ViewGroup所接收,即用来传递信息
    ClipData data = ClipData.newPlainText("", "");

    // 设置拖动阴影,即你所拖动的那个图标(这也同时说明,你拖动的不过是一个影分身,本尊其实并没有移动)
    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
    view.startDrag(data, shadowBuilder, view, 0);

    view.setBackground(null);

    isItemOnDragListener = true;
    return true;
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    cellLayout.setY(luncherParent.getHeight());
    middleLayout.setY(-luncherParent.getHeight());
  }

  private void hiddenLayout() {
    cellLayout.animate().y(luncherParent.getHeight());
    middleLayout.animate().y(-luncherParent.getHeight());
    luncherDemoFragment.clearLayout();
  }
}