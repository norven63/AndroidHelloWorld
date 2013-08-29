package com.myAndroid.helloworld.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService extends Service {

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("TestService", "onCreate()");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i("TestService", "onStartCommand()");
    Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i("TestService", "onDestroy()");
  }

  @Override
  public IBinder onBind(Intent intent) {
    return new Binder();
  }

}
