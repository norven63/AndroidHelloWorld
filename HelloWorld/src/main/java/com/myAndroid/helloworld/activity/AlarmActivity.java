package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.myAndroid.helloworld.R;

public class AlarmActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_manager_layout);
	}

	/**
	 * 开启Alarm
	 * 
	 * @param view
	 */
	public void open(View view) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, SecondActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		alarmManager.setInexactRepeating(AlarmManager.RTC, 0, 5 * 1000, pendingIntent);
	}

	/**
	 * 取消Alarm
	 * 注意这里cancel塞的参数,虽然不是同一个PendingIntent,但是他们因为效果一样,
	 * 所以依然可以被AlarmManager用来执行cancel的操作
	 * 
	 * @param view
	 */
	public void close(View view) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, SecondActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		alarmManager.cancel(pendingIntent);
	}
}
