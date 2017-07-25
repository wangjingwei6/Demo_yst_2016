package com.wjw.activity;

import com.example.ddddd.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class SubPageActivity extends FragmentActivity{
	public static final String CLASSFRAMENT = "CLASSFRAMENT";
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.AppTheme);
        setContentView(R.layout.ns_sub_main);
        
        Intent intent = getIntent();
       
        if(intent.getSerializableExtra(CLASSFRAMENT)!=null){
        	Class<? extends Fragment> classFrament = (Class<? extends Fragment>) intent.getSerializableExtra(CLASSFRAMENT);
	        changeFragment(classFrament);
        }
    }

    /**
     * �л���ͼ
     *
     * @param fragment
     */
    private void changeFragment(Class<? extends Fragment> classFrament) {
		try {
			Fragment fragment = classFrament.newInstance();
			fragment.setArguments(getIntent().getBundleExtra("bundle"));
			FragmentTransaction ft = SubPageActivity.this.getSupportFragmentManager().beginTransaction();
	        ft.replace(R.id.main, fragment);
	        ft.commitAllowingStateLoss();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    }

	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}
  
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main);
		fragment.onActivityResult(arg0, arg1, arg2);
	}
	
}