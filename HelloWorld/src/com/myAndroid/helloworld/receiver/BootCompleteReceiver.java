package com.myAndroid.helloworld.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myAndroid.helloworld.service.SendMessageService;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startService = new Intent(context,SendMessageService.class);
		context.startService(startService);
	}
}
