package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class DataListActivity extends Activity {
  public static final String[] DATALIST = { "iteme1", "iteme2", "iteme3", "iteme4" };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ExpandableListView elv = new ExpandableListView(this);
    elv.setGroupIndicator(null);
    elv.setChildIndicator(null);

    String[] titles = { "节点一", "节点二", "节点三" };
    String[] chrild1 = { "节点一I", "节点一II", "节点一III" };
    String[] chrild2 = { "节点二I", "节点二II" };
    String[] chrild3 = { "节点三I", "节点三II", "节点三III" };
    String[][] contents = { chrild1, chrild2, chrild3 };
    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, contents, titles);

    elv.setAdapter(adapter);

    setContentView(elv);

    // ListView listView = getListView();
    // listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    // listView.setTextFilterEnabled(true);
    //
    // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DATALIST));
  }
}
