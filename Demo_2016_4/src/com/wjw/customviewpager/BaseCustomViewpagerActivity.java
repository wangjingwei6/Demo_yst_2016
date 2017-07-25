package com.wjw.customviewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ddddd.R;
import com.wjw.util.OtherUtils;

public class BaseCustomViewpagerActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.aaa_customviewpager_all_act);
		
		initView();
		
	}

	private void initView() {
		
		findViewById(R.id.triangle_viewpager_but).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(OtherUtils.isAllowClickable()){
					startActivity(new Intent(BaseCustomViewpagerActivity.this,TriangleViewpagerActivity.class));
				}
			}
		});
		
		findViewById(R.id.color_viewpager_but).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(OtherUtils.isAllowClickable()){
					startActivity(new Intent(BaseCustomViewpagerActivity.this,ColorViewPagerActivity.class));
				}
			}
		});
		
		
		
	}
	
}
