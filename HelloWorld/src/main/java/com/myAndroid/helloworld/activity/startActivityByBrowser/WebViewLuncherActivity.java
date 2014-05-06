package com.myAndroid.helloworld.activity.startActivityByBrowser;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewLuncherActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		webView.loadUrl("file:///android_asset/start_activity_by_browser.html");
		setContentView(webView);
	}
}
