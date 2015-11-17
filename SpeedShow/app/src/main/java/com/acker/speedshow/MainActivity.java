package com.acker.speedshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnOpen;
    private Button btnClose;
    private PreferenceUtil preUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preUtil = PreferenceUtil.getSingleton(getApplicationContext());
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(MainActivity.this,
                FloatWindowService.class);
        btnOpen = (Button) findViewById(R.id.buttonOpen);
        btnOpen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startService(intent);
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
            preUtil.saveBoolean(getResources().getString(R.string.action_boot),item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
