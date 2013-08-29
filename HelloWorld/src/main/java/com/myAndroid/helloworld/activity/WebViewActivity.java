package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;

import com.myAndroid.helloworld.R;

public class WebViewActivity extends Activity {
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.webview_activity);

    WebView webView = (WebView) findViewById(R.id.webview);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setPluginState(PluginState.ON);
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);
    // webView.loadUrl("http://www.baidu.com");
    webView.loadUrl("http://server.drive.goodow.com/serve?id=muxbwsba8eelsfqcq7e7oe3qnkq8m7p7wstmm93zycp8lqc8sb9uo9dpjwyx7kfryu7s4abztv3nr1j4oovzo1gebaivyf7xpcqwx3eineiyt6m9zvo");
  }
}
