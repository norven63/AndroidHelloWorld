package com.myAndroid.helloworld.fragment;

import com.myAndroid.helloworld.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenceFragment_2 extends PreferenceFragment {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 加载首选项页面
    addPreferencesFromResource(R.xml.preference_2);
  }
}
