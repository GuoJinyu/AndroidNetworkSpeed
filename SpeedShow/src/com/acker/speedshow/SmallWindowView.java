package com.acker.speedshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SmallWindowView extends LinearLayout {
	public int viewWidth;
	public int viewHeight;

	public SmallWindowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		View view = findViewById(R.id.linLayoutSmall);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}
}
