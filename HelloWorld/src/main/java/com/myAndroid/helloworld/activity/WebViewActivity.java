package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.myAndroid.helloworld.R;

import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
  private ProgressBar progressBar;
  private WebView webView;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.webview_activity);

    progressBar = (ProgressBar) findViewById(R.id.webViewBar);
    progressBar.setMax(100);

    webView = (WebView) findViewById(R.id.webview);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setPluginState(PluginState.ON);
    webSettings.setSupportZoom(true);

    webSettings.setBuiltInZoomControls(true);

    // 设置WebView屏幕对齐
    webSettings.setUseWideViewPort(true);
    webSettings.setLoadWithOverviewMode(true);

    webView.loadUrl("http://www.baidu.com");

    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, final int newProgress) {
        super.onProgressChanged(view, newProgress);
        // 根据加载进度,动态更新进度条
        progressBar.setProgress(newProgress);

        if (newProgress == 100) {
          progressBar.setVisibility(View.GONE);
          webView.setVisibility(View.VISIBLE);
        }
      }
    });

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);// 页面中的跳转行为在webView中执行,而不是打开一个浏览器执行
        return true;
      }
    });
  }
}
