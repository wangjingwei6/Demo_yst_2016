package com.wjw.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

public class HttpURLConnectionUtil {

	public interface CallBack {
		void onRequestComplete(String result);
	}

	/**
	 * 异步的Get请求
	 * 
	 * @param urlStr
	 * @param callBack
	 */
	public static void doGetAsyn(final String urlStr, final CallBack callBack, final Map<String, String> params) {
		new Thread() {
			public void run() {
				try {
					String result = sendGET(urlStr, params);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	/**
	 * 异步的Post请求
	 * 
	 * @param urlStr
	 * @param params
	 * @param callBack
	 * @throws Exception
	 */
	public static void doPostAsyn(final String urlStr, final Map<String, String> params, final CallBack callBack) throws Exception {
		new Thread() {
			public void run() {
				try {
					String result = sendPOST(urlStr, params);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();

	}

	/**
	 * GET方式 附带图片数据，上传提交信息
	 * 
	 * @param url
	 * @param params
	 *            请求所需参数
	 * @return 响应数据
	 */
	public static String sendGET(String url, Map<String, String> params) {
		String mUrl = null;
		StringBuilder sb = new StringBuilder(url);
		if (params != null && !params.isEmpty()) {
			sb.append("?");
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);

			mUrl = sb.toString();

		} else {

			mUrl = url;
		}

		HttpURLConnection httpConn = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			URL urlObj = new URL(mUrl);
			httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setReadTimeout(8 * 1000);
			httpConn.setConnectTimeout(8 * 1000);
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Accept-Charset", "utf-8");
			httpConn.setRequestProperty("contentType", "utf-8");
			httpConn.connect();
			int responseCode = httpConn.getResponseCode();
			Log.e("HttpURLConnectionUtil", "responseCode: " + responseCode);
			if (responseCode == 200) {
				InputStream is = httpConn.getInputStream();
				String result = IOHelper.streamToString(is, "utf-8");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * POST方式 附带图片数据，上传提交信息
	 * 
	 * @return 响应数据
	 */
	public static String sendPOST(String url, Map<String, String> params) {

		StringBuilder sb = new StringBuilder(url);
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		HttpURLConnection httpConn = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			URL urlObj = new URL(url);
			httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setReadTimeout(8 * 1000);
			httpConn.setConnectTimeout(8 * 1000);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Accept-Charset", "utf-8");
			httpConn.setRequestProperty("contentType", "utf-8");
			// post请求的参数
			OutputStream out = httpConn.getOutputStream();
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();
			httpConn.connect();
			int responseCode = httpConn.getResponseCode();
			Log.e("HttpURLConnectionUtil", "responseCode: " + responseCode);
			if (responseCode == 200) {
				InputStream is = httpConn.getInputStream();
				String result = IOHelper.streamToString(is, "utf-8");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 上传文件(图片等等)
	 * 
	 * @param imagePath
	 * @param imageName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String uploadFile(String uploadUrl, String imagePath, String imageName) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"urlfile\";filename=\"" + imageName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			File files = new File(imagePath);
			if (files.exists()) {
				Log.e("HttpURLConnectionUtil", "the file is === " + imagePath);
				Log.e("HttpURLConnectionUtil", "the file is === " + imageName);
			}
			FileInputStream fStream = new FileInputStream(files);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			Log.e("HttpURLConnectionUtil", "ds to string is === " + ds.toString());
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			Log.e("HttpURLConnectionUtil", "" + "上传结果 ==== " + b.toString().trim());
			ds.close();
			return b.toString().trim();
		} catch (Exception e) {
			Log.e("HttpURLConnectionUtil", "" + "上传失败 ======= " + e.getMessage());
			return null;
		}
	}
}
