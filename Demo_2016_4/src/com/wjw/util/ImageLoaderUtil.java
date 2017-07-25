package com.wjw.util;

import com.example.ddddd.BuildConfig;
import com.example.ddddd.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoaderUtil {

	private static ImageLoader mImageLoader = null;
	private static final int MAX_DISK_CACHE = 1024 * 1024 * 50;
	private static final int MAX_MEMORY_CACHE = 1024 * 1024 * 10;

	/**
	 * 是否显示日志
	 */
	private static boolean isShowLog = false;

	/**
	 * 单例构建
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {

		if (mImageLoader == null) {
			synchronized (ImageLoaderUtil.class) {
				mImageLoader = ImageLoader.getInstance();
			}
		}
		return mImageLoader;
	}

	/**
	 * 在Application中初始化参数
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder build = new ImageLoaderConfiguration.Builder(context);
		build.tasksProcessingOrder(QueueProcessingType.LIFO);
		build.diskCacheSize(MAX_DISK_CACHE);
		build.memoryCacheSize(MAX_MEMORY_CACHE);
		build.memoryCache(new LruMemoryCache(MAX_MEMORY_CACHE));
		if (BuildConfig.DEBUG && isShowLog) {
			build.writeDebugLogs();
		}
		getImageLoader().init(build.build());
	}

	/**
     * 自定义Option
     */
    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
    	mImageLoader.displayImage(url, target, options);
    }
	
	public static void displayImage( String url,ImageView imageView) {
		mImageLoader.displayImage(url, imageView, getDefaultOptions(), null);
	}

	public static void displayImage( String url,ImageView imageView, ImageLoadingListener listener) {
		mImageLoader.displayImage(url, imageView, getDefaultOptions(), listener);
	}
	
	  /**
     * 图片列表页专用
     */
    public static void displayImageList(String url, ImageView target, int loadingResource) {
    	mImageLoader.displayImage(url, target, getOptions4PictureList(loadingResource));
    }
	
	  /**
     * 图片列表页专用
     */
    public static void displayImageList(String url, ImageView target, int loadingResource, SimpleImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
    	mImageLoader.displayImage(url, target, getOptions4PictureList(loadingResource), loadingListener, progressListener);
    }
	
    /**
     * 图片详情页专用
     */
    public static void displayImage4Detail(String url, ImageView target, SimpleImageLoadingListener loadingListener) {
    	mImageLoader.displayImage(url, target, getOption4ExactlyType(), loadingListener);
    }
    
	/**
	 * 头像专用
	 */
	public static void displayHeadIcon(String url, ImageView target) {
		mImageLoader.displayImage(url, target, getOptions4Header());

	}
	
	
	/**
	 * 默认Options
	 *
	 * @return
	 */
	public static DisplayImageOptions getDefaultOptions() {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.ic_gf_default_photo)
				.showImageOnFail(R.drawable.ic_gf_default_photo)
				.showImageOnLoading(R.drawable.ic_gf_default_photo).build();
	}


	/**
	 * 加载头像专用Options，默认加载中、失败和空url为 ic_loading_small
	 *
	 * @return
	 */
	public static DisplayImageOptions getOptions4Header() {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).build();
	}

	/**
	 * 加载图片列表专用，加载前会重置View
	 * {@link com.nostra13.universalimageloader.core.DisplayImageOptions.Builder#resetViewBeforeLoading}
	 * = true
	 *
	 * @param loadingResource
	 * @return
	 */
	public static DisplayImageOptions getOptions4PictureList(int loadingResource) {
		return new DisplayImageOptions.Builder()
				   .cacheInMemory(true).cacheOnDisk(true)
				   .bitmapConfig(Bitmap.Config.RGB_565)
				   .resetViewBeforeLoading(true)
				   .showImageOnLoading(loadingResource)
				   .showImageForEmptyUri(loadingResource)
				   .showImageOnFail(loadingResource).build();
	}
	
	
    /**
     * 设置图片放缩类型为模式EXACTLY，用于图片详情页的缩放
     *
     * @return
     */
    public static DisplayImageOptions getOption4ExactlyType() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

	// ============================================================================

}
