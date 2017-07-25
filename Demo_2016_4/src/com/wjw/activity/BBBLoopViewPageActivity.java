package com.wjw.activity;

import android.app.Activity;

import com.example.ddddd.R;
import com.jack.jazzypager.JazzyViewPager;
import com.jack.jazzypager.OutlineContainer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


@SuppressLint("HandlerLeak")
public class BBBLoopViewPageActivity extends Activity {

    private JazzyViewPager mViewPager;
    private ViewGroup mPointsLl;
    
    private int initPosition = 0;
    private static int  LOOP_PAGE = 666;
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case 666:
				if(initPosition==0){
					mViewPager.setCurrentItem(mViewPager.getCurrentItem());
					initPosition = 1;
					mHandler.sendEmptyMessage(LOOP_PAGE);
				}else{
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
					mHandler.sendEmptyMessageDelayed(LOOP_PAGE, 2500);
				}
				
				break;

			default:
				break;
			}
    	};
    };

    private static final int[] imgIds = new int[]{R.drawable.a, R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbb_loopviewpager);
        
        mViewPager = (JazzyViewPager) findViewById(R.id.viewpager);
        mPointsLl = (ViewGroup) findViewById(R.id.points_ll);
        
        View view;
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(10, 10);
        params.gravity = Gravity.CENTER;
        for (int position = 0; position < imgIds.length; position++) {
            view = new View(this);
            view.setBackgroundResource(R.drawable.point_bg);
            view.setEnabled(false);

            if (position > 0) {
                params.leftMargin = 15;
            }

            mPointsLl.addView(view, params);
        }

        mPointsLl.getChildAt(0).setEnabled(true);;

        mViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.CubeOut);
        mViewPager.setFadeEnabled(true);
        mViewPager.setPageMargin(0);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
//                return view == object;

                if (view instanceof OutlineContainer) {
                    return ((OutlineContainer) view).getChildAt(0) == object;
                } else {
                    return view == object;
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(BBBLoopViewPageActivity.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setBackgroundResource(imgIds[position % imgIds.length]);
                container.addView(iv);
                mViewPager.setObjectForPosition(iv, position);
                return iv;
            }
        });
        
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            	Log.e("onPageSelected", "position : "+position);
            	
                View view;
                for (int i = 0; i < mPointsLl.getChildCount(); i++) {
                    view = mPointsLl.getChildAt(i);
                    if (i == position % imgIds.length) {
                        view.setEnabled(true);
                    } else {
                        view.setEnabled(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        
        int item = imgIds.length*2;
		mViewPager.setCurrentItem(item);
    }
    
   
    @Override
    protected void onResume() {
    	super.onResume();
    	mHandler.sendEmptyMessageDelayed(LOOP_PAGE, 2500);				
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mHandler.removeMessages(LOOP_PAGE);
    }
}
