package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class DataListActivity extends Activity {
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

    String[] title = { "动物", "水果", "英雄" };
    String[] content1 = { "老鼠", "豹子", "老陈" };
    String[] content2 = { "苹果", "香蕉" };
    String[] content3 = { "盖伦", "斧王", "翟江" };
    String[][] contents = { content1, content2, content3 };

    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, contents, title);

    ExpandableListView exListView = (ExpandableListView) findViewById(R.id.exListView);

    exListView.setAdapter(adapter);
  }
}
