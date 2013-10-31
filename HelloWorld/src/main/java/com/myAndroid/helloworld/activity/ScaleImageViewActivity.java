package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class ScaleImageViewActivity extends Activity {
  private SurfaceHolder surfaceHolder;
  private SurfaceView surfaceView;
  private float lastSpan = 0;
  private Bitmap bitmap;
  private Matrix matrix;
  private float scale = 1;

  public void initView(View view) {
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
    if (surfaceView.getHeight() < bitmap.getHeight()) {
      float scale = ((float) surfaceView.getHeight()) / bitmap.getHeight();
      matrix.setScale(scale, scale);
    }
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    Canvas canvas = surfaceHolder.lockCanvas();
    canvas.drawBitmap(bitmap, 0, 0, null);
    surfaceHolder.unlockCanvasAndPost(canvas);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return new ScaleGestureDetector(this, new OnScaleGestureListener() {
      @Override
      public boolean onScale(ScaleGestureDetector detector) {

        if (0 == lastSpan) {
          lastSpan = detector.getCurrentSpan();
        }

        // 根据当前指距和上次指距的比率是否>1来判断此次是缩小还是放大操作
        float currentScale = detector.getCurrentSpan() / lastSpan;
        if (1 < currentScale) {
          scale += currentScale / 26;// 除以29只是为了降低其灵敏度
        } else if (1 > currentScale) {
          scale -= currentScale / 23;// 比width少除以3个百分比,是因为感觉缩小的时候比较难错
        }

        matrix.setScale(scale, scale);

        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.BLACK);// 清空画布
        canvas.drawBitmap(bitmap, matrix, null);
        surfaceHolder.unlockCanvasAndPost(canvas);

        lastSpan = detector.getCurrentSpan();// 缓存上次两指间的距离

        return false;
      }

      @Override
      public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
      }

      @Override
      public void onScaleEnd(ScaleGestureDetector detector) {
      }
    }).onTouchEvent(event);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.scaleimage);

    matrix = new Matrix();
    matrix.setScale(1, 1);
    surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
    surfaceHolder = surfaceView.getHolder();
  }
}
