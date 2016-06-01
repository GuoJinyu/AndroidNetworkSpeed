package com.acker.speedshow.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.acker.speedshow.R;
import com.acker.speedshow.application.Constants;
import com.acker.speedshow.service.FloatWindowService;
import com.acker.speedshow.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements Constants {
    private Button btnOpen;
    private Button btnClose;
    private PreferenceUtil preUtil;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preUtil = PreferenceUtil.getSingleton(getApplicationContext());
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, FloatWindowService.class);
        btnOpen = (Button) findViewById(R.id.buttonOpen);
        btnOpen.setOnClickListener(new OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_alert), Toast.LENGTH_LONG).show();
                    Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(permissionIntent, OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    startService(intent);
                }
            }
        });
        btnClose = (Button) findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this,
                        FloatWindowService.class);
                stopService(intent);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setChecked(preUtil.getBoolean(getResources().getString(R.string.action_boot), true));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_boot) {
            item.setChecked(!item.isChecked());
            preUtil.saveBoolean(getResources().getString(R.string.action_boot), item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_result), Toast.LENGTH_SHORT).show();
            } else {
                startService(intent);
            }
        }
    }
}

