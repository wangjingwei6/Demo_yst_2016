package com.wjw.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;

@SuppressWarnings({ "unused", "deprecation" })
public class BitmapUtils {
	
	/**
	 * 省内存方式从本地读取图片 ，避免OOM,并且压缩。
	 */

	public static Bitmap readBitmap(Context context, int resId) {

		InputStream is = context.getResources().openRawResource(resId);
		ByteArrayOutputStream baos = null;
		
		byte[] buffer = new byte[1024 * 8];
		int len = 0;
		try {
			baos = new ByteArrayOutputStream();
			while((len=is.read(buffer))!=-1){
				baos.write(buffer,0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 不返回实际bitmap。不分配内存空间 但是可以得到一些图片信息
		
		double size = baos.toByteArray().length/102400.0;
		if(size>1){
			options.inSampleSize = (int) size;
		}else{
			options.inSampleSize = 1;
		}

		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true; // 让系统能及时回收内存
		options.inInputShareable = true; // 让系统能及时回收内存

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

		return bitmap;
	}

	/**
	 * 质量压缩图片(根据图片压缩)
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				options -= 5;// 每次都减少5
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题 <br>
	 * 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
	 * 
	 * @param bts
	 * @param maxBytes
	 *            压缩后的图像最大大小 单位为byte
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap bitmap, long maxBytes) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 90;
			while (baos.toByteArray().length > maxBytes) {
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 10;
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap mBitmap = BitmapFactory.decodeStream(isBm, null, null);
			return mBitmap;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 图片按比例大小压缩图片（根据路径获取图片并压缩)
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getImageFromFile(String srcPath, Activity activity) {

		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		int screenWidth = mDisplay.getWidth();
		int screenHeight = mDisplay.getHeight();

		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

			int w = newOpts.outWidth;
			int h = newOpts.outHeight;

			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > screenWidth) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) Math.ceil(newOpts.outWidth / screenWidth);
			} else if (w < h && h > screenHeight) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) Math.ceil(newOpts.outHeight / screenHeight);
			}
			if (be <= 0)
				be = 1;

			newOpts.inSampleSize = be;// 设置缩放比例

			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			newOpts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 图片按比例大小压缩图片（根据Bitmap图片压缩）：
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap getImageFromBitmap(Bitmap image, Activity activity) {
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		int screenWidth = mDisplay.getWidth();
		int screenHeight = mDisplay.getHeight();

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;

			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > screenWidth) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) Math.ceil(newOpts.outWidth / screenWidth);
			} else if (w < h && h > screenHeight) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) Math.ceil(newOpts.outHeight / screenHeight);
			}
			if (be <= 0)
				be = 1;

			newOpts.inSampleSize = be;// 设置缩放比例

			newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			newOpts.inJustDecodeBounds = false;
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/** 
     * drawable转bitmap 
     *  
     * @param drawable 
     * @return 
     */  
    private Bitmap drawableToBitamp(Drawable drawable)  
    {  
        if (drawable instanceof BitmapDrawable)  
        {  
            BitmapDrawable bd = (BitmapDrawable) drawable;  
            return bd.getBitmap();  
        }  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        drawable.draw(canvas);  
        return bitmap;  
    }  
	
	
	
	
	

}
