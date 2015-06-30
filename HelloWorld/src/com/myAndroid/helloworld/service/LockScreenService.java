package com.myAndroid.helloworld.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.myAndroid.helloworld.activity.LockScreenActivity;

public class LockScreenService extends Service {
	private Intent intent;

	/**
	 * 自制锁屏要点:
	 * 1.处理ACTION_SCREEN_OFF事件,用以启动Activity
	 * 2.Activity拦截显示开锁界面的时机
	 */
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				// 注意这个事件的现象:在锁屏时显示LockScreenActivity的界面,但是无法阻止设备锁屏的功能。而在随后开屏时会第一时间显示LockScreenActivity的界面
				Intent newIntent = new Intent(LockScreenService.this, LockScreenActivity.class);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意此处标签
				startActivity(newIntent);
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				// TODO
				// 开屏事件
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null == this.intent) {
			this.intent = intent;// 缓存intent,以备重新启动时用
		}

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);// 监听锁屏
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);// 监听开屏

		registerReceiver(broadcastReceiver, intentFilter);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(broadcastReceiver);

		if (null != intent) {
			startService(intent);
		}
	}
}
