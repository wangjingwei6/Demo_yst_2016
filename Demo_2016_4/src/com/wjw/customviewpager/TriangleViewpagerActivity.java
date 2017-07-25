package com.wjw.customviewpager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.activity.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class TriangleViewpagerActivity extends BaseActivity {
	
	private ViewPager mViewPager;
	private CustomViewpagerIndicator mIndicator;
	private List<String>mTitles = Arrays.asList("视频","新闻","段子","体育","标题1","标题2","标题3","标题4","标题5");
	private List<NewInstanceSimpleFragment>mContents = new ArrayList<NewInstanceSimpleFragment>();
	private FragmentPagerAdapter mAdapter;
	
	
	@Override
	public void setContentView() {
		setContentView(R.layout.customviewpager_triangle_act);
	}

	@Override
	public void initView() {
		mViewPager 	= (ViewPager)findViewById(R.id.id_viewpager);
		mIndicator  = (CustomViewpagerIndicator)findViewById(R.id.id_indicator);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDatas();
		
		mIndicator.setVisibleTabCount(4);
		mIndicator.setTabItemTitles(mTitles);
		
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager,0);
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() { 
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	private void initDatas() {
		
		for(String title:mTitles){
			NewInstanceSimpleFragment fragment = NewInstanceSimpleFragment.newInstance(title);
			mContents.add(fragment);
		}
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mContents.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return mContents.get(position);
			}
		};
		
	}

}
