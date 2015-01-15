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
import android.widget.AdapterView.OnItemClickListener;
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

	private boolean stopSubViewScroll = false;// 是否需要阻止子控件的滑动行为（拖拽时打开,手指放开时关闭）
	private boolean isRefreshing = false;// 是否正在执行刷新操作
	private boolean shouldRefresh = true;// 是否可以刷新
	private boolean isFirstLayout = true;// 是否为头一次渲染至屏幕之上
	private boolean canDrag;// 是否可以拖拽（子布局拉到最顶或者最底时打开）
	private float currentY;// 当前手指触屏的Y坐标

	private OnRefreshListener onTopDragRefreshListener;// 顶部拖拽刷新监听
	private OnRefreshListener onBottomDragRefreshListener;// 底部拖拽刷新监听
	private View headView;
	private View footView;
	private BaseAdapter adapter;
	private OnItemClickListener onItemClickListener;
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
		public boolean onTouchEvent(MotionEvent ev) {
			// 判断是否需要阻止滑动
			if (stopSubViewScroll) {
				return false;
			}

			return super.onTouchEvent(ev);
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
		public boolean onTouchEvent(MotionEvent ev) {
			// 判断是否需要阻止滑动
			if (stopSubViewScroll) {
				return false;
			}

			return super.onTouchEvent(ev);
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
			boolean hasHeadViewMoved = false;// 标记头图标是否向下移动了足够的距离
			if (headView != null) {
				float totalDistance = headView.getY() - ((Float) headView.getTag(R.id.firstY));
				if (totalDistance >= headView.getHeight()) {
					hasHeadViewMoved = true;
				}
			}

			boolean hasFootViewMoved = false;// 标记尾图标是否向上移动了足够的距离
			if (footView != null) {
				float totalDistance = ((Float) footView.getTag(R.id.firstY)) - footView.getY();
				if (totalDistance >= footView.getHeight()) {
					hasFootViewMoved = true;
				}
			}

			if (!isRefreshing && shouldRefresh && canDrag && (hasHeadViewMoved || hasFootViewMoved)) {
				isRefreshing = true;// 标记正在执行刷新操作

				if (hasHeadViewMoved) {
					if (onTopDragRefreshListener != null) {
						onTopDragRefreshListener.onRefresh();
					} else {
						taskFinished();
					}
				} else if (hasFootViewMoved) {
					if (onBottomDragRefreshListener != null) {
						onBottomDragRefreshListener.onRefresh();
					} else {
						taskFinished();
					}
				}
			} else if (canDrag) {
				taskFinished();
			}
			// 刷新操作========end=========
		}
	}

	/**
	 * MotionEvent移动事件策略类
	 */
	private class MotionEventStrategyForMove implements MotionEventStrategy {
		@Override
		public void handelMotionEvent(View contentListView, MotionEvent event) {
			if (isRefreshing) {
				return;
			}

			float distanceY = event.getY() - currentY;
			currentY = event.getY();

			// 若位移量大于2.5则进行滑动
			if (canDrag && Math.abs(distanceY) > 2.5) {
				stopSubViewScroll = true;// 阻止子控件继续滑动

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
		final float dividerHeight = typedArray.getDimension(R.styleable.DragToReFreshLayout_dividerHeight, 0);
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
						} else {
							((DragToFreshListView) contentListView).setDivider(null);
						}
					} else {
						contentListView = new DragToFreshGridView(getContext());
						((DragToFreshGridView) contentListView).setNumColumns(numColumns);
					}

					addView(contentListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

					if (adapter != null) {
						contentListView.setAdapter(adapter);
					}

					if (onItemClickListener != null) {
						contentListView.setOnItemClickListener(onItemClickListener);
					}

					if (dividerHeight != 0 && contentListView instanceof DragToFreshListView) {
						float scale = context.getResources().getDisplayMetrics().density;
						((DragToFreshListView) contentListView).setDividerHeight((int) (dividerHeight * scale + 0.5f));
					}

					if (null != footView) {
						// 将footView加回来
						addView(footView);
					}
				}
			}
		});

		onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(final View contentListView, final MotionEvent event) {
				MotionEventStrategy strategy = createStrategyWithMotionEvent(event);
				if (strategy != null) {
					strategy.handelMotionEvent(contentListView, event);
				}

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

	public void setOnTopDragRefreshListener(OnRefreshListener onTopDragRefreshListener) {
		this.onTopDragRefreshListener = onTopDragRefreshListener;
	}

	public void setOnBottomDragRefreshListener(OnRefreshListener onBottomDragRefreshListener) {
		this.onBottomDragRefreshListener = onBottomDragRefreshListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
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
		/*
		 * 复位相关动画
		 */
		if (null != headView && null != headView.getTag(R.id.firstY)) {
			headView.animate().setInterpolator(new LinearInterpolator()).y((Float) headView.getTag(R.id.firstY));
		}

		if (null != footView && null != footView.getTag(R.id.firstY)) {
			footView.animate().setInterpolator(new LinearInterpolator()).y((Float) footView.getTag(R.id.firstY));
		}

		contentListView.animate().setInterpolator(new LinearInterpolator()).y(0f);
		
		/*
		 * 重置各个标记位
		 */
		canDrag = false;
		isRefreshing = false;
		stopSubViewScroll = false;
	}
}
