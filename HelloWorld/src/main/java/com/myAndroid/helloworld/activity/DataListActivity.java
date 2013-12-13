package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;

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

public class DataListActivity extends Activity {
  private boolean canScrollHeadDown = false;

  @SuppressLint("NewApi")
  // 新增一个碎片,并覆盖当前页面
  public void addFragment() {
    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    Fragment1 df = new Fragment1();
    ft.replace(android.R.id.content, df);

    ft.addToBackStack(null);// 可以被BACK键返回
    ft.commit();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_datalist);

    // ListView下拉刷新
    // ///////////////////////start///////////////////////////////////
    final ListView listView = (ListView) findViewById(R.id.listHead);

    // 设置Header
    LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View headView = layoutInflater.inflate(R.layout.list_head_layout, null);
    listView.addHeaderView(headView);

    // 设置Adapter
    String[] DATALIST = {"aaa", "bbb", "ccc", "ddd"};
    listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DATALIST));

    // 重头戏来了,请注意阅读
    listView.setOnScrollListener(new OnScrollListener() {
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
    listView.setOnTouchListener(new OnTouchListener() {
      private float startPointY;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            startPointY = event.getY();

            break;
          case MotionEvent.ACTION_UP:
            v.animate().y((Float) v.getTag());// 松开手后复位
            Toast.makeText(DataListActivity.this, "!!! " + headView.getHeight(), Toast.LENGTH_SHORT).show();

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
        return false;
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
