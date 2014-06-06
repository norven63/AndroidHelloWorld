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
	public interface OnUpdateListener {
		public void onUpdate();
	}

	private class DragToFreshListView extends ListView {
		private float startPointY;
		private boolean canPull = false;// 标记是否可以拖拽

		public DragToFreshListView(Context context) {
			super(context);

			this.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View contentListView, MotionEvent event) {
					if (null == headView.getTag()) {
						headView.setTag(headView.getY());
					}

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							startPointY = event.getY();

							break;
						case MotionEvent.ACTION_UP:
							// 复位动画
							contentListView.animate().y(0f);
							headView.animate().y((Float) headView.getTag());

							// 重置标记位
							canPull = false;
							canUpdate = true;

							break;
						case MotionEvent.ACTION_MOVE:
							float distanceY = event.getY() - startPointY;
							startPointY = event.getY();

							do {
								if (canPull && Math.abs(distanceY) > 1.8) {// 如果改用move>1.8则只允许下拉刷新,现在Math.abs(move)则是可以上下拉都能刷新
									float moveRange = 2f * (distanceY / Math.abs(distanceY));
									contentListView.setY(contentListView.getY() + moveRange);
									headView.setY(headView.getY() + moveRange);

									// 刷新操作
									if (canUpdate && onUpdateListener != null && headView.getY() == 0f) {
										canUpdate = false;
										onUpdateListener.onUpdate();
									}
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

	private BaseAdapter adapter;
	private View headView;
	private DragToFreshListView contentListView;
	private boolean canUpdate = true;
	private OnUpdateListener onUpdateListener;

	public DragToFreshLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				headView = getChildAt(0);
				if (null == headView) {
					return;
				}

				// 隐藏headView
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headView.getLayoutParams();
				params.topMargin = -headView.getHeight();
				headView.setLayoutParams(params);

				// 加入ListView
				contentListView = new DragToFreshListView(getContext());
				addView(contentListView);
				contentListView.setAdapter(adapter);
			}
		});
	}

	public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
		this.onUpdateListener = onUpdateListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}
}
