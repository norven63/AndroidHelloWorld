package com.myAndroid.helloworld.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

public class ImageViewActivity extends Activity {
  private class InitImageBitmapTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
      Bitmap bitmap = null;

      try {
        URLConnection connection = (new URL(params[0]).openConnection());
        connection.setDoInput(true);
        connection.connect();
        InputStream bitmapStream = connection.getInputStream();
        bitmap = BitmapFactory.decodeStream(bitmapStream);
        bitmapStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
      super.onPostExecute(result);
      imageView.setImageBitmap(result);
    }
  }

  private TextView textView;

  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imageview_activity);

    textView = (TextView) findViewById(R.id.textview);
    textView.setText("ExternalStorageDirectory: " + Environment.getExternalStorageDirectory().getPath() + "\nExternalStorageState: "
        + Environment.getExternalStorageState());

    imageView = (ImageView) findViewById(R.id.imageview);

    // Options的使用
    BitmapFactory.Options opts = new Options();
    opts.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(getResources(), R.drawable.test_image, opts);
    int bitmapWidth = opts.outWidth;
    int bitmapHeight = opts.outHeight;
    opts.inSampleSize = 10;
    opts.inJustDecodeBounds = false;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image, opts);

    // Matrix的使用
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    int screenWidth = displayMetrics.widthPixels;
    int screenHeight = displayMetrics.heightPixels;

    float newWidth = 1;
    if (bitmapWidth > screenWidth) {
      newWidth = ((float) screenWidth) / bitmapWidth;
    }

    float newHeight = 1;
    if (bitmapHeight > screenHeight) {
      newHeight = ((float) screenHeight) / bitmapHeight;
    }

    Matrix matrix = new Matrix();
    matrix.setScale(newWidth, newHeight);

    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    imageView.setImageBitmap(newBitmap);
    // new
    // InitImageBitmapTask().execute("http://server.drive.goodow.com/serve?id=0imclfyoudt52aax2tsmxbm0ukjn6c6szcwyvkwu4khut8x68pexfi5ls7y8m6yj68qrfiym7he4ywo8mihrwcynhw94tk2wiqo53dca0mgv14th5ib");
  }
}
