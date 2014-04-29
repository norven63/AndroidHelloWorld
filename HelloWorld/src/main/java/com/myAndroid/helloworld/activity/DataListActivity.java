package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;

/**
 * 此类中涉及到ListView下拉刷新的案例。但是此例还是建议用ScrollView配合ListView来实现， 下拉的不是ListView而是外层的ScrollView，原因很简单，
 * ScrollView比起ListView更容易判断是否已经滑动至最顶部。具体代码可以参考DreamBook项目中的书架功能。
 * 
 * P.S：上述方案需要注意点一点细节是，由于ListView的onTouch()事件会拦截ACTION_DOWN动作，所以 ScronllView中的重置操作必须放在ACTION_UP动作中处理
 * 
 * @author Administrator
 */
public class DataListActivity extends Activity {
  private boolean canScrollHeadDown = false;

  private ListView headListView;

  @SuppressLint("NewApi")
  // 新增一个碎片,并覆盖当前页面.
  public void addFragment() {
    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    Fragment1 df = new Fragment1();
    ft.replace(android.R.id.content, df);

    ft.addToBackStack(null);// 可以被BACK键返回
    ft.commit();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    // 将ListView定位至某个选项位置，需要注意的是，之所以放在onWindowFocusChanged()内执行此方法的原因是为了等待ListView图像加载完毕
    headListView.setSelection(1);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_datalist);

    // ListView下拉刷新
    // ///////////////////////start///////////////////////////////////
    headListView = (ListView) findViewById(R.id.listHead);

    // 设置Header
    LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View headView = layoutInflater.inflate(R.layout.list_head_layout, null);
    headListView.addHeaderView(headView);

    // 设置Adapter
    String[] DATALIST = {"aaa", "bbb", "ccc", "ddd"};
    headListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DATALIST));

    // 重头戏来了,请注意阅读
    headListView.setOnScrollListener(new OnScrollListener() {
      private int visibleItemCount = 0;
      private int firstVisibleItem;

      /**
       * 滑动时调用
       */
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.visibleItemCount == 0) {
          this.visibleItemCount = visibleItemCount;
        }
        this.firstVisibleItem = firstVisibleItem;

        // 当首元素为第一个,且本页显示的元素数为初始赋值数,说明已经滑倒顶部了,于是把标记位设为可以向下拉动
        if (this.firstVisibleItem == 0 && visibleItemCount == this.visibleItemCount) {
          DataListActivity.this.canScrollHeadDown = true;
        } else {
          DataListActivity.this.canScrollHeadDown = false;
        }
      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
          case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
            Toast.makeText(DataListActivity.this, "SCROLL_STATE_TOUCH_SCROLL:触碰滑动", Toast.LENGTH_SHORT).show();

            break;
          case OnScrollListener.SCROLL_STATE_IDLE:
            Toast.makeText(DataListActivity.this, "SCROLL_STATE_IDLE:空闲", Toast.LENGTH_SHORT).show();

            break;
          case OnScrollListener.SCROLL_STATE_FLING:
            Toast.makeText(DataListActivity.this, "SCROLL_STATE_FLING:自动滑动", Toast.LENGTH_SHORT).show();

            break;
          default:
            break;
        }
      }
    });
    headListView.setOnTouchListener(new OnTouchListener() {
      private float startPointY;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        boolean returnValue = false;

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            startPointY = event.getY();

            break;
          case MotionEvent.ACTION_UP:
            v.animate().y((Float) v.getTag());// 松开手后复位

            break;
          case MotionEvent.ACTION_MOVE:
            if (null == v.getTag()) {
              v.setTag(v.getY());
            }

            float move = event.getY() - startPointY;
            // 请注意这里setY()方法设置的参数算子以及在判断处设置的判断条件(即Math.abs(move) > 5)
            if (canScrollHeadDown && Math.abs(move) > 5) {
              v.setY(v.getY() + move * 3 / 5);
            }
            startPointY = event.getY();

            break;
          default:
            break;
        }
        return returnValue;
      }
    });
    // ///////////////////////end///////////////////////////////////

    // 可展开的ListView
    String[] title = {"动物", "水果", "英雄"};
    String[] content1 = {"老鼠", "豹子", "老陈"};
    String[] content2 = {"苹果", "香蕉"};
    String[] content3 = {"盖伦", "斧王", "翟江"};
    String[][] contents = {content1, content2, content3};

    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, contents, title);

    ExpandableListView exListView = (ExpandableListView) findViewById(R.id.exListView);

    exListView.setAdapter(adapter);
  }
}
