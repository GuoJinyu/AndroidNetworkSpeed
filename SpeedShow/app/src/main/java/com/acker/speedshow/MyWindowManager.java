package com.acker.speedshow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.TrafficStats;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MyWindowManager implements Constants {

    private WindowManager mWindowManager;
    private WindowView mBigWindowView;
    private WindowView mSmallWindowView;
    private LayoutParams windowParams;
    private TextView tvMobileTx;
    private TextView tvMobileRx;
    private TextView tvWlanTx;
    private TextView tvWlanRx;
    private TextView tvSum;
    private long rxtxTotal = 0;
    private long mobileRecvSum = 0;
    private long mobileSendSum = 0;
    private long wlanRecvSum = 0;
    private long wlanSendSum = 0;
    private long exitTime = 0;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");
    private static MyWindowManager instance;

    public static MyWindowManager getInstance() {
        if (instance == null) {
            instance = new MyWindowManager();
        }
        return instance;
    }

    public void createWindow(final Context context) {
        createWindow(context, SMALL_WINDOW_TYPE);
    }

    private void createWindow(final Context context, int type) {
        final WindowManager windowManager = getWindowManager(context);
        if (windowParams == null) {
            windowParams = getWindowParams(context);
        }
        switch (type) {
            case BIG_WINDOW_TYPE:
                removeWindow(context, mSmallWindowView);
                mSmallWindowView = null;
                if (mBigWindowView == null) {
                    mBigWindowView = new BigWindowView(context);
                    setOnTouchListener(windowManager, context, mBigWindowView, SMALL_WINDOW_TYPE);
                    windowManager.addView(mBigWindowView, windowParams);
                }
                tvMobileRx = (TextView) mBigWindowView.findViewById(R.id.tvMobileRx);
                tvMobileTx = (TextView) mBigWindowView.findViewById(R.id.tvMobileTx);
                tvWlanRx = (TextView) mBigWindowView.findViewById(R.id.tvWlanRx);
                tvWlanTx = (TextView) mBigWindowView.findViewById(R.id.tvWlanTx);
                break;
            case SMALL_WINDOW_TYPE:
                removeWindow(context, mBigWindowView);
                mBigWindowView = null;
                if (mSmallWindowView == null) {
                    mSmallWindowView = new SmallWindowView(context);
                    setOnTouchListener(windowManager, context, mSmallWindowView, BIG_WINDOW_TYPE);
                    windowManager.addView(mSmallWindowView, windowParams);
                }
                tvSum = (TextView) mSmallWindowView.findViewById(R.id.tvSum);
                break;
            default:
                break;
        }
    }

    public void initData() {
        mobileRecvSum = TrafficStats.getMobileRxBytes();
        mobileSendSum = TrafficStats.getMobileTxBytes();
        wlanRecvSum = TrafficStats.getTotalRxBytes() - mobileRecvSum;
        wlanSendSum = TrafficStats.getTotalTxBytes() - mobileSendSum;
        rxtxTotal = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
    }

    private LayoutParams getWindowParams(Context context) {
        final WindowManager windowManager = getWindowManager(context);
        Point sizePoint = new Point();
        windowManager.getDefaultDisplay().getSize(sizePoint);
        int screenWidth = sizePoint.x;
        int screenHeight = sizePoint.y;
        LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.type = LayoutParams.TYPE_PHONE;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        windowParams.gravity = Gravity.START | Gravity.TOP;
        windowParams.width = LayoutParams.WRAP_CONTENT;
        windowParams.height = LayoutParams.WRAP_CONTENT;
        windowParams.x = screenWidth;
        windowParams.y = screenHeight / 2;
        return windowParams;
    }

    private void setOnTouchListener(final WindowManager windowManager, final Context context, final WindowView windowView, final int type) {
        windowView.setOnTouchListener(new OnTouchListener() {
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
                        windowManager.updateViewLayout(windowView, windowParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if ((System.currentTimeMillis() - exitTime) < CHANGE_DELAY) {
                            createWindow(context, type);
                            return true;
                        } else {
                            exitTime = System.currentTimeMillis();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void removeWindow(Context context, WindowView windowView) {
        if (windowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(windowView);
        }
    }

    public void removeAllWindow(Context context) {
        removeWindow(context, mBigWindowView);
        removeWindow(context, mSmallWindowView);
        mBigWindowView = null;
        mSmallWindowView = null;
    }

    public void updateViewData(Context context) {

        long tempSum = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum - rxtxTotal;
        double totalSpeed = rxtxLast * 1000 / TIME_SPAN;
        rxtxTotal = tempSum;
        long tempMobileRx = TrafficStats.getMobileRxBytes();
        long tempMobileTx = TrafficStats.getMobileTxBytes();
        long tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx;
        long tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx;
        long mobileLastRecv = tempMobileRx - mobileRecvSum;
        long mobileLastSend = tempMobileTx - mobileSendSum;
        long wlanLastRecv = tempWlanRx - wlanRecvSum;
        long wlanLastSend = tempWlanTx - wlanSendSum;
        double mobileRecvSpeed = mobileLastRecv * 1000 / TIME_SPAN;
        double mobileSendSpeed = mobileLastSend * 1000 / TIME_SPAN;
        double wlanRecvSpeed = wlanLastRecv * 1000 / TIME_SPAN;
        double wlanSendSpeed = wlanLastSend * 1000 / TIME_SPAN;
        mobileRecvSum = tempMobileRx;
        mobileSendSum = tempMobileTx;
        wlanRecvSum = tempWlanRx;
        wlanSendSum = tempWlanTx;
        if (mBigWindowView != null) {
            if (mobileRecvSpeed >= 0d) {
                tvMobileRx.setText(showSpeed(mobileRecvSpeed));
            }
            if (mobileSendSpeed >= 0d) {
                tvMobileTx.setText(showSpeed(mobileSendSpeed));
            }
            if (wlanRecvSpeed >= 0d) {
                tvWlanRx.setText(showSpeed(wlanRecvSpeed));
            }
            if (wlanSendSpeed >= 0d) {
                tvWlanTx.setText(showSpeed(wlanSendSpeed));
            }
        }
        if (mSmallWindowView != null && totalSpeed >= 0d) {
            tvSum.setText(showSpeed(totalSpeed));
        }

    }

    private String showSpeed(double speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        }
        return speedString;
    }

    public boolean isWindowShowing() {
        return mBigWindowView != null || mSmallWindowView != null;
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
