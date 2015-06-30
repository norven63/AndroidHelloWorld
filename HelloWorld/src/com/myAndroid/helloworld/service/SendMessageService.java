package com.myAndroid.helloworld.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;

public class SendMessageService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SmsManager smg = SmsManager.getDefault();
		smg.sendTextMessage("+电话号码", null, "HelloWorld", null, null);
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
