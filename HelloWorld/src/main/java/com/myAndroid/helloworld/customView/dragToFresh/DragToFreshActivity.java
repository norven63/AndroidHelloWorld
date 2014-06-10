package com.myAndroid.helloworld.customView.dragToFresh;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Lists;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.customView.dragToFresh.DragToFreshLayout.OnUpdateListener;

public class DragToFreshActivity extends Activity {
	private DragToFreshLayout dragToFreshLayout;

	public class MyBaseAdapter extends BaseAdapter {
		private List<TextView> dateSource = Lists.newArrayList();

		public MyBaseAdapter() {
			super();

			for (int i = 0; i < 9; i++) {
				TextView textView = (TextView) getLayoutInflater().inflate(R.layout.drag_to_fresh_textview, null);
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

		setContentView(R.layout.activity_drag4fresh_layout);

		BaseAdapter adapter = new MyBaseAdapter();
		dragToFreshLayout = (DragToFreshLayout) findViewById(R.id.dragToFreshListView);
		dragToFreshLayout.setAdapter(adapter);
		dragToFreshLayout.setOnUpdateListener(new OnUpdateListener() {
			@Override
			public void onUpdate() {
				Toast.makeText(DragToFreshActivity.this, "update!!!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
