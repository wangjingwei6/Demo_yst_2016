package com.wjw.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.ddddd.R;
import com.wjw.adapter.RecycleStaggeredAdapter;
import com.wjw.adapter.RecycleStaggeredAdapter.OnItemClickLitener;
import com.wjw.bean.RecycleViewBean;
import com.wjw.customview.CustomRecycleDecoration;
import com.wjw.customview.MProcessDialog;
import com.wjw.customview.MyCustomSwipeRefreshLayout;
import com.wjw.customview.MyCustomSwipeRefreshLayout.OnLoadListener;
import com.wjw.util.CustomAlterDialogUtil;
import com.wjw.util.CustomAlterDialogUtil.DialogClickListenner;
import com.wjw.util.WindowUtils;

public class RecycleViewActivity extends Activity {
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private RecyclerView recycler_view;
	private RecycleStaggeredAdapter recycleAdapter;
	private String[] urls = new String[] {
			"upload/AppIndex/20160503181701445293816.jpg",
			"upload/AppIndex/20160503181532820347948.jpg",
			"upload/AppIndex/20160503180108367907046.jpg",
			"upload/AppIndex/20160503175752976837926.jpg",
			"upload/AppIndex/20160503180450883763457.jpg",
			"upload/AppIndex/20160503200758508016122.jpg", };
	private List<RecycleViewBean> beans = new ArrayList<RecycleViewBean>();
	private CustomRecycleDecoration mCustomRecycleDecoration;
	private  boolean isLoadingMore = false;
	private int lastVisibleItem;
	private int scrollY;
	private LinearLayoutManager linearLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recycle_act);
		initRequestData();
		initView();
	}

	private void initRequestData() {
		for (int i = 0; i < urls.length; i++) {
			beans.add(new RecycleViewBean(urls[i]));
		}
	}

	private void addData(int type) {
		switch (type) {
		case 1: //刷新
			for (int i = 0; i < 2; i++) {
				beans.add(i,new RecycleViewBean(urls[i]));
			}
			break;
		case 2: //加载
			for (int i = 0; i < 5; i++) {
				beans.add(new RecycleViewBean(urls[i]));
			}
			
			break;
		default:
			break;
		}
		
	}

	private void initView() {
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.yellow,R.color.red, R.color.green);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mSwipeRefreshLayout.postDelayed(new Runnable() {

					@Override
					public void run() {
						mSwipeRefreshLayout.setRefreshing(false);
						addData(1);
						recycleAdapter.notifyDataSetChanged();
					}
				}, 3000);
			}
		});


		recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
		recycler_view.setHasFixedSize(true);
		// 设置item动画
		recycler_view.setItemAnimator(new DefaultItemAnimator());
//		mCustomRecycleDecoration = new CustomRecycleDecoration(16);
//		mCustomRecycleDecoration.Type = mCustomRecycleDecoration.LinearLayoutManager;
//		mCustomRecycleDecoration.Column = 3; // 列数
//		mCustomRecycleDecoration.Orientation = mCustomRecycleDecoration.VERTICAL; // 线性布局管理器时才设置,其余无意义
		recycler_view.addItemDecoration(mCustomRecycleDecoration);

		recycleAdapter = new RecycleStaggeredAdapter(beans, this);
		recycler_view.setAdapter(recycleAdapter);
		
		linearLayoutManager = new LinearLayoutManager(RecycleViewActivity.this, LinearLayoutManager.VERTICAL, false);
		 recycler_view.setLayoutManager(linearLayoutManager);
		//recycler_view.setLayoutManager(new GridLayoutManager(RecycleViewActivity.this, mCustomRecycleDecoration.Column,GridLayoutManager.VERTICAL, false));
		// recycler_view.setLayoutManager(new StaggeredGridLayoutManager(mCustomRecycleDecoration.Column,StaggeredGridLayoutManager.VERTICAL));
		 
		 
		 recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {

			private int botItemBottom;
			private int recyclerviewHeight;

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				  Log.i("WEBVIEW","scrollY = "+scrollY);
				
				 if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==recycleAdapter.getItemCount()&&botItemBottom==recyclerviewHeight) {
					 if(mCustomRecycleDecoration.Type == mCustomRecycleDecoration.LinearLayoutManager&&
								mCustomRecycleDecoration.Orientation == mCustomRecycleDecoration.VERTICAL){
						 Log.i("WEBVIEW","进来了");
						 
						 showInfoProgressDialog();
						 new Handler().postDelayed(new Runnable() {
							public void run() {
								if(isLoadingMore){
			                         Log.i("WEBVIEW","没有加载!");
			                    } else{
			                    	runOnUiThread(new Runnable() {
										@Override
										public void run() {
											dismissInfoProgressDialog();
											addData(2);
											recycleAdapter.notifyDataSetChanged();
											 isLoadingMore = false;
										}
									});
			                    }
							}
						}, 3000);
						 
					 }
				 }
			}
			 
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				scrollY = dy;
				lastVisibleItem = ((LinearLayoutManager) linearLayoutManager).findLastVisibleItemPosition();
				 
				 View topView = recyclerView.getChildAt(0);
				 View bottomView =recyclerView.getChildAt(recyclerView.getChildCount()-1);
				 
				 int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
				 int itemCount = linearLayoutManager.getItemCount();
				 recyclerviewHeight = recyclerView.getHeight();
				 int itemHeight = topView.getHeight();
				 
				 int topItemBottom = linearLayoutManager.getDecoratedBottom(topView); //顶部第一个item的底部高度渐变值  参照recycleview的顶部高度 
				 botItemBottom = linearLayoutManager.getDecoratedBottom(bottomView);
				 
//				 Log.i("WEBVIEW", "firstItemPosition=="+firstItemPosition);
//				 Log.i("WEBVIEW", "itemCount=="+itemCount);
//				 Log.i("WEBVIEW", "recyclerviewHeight=="+recyclerviewHeight);
//				 Log.i("WEBVIEW", "botItemBottom=="+itemHeight);
//				 Log.i("WEBVIEW", "topItemBottom=="+topItemBottom);
//				 Log.i("WEBVIEW", "botItemBottom=="+botItemBottom);
				 
				 
			}
		});
		 
		recycleAdapter.setOnItemClickLitener(new OnItemClickLitener() {

			@Override
			public void onItemLongClick(View view, final int position) {
				CustomAlterDialogUtil.remindUserDialog(RecycleViewActivity.this,"取消","确认删除","您确认要删除此条信息？",new DialogClickListenner() {
					
					@Override
					public void confirm() {
						deleteData(position);
					}
					
					@Override
					public void cancle() {
						
					}
				});
			}

			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(RecycleViewActivity.this, position + " 条被点击了！",
						2000).show();
				;
			}
		});
	}

	public void deleteData(int position) {
		recycleAdapter.notifyItemRemoved(position);
		beans.remove(position);
	}
	
	private MProcessDialog mInfoProgressDialog;

	public void showInfoProgressDialog() {
		if (mInfoProgressDialog == null) {
			mInfoProgressDialog = new MProcessDialog(this);
		}
		mInfoProgressDialog.setMessage("加载中");
		mInfoProgressDialog.setCancelable(false);
		mInfoProgressDialog.show();
	}

	public void dismissInfoProgressDialog() {
		if (mInfoProgressDialog != null && mInfoProgressDialog.isShowing()) {
			mInfoProgressDialog.dismiss();
		}
	}
}
