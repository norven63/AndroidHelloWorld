package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;

public class LuncherDemoFragment extends Fragment {
  private CellView cellView;
  @SuppressLint("HandlerLeak")
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      ViewGroup parentView = (ViewGroup) msg.obj;
      final View view = parentView.getChildAt(msg.arg1);
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
      dragView.animate().x(x).y(y).setDuration(300).setListener(new AnimatorListener() {
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
          dragView.setAlpha((float) 1.0);
          // 设置标记位,不让ViewGroup作出将本尊移回原位的响应操作
          isItemOnDragListener = false;
        }
      });
    }
  };

  private OnDragListener viewDragListener = new OnDragListener() {
    @Override
    public boolean onDrag(final View view, DragEvent event) {
      final View dragView = (View) event.getLocalState();

      // 如果拖动事件遮蔽自己则直接return
      if (view.equals(dragView)) {
        return false;
      }

      final GridLayout parentView = (GridLayout) view.getParent();
      final int index = parentView.indexOfChild(view);

      Timer timer = (Timer) view.getTag(R.id.timer);

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
          }, 600);

          break;

        case DragEvent.ACTION_DRAG_EXITED:
          // 取消定时器,并重置
          timer.cancel();
          view.setTag(R.id.timer, new Timer());

          break;

        case DragEvent.ACTION_DROP:
          return false;

        default:

          break;
      }

      return true;
    }
  };
  private OnDragListener viewGroupDragListener = new OnDragListener() {
    @Override
    public boolean onDrag(View v, DragEvent event) {
      View dragView = (View) event.getLocalState();

      switch (event.getAction()) {
        case DragEvent.ACTION_DROP:
          dragView.setAlpha((float) 1.0);
          dragView.setOnDragListener(viewDragListener);

          float e_x = event.getX() - dragView.getWidth() / 2;
          float e_y = event.getY() - dragView.getHeight() / 2;

          if (isItemOnDragListener) {
            // 从拖动点移回至原位的动画
            float x = dragView.getX();
            float y = dragView.getY();

            dragView.setX(e_x);
            dragView.setY(e_y);

            dragView.animate().x(x).y(y).setDuration(250);
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

  private boolean isItemOnDragListener = true;

  /**
   * @return the cellView
   */
  public CellView getCellView() {
    return cellView;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    GridLayout gridLayout = new GridLayout(this.getActivity());
    gridLayout.setColumnCount(4);
    gridLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    gridLayout.setOnDragListener(viewGroupDragListener);

    for (View view : cellView.getChildViews()) {
      view.setAlpha((float) 1.0);
      view.setOnDragListener(viewDragListener);

      if (null != ((ViewGroup) view.getParent())) {
        ((ViewGroup) view.getParent()).removeView(view);
      }
      gridLayout.addView(view);
    }

    return gridLayout;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  /**
   * @param cellView the cellView to set
   */
  public void setCellView(CellView cellView) {
    this.cellView = cellView;
  }
}
