package com.wjw.activity;

import com.example.ddddd.R;

import fragment.Fragment1;
import fragment.Fragment2;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SecondActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second);
		
		if(savedInstanceState!=null){
			Toast.makeText(this,savedInstanceState.getString("WJW"),Toast.LENGTH_SHORT).show();
		}
		Intent intent = new Intent();
		intent.putExtra("Result", "这是返回来的数据");
		setResult(10086,intent);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction =
		fragmentManager.beginTransaction();
		
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		
		if(d.getWidth()>d.getHeight()){ //横
			Fragment1 fragment1 = new Fragment1();
			fragmentTransaction.replace(R.id.content_framlayout, fragment1);
			
		}else{//竖
			Fragment2 fragment2 = new Fragment2();
			fragmentTransaction.replace(R.id.content_framlayout, fragment2);
		}
		fragmentTransaction.commit();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("WJW","保存的数据");
		super.onSaveInstanceState(outState);
	}
}
