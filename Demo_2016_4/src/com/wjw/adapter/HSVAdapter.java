package com.wjw.adapter;

import com.example.ddddd.R;
import com.wjw.util.BitmapUtils;
import com.wjw.util.ImageLoaderUtil;
import com.wjw.util.WindowUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 为画廊定义适配器
 * 
 * @author wangjingwei
 * 
 */
@SuppressLint("ViewHolder")
public class HSVAdapter extends BaseAdapter {

	public int[] imgIDs;
	private Context mContext;
	private Bitmap readBitmap;
	public HSVAdapter(Context context, int[] imgs) {
		this.mContext = context;
		this.imgIDs = imgs;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addObject(int[] imgs) {
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ImageHolder mImageHolder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.image_gallery_item, null);
			mImageHolder = new ImageHolder(convertView);
			convertView.setTag(mImageHolder);
		}
		mImageHolder = (ImageHolder) convertView.getTag();
		
//		ImageLoaderUtil.displayImage("", mImageHolder.image);
		
		Bitmap bitmap = BitmapUtils.readBitmap(mContext, imgIDs[position]);
		mImageHolder.image.setBackgroundDrawable(new BitmapDrawable(bitmap));
		return convertView;
	}

	class ImageHolder {
		private ImageView image;

		public ImageHolder(View view) {
			image = (ImageView) view.findViewById(R.id.img_item);
		}
	}

	public void recycle() {
		// 先判断是否已经回收
		if (readBitmap != null && !readBitmap.isRecycled()) {
			// 回收并且置为null
			readBitmap.recycle();
			readBitmap = null;
		}
		System.gc();
	}

}