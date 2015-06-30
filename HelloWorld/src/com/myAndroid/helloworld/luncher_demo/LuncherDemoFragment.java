package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;

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
import android.widget.GridLayout;

public class LuncherDemoFragment extends Fragment {
  private GridLayout gridLayout;

  private CellView cellView;
  @SuppressLint("HandlerLeak")
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      final View dragView = gridLayout.getChildAt(msg.arg2);

      ((ViewGroup) dragView.getParent()).removeView(dragView);
      gridLayout.addView(dragView, msg.arg1);

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

      final int index = gridLayout.indexOfChild(view);

      Timer timer = (Timer) view.getTag(R.id.timer);

      switch (event.getAction()) {
        case DragEvent.ACTION_DRAG_ENTERED:
          timer.schedule(new TimerTask() {
            @Override
            public void run() {
              handler.post(new Runnable() {
                @Override
                public void run() {
                  Message message = new Message();
                  message.arg1 = index;
                  message.arg2 = gridLayout.indexOfChild(dragView);

                  handler.sendMessage(message);
                }
              });
            }
          }, 250);

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
    @SuppressLint("NewApi")
    @Override
    public boolean onDrag(View v, DragEvent event) {
      View dragView = (View) event.getLocalState();

      switch (event.getAction()) {
        case DragEvent.ACTION_DROP:
          dragView.setBackground((Drawable) dragView.getTag(R.id.bg));
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

  public void clearLayout() {
    this.cellView = null;

    gridLayout.removeAllViews();
  }

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
    return inflater.inflate(R.layout.luncher_demo_folder, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    gridLayout = (GridLayout) getView().findViewById(R.id.luncerFolder);
    gridLayout.setOnDragListener(viewGroupDragListener);
  }

  /**
   * @param cellView the cellView to set
   */
  public void setCellView(CellView cellView) {
    this.cellView = cellView;

    for (View view : cellView.getChildViews()) {
      view.setAlpha((float) 1.0);
      view.setOnDragListener(viewDragListener);

      if (null != ((ViewGroup) view.getParent())) {
        ((ViewGroup) view.getParent()).removeView(view);
      }

      gridLayout.addView(view);
    }
  }
}
