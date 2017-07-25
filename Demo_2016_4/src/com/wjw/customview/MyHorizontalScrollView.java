package com.wjw.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
* @create date 2012-01-14
* @author 刘利涛    HorizontalScrollView
* @class description 自定义HorizontalScrollView实现更多的滑动效果
*/
public class MyHorizontalScrollView extends HorizontalScrollView{
	public MyHorizontalScrollView(Context context) {
		super(context);
	}
	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}
	public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 *  l 是当前View相对于母视图左上角X的偏移量
	 *  oldl 值是上一次X的偏移量
	 *  t 是当前View相对于屏幕左上角Y的偏移量
	 *  oldt 是上一次Y的偏移量
	 *  getScrollX: 当前View左上角对于父视图左上角的偏移量
	 *  
	 *  
	 *  getWidth : 当前ScrollView的宽度
	 *  view.getWidth : 当前嵌在ScrollView里 LinearLayout的宽度
	 *  view.getLeft():子View (LinearLayout)左边相对于父视图左边的距离
	 *  view.getRight():子View(LinearLayout)右边相对于父视图左边的距离
	 *  
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	        View view = (View) getChildAt(getChildCount()-1);
	        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
	       int leftMargin =  lp.leftMargin;
	       int rightMargin =  lp.rightMargin;
	        
	    	Log.e("CustomHorizontalScrollviewActivity", "l==getScrollX==  "+leftMargin+"=="+l+"=="+getScrollX()+"=="+view.getLeft()+"=="+view.getRight()+"=="+getWidth()+"===="+view.getWidth());
	        
	        if(view.getLeft()-leftMargin == getScrollX() ){ //view 滑动到最左边
	        	Log.i("CustomHorizontalScrollviewActivity", "在左边滑动");
	        	onScrollListener.onLeft();
	        	
	        }else if (view.getRight()-rightMargin == getScrollX()+getWidth()){//view 滑动到最右边
	        	Log.i("CustomHorizontalScrollviewActivity", "在右边滑动");
	        	onScrollListener.onRight();
	        	
	        }else{//view在中间滑动
	        	Log.i("CustomHorizontalScrollviewActivity", "在中间滑动");
	        	onScrollListener.onScroll();
	        }
	        
	        super.onScrollChanged(l, t, oldl, oldt);
	}
	    
	    /**
	     * 定义接口
	     * @author admin
	     */
	    public interface OnScrollListener1{
	    	void onRight();
	    	void onLeft();
	    	void onScroll();
	    }
	    private OnScrollListener1 onScrollListener;
	    public void setOnScrollListener(OnScrollListener1 onScrollListener){
	    	this.onScrollListener=onScrollListener;
	    }
}
