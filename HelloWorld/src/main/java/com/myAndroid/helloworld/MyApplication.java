package com.myAndroid.helloworld;

import java.io.File;

import android.widget.Toast;

import android.app.Application;

public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Toast.makeText(this, "Welcome!!!(来自Application)", Toast.LENGTH_SHORT).show();

    File file = getFilesDir();// 路径为/data/data/com.myAndroid.helloworld/files

    System.out.println(file.getAbsolutePath());
  }
}
