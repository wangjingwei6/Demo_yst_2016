package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.util.BitmapUtils;

import listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class GoogleExpandableListActivity extends Activity {

	private MyExpandableListAdapter mExpandableListAdapter;
	private static ArrayList<Integer> mExpandList = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listviews_googlecards);
		
		ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);

		mExpandableListAdapter = new MyExpandableListAdapter(this, getItems());
		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListAdapter);
		alphaInAnimationAdapter.setAbsListView(listView);
		listView.setAdapter(alphaInAnimationAdapter);

	}

	
	private ArrayList<Integer> getItems() {
		for (int i = 0; i < 6; i++) {
			mExpandList.add(R.drawable.img_nature1);
			mExpandList.add(R.drawable.img_nature2);
			mExpandList.add(R.drawable.img_nature3);
			mExpandList.add(R.drawable.img_nature4);
			mExpandList.add(R.drawable.img_nature5);
		}
		return mExpandList;
	}
	
	private static class MyExpandableListAdapter extends ExpandableListItemAdapter<Integer> {

		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache;

		/**
		 * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
		 * items == null.
		 */
		private MyExpandableListAdapter(Context context, List<Integer> items) {
			super(context, R.layout.activity_listviews_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
			mContext = context;

			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024/8);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory;
			mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(Integer key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}

		@Override
		public View getTitleView(int position, View convertView, ViewGroup parent) {
			TextView tv = (TextView) convertView;
			if (tv == null) {
				tv = new TextView(mContext);
			}
			tv.setText("this is card num "+position);
			return tv;
		}

		@Override
		public View getContentView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			
			setImageView(imageView,position);

			return imageView;
		}
		
		private void setImageView(ImageView imageView,int position) {
			Bitmap bitmap = getBitmapFromMemCache(mExpandList.get(position));
			if (bitmap == null) {
				bitmap = BitmapUtils.readBitmap(mContext, mExpandList.get(position));
				addBitmapToMemoryCache(mExpandList.get(position), bitmap);
			}
			imageView.setImageBitmap(bitmap);
		}

		private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
			if (getBitmapFromMemCache(key) == null) {
				mMemoryCache.put(key, bitmap);
			}
		}

		private Bitmap getBitmapFromMemCache(int key) {
			return mMemoryCache.get(key);
		}
	}
	

}
