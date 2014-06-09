package com.myAndroid.helloworld.customView;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatView extends ImageView {
	private int barHeight;
	private WindowManager.LayoutParams mWindowParams;
	private WindowManager mWindowManager;

	private float startX;
	private float startY;

	public FloatView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				startY = event.getY();

				break;
			case MotionEvent.ACTION_MOVE:
				// RawX和RawY都是屏幕的触点坐标，而非View上的触点坐标
				mWindowParams.x = (int) (event.getRawX() - startX);
				mWindowParams.y = (int) (event.getRawY() - startY - barHeight);

				mWindowManager.updateViewLayout(FloatView.this, mWindowParams);

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

		mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

		mWindowParams = new WindowManager.LayoutParams();

		mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;

		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		mWindowParams.height = 80;
		mWindowParams.width = 80;

		// 注意，这里是以窗口为背景的x、y坐标
		mWindowParams.x = 100;
		mWindowParams.y = 100;

		mWindowManager.addView(this, mWindowParams);
	}
}
