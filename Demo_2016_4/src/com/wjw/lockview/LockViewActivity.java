package com.wjw.lockview;

import com.example.ddddd.R;
import com.wjw.lockview.GestureLockViewGroup.OnGestureLockViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class LockViewActivity extends Activity {

	private GestureLockViewGroup mGestureLockViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lockview_act);

		mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
		mGestureLockViewGroup.setAnswer(new int[] { 1, 4, 7, 8, 9 });
//		mGestureLockViewGroup.setUnMatchExceedBoundary(5); //错误重试次数
		mGestureLockViewGroup.setOnGestureLockViewListener(new OnGestureLockViewListener() {

			@Override
			public void onUnmatchedExceedBoundary() {
				Log.i("LockViewActivity", "onUnmatchedExceedBoundary====");
				Toast.makeText(LockViewActivity.this, "错误5次,滚吧~", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						finish();
					}
				}, 1500);
				
			}

			@Override
			public void onGestureEvent(boolean matched) {
				Log.e("LockViewActivity", "onGestureEvent====");
				if(matched){
					Toast.makeText(LockViewActivity.this, "恭喜~~~", Toast.LENGTH_SHORT).show();
					finish();
					
				}else{
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mGestureLockViewGroup.reset();		
						}
					}, 1500);
					Toast.makeText(LockViewActivity.this, "手势不对!!!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onBlockSelected(int cId) {
				
				Log.i("LockViewActivity", "onBlockSelected====");
			
			}
		});
	}

}
