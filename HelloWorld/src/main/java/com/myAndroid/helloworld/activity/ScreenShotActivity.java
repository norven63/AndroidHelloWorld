package com.myAndroid.helloworld.activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.myAndroid.helloworld.R;

public class ScreenShotActivity extends Activity {
	private Thread saveTaskThread;

	public void screenRecorder(final View v) {
		// 消除点中状态
		v.clearFocus();
		v.setPressed(false);

		v.animate().y(300f).setDuration(1000).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				((Button) v).setTextColor(getResources().getColor(R.color.blue));
				v.animate().x(600f).setInterpolator(new LinearInterpolator()).setDuration(1000);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
			}
		});

		final BlockingQueue<Bitmap> blockingQueue = new LinkedBlockingDeque<Bitmap>();
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
			private String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jieping/";

			private int i = 0;

			@Override
			public void run() {
				if (saveTaskThread == null) {
					saveTaskThread = Thread.currentThread();
				}

				try {
					while (true) {
						i++;
						Log.e("ZXX", "准备save：" + i);

						Bitmap rootBitmap = blockingQueue.take();

						if (rootBitmap != null) {
							FileOutputStream out = new FileOutputStream(fname + i + ".png");

							rootBitmap.compress(Bitmap.CompressFormat.PNG, 10, out);

							out.flush();
							out.close();

							Log.e("ZXX", "成功save：" + i);
						} else {
							Log.e("ZXX", "获取截屏Bitmap为null");
						}
					}
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						Log.e("ZXX", "线程已中止。");
					} else if (e instanceof IOException) {
						Log.e("ZXX", "IO异常。");
					}
				}
			}
		});

		// DisplayMetrics metric = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metric);
		// final Bitmap rootBitmap = Bitmap.createBitmap(metric.widthPixels,
		// metric.heightPixels, Config.ARGB_8888);
		// final View rootView = getWindow().getDecorView();

		final View rootView = getWindow().getDecorView();
		rootView.setDrawingCacheEnabled(true);

		// 创建定时器
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

		// 开始任务
		final Future future1 = schedule.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					// rootView.draw(new Canvas(rootBitmap));

					rootView.buildDrawingCache();
					Bitmap rootBitmap = rootView.getDrawingCache();

					Log.e("ZXX", "获取rootBitmap");
					blockingQueue.put(rootBitmap);
				} catch (InterruptedException e) {
					e.printStackTrace();

					if (saveTaskThread != null) {
						saveTaskThread.interrupt();
					}
				}
			}
		}, 0, 100, TimeUnit.MILLISECONDS);

		// 取消任务
		schedule.schedule(new Runnable() {
			@Override
			public void run() {
				future1.cancel(false);

				if (saveTaskThread != null) {
					saveTaskThread.interrupt();
				}
			}
		}, 3000, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_shot);
	}
}
