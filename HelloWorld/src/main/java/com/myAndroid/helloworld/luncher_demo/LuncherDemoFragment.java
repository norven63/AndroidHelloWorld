package com.myAndroid.helloworld.luncher_demo;

import java.util.ArrayList;

import android.widget.Button;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class LuncherDemoFragment extends Fragment {
  private ArrayList<View> items = new ArrayList<View>();;

  public void addIteme(View view) {
    if (null == items) {
      items = new ArrayList<View>();
    }

    items.add(view);
  }

  public void clearItmes() {
    if (null == items) {
      items = new ArrayList<View>();
    }

    items.clear();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    GridLayout gridLayout = new GridLayout(this.getActivity());
    gridLayout.setColumnCount(4);
    for (View view : items) {
      gridLayout.addView(view);
    }

    return gridLayout;
  }
}
