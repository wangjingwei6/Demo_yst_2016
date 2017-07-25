package com.wjw.sildeswipeactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.ddddd.R;

public class BaseSlideStyleActivity extends Activity implements OnClickListener {

	private Button slide_cehua_qq;
	private Button slide_huadong_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aaa_custom_slidestyle_all_act);

		slide_cehua_qq = (Button) findViewById(R.id.slide_cehua_qq);
		slide_huadong_activity = (Button) findViewById(R.id.slide_huadong_activity);

		
		slide_cehua_qq.setOnClickListener(this);
		slide_huadong_activity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide_cehua_qq:
			startActivity(new Intent(BaseSlideStyleActivity.this, QQSlideActivity.class));
			break;
		case R.id.slide_huadong_activity: //滑动切换页面
			startActivity(new Intent(BaseSlideStyleActivity.this, SlideSwipeActivity.class));
			overridePendingTransition(R.anim.anim_in_right,R.anim.anim_out_left);
			break;

		default:
			break;
		}
	}

}
