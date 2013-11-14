package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CellView extends View {
  public ArrayList<View> childViews;
  private Paint paint;

  public CellView(Context context) {
    super(context);

    childViews = new ArrayList<View>();
    paint = new Paint();
  }

  public CellView(Context context, AttributeSet attrs) {
    super(context, attrs);

    childViews = new ArrayList<View>();
    paint = new Paint();
  }

  public CellView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    childViews = new ArrayList<View>();
    paint = new Paint();
  }

  public void addChildView(View childView) {
    childViews.add(childView);

    this.invalidate();
  }

  /**
   * @return the childViews
   */
  public ArrayList<View> getChildViews() {
    return childViews;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawColor(R.color.gray);

    int x = 10;
    int y = 10;

    int operator = 0;// 终止算子,保证只显示前4个图标
    if (childViews.size() > 4) {
      operator = childViews.size() - 4;
    }
    if (childViews.size() != 0) {
      for (int i = childViews.size() - 1; i >= operator; i--) {
        ImageView childView = (ImageView) childViews.get(i);
        Bitmap bitmap = ((BitmapDrawable) childView.getBackground()).getBitmap();

        canvas.drawBitmap(bitmap, x, y, paint);

        if (x == 10) {
          x += bitmap.getWidth();
        } else if (y == 10) {
          y += bitmap.getHeight();
          x = 10;
        }
      }
    }

    super.onDraw(canvas);
  }
}
