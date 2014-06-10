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

public class Drag4FreshLayout extends LinearLayout {
	public interface OnUpdateListener {
		public void onUpdate();
	}

	private class DragToFreshListView extends ListView {
		public DragToFreshListView(Context context) {
			super(context);

			this.setOnTouchListener(onTouchListener);
		}

		@Override
		protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
			canDrag = clampedY;// 捕捉允许拖拽的时机，此法甚妙
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
			canDrag = clampedY;// 捕捉允许拖拽的时机，用此法感觉良好
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	private final int USE_LISTVIEW = 9999;
	private boolean isFirstLayout = true;// 标记是否为第一次渲染布局
	private boolean canDrag;// 标记是否允许拖拽
	private boolean canUpdate = true;
	private float currentY;
	private float startY;
	private View headView;
	private View footView;
	private BaseAdapter adapter;
	private OnUpdateListener onUpdateListener;
	private OnTouchListener onTouchListener;

	public Drag4FreshLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Drag4FreshLayout);
		final int numColumns = typedArray.getInt(R.styleable.Drag4FreshLayout_numColumns, USE_LISTVIEW);
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
					// 隐藏headView
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headView.getLayoutParams();
					params.topMargin = -headView.getHeight();
					headView.setLayoutParams(params);
				}

				if (null != footView) {
					removeView(footView);
				}

				AbsListView contentListView;
				// 加入Content
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
						// 复位动画
						contentListView.animate().y(0f);
						if (null != headView) {
							headView.animate().y((Float) headView.getTag(R.id.firstY));
						}

						if (null != footView) {
							footView.animate().y((Float) footView.getTag(R.id.firstY));
						}

						// 重置标记位
						canDrag = false;
						canUpdate = true;

						// 刷新操作
						int top = -123;
						int bottom = -123;

						int totalDistance = (int) (event.getY() - startY);// 计算出一共拉滑了多少距离

						if (headView != null) {
							top = headView.getTop() + totalDistance;
						}

						if (footView != null) {
							bottom = footView.getBottom() + totalDistance;
						}

						if (canUpdate && onUpdateListener != null && (top >= 0 || bottom >= 0)) {
							canUpdate = false;
							onUpdateListener.onUpdate();
						}

						break;
					case MotionEvent.ACTION_MOVE:
						float distanceY = event.getY() - currentY;
						currentY = event.getY();

						if (canDrag && Math.abs(distanceY) > 2.5) {// 如果改用distanceY>?则只能够下拉操作,现在Math.abs(distanceY)则是可以上下拉操作都能够
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

	public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
		this.onUpdateListener = onUpdateListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		adapter.notifyDataSetChanged();
	}
}
