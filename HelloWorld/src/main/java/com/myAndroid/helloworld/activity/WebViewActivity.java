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

    webView.loadUrl("http://server.drive.goodow.com/serve?id=f6iose8sw77zscz7jo1xxy60ndq5m62s8qwja9ib3pj5etaa1lwiefl8qcibo9nu4i5suormb773znn7wgodiy46wr428jqur2hlsotdv3fhmjzgkae");

    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, final int newProgress) {
        super.onProgressChanged(view, newProgress);
        progressBar.setProgress(newProgress);

        if (newProgress == 100) {
          progressBar.setVisibility(View.GONE);
          webView.setVisibility(View.VISIBLE);
        }
      }
    });
  }
}
