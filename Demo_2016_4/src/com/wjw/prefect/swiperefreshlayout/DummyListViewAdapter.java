package com.wjw.prefect.swiperefreshlayout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by oliviergoutay on 1/23/15.
 */
public class DummyListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDummyStrings = new ArrayList<String>();
    private ImageLoader mImageLoader;

    public DummyListViewAdapter(Context mContext,List<String> urls) {
        this.mContext = mContext;
        this.mDummyStrings = urls;
        initImageLoader();
    }

    @Override
    public int getCount() {
        return mDummyStrings.size();
    }
    
    @Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    
    @Override
    public Object getItem(int position) {
        return mDummyStrings.get(position);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.recycle_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recycle_item_img = (ImageView) convertView.findViewById(R.id.recycle_item_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        mImageLoader.displayImage("http://yst-images.img-cn-hangzhou.aliyuncs.com/"+mDummyStrings.get(position), viewHolder.recycle_item_img);

        return convertView;
    }

    static class ViewHolder {
    	ImageView recycle_item_img;
    }
    
    private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.experience_default)
				.showImageOnFail(R.drawable.experience_default)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
		
	}

}
