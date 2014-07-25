package com.myAndroid.helloworld.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatView extends ImageView {
	private int barHeight;
	private WindowManager.LayoutParams windowParams;
	private WindowManager windowManager;

	private float startX;
	private float startY;

	public FloatView(Context context) {
		super(context);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				startY = event.getY();

				break;
			case MotionEvent.ACTION_MOVE:
				// RawX和RawY都是屏幕的触点坐标，而非View上的触点坐标
				windowParams.x = (int) (event.getRawX() - startX);
				windowParams.y = (int) (event.getRawY() - startY - barHeight);

				windowManager.updateViewLayout(FloatView.this, windowParams);

				break;
		}

		return true;
	}

	public void show() {
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 获取到状态栏的高度
				Rect frame = new Rect();
				getWindowVisibleDisplayFrame(frame);
				barHeight = frame.top;
			}
		});

		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

		windowParams = new WindowManager.LayoutParams();

		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		windowParams.height = 80;
		windowParams.width = 80;

		// 注意，这里是基于窗口为背景的x、y坐标
		windowParams.x = 100;
		windowParams.y = 100;

		windowParams.gravity = Gravity.LEFT | Gravity.TOP;

		windowManager.addView(this, windowParams);
	}
}
