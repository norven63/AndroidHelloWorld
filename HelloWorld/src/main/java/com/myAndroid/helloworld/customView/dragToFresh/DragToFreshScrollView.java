package com.myAndroid.helloworld.customView.dragToFresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class DragToFreshScrollView extends ScrollView {
	private boolean canPull = false;// 标记是否可以拖拽

	public DragToFreshScrollView(Context context) {
		super(context);

		init();
	}

	public DragToFreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	protected final void init() {
		this.setOnTouchListener(new OnTouchListener() {
			private float startPointY;

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				View childView = ((ViewGroup) view).getChildAt(0);

				if (null == childView) {
					return false;
				}

				if (null == childView.getTag()) {
					childView.setTag(childView.getY());
				}

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startPointY = event.getY();

					break;
				case MotionEvent.ACTION_UP:
					// 复位动画
					childView.animate().y((Float) childView.getTag());

					// 重置标记位
					canPull = false;

					break;
				case MotionEvent.ACTION_MOVE:
					float move = event.getY() - startPointY;
					startPointY = event.getY();

					do {
						if (canPull && Math.abs(move) > 1) {
							childView.setY(childView.getY() + move * 1 / 3);
						}
					} while (false);

					break;
				}

				return false;
			}
		});
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		canPull = clampedY;// 捕捉可以下拉的时机，利用此法感觉良好

		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}
}
