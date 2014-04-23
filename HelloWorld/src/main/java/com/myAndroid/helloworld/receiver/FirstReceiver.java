package com.myAndroid.helloworld.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.myAndroid.helloworld.activity.SendOrderReceiverActivity;

public class FirstReceiver extends BroadcastReceiver {
	public static final String DATA_FROM_FIRST = "Data_from_first";

	@Override
	public void onReceive(Context context, Intent intent) {
		String string = intent.getStringExtra(SendOrderReceiverActivity.DATA);
		Toast.makeText(context, "1: " + string, Toast.LENGTH_SHORT).show();

		Bundle bundle = new Bundle();
		bundle.putString(DATA_FROM_FIRST, "Data from 一!");
		setResultExtras(bundle);// 为下一个接收器传入数据,可以一直被传递

		Log.i("ORDER_RECEIVER", "first");
	}
}
