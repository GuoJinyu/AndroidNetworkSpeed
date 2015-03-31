package com.acker.speedshow;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class FloatWindowService extends Service implements Constants {

	private Handler handler = new Handler();
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("TAG", Thread.currentThread().getId() + "----->1");
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, TIME_SPAN);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
		MyWindowManager.removeBigWindow(getApplicationContext());
		MyWindowManager.removeSmallWindow(getApplicationContext());
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			// 当前没有悬浮窗显示，则创建悬浮窗。
			Log.e("TAG", Thread.currentThread().getId() + "----->2");
			if (!MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.initData();
						MyWindowManager
								.createSmallWindow(getApplicationContext());
					}
				});
			}
			// 当前有悬浮窗显示，则更新内存数据。
			else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Log.e("TAG", Thread.currentThread().getId() + "----->3");
						MyWindowManager.updateViewData(getApplicationContext());
					}
				});
			}
		}

	}
}
