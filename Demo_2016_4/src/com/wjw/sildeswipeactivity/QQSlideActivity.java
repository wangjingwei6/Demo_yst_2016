package com.wjw.sildeswipeactivity;

import com.example.ddddd.R;

import android.app.Activity;
import android.os.Bundle;

public class QQSlideActivity extends Activity{
	
	private QQSlideLayout mQQSlideLayout;
	private MainContentLayout mMainContentLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.slide_qq_activity);
		
		
		mQQSlideLayout = (QQSlideLayout) findViewById(R.id.slide_layout);
		mMainContentLayout = (MainContentLayout)findViewById(R.id.mainContent);
		mMainContentLayout.setDragLayout(mQQSlideLayout);
	}

}
