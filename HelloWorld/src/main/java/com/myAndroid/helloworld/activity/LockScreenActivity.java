package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.myAndroid.helloworld.R;

public class LockScreenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//
		// 拦截锁屏的时机
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);// 拦截显示开锁界面的时机

		setContentView(R.layout.lock_screen_layout);
	}

	public void finishSelf(View view) {
		finish();
	}
}
