package com.wjw.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.util.BitmapUtils;

import listviewanimations.ArrayAdapter;
import listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class GooleGridViewStyleActivity extends BaseActivity {

	private static ArrayList<Integer> imgs = new ArrayList<Integer>();
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_listviews_gridview);

		GridView gridView = (GridView) findViewById(R.id.activity_gridview_gv);
		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(new MyAdapter(this, getItems()));
		alphaInAnimationAdapter.setAbsListView(gridView);
		gridView.setAdapter(alphaInAnimationAdapter);

	}

	@Override
	public void initView() {
		
	}


	private ArrayList<Integer> getItems() {
		for (int i = 0; i < 9; i++) {
			imgs.add(R.drawable.img_nature1);
			imgs.add(R.drawable.img_nature2);
			imgs.add(R.drawable.img_nature3);
			imgs.add(R.drawable.img_nature4);
			imgs.add(R.drawable.img_nature5);
		}
		return imgs;
	}

	private static class MyAdapter extends ArrayAdapter<Integer> {

		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache;

		public MyAdapter(Context context, List<Integer> list) {
			super(list);
			mContext = context;
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			final int cacheSize = maxMemory;
			mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(Integer key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}

		private class ImageClickListener implements OnClickListener {

			private int mPosition;

			public ImageClickListener(int position) {
				mPosition = position;
			}

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, getItem(mPosition) + 1, Toast.LENGTH_SHORT).show();

			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			ImageView imageView = (ImageView) convertView;

			if (imageView == null) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER);
			}
			
			setImageView(imageView, position);

			return imageView;
		}

		private void setImageView(ImageView imageView, int position) {
			Bitmap bitmap = getBitmapFromMemCache(imgs.get(position));
			if (bitmap == null) {
				bitmap = BitmapUtils.readBitmap(mContext, imgs.get(position));
				addBitmapToMemoryCache(imgs.get(position), bitmap);
			}
			imageView.setImageBitmap(bitmap);
			imageView.setOnClickListener(new ImageClickListener(position));
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
