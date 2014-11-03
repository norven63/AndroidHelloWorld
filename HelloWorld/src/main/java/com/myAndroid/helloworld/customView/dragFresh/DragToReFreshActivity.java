package com.myAndroid.helloworld.customView.dragFresh;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.customView.dragFresh.DragToReFreshView.OnRefreshListener;

public class DragToReFreshActivity extends Activity {
	private DragToReFreshView dragToFreshLayout;

	public class MyBaseAdapter extends BaseAdapter {
		private List<TextView> dateSource = Lists.newArrayList();

		public MyBaseAdapter() {
			super();

			for (int i = 0; i < 15 * 3; i++) {
				TextView textView = (TextView) getLayoutInflater().inflate(R.layout.drag_to_refresh_item, null);
				dateSource.add(textView);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getItem(int position) {
			return dateSource.get(position);
		}

		@Override
		public int getCount() {
			return dateSource.size();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_drag_to_refresh_layout);

		BaseAdapter adapter = new MyBaseAdapter();
		dragToFreshLayout = (DragToReFreshView) findViewById(R.id.dragToFreshListView);
		dragToFreshLayout.setAdapter(adapter);
		dragToFreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(DragToReFreshActivity.this, "Update!", Toast.LENGTH_LONG).show();
				dragToFreshLayout.taskFinished();// 标记任务完成(一般在异步任务回调中执行此接口)
			}
		});
	}
}
