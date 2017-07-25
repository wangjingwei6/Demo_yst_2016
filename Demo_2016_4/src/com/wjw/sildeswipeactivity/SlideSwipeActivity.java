package com.wjw.sildeswipeactivity;

import java.util.ArrayList;

import com.example.ddddd.R;
import com.wjw.sildeswipeactivity.SlideSwipeLayout.SwipeFinishScroll;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

public class SlideSwipeActivity extends Activity {

	// private SlideSwipeLayout swipe_layout;
	private SwipeHelper mSwipeHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aaa_slideswipe_activity);

		mSwipeHelper = new SwipeHelper(this);
		mSwipeHelper.onActivityFinish();
		mSwipeHelper.setSwipeEdge(ViewDragHelper.EDGE_ALL);

	}
 
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		mSwipeHelper.onPostCreate();

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
	}

}
