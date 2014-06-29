package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class ScreenShotActivity extends Activity {
  public void screenRecorder(View v) {
    String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jieping.png";

    // View rootView = v.getRootView();// 无状态栏
    View rootView = getWindow().getDecorView();
    rootView.setDrawingCacheEnabled(true);
    rootView.buildDrawingCache();

    Bitmap rootBitmap = rootView.getDrawingCache();

    if (rootBitmap != null) {
      try {
        FileOutputStream out = new FileOutputStream(fname);

        rootBitmap.compress(Bitmap.CompressFormat.PNG, 10, out);

        out.flush();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Toast.makeText(this, "获取屏幕图像失败！", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screen_shot);
  }
}
