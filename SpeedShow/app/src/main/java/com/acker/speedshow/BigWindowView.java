package com.acker.speedshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class BigWindowView extends WindowView {

	public BigWindowView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.rlLayoutBig);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}
}
