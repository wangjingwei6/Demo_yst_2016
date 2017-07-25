package com.wjw.sildeswipeactivity;

import com.example.ddddd.R;

import android.app.Activity;
import android.view.LayoutInflater;

public class SwipeHelper {

	private Activity mActivity;

	private SlideSwipeLayout mSlideSwipeLayout;

	public SwipeHelper(Activity activity) {
		this.mActivity = activity;
	}

	public void onActivityFinish() {
		mSlideSwipeLayout = (SlideSwipeLayout) LayoutInflater.from(mActivity).inflate(R.layout.slideswipe_layout, null);

		mSlideSwipeLayout.setSwipeFinishScroll(new SlideSwipeLayout.SwipeFinishScroll() {

			@Override
			public void complete() {
				mActivity.finish();
			}
		});
	}

	public void onPostCreate() {
		mSlideSwipeLayout.attachToActivity(mActivity);
	}

	public void setSwipeEdge(int edge) {
		mSlideSwipeLayout.setSwipeEdge(edge);
	}

}
