package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.jack.jazzypager.JazzyViewPager;
import com.jack.jazzypager.OutlineContainer;
import com.jack.jazzypager.JazzyViewPager.TransitionEffect;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class AAALoopViewPageActivity extends Activity implements OnPageChangeListener {

    private List<ImageView> mListData;
    private String[] imageDescriptionArrays; // 图片描述数据
	private TextView tvImageDescription; // 图片描述
	private LinearLayout llPointGroup; // 点的组
	private int previousEnabledPosition; // 前一个被选中的点的索引
	private JazzyViewPager mViewPager;
	private boolean isStop;
	private int initPosition = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa_loopviewpager);
        
        initView();
        
        new Thread(new Runnable() {

			@Override
        	public void run() {
        		isStop = true;
        		while(isStop) {
        			System.out.println("正在切换中..");
        			
        			// 运行在ui(主)线程的任务
        			runOnUiThread(new Runnable() {
        				public void run() {
        					if(initPosition==0){
								mViewPager.setCurrentItem(mViewPager.getCurrentItem());
								initPosition=1;
							}else{
								mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
							}
        				};
        			});
        			
        			SystemClock.sleep(3000);
        		}
        	}
        }).start();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		isStop = false;
		
	}

	private void initView() {
		mViewPager = (JazzyViewPager) findViewById(R.id.viewpager);
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setFadeEnabled(true);
		mViewPager.setPageMargin(0);
		
		tvImageDescription = (TextView) findViewById(R.id.tv_image_description);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		
		initData();
		
		MyAdapter mAdapter = new MyAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		
		// 初始化默认选中的点和图片描述
		previousEnabledPosition = 0;
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(true);
		tvImageDescription.setText(imageDescriptionArrays[previousEnabledPosition]);

		// 滚动到Integer.MAX_VALUE / 2;
		int item = Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % mListData.size());
		
		mViewPager.setCurrentItem(item);
	}

	private void initData() {
		int[] imageResIDs = {
				R.drawable.a,
				R.drawable.b,
				R.drawable.c,
				R.drawable.d,
				R.drawable.e
		};
		
		imageDescriptionArrays = new String[] {
				"巩俐不低俗，我就不能低俗", 
				"扑树又回来啦！再唱经典老歌引万人大合唱", 
				"揭秘北京电影如何升级", 
				"乐视网TV版大派送", 
				"热血屌丝的反杀"
		};
		
		mListData = new ArrayList<ImageView>();
		
		ImageView iv;
		View v;
		LayoutParams params;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			mListData.add(iv);
			
			v = new View(this);
			v.setBackgroundResource(R.drawable.point_bg);
			params = new LayoutParams(8, 8);
			if(i != 0) {
				params.leftMargin = 10;
			}
			v.setLayoutParams(params);
			v.setEnabled(false);
			llPointGroup.addView(v);
		}
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * 是否复用拖动的对象
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
//			return arg0 == arg1;
			
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == object;
			} else {
				return view == object;
			}
		}

		/**
		 * 销毁对应position位置的view对象
		 * 
		 * container 就是当前适配器绑定的那个ViewPager对象.
		 * object 就是被移除的那个view对象
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object); //非3D
//			container.removeView(mViewPager.findViewFromObject(position));//3D效果
		}

		/**
		 * 加载对应position的页面的布局对象
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 1. 把集合中对应position位置的ImageView对象, 添加到ViewPager中
			ImageView iv = mListData.get(position % mListData.size());
			container.addView(iv);
//			mViewPager.setObjectForPosition(iv,position);
			
			// 2. 把集合中对应position位置的ImageView对象返回回去.
			return iv;
		}
	}

	/**
	 * 当页面滑动时
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	/**
	 * 当新的页面被选中时
	 */
	@Override
	public void onPageSelected(int position) {
//		System.out.println("页面: " + position);
		Log.e("onPageSelected","position : "+position);
		
		int newPosition = position % mListData.size();
		
		// 把之前的点给置为灰色, 把现在对应position位置的点, 置为白色.
		llPointGroup.getChildAt(previousEnabledPosition).setEnabled(false);
		llPointGroup.getChildAt(newPosition).setEnabled(true);
		
		previousEnabledPosition = newPosition; // 把当前的页面索引赋值给前一个索引变量.
		
		// 图片描述设置为position位置的数据
		tvImageDescription.setText(imageDescriptionArrays[newPosition]);
	}

	/**
	 * 页面滚动状态改变时
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
}
