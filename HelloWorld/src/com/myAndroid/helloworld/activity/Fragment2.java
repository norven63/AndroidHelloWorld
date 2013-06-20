package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

@SuppressLint("NewApi")
public class Fragment2 extends ListFragment {
  public static final String[] DATALIST = { "囧", "囧", "囧", "囧" };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DATALIST));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_list, container, false);
  }
}
