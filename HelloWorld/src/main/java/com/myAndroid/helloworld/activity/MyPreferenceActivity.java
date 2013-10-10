package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import java.util.List;

import android.os.Bundle;

import android.widget.Toast;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MyPreferenceActivity extends PreferenceActivity {
  @Override
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.preference_headers, target);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 为SharedPreferences注册监听,注意该对象的获得方式,并且需要写在onCreate()方法里
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    String editText = sharedPreferences.getString("editText", "");
    Toast.makeText(MyPreferenceActivity.this, editText, Toast.LENGTH_SHORT).show();

    sharedPreferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(MyPreferenceActivity.this, "SharedPreferences已通过  " + key + " 修改", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
