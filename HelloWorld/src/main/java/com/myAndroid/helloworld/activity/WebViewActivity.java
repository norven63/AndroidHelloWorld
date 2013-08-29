package com.myAndroid.helloworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
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

    webView.loadUrl("http://server.drive.goodow.com/serve?id=c5kzd3ek7nlshol1cwj3tfjdcana4cvu1cl1e7hu491wc72i7le9cvrlgcmhzb2rky8cdq6c0xwbqze9j3xr95bfmi6propkdlsq7n9z42l5h5qlrjs");

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
