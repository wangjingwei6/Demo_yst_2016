package com.wjw.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

public class SafeUtil {

	/**
	 * Base64数据加密 不能有特殊字符
	 * 
	 * @param text
	 * @return
	 */
	public static String stringEncode(String text) {
		if (TextUtils.isEmpty(text)) {
			return "";
		}
		String encode = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encode.length(); i++) {
			char oldChar = encode.charAt(i);
			char newChar = (char) (oldChar * 2 + 1);
			sb.append(newChar);
		}
		return sb.toString();
	}

	/**
	 * Base64数据解密 不能有特殊字符
	 * 
	 * @param text
	 * @return
	 */
	public static String stringDecode(String text) {
		if (TextUtils.isEmpty(text)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.toString().length(); i++) {
			char oldChar = text.toString().charAt(i);
			char newChar = (char) ((oldChar - 1) / 2);
			sb.append(newChar);
		}
		return new String(Base64.decode(sb.toString(), Base64.DEFAULT));

	}
	
	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	
	/**
	 * MD5加密
	 * @param plainText
	 * @return
	 */
	public static String toMD5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
