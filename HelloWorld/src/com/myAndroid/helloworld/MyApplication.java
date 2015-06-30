package com.myAndroid.helloworld;

import java.io.File;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		File file = getFilesDir();// 路径为/data/data/com.myAndroid.helloworld/files

		// startService(new Intent(this, LockScreenService.class));// 启动锁屏的Service
	}
}
