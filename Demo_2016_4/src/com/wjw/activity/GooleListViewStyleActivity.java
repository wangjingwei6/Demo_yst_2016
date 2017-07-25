/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import listviewanimations.ArrayAdapter;
import listviewanimations.itemmanipulation.OnDismissCallback;
import listviewanimations.itemmanipulation.SwipeDismissAdapter;
import listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import com.example.ddddd.R;
import com.wjw.util.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GooleListViewStyleActivity extends BaseActivity implements OnDismissCallback {

	private GoogleCardsAdapter mGoogleCardsAdapter;
	private static ArrayList<Integer> imgIds = new ArrayList<Integer>();

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_listviews_googlecards);
	}

	@Override
	public void initView() {

		ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);

		mGoogleCardsAdapter = new GoogleCardsAdapter(this);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter,
				this));
		swingBottomInAnimationAdapter.setAbsListView(listView);

		listView.setAdapter(swingBottomInAnimationAdapter);
		
			
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(GooleListViewStyleActivity.this, "This is card " + (position + 1), Toast.LENGTH_SHORT).show();
			}
		});

		mGoogleCardsAdapter.addAll(getItems());

	}

	private ArrayList<Integer> getItems() {
		for (int i = 0; i < 3; i++) {
			imgIds.add(R.drawable.img_nature1);
			imgIds.add(R.drawable.img_nature2);
			imgIds.add(R.drawable.img_nature3);
			imgIds.add(R.drawable.img_nature4);
			imgIds.add(R.drawable.img_nature5);
		}
		return imgIds;
	}

	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			mGoogleCardsAdapter.remove(position);
		}
	}

	private static class GoogleCardsAdapter extends ArrayAdapter<Integer> {

		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache;

		public GoogleCardsAdapter(Context context) {
			mContext = context;

			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);

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
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("Animation", "getView " + position);

			ViewHolder viewHolder;
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.activity_listviews_googlecards_card, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
				view.setTag(viewHolder);

				viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			viewHolder.textView.setText("This is card " + (getItem(position) + 1));
			setImageView(viewHolder, position);

			return view;
		}

		private void setImageView(ViewHolder viewHolder, int position) {
			Bitmap bitmap = getBitmapFromMemCache(imgIds.get(position));
			if (bitmap == null) {
				bitmap = BitmapUtils.readBitmap(mContext, imgIds.get(position));
				addBitmapToMemoryCache(imgIds.get(position), bitmap);
			}
			viewHolder.imageView.setImageBitmap(bitmap);
		}

		private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
			if (getBitmapFromMemCache(key) == null) {
				mMemoryCache.put(key, bitmap);
			}
		}

		private Bitmap getBitmapFromMemCache(int key) {
			return mMemoryCache.get(key);
		}

		private static class ViewHolder {
			TextView textView;
			ImageView imageView;
		}
	}

}
