package com.fxxkywcx.nostudy.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;

public class ADsFlipper extends ViewFlipper {
    private GestureDetector listener = null;
    public ADsFlipper(Context context) {
        super(context);
    }

    public ADsFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnGestureListener(GestureDetector.SimpleOnGestureListener listener) {
        this.listener = new GestureDetector(listener);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener == null)
            return super.onTouchEvent(event);
        else
            return listener.onTouchEvent(event);
    }
}
