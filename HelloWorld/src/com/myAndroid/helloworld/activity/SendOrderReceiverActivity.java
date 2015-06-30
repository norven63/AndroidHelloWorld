package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.myAndroid.helloworld.R;

public class SendOrderReceiverActivity extends Activity {
	public static final String RECEIVER_PERMISSION = "com.myAndroid.hellorld.orderReceiver.permission";
	public static final String RECEIVER_ACTION = "com.myAndroid.hellorld.orderReceiver.action";

	public static final String DATA = "data";

	// 按钮点击触发
	public void sendOrderReceiver(View view) {
		Intent intent = new Intent(RECEIVER_ACTION);
		intent.putExtra(DATA, "Data from activity!");
		sendOrderedBroadcast(intent, RECEIVER_PERMISSION);

		Log.i("ORDER_RECEIVER", "Receiver has been sent!");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.order_receiver);
	}
}
