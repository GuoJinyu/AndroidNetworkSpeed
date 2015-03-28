package com.acker.speedshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	private Button btnOpen;
	private Button btnClose;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						FloatWindowService.class);
						stopService(intent);
			}
			
		});
	}
}
