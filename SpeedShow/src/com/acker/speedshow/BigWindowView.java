package com.acker.speedshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class BigWindowView extends RelativeLayout {

	public int viewWidth;
	public int viewHeight;

	public BigWindowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.rlLayoutBig);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}
}
