package com.wjw.sildeswipeactivity;

import java.lang.reflect.Method;

import com.nineoldandroids.view.ViewHelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SlideSwipeLayout extends FrameLayout {

	private View mDragView;// 滑动的VIEW
	private ViewDragHelper mViewDragHelper;
	private int mCurEdgeFlag = ViewDragHelper.EDGE_LEFT; // 默认
	private int mSwipeEdge = ViewDragHelper.EDGE_LEFT;

	private Point mCurArrivePoint = new Point(); // 当前的点
	private Point mAutoBackOrignalPoint = new Point(); // 还原后的初始点

	private Activity mActivity;

	private int mMainLeft;
	private int mMainRight;
	private int mMainBottom;

	public SlideSwipeLayout(Context context) {
		this(context, null);
	}

	public SlideSwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();

	}

	private SwipeFinishScroll mFinishScroll;

	interface SwipeFinishScroll {
		void complete();
	}

	public void setSwipeFinishScroll(SwipeFinishScroll finishScroll) {
		this.mFinishScroll = finishScroll;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	/**
	 * 初始化ViewDragHelper
	 */
	private void init() {

		mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				mCurEdgeFlag = edgeFlags;

				if (mDragView == null)
					mDragView = getChildAt(0);
				mViewDragHelper.captureChildView(mDragView, pointerId);
			}

			@Override
			public boolean tryCaptureView(View childView, int arg1) {
				return childView == mDragView;
			}

			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				mCurArrivePoint.x = left;

				// 允许左右触发滑动，否则return 0
				if (mCurEdgeFlag != ViewDragHelper.EDGE_BOTTOM) {
					return left;
				} else {
					return 0;
				}
			}

			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				mCurArrivePoint.y = top;

				if (mCurEdgeFlag == ViewDragHelper.EDGE_BOTTOM) {
					return top;
				} else {
					return 0;
				}
			}

			@Override
			public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
				super.onViewPositionChanged(changedView, left, top, dx, dy);
				switch (mCurEdgeFlag) {
				case ViewDragHelper.EDGE_LEFT:
					
					if (left >= getWidth()) {
						if (mFinishScroll != null) {
							mFinishScroll.complete();
						}
					}
					
					if(changedView==mDragView){
						mMainLeft = left;
						animViews(mMainLeft);
					}
					break;
				case ViewDragHelper.EDGE_RIGHT:
					if (left <= -getWidth()) {
						if (mFinishScroll != null) {
							mFinishScroll.complete();
						}
					}
					break;
				case ViewDragHelper.EDGE_BOTTOM:
					if (top <= -getHeight()) {
						if (mFinishScroll != null) {
							mFinishScroll.complete();
						}
					}
					break;
				}

				if (mMainLeft < 0) {
					mMainLeft = 0;
				} else if (mMainLeft > getWidth()) {
					mMainLeft = getWidth();
				}
			}
			

			@Override
			public void onViewReleased(View releasedChild, float xvel, float yvel) {
				super.onViewReleased(releasedChild, xvel, yvel);

				switch (mCurEdgeFlag) {
				case ViewDragHelper.EDGE_LEFT:
					// 水平滑动超过一半，触发结束
					if (mCurArrivePoint.x > getWidth() / 3 * 1) {
						mViewDragHelper.settleCapturedViewAt(getWidth(), mAutoBackOrignalPoint.y);
					} else {
						mViewDragHelper.settleCapturedViewAt(mAutoBackOrignalPoint.x, mAutoBackOrignalPoint.y);
					}
					break;

				case ViewDragHelper.EDGE_RIGHT:
					// 水平滑动超过一半，触发结束
					if (mCurArrivePoint.x < -getWidth() / 3 * 1) {
						mViewDragHelper.settleCapturedViewAt(-getWidth(), mAutoBackOrignalPoint.y);
					} else {
						mViewDragHelper.settleCapturedViewAt(mAutoBackOrignalPoint.x, mAutoBackOrignalPoint.y);
					}
					break;
				case ViewDragHelper.EDGE_BOTTOM:
					// 垂直滑动超过一半，触发结束
					if (mCurArrivePoint.y < -getHeight() / 3 * 1) {
						mViewDragHelper.settleCapturedViewAt(mAutoBackOrignalPoint.x, -getHeight());
					} else {
						mViewDragHelper.settleCapturedViewAt(mAutoBackOrignalPoint.x, mAutoBackOrignalPoint.y);
					}
					break;
				}

				mCurArrivePoint.x = 0;
				mCurArrivePoint.y = 0;
				invalidate();

			}

		});

		mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
		// mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
		// mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
	}

	/**
	 * view滑动透明渐变动画
	 * @param mMainLeft
	 */
	private void animViews(int mMainLeft) {
		float  defaultPercent= mMainLeft/(float)(getWidth());
		float percent = (float) (1-defaultPercent*0.5);
		ViewHelper.setAlpha(mDragView, percent);

	}
	

	@Override
	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			invalidate();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mViewDragHelper.cancel();
			return false;
		}
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	public void setSwipeEdge(int edge) {
		this.mSwipeEdge = edge;
		mViewDragHelper.setEdgeTrackingEnabled(mSwipeEdge);
	}

	// 核心代码，绑定到相应activity
	public void attachToActivity(Activity activity) {
		this.mActivity = activity;
		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();
		ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
		decorChild.setBackgroundResource(background);
		decorView.removeView(decorChild);
		addView(decorChild);
		decorView.addView(this);

	}

}
