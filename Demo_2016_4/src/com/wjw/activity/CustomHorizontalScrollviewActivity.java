package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ddddd.R;
import com.wjw.adapter.HSVAdapter;
import com.wjw.bean.GetAllTypeData;
import com.wjw.customview.HSVLayout;
import com.wjw.customview.MyHorizontalScrollView;
import com.wjw.customview.MyHorizontalScrollView.OnScrollListener1;

public class CustomHorizontalScrollviewActivity extends Activity {
	private MyHorizontalScrollView myScrollView;
	private MyHorizontalScrollView myScrollView2;
	
	private LinearLayout listButtonLinear;
	private HSVLayout listImagelinearLinear;

	private List<String> arrayList;

	private void requestListData() {
		arrayList = new ArrayList<String>();
		arrayList.add("1111");
		arrayList.add("2222");
		arrayList.add("3333");
		arrayList.add("4444");
		arrayList.add("5555");
		arrayList.add("6666");
		arrayList.add("7777");
		arrayList.add("8888");
		arrayList.add("9999");
		arrayList.add("AAAA");
		arrayList.add("BBBB");
		arrayList.add("CCCC");
		arrayList.add("DDDD");
		arrayList.add("EEEE");
		arrayList.add("FFFF");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_custom_hscrollview_main);

		requestListData();
		initScrollVidewOne();

	}

	private void initScrollVidewOne() {

		myScrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		myScrollView2 = (MyHorizontalScrollView) findViewById(R.id.myScrollView2);
		
		listButtonLinear = (LinearLayout) findViewById(R.id.listLinear);
		listImagelinearLinear = (HSVLayout) findViewById(R.id.listLinear2);
		listImagelinearLinear.setChildConfig(3);
		
		listButtonLinear.removeAllViews();
		listImagelinearLinear.removeAllViews();
		
		initButtonData();
		initImageData();

		myScrollView.setOnScrollListener(new OnScrollListener1() {

			@Override
			public void onScroll() {
			}

			@Override
			public void onRight() {
				Toast.makeText(CustomHorizontalScrollviewActivity.this, "滑动到最右边", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLeft() {
				Toast.makeText(CustomHorizontalScrollviewActivity.this, "滑动到最左边", Toast.LENGTH_SHORT).show();
			}
		});
		
		myScrollView2.setOnScrollListener(new OnScrollListener1() {
			
			@Override
			public void onScroll() {
			}
			
			@Override
			public void onRight() {
				Toast.makeText(CustomHorizontalScrollviewActivity.this, "滑动到最右边", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLeft() {
				Toast.makeText(CustomHorizontalScrollviewActivity.this, "滑动到最左边", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initButtonData() {
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = 4;
		mLayoutParams.rightMargin = 4;
		mLayoutParams.topMargin = 8;
		mLayoutParams.bottomMargin = 8;

		for (int i = 0; i < arrayList.size(); i++) {
			Button button = new Button(CustomHorizontalScrollviewActivity.this);
			button.setText(arrayList.get(i));
			button.setTextSize(16);
			button.setMaxHeight(32);
			listButtonLinear.addView(button, mLayoutParams);
			button.setOnClickListener(new MyButtonClick());
			button.setId(i);
		}

	}

	private void initImageData() {
		int[] imgIDs = GetAllTypeData.getInstance().getImgIDs();
		
		HSVAdapter mHsvAdapter = new HSVAdapter(CustomHorizontalScrollviewActivity.this,imgIDs);
		
		listImagelinearLinear.setAdapter(mHsvAdapter);
		
	}
	
	
	
	
	class MyButtonClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			Toast.makeText(CustomHorizontalScrollviewActivity.this,"点击了第 "+(id+1)+" 个按钮,内容是: "+arrayList.get(id), Toast.LENGTH_SHORT).show();
		}
		
	}

}
