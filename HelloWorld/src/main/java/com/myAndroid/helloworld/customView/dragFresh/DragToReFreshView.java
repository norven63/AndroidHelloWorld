package com.myAndroid.helloworld.customView.dragFresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.myAndroid.helloworld.R;

/**
 * 下拉刷新控件
 */
@SuppressLint("ClickableViewAccessibility")
public class DragToReFreshView extends LinearLayout {
	private final int USE_LISTVIEW = 199991;
	private final int NO_DIVIDERID = 299992;
	private boolean isRefreshing = false;
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

	/**
	 * 刷新操作监听接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * ListView
	 */
	private class DragToFreshListView extends ListView {
		public DragToFreshListView(Context context) {
			super(context);

			this.setOnTouchListener(onTouchListener);
		}

		@Override
		protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
			canDrag = clampedY;// 捕捉滑动到顶部或者底部的时机
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	/**
	 * GridView
	 */
	private class DragToFreshGridView extends GridView {
		public DragToFreshGridView(Context context) {
			super(context);

			this.setOnTouchListener(onTouchListener);
		}

		@Override
		protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
			canDrag = clampedY;// 捕捉滑动到顶部或者底部的时机
			super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	/**
	 * MotionEvent策略接口
	 */
	private interface MotionEventStrategy {
		void handelMotionEvent(View contentListView, MotionEvent event);
	}

	/**
	 * MotionEvent按下事件策略类
	 */
	private class MotionEventStrategyForDown implements MotionEventStrategy {
		@Override
		public void handelMotionEvent(View contentListView, MotionEvent event) {
			// 缓存headView和footView的坐标值=======start========
			if (null != headView && null == headView.getTag(R.id.firstY)) {
				headView.setTag(R.id.firstY, headView.getY());
			}

			if (null != footView && null == footView.getTag(R.id.firstY)) {
				footView.setTag(R.id.firstY, footView.getY());
			}
			// 缓存headView和footView的坐标值=======end========

			startY = event.getY();
			currentY = event.getY();
		}
	}

	/**
	 * MotionEvent抬起事件策略类
	 */
	private class MotionEventStrategyForUp implements MotionEventStrategy {
		@Override
		public void handelMotionEvent(View contentListView, MotionEvent event) {
			// 刷新操作========start=========
			int totalDistance = (int) (event.getY() - startY);// 计算出一共拉滑了多少距离

			if (onRefreshListener != null && !isRefreshing && shouldRefresh && canDrag
					&& ((headView != null && headView.getHeight() <= totalDistance / 2) || (footView != null && footView.getHeight() <= -totalDistance / 2))) {

				isRefreshing = true;

				onRefreshListener.onRefresh();
			}
			// 刷新操作========end=========

			// 复位相关动画
			contentListView.animate().setInterpolator(new LinearInterpolator()).y(0f);
			if (null != headView && null != headView.getTag(R.id.firstY)) {
				headView.animate().setInterpolator(new LinearInterpolator()).y((Float) headView.getTag(R.id.firstY));
			}

			if (null != footView && null != footView.getTag(R.id.firstY)) {
				footView.animate().setInterpolator(new LinearInterpolator()).y((Float) footView.getTag(R.id.firstY));
			}

			// 重置标记位
			canDrag = false;
		}
	}

	/**
	 * MotionEvent移动事件策略类
	 */
	private class MotionEventStrategyForMove implements MotionEventStrategy {
		@Override
		public void handelMotionEvent(View contentListView, MotionEvent event) {
			float distanceY = event.getY() - currentY;
			currentY = event.getY();

			// 若位移量大于2.5则进行滑动
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
		}
	}

	public DragToReFreshView(final Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragToReFreshLayout);
		final int numColumns = typedArray.getInt(R.styleable.DragToReFreshLayout_column, USE_LISTVIEW);
		final int dividerId = typedArray.getResourceId(R.styleable.DragToReFreshLayout_divider, NO_DIVIDERID);
		typedArray.recycle();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (isFirstLayout) {
					isFirstLayout = false;// 防止循环加载

					headView = findViewById(R.id.dragToReFresh_headView);
					footView = findViewById(R.id.dragToReFresh_footView);

					if (null != headView) {
						// 向上隐藏headView
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headView.getLayoutParams();
						params.topMargin = -headView.getHeight();
						headView.setLayoutParams(params);
					}

					if (null != footView) {
						// 临时移除footView，下面的数据加载会影响到布局的高度，在数据填充完之后再将footView加回来
						removeView(footView);
					}

					// 根据numColumns属性的值来判断是用ListView还是GridView加载数据
					if (numColumns == USE_LISTVIEW) {
						contentListView = new DragToFreshListView(getContext());

						if (dividerId != NO_DIVIDERID) {
							((DragToFreshListView) contentListView).setDivider(getResources().getDrawable(dividerId));
						}
					} else {
						contentListView = new DragToFreshGridView(getContext());
						((DragToFreshGridView) contentListView).setNumColumns(numColumns);
					}

					addView(contentListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					contentListView.setAdapter(adapter);

					if (null != footView) {
						// 将footView加回来
						addView(footView);
					}
				}
			}
		});

		onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View contentListView, MotionEvent event) {
				MotionEventStrategy strategy = createStrategyWithMotionEvent(event);
				strategy.handelMotionEvent(contentListView, event);

				return false;
			}
		};
	}

	private MotionEventStrategy createStrategyWithMotionEvent(MotionEvent event) {
		MotionEventStrategy returnValue = null;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				return new MotionEventStrategyForDown();

			case MotionEvent.ACTION_MOVE:
				return new MotionEventStrategyForMove();

			case MotionEvent.ACTION_UP:
				return new MotionEventStrategyForUp();

			default:
				return returnValue;
		}
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter == null) {
			throw new NullPointerException("入参非法：" + getClass().getSimpleName() + ".setAdapter入参不可为null！");
		}

		if (null != contentListView) {
			contentListView.setAdapter(adapter);
		}
		this.adapter = adapter;
		adapter.notifyDataSetChanged();
	}

	/**
	 * 显示headView和footView
	 */
	public void showHeadAndFootView() {
		if (null != headView) {
			headView.setVisibility(View.VISIBLE);
		}

		if (null != footView) {
			footView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏headView和footView
	 */
	public void hideHeadAndFootView() {
		if (null != headView) {
			headView.setVisibility(View.INVISIBLE);
		}

		if (null != footView) {
			footView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 关闭刷新功能
	 */
	public void closeRefresh() {
		shouldRefresh = false;
	}

	/**
	 * 打开刷新功能
	 */
	public void openRefresh() {
		shouldRefresh = true;
	}

	/**
	 * 任务完成
	 */
	public void taskFinished() {
		this.isRefreshing = false;
	}
}
