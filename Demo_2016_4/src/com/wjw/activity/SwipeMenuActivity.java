package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ddddd.R;
import com.wjw.swipemeaulistview.SwipeMenu;
import com.wjw.swipemeaulistview.SwipeMenuCreator;
import com.wjw.swipemeaulistview.SwipeMenuItem;
import com.wjw.swipemeaulistview.SwipeMenuListView;
import com.wjw.swipemeaulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.wjw.util.ToastUtil;

public class SwipeMenuActivity extends Activity {

	private SwipeMenuListView swipelist;
	private List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
	private AppAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe);
		mAppList = getPackageManager().getInstalledApplications(0);
		swipelist = (SwipeMenuListView) findViewById(R.id.swipelist);

		initData();
		initSwipeList();
	}

	private void initData() {
		adapter = new AppAdapter();
		swipelist.setAdapter(adapter);
	}

	private void initSwipeList() {
		SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				SwipeMenuItem zhidingItem = new SwipeMenuItem(
						getApplicationContext());
				zhidingItem.setBackground(new ColorDrawable(Color
						.parseColor("#cccccc")));
				// set item width
				zhidingItem.setWidth(dp2px(72));

				zhidingItem.setTitle("置顶");
				zhidingItem.setTitleSize(18);
				zhidingItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(zhidingItem);

				SwipeMenuItem noReadItem = new SwipeMenuItem(
						getApplicationContext());
				noReadItem.setBackground(new ColorDrawable(Color
						.parseColor("#fd7400")));
				// set item width
				noReadItem.setWidth(dp2px(90));

				noReadItem.setTitle("望咩望");
				noReadItem.setTitleSize(18);
				noReadItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(noReadItem);

				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(72));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		// set creator
		swipelist.setMenuCreator(swipeMenuCreator);

		swipelist.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				ApplicationInfo item = mAppList.get(position);

				switch (index) {
				case 0:
					mAppList.remove(position);
					adapter.flushTop(mAppList, item);
					break;
				case 1:
					ToastUtil.MakeToast_Center(SwipeMenuActivity.this,
							"食屎啦你~~~~~~");
					break;
				case 2:
					mAppList.remove(position);
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void flushTop(List<ApplicationInfo> mAppList,ApplicationInfo item){
			// 集合删除选中那条数据（遍历）
//			for (ApplicationInfo applicationInfo : mAppList) {
//				if (applicationInfo.equals(item)) {
//					mAppList.remove(applicationInfo);
//				}
//			}
			// 添加到集合到首条并且刷新
			mAppList.add(0, item);
			adapter.notifyDataSetChanged();

		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.swipe_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
