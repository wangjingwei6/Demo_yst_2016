package com.wjw.prefect.swiperefreshlayout;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.adapter.RecycleStaggeredAdapter;
import com.wjw.adapter.RecycleStaggeredAdapter.OnItemClickLitener;
import com.wjw.bean.RecycleViewBean;
import com.wjw.customview.CustomRecycleDecoration;
import com.wjw.util.ToastUtil;
import com.wjw.util.CustomAlterDialogUtil;
import com.wjw.util.CustomAlterDialogUtil.DialogClickListenner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SwipeRefreshListViewActivity extends Activity implements
		SwipyRefreshLayout.OnRefreshListener {

	private ListView mListView;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private List<String> urls = new ArrayList<String>();
	private List<String> defaultUrls = new ArrayList<String>();
	
	private DummyListViewAdapter dummyListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		initLayout();
	}

	private void initData() {
		if(urls!=null){
			urls.clear();
			urls.add("upload/AppIndex/20160503181701445293816.jpg");
			urls.add("upload/AppIndex/20160503180108367907046.jpg");
			urls.add("upload/AppIndex/20160503181532820347948.jpg");
			urls.add("upload/AppIndex/20160503175752976837926.jpg");
			urls.add("upload/AppIndex/20160503180450883763457.jpg");
			urls.add("upload/AppIndex/20160503200758508016122.jpg");
			
			defaultUrls.addAll(urls);
		}
	}

	private void addData(int count, int type) {

		switch (type) {
		case 1: // 下拉刷新
			urls.clear();
			for (int i = 0; i < count; i++) {
				urls.add("upload/AppIndex/20160503181532820347948.jpg");
			}

			break;
		case 2:// 上啦加载
			for (int i = 0; i < count; i++) {
				urls.add(defaultUrls.get(i));
			}
			break;

		default:
			break;
		}
	}

	private void initLayout() {
		mListView = (ListView) findViewById(R.id.listview);
		dummyListViewAdapter = new DummyListViewAdapter(this, urls);
		mListView.setAdapter(dummyListViewAdapter);

		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setColorSchemeResources(R.color.blue, R.color.yellow,R.color.red, R.color.green);
		mSwipyRefreshLayout.setOnRefreshListener(this);

	}

	/**
	 * Called when the
	 * {@link com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout}
	 * is in refresh mode. Just for example purpose.
	 */
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		Log.d("SwipeRefreshListViewActivity", "Refresh triggered at "
				+ (direction == SwipyRefreshLayoutDirection.TOP ? "top"
						: "bottom"));

		if (direction == SwipyRefreshLayoutDirection.TOP) {// 下拉刷新
			addData(4, 1);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					SwipeRefreshListViewActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									dummyListViewAdapter.notifyDataSetChanged();
									mSwipyRefreshLayout.setRefreshing(false);
									ToastUtil.MakeToast_Center(SwipeRefreshListViewActivity.this,"刷新完成");
								}
							});
				}
			}, 2500);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {// 上啦加载
			addData(4, 2);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					SwipeRefreshListViewActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									dummyListViewAdapter.notifyDataSetChanged();
									mSwipyRefreshLayout.setRefreshing(false);
									ToastUtil.MakeToast_Center(SwipeRefreshListViewActivity.this,"加载完成");
								}
							});
				}
			}, 3000);
		}
	}
}
