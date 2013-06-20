package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class Fragment1 extends ListFragment {
  private String[] DATALIST = { "iteme1", "iteme2", "iteme3", "iteme4" };
  private ArrayAdapter<String> adapter;
  public static final String DATA_KEY = "folders";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DATALIST);
    setListAdapter(adapter);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_list, container, false);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    DATALIST[0] = "XXXX";

    adapter.notifyDataSetChanged();
  }
}
