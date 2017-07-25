package com.wjw.application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjw.util.ImageLoaderUtil;

import android.app.Application;
import android.content.Context;

public class MyApplication  extends Application  {
	
	public static ImageLoader mImageLoader;
	public static Context applicationContext;
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		ImageLoaderUtil.initImageLoader(applicationContext);
	}
}
