package com.myAndroid.helloworld.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.myAndroid.helloworld.activity.SendOrderReceiverActivity;

public class SecondReceiver extends BroadcastReceiver {
	public static final String DATA_FROM_SECOND = "Data_from_second";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("abc", "二进入!");

		String string = intent.getStringExtra(SendOrderReceiverActivity.DATA);
		Bundle bundle = getResultExtras(false);// 若上一个接收器没有塞Bundle时:false-则返回null;true-新的对象
		String string2 = "";
		if (null != bundle) {
			string2 = bundle.getString(FirstReceiver.DATA_FROM_FIRST);
		}

		Bundle bundle2 = new Bundle();
		bundle.putString(DATA_FROM_SECOND, "Data from 二!");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.e("abc", "二结束!");

		setResultExtras(bundle2);

		Toast.makeText(context, "2: " + string + " : " + string2, Toast.LENGTH_SHORT).show();

		Log.i("ORDER_RECEIVER", "second");
	}
}
