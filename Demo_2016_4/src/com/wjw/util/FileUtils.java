package com.wjw.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	public static final String SD_AbsolutePath = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
			? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";// 保存到SD卡
			
	public static String AppName = "WJWDemo2016";
	
	public static String SAVE_LONG_PATH = SD_AbsolutePath+"/"+AppName;
	public static String SAVE_SHORT_PATH = SD_AbsolutePath;
	
	public static String defaultFileName = System.currentTimeMillis()+".jpg";
			

	/****
	 * 获取SD卡中图片
	 * 
	 * @param filePath
	 */
	public static Bitmap getBitmap(String fileName) {
		File imagePic;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			imagePic = new File(fileName);
			if (imagePic.exists()) {
				return BitmapFactory.decodeFile(imagePic.getAbsolutePath());
			}
		}
		return null;
	}
	
	/**
	 * 保存图片到SD文件 ============= 未指定文件名的情况 默认使用拟定的 SD全路径 + 当前时间作为fileName
	 * @param bm
	 * @throws IOException
	 */
	public static void saveBitmap2File(Bitmap bm) throws IOException {
    	File foder = new File(SAVE_LONG_PATH);
    	if (!foder.exists()) {
    	foder.mkdirs();
    	}
    	File myTargetFile = new File(SAVE_LONG_PATH, defaultFileName);
    	if (!myTargetFile.exists()) {
    		myTargetFile.createNewFile();
    	}
    	FileOutputStream fOut =  new FileOutputStream(myTargetFile);  
    	bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    	fOut.flush();
    	fOut.close();
    	}
	
	/**
	 * 保存图片到SD文件 =============指定文件名的情况 使用拟定的 SD全路径 + 传进来的 fileName
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveBitmap2File(Bitmap bm, String fileName) throws IOException {
    	File foder = new File(SAVE_LONG_PATH);
    	if (!foder.exists()) {
    	foder.mkdirs();
    	}
    	File myTargetFile = new File(SAVE_LONG_PATH, fileName);
    	if (!myTargetFile.exists()) {
    		myTargetFile.createNewFile();
    	}
    	FileOutputStream fOut =  new FileOutputStream(myTargetFile);  
    	bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    	fOut.flush();
    	fOut.close();
    	}
	
	/**
	 * 保存图片到SD文件 =============指定文件名的情况 使用拟定的 SD全路径 + 传进来的 fileName
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveBitmap2File_AbsolutePath(Bitmap bm, String fileName) throws IOException {
    	File myTargetFile = new File(fileName);
    	if (!myTargetFile.exists()) {
    		myTargetFile.createNewFile();
    	}else{
    		Log.i("WEBVIEW", "saveBitmap2File_AbsolutePath");
    	}
    	FileOutputStream fOut =  new FileOutputStream(myTargetFile);  
    	bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    	fOut.flush();
    	fOut.close();
    	}
	
}
