package com.wjw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ddddd.R;

/**
 * 所有的Activity继承的基类Activity,包含了ActionBar菜单
 * 
 * @author wangjingwei
 */
public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	
	/**
	 * 初始化布局文件
	 */
	public abstract void setContentView();
		
	
	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initView();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_about:
			Toast.makeText(BaseActivity.this,"关于", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_feedback:
			Toast.makeText(BaseActivity.this,"反馈", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_share:
			Toast.makeText(BaseActivity.this,"分享", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
