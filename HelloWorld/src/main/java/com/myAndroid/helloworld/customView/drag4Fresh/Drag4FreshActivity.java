package com.myAndroid.helloworld.customView.drag4Fresh;

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
import com.myAndroid.helloworld.customView.drag4Fresh.Drag4ReFreshLayout.OnRefreshListener;

public class Drag4FreshActivity extends Activity {
	private Drag4ReFreshLayout dragToFreshLayout;

	public class MyBaseAdapter extends BaseAdapter {
		private List<TextView> dateSource = Lists.newArrayList();

		public MyBaseAdapter() {
			super();

			for (int i = 0; i < 15 * 3; i++) {
				TextView textView = (TextView) getLayoutInflater().inflate(R.layout.drag4fresh_item, null);
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
		dragToFreshLayout = (Drag4ReFreshLayout) findViewById(R.id.dragToFreshListView);
		dragToFreshLayout.setAdapter(adapter);
		dragToFreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(Drag4FreshActivity.this, "update!!!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
