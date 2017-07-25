package com.wjw.customviewpager;

import java.util.List;

import com.example.ddddd.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomViewpagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;// 绘制三角形需要的path 线
	private int mTriangleWidth; // 三角形宽度
	private int mTriangleHeight;// 三角形高度
	private static final float RADIO_TRIANGLE_WIDTH = 1 / 6f; // 默认三角形比例
	private final float DIMENSION_TRIANGLE_WIDTH_MAX = (int) getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH; // 三角形底边最大宽度

	private int mInitTrnslationX;// 初始化的偏移位置
	private int mTranslationX; // 移动时的偏移位置

	private int mTabVisibleCount; // 可见tab数量
	private static final int COUNT_DEFAULT_TAB = 4;

	public List<String> mTitles;

	private static final int COLOR_TEXT_NORMAL = 0X77FFFFFF; // 正常颜色
	private static final int COLOR_TEXT_HIGH = 0XFFFFFFFF; // 选中颜色

	public CustomViewpagerIndicator(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public CustomViewpagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 获取可见Ttab的数量
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPagerIndicator);

		mTabVisibleCount = a.getInt(R.styleable.MyViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
		if (mTabVisibleCount < 0) {
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		}
		a.recycle();

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffffff"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	/**
	 * 当xml文件加载文件完成时候回调
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		// ==============================鸡肋 有问题 没有值
		int cCount = getChildCount();
		Log.e("VIEWPAGER", "===" + cCount);
		if (cCount == 0)
			return;

		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}

		setItemClickEvent();

	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @return
	 */
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return outMetrics.widthPixels;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH); // 三角形底边宽度
		mTriangleWidth = (int) Math.min(mTriangleWidth, DIMENSION_TRIANGLE_WIDTH_MAX);
		mInitTrnslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2; // 三角形初始化位置

		initTriangle();

	}

	/**
	 * 初始化三角形
	 */
	private void initTriangle() {

		mTriangleHeight = mTriangleWidth / 2;

		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
		mPath.close();

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();

		canvas.translate(mInitTrnslationX + mTranslationX, getHeight() + 3);
		canvas.drawPath(mPath, mPaint);

		canvas.restore();

		super.dispatchDraw(canvas);
	}

	/**
	 * 指示器跟随手指移动
	 * 
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset) {
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (offset + position));

			// 容器移动，在tab处于移动最后一个时
			if (position >= (mTabVisibleCount - 2) && offset > 0 && getChildCount() > mTabVisibleCount && position < (getChildCount() - 2)) {
				if (mTabVisibleCount != 1) {
					this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * offset), 0);
				} else {
					this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
				}
			}

		invalidate();
	}

	/**
	 * 设置可见的Tab数量
	 * 
	 * @param count
	 */
	public void setVisibleTabCount(int count) {
		mTabVisibleCount = count;
	}

	public void setTabItemTitles(List<String> titles) {

		if (titles != null && titles.size() > 0) {

			this.removeAllViews();

			mTitles = titles;

			for (String title : mTitles) {
				addView(generateTextView(title));
			}
		}

		setItemClickEvent();

	}

	/**
	 * 根据title 创建Tab
	 * 
	 * @param title
	 * @return
	 */
	private View generateTextView(String title) {

		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setLayoutParams(lp);
		return tv;
	}

	private ViewPager mViewPager;

	public PageOnChangeListener mListener;

	public void setOnPageOnChangeListener(PageOnChangeListener listener) {
		this.mListener = listener;
	}

	public interface PageOnChangeListener {
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);

	}

	/**
	 * 设置关联的ViewPager
	 * 
	 * @param viewpager
	 * @param pos
	 */
	@SuppressWarnings("deprecation")
	public void setViewPager(ViewPager viewpager, final int pos) {
		mViewPager = viewpager;

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				highLightTextView(position);
				if (mListener != null) {
					mListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				scroll(position, positionOffset);
				if (mListener != null) {
					mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (mListener != null) {
					mListener.onPageScrollStateChanged(arg0);
				}
			}
		});

		mViewPager.setCurrentItem(pos);
		highLightTextView(pos);

	}

	/**
	 * 重置所有Tab颜色
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	/**
	 * 设置选中高亮颜色
	 * 
	 * @param pos
	 */
	private void highLightTextView(int pos) {
		resetTextViewColor();
		View view = getChildAt(pos);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGH);
		}
	}

	/**
	 * 设置Tab的点击事件
	 * 
	 */
	private void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;

			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

}
