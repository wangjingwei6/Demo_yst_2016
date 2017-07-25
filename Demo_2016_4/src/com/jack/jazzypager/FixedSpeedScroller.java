package com.jack.jazzypager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

@SuppressLint("NewApi")
public class FixedSpeedScroller extends Scroller{
	
    private int mDuration = 3000;
	
    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
	
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    	// TODO Auto-generated method stub
    	super.startScroll(startX, startY, dx, dy, mDuration);
    }
    
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
    	// TODO Auto-generated method stub
    	super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int mDuration) {
	this.mDuration = mDuration;
    }
	
    public int getmDuration() {
	return mDuration;
    }
    
}