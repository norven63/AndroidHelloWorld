package com.myAndroid.helloworld.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import com.myAndroid.helloworld.R;

public class ImageViewActivity extends Activity {
  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imageview_activity);

    imageView = (ImageView) findViewById(R.id.imageview);
    
    new InitImageBitmapTask().execute("http://server.drive.goodow.com/serve?id=0imclfyoudt52aax2tsmxbm0ukjn6c6szcwyvkwu4khut8x68pexfi5ls7y8m6yj68qrfiym7he4ywo8mihrwcynhw94tk2wiqo53dca0mgv14th5ib");
  }

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
}
