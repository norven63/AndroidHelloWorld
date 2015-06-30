package com.myAndroid.helloworld.activity.startActivityByBrowser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StartByBrowserActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView textView = new TextView(this);
		textView.setText("这是从浏览器启动Activity");
		setContentView(textView);
	}
}
