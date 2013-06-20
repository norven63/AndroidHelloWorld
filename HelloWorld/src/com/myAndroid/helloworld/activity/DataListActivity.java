package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DataListActivity extends Activity {
  public static final String[] DATALIST = { "iteme1", "iteme2" };
  ListView listView;
  ArrayAdapter<String> adapter;

  public void doChangeList(View view) {
    DATALIST[0] = "123";
    // adapter.addAll(DATALIST);
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_datalist);
    listView = (ListView) findViewById(R.id.listView_);

    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DATALIST);

    listView.setAdapter(adapter);
  }
}
