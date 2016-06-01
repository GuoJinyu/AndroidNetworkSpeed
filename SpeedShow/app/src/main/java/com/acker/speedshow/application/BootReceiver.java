package com.acker.speedshow.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.acker.speedshow.R;
import com.acker.speedshow.service.FloatWindowService;
import com.acker.speedshow.util.PreferenceUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (PreferenceUtil.getSingleton(context).getBoolean(context.getResources().getString(R.string.action_boot), true)) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                context.startService(new Intent(context, FloatWindowService.class));
            }
        }
    }
}
