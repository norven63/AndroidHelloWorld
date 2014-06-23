package com.myAndroid.helloworld.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

import com.myAndroid.helloworld.R;

public class ScaleImageView extends ImageView {
  private Canvas canvas;
  private Bitmap bitmap;

  public ScaleImageView(Context context, AttributeSet attrs) {
    super(context, attrs);

    this.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image_view2));
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return new ScaleGestureDetector(getContext(), new OnScaleGestureListener() {
      @Override
      public boolean onScale(ScaleGestureDetector detector) {
        float scale = 1;
        scale *= detector.getScaleFactor();
        Log.i("SCALE", scale + " p: " + detector.getPreviousSpan() + " c: " + detector.getCurrentSpan());
        Matrix matrix = getImageMatrix();
        matrix.setScale(scale, scale);

        Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        setImageBitmap(scaleBitmap);

        invalidate();

        return true;
      }

      @Override
      public boolean onScaleBegin(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
        return true;
      }

      @Override
      public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub

      }
    }).onTouchEvent(event);
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    if (null == bitmap) {
      bitmap = bm;
    }

    super.setImageBitmap(bm);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (null == this.canvas) {
      this.canvas = canvas;
    }

    super.onDraw(canvas);
  }
}
