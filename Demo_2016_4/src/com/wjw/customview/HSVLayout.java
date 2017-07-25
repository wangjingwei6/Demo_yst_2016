package com.wjw.customview;

import com.wjw.adapter.HSVAdapter;
import com.wjw.util.WindowUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HSVLayout extends LinearLayout {

	private HSVAdapter adapter;
	private Context context;
	
	private int widthScale;

	public HSVLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	

	public void setChildConfig(int widthScale) {
		this.widthScale = widthScale;
	}



	public void setAdapter(HSVAdapter adapter) {
		float desity = WindowUtils.getDesity((Activity) context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((WindowUtils.getScreenWidth((Activity) context))/widthScale, LayoutParams.MATCH_PARENT);
		this.adapter = adapter;
		for (int i = 0; i < adapter.getCount(); i++) {
			final int position = (Integer) adapter.getItem(i);
			View view = adapter.getView(i, null, null);
			view.setPadding(8, 0, 8, 0);
			// 为视图设定点击监听器
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

//					Toast.makeText(context, "您选择了" + (position + 1) + " 个image", Toast.LENGTH_SHORT).show();

				}
			});
			this.setOrientation(HORIZONTAL);
			this.addView(view, params);
		}
	}
}