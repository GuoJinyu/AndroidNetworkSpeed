package com.acker.speedshow;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.TrafficStats;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MyWindowManager implements Constants {

	private static WindowManager mWindowManager;
	private static BigWindowView mBigWindowView;
	private static SmallWindowView mSmallWindowView;
	private static LayoutParams windowParams;
	private static TextView tvMobileTx;
	private static TextView tvMobileRx;
	private static TextView tvWlanTx;
	private static TextView tvWlanRx;
	private static TextView tvSum;
	private static long rxtxTotal = 0;
	private static long rxtxLast = 0;
	private static double totalSpeed;
	private static long mobileLastRecv = 0;
	private static long mobileRecvSum = 0;
	private static double mobileRecvSpeed;
	private static long mobileLastSend = 0;
	private static long mobileSendSum = 0;
	private static double mobileSendSpeed;
	private static long wlanLastRecv = 0;
	private static long wlanRecvSum = 0;
	private static double wlanRecvSpeed;
	private static long wlanLastSend = 0;
	private static long wlanSendSum = 0;
	private static double wlanSendSpeed;
	private static long exitTime = 0;

	private static DecimalFormat showFloatFormat = new DecimalFormat("0.00");

	public static void createBigWindow(final Context context) {
		final WindowManager windowManager = getWindowManager(context);
		Point sizePoint = new Point();
		windowManager.getDefaultDisplay().getSize(sizePoint);
		int screenWidth = sizePoint.x;
		int screenHeight = sizePoint.y;
		if (mBigWindowView == null) {
			mBigWindowView = new BigWindowView(context);
			if (windowParams == null) {
				windowParams = new WindowManager.LayoutParams();
				windowParams.type = LayoutParams.TYPE_PHONE;
				windowParams.format = PixelFormat.RGBA_8888;
				windowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				windowParams.gravity = Gravity.START | Gravity.TOP;
				windowParams.width = mBigWindowView.viewWidth;
				windowParams.height = mBigWindowView.viewHeight;
				windowParams.x = screenWidth;
				windowParams.y = screenHeight / 2;
			}
			removeSmallWindow(context);
			windowManager.addView(mBigWindowView, windowParams);
		}
		tvMobileRx = (TextView) mBigWindowView.findViewById(R.id.tvMobileRx);
		tvMobileTx = (TextView) mBigWindowView.findViewById(R.id.tvMobileTx);
		tvWlanRx = (TextView) mBigWindowView.findViewById(R.id.tvWlanRx);
		tvWlanTx = (TextView) mBigWindowView.findViewById(R.id.tvWlanTx);
		// 设置悬浮窗的Touch监听
		mBigWindowView.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = windowParams.x;
					paramY = windowParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					windowParams.x = paramX + dx;
					windowParams.y = paramY + dy;
					// 更新悬浮窗位置
					windowManager
							.updateViewLayout(mBigWindowView, windowParams);
					break;
				case MotionEvent.ACTION_UP:
					if ((System.currentTimeMillis() - exitTime) < CHANGE_DELAY) {
						removeBigWindow(context);
						createSmallWindow(context);
					} else{
						exitTime = System.currentTimeMillis();
					}
					break;
				}
				return true;
			}
		});
	}

	public static void initData() {
		mobileRecvSum = TrafficStats.getMobileRxBytes();
		mobileSendSum = TrafficStats.getMobileTxBytes();
		wlanRecvSum = TrafficStats.getTotalRxBytes() - mobileRecvSum;
		wlanSendSum = TrafficStats.getTotalTxBytes() - mobileSendSum;
		rxtxTotal = TrafficStats.getTotalRxBytes()
				+ TrafficStats.getTotalTxBytes();
	}

	public static void createSmallWindow(final Context context) {
		final WindowManager windowManager = getWindowManager(context);
		Point sizePoint = new Point();
		windowManager.getDefaultDisplay().getSize(sizePoint);
		int screenWidth = sizePoint.x;
		int screenHeight = sizePoint.y;
		if (mSmallWindowView == null) {
			mSmallWindowView = new SmallWindowView(context);
			if (windowParams == null) {
				windowParams = new WindowManager.LayoutParams();
				windowParams.type = LayoutParams.TYPE_PHONE;
				windowParams.format = PixelFormat.RGBA_8888;
				windowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				windowParams.gravity = Gravity.START | Gravity.TOP;
				windowParams.width = mSmallWindowView.viewWidth;
				windowParams.height = mSmallWindowView.viewHeight;
				windowParams.x = screenWidth;
				windowParams.y = screenHeight / 2;
			}
			removeBigWindow(context);
			windowManager.addView(mSmallWindowView, windowParams);
		}
		tvSum = (TextView) mSmallWindowView.findViewById(R.id.tvSum);
		// 设置悬浮窗的Touch监听
		mSmallWindowView.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = windowParams.x;
					paramY = windowParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					windowParams.x = paramX + dx;
					windowParams.y = paramY + dy;
					// 更新悬浮窗位置
					windowManager.updateViewLayout(mSmallWindowView,
							windowParams);
					break;
				case MotionEvent.ACTION_UP:
					if ((System.currentTimeMillis() - exitTime) < CHANGE_DELAY) {
						removeSmallWindow(context);
						createBigWindow(context);
					} else{
						exitTime = System.currentTimeMillis();
					}
					break;
				}
				return true;
			}
		});
	}

	public static void removeBigWindow(Context context) {
		if (mBigWindowView != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(mBigWindowView);
			mBigWindowView = null;
		}
	}

	public static void removeSmallWindow(Context context) {
		if (mSmallWindowView != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(mSmallWindowView);
			mSmallWindowView = null;
		}
	}

	public static void updateViewData(Context context) {

		long tempSum = TrafficStats.getTotalRxBytes()
				+ TrafficStats.getTotalTxBytes();
		rxtxLast = tempSum - rxtxTotal;
		totalSpeed = rxtxLast * 1000 / (float) TIME_SPAN;
		rxtxTotal = tempSum;
		long tempMobileRx = TrafficStats.getMobileRxBytes();
		long tempMobileTx = TrafficStats.getMobileTxBytes();
		long tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx;
		long tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx;
		mobileLastRecv = tempMobileRx - mobileRecvSum;
		mobileLastSend = tempMobileTx - mobileSendSum;
		wlanLastRecv = tempWlanRx - wlanRecvSum;
		wlanLastSend = tempWlanTx - wlanSendSum;
		mobileRecvSpeed = mobileLastRecv * 1000 / (float) TIME_SPAN;
		mobileSendSpeed = mobileLastSend * 1000 / (float) TIME_SPAN;
		wlanRecvSpeed = wlanLastRecv * 1000 / (float) TIME_SPAN;
		wlanSendSpeed = wlanLastSend * 1000 / (float) TIME_SPAN;
		mobileRecvSum = tempMobileRx;
		mobileSendSum = tempMobileTx;
		wlanRecvSum = tempWlanRx;
		wlanSendSum = tempWlanTx;
		if (mBigWindowView != null) {
			tvMobileRx.setText(showSpeed(mobileRecvSpeed));
			tvMobileTx.setText(showSpeed(mobileSendSpeed));
			tvWlanRx.setText(showSpeed(wlanRecvSpeed));
			tvWlanTx.setText(showSpeed(wlanSendSpeed));
		}
		if (mSmallWindowView != null) {
			tvSum.setText(showSpeed(totalSpeed));
		}

	}

	private static String showSpeed(double speed) {
		String speedString;
		if (speed >= 1048576.0) {
			speedString = showFloatFormat.format(speed / 1048576.0) + "MB/s";
		} else if (speed >= 1024.0) {
			speedString = showFloatFormat.format(speed / 1024.0) + "KB/s";
		} else {
			speedString = showFloatFormat.format(speed) + "B/s";
		}
		return speedString;
	}

	public static boolean isWindowShowing() {
		return mBigWindowView != null || mSmallWindowView != null;
	}

	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
}
