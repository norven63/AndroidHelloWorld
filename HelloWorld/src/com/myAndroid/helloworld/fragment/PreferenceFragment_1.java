package com.myAndroid.helloworld.fragment;

import com.myAndroid.helloworld.R;

import android.widget.Toast;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class PreferenceFragment_1 extends PreferenceFragment {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 加载首选项页面
    addPreferencesFromResource(R.xml.preference_1);

    // 获取单选框对象
    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("checkBox");

    // 设置点击事件
    checkBoxPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {

        Toast.makeText(PreferenceFragment_1.this.getActivity(), "Click", Toast.LENGTH_SHORT).show();
        return true;
      }
    });
  }
}
