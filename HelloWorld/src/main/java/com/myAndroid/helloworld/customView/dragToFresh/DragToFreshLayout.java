package com.myAndroid.helloworld.customView.dragToFresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DragToFreshLayout extends LinearLayout {
	private BaseAdapter adapter;
	private View headView;

	public DragToFreshLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				headView = getChildAt(0);
				if (null == headView) {
					return;
				}

				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headView.getLayoutParams();
				params.topMargin = -headView.getHeight();
				headView.setLayoutParams(params);

				DragToFreshListView dragToFreshListView = new DragToFreshListView(getContext());
				addView(dragToFreshListView);
				dragToFreshListView.setAdapter(adapter);
			}
		});
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	private class DragToFreshListView extends ListView {
		private boolean canPull = false;// 标记是否可以拖拽

		public DragToFreshListView(Context context) {
			super(context);

			this.setOnTouchListener(new OnTouchListener() {
				private float startPointY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (null == v.getTag()) {
						v.setTag(v.getY());
						headView.setTag(headView.getY());
					}

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startPointY = event.getY();

						break;
					case MotionEvent.ACTION_UP:
						// 复位动画
						DragToFreshListView.this.animate().y((Float) v.getTag());
						headView.animate().y((Float) headView.getTag());

						// 重置标记位
						canPull = false;

						break;
					case MotionEvent.ACTION_MOVE:
						float move = event.getY() - startPointY;
						startPointY = event.getY();

						do {
							if (canPull && Math.abs(move) > 1) {// 如果改用move>1则只允许下拉刷新,现在Math.abs(move)则是可以上下拉都能刷新
								v.setY(v.getY() + move * 1 / 3);
								headView.setY(headView.getY() + move * 1 / 3);
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
}
