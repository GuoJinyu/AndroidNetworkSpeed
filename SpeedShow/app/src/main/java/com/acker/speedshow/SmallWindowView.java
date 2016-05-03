package com.acker.speedshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class SmallWindowView extends WindowView {

    public SmallWindowView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.linLayoutSmall);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }
}
