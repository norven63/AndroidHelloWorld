package com.myAndroid.helloworld.customView.drag4Fresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.myAndroid.helloworld.R;

public class Drag4ReFreshLayout extends LinearLayout {
	private final int USE_LISTVIEW = 9999;
	private boolean shouldRefresh = true;
	private boolean isFirstLayout = true;
	private boolean canDrag;
	private float currentY;
	private float startY;
	private View headView;
	private View footView;
	private BaseAdapter adapter;
	private OnRefreshListener onRefreshListener;
	private OnTouchListener onTouchListener;
	private AbsListView contentListView;

	public interface OnRefreshListener {
		public void onRefresh();
	}

	private class DragToFreshListView extends ListView {
		public DragToFreshListView(Context context) {
			super(context);

			this.setOnTouchListener(onTouchListener);
		}

		@Override
		protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
			canDrag = clampedY;
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	private class DragToFreshGridView extends GridView {
		public DragToFreshGridView(Context context) {
			super(context);

			this.setOnTouchListener(onTouchListener);
		}

		@Override
		protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
			canDrag = clampedY;
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	public Drag4ReFreshLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Drag4ReFreshLayout);
		final int numColumns = typedArray.getInt(R.styleable.Drag4ReFreshLayout_numColumns, USE_LISTVIEW);
		typedArray.recycle();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isFirstLayout) {
					return;
				}

				isFirstLayout = false;

				headView = findViewById(R.id.drag4fresh_headView);
				footView = findViewById(R.id.drag4fresh_footView);

				if (null != headView) {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headView.getLayoutParams();
					params.topMargin = -headView.getHeight();
					headView.setLayoutParams(params);
				}

				if (null != footView) {
					removeView(footView);
				}

				if (numColumns == USE_LISTVIEW) {
					contentListView = new DragToFreshListView(getContext());
				} else {
					contentListView = new DragToFreshGridView(getContext());
					((DragToFreshGridView) contentListView).setNumColumns(numColumns);
				}

				addView(contentListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				contentListView.setAdapter(adapter);

				if (null != footView) {
					addView(footView);
				}
			}
		});

		onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View contentListView, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (null != headView && null == headView.getTag(R.id.firstY)) {
							headView.setTag(R.id.firstY, headView.getY());
						}

						if (null != footView && null == footView.getTag(R.id.firstY)) {
							footView.setTag(R.id.firstY, footView.getY());
						}

						startY = event.getY();
						currentY = event.getY();

						break;
					case MotionEvent.ACTION_UP:
						// 刷新操作========start=========
						int totalDistance = (int) (event.getY() - startY);// 计算出一共拉滑了多少距离

						if (onRefreshListener != null
								&& shouldRefresh
								&& canDrag
								&& ((headView != null && headView.getHeight() <= totalDistance / 2) || (footView != null && -footView
										.getHeight() >= totalDistance / 2))) {

							onRefreshListener.onRefresh();
						}
						// 刷新操作========end=========

						// 复位相关动画
						contentListView.animate().y(0f);
						if (null != headView) {
							headView.animate().y((Float) headView.getTag(R.id.firstY));
						}

						if (null != footView) {
							footView.animate().y((Float) footView.getTag(R.id.firstY));
						}

						// 重置标记位
						canDrag = false;

						break;
					case MotionEvent.ACTION_MOVE:
						float distanceY = event.getY() - currentY;
						currentY = event.getY();

						if (canDrag && Math.abs(distanceY) > 2.5) {
							float moveRange = 3f * (distanceY / Math.abs(distanceY));

							contentListView.setY(contentListView.getY() + moveRange);

							if (null != headView) {
								headView.setY(headView.getY() + moveRange);
							}

							if (null != footView) {
								footView.setY(footView.getY() + moveRange);
							}
						}

						break;
				}

				return false;
			}
		};
	}

	public void showHeadAndFootView() {
		if (null != headView) {
			headView.setVisibility(View.VISIBLE);
		}

		if (null != footView) {
			footView.setVisibility(View.VISIBLE);
		}
	}

	public void hideHeadAndFootView() {
		if (null != headView) {
			headView.setVisibility(View.INVISIBLE);
		}

		if (null != footView) {
			footView.setVisibility(View.INVISIBLE);
		}
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (null != contentListView) {
			contentListView.setAdapter(adapter);
		}
		this.adapter = adapter;
		adapter.notifyDataSetChanged();
	}

	public void closeRefresh() {
		shouldRefresh = false;
	}

	public void openRefresh() {
		shouldRefresh = true;
	}
}
