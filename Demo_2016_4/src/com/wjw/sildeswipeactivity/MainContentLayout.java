package com.wjw.sildeswipeactivity;

import com.wjw.sildeswipeactivity.QQSlideLayout.Status;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MainContentLayout extends RelativeLayout {

    private QQSlideLayout mDragLayout;

    public MainContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainContentLayout(Context context) {
        super(context);
    }

    public void setDragLayout(QQSlideLayout mDragLayout) {
        this.mDragLayout = mDragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mDragLayout.getStatus() == Status.Close){
            return super.onInterceptTouchEvent(ev);
        }else {
            return true;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mDragLayout.getStatus() == Status.Close){
            return super.onTouchEvent(event);
        }else {
            if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP){
                mDragLayout.close();
            }
            return true;
        }
        
    }

}