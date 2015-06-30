package com.myAndroid.helloworld.luncher_demo;

import com.myAndroid.helloworld.R;

import java.util.ArrayList;

import android.graphics.Bitmap.Config;

import android.view.ViewGroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CellView extends View {
  // 控制onDraw方法何时可以设置背景
  private boolean flag;

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
    if (null != childView.getTag()) {
      CellView cellView = (CellView) childView.getTag();
      cellView.removeChildView(childView);
      childView.setTag(null);
    }

    if (null != ((ViewGroup) childView.getParent())) {
      ((ViewGroup) childView.getParent()).removeView(childView);
    }

    childViews.add(childView);

    this.flag = true;
    this.invalidate();
  }

  /**
   * @return the childViews
   */
  public ArrayList<View> getChildViews() {
    return childViews;
  }

  public void removeChildView(View childView) {
    childViews.remove(childView);

    // 删除空文件夹
    if (0 == childViews.size()) {
      ((ViewGroup) this.getParent()).removeView(this);
    }

    this.flag = true;
    this.invalidate();
  }

  @SuppressWarnings("deprecation")
  @SuppressLint({"DrawAllocation", "NewApi"})
  @Override
  protected void onDraw(Canvas canvas) {
    Canvas backGroundCanvas = new Canvas();
    Bitmap cacheBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
    backGroundCanvas.setBitmap(cacheBitmap);// 设置画布的Bitmap,使图像都画在此Bitmap上

    Bitmap backBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.layout_background)).getBitmap();

    int bitmapWidth = backBitmap.getWidth();
    int bitmapHeight = backBitmap.getHeight();

    int canvasWidth = backGroundCanvas.getWidth();
    int canvasHeight = backGroundCanvas.getHeight();

    float newWidth = 1;
    if (bitmapWidth > canvasWidth) {
      newWidth = ((float) canvasWidth) / bitmapWidth;
    }

    float newHeight = 1;
    if (bitmapHeight > canvasHeight) {
      newHeight = ((float) canvasHeight) / bitmapHeight;
    }

    Matrix matrix = new Matrix();
    matrix.setScale(newWidth, newHeight);

    backBitmap = Bitmap.createBitmap(backBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

    backGroundCanvas.drawBitmap(backBitmap, 0, 0, paint);

    int x = 25;
    int y = 25;

    int operator = 0;// 终止算子,其保证只显示前4个图标
    if (childViews.size() > 4) {
      operator = childViews.size() - 4;
    }
    if (childViews.size() != 0) {
      for (int i = childViews.size() - 1; i >= operator; i--) {
        ImageView childView = (ImageView) childViews.get(i);
        Bitmap bitmap = ((BitmapDrawable) childView.getBackground()).getBitmap();

        backGroundCanvas.drawBitmap(bitmap, x, y, paint);

        if (x == 25) {
          x += bitmap.getWidth();
        } else if (y == 25) {
          y += bitmap.getHeight();
          x = 25;
        }
      }
    }

    BitmapDrawable bitmapDrawable = new BitmapDrawable(cacheBitmap);
    this.setTag(R.id.bg, bitmapDrawable);// 缓存于tag中,在拖动时用(拖动时会设置背景为null)

    if (flag) {
      this.setBackground(bitmapDrawable);

      flag = false;
    }

    super.onDraw(canvas);
  }
}
