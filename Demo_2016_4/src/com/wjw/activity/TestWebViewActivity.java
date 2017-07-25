package com.wjw.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ddddd.R;
import com.wjw.bean.Contact;
import com.wjw.util.FileUtils;
import com.wjw.util.SafeUtil;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class TestWebViewActivity extends Activity {

	private Button android_invoke_js_insertform; // 调用JS方法插入表格内容
	private Button android_invoke_js_setParams; // 调用JS方法传入一段内容
	private WebView mWebView;
	private ProgressBar mProgressBar = null;
	private ImageView img;

	private ArrayList<String> loadHistoryUrls = new ArrayList<String>();
	private String FIRST_HTML = "file:///android_asset/phoneui.html";

	private String imageFilePath;
	private Uri photoUri;
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_CAMERA = 2;
	private static final int REQUEST_PICKED = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		initProgressbar();
		initView();

	}

	private void initProgressbar() {
		mProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void initView() {

		android_invoke_js_insertform = (Button) findViewById(R.id.android_invoke_js_insertform);
		android_invoke_js_setParams = (Button) findViewById(R.id.android_invoke_js_setParams);

		img = (ImageView) findViewById(R.id.img);

		mWebView = (WebView) findViewById(R.id.webview_act);
		WebSettings settings = mWebView.getSettings();

		settings.setJavaScriptEnabled(true);// 支持javaScript
		settings.setSaveFormData(false);// 不保存表单
		settings.setSupportZoom(false);// 不支持缩放
		settings.setBuiltInZoomControls(false);// 不显示缩放按钮
		settings.setSavePassword(false);// 不保存密码
		settings.setUseWideViewPort(true);// 自适应屏幕
		settings.setLoadWithOverviewMode(true);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				loadHistoryUrls.add(url);
				return true;
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(TestWebViewActivity.this,
						"onJsAlert *** " + message + " URL " + url, 2000)
						.show();
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				Log.i("WEBVIEW", "onProgressChanged");

				if (mProgressBar != null) {
					if (newProgress == 100) {
						mProgressBar.setVisibility(View.GONE);
					} else {
						if (View.INVISIBLE == mProgressBar.getVisibility()) {
							mProgressBar.setVisibility(View.VISIBLE);
						}
						mProgressBar.setProgress(newProgress);
					}
				}
				super.onProgressChanged(view, newProgress);
			}

		});
		mWebView.loadUrl(FIRST_HTML);
		loadHistoryUrls.add(FIRST_HTML);
		// mWebView.loadUrl("https://www.baidu.com");

		mWebView.addJavascriptInterface(new SharpJavaScript(), "sharp");

		android_invoke_js_insertform.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(TestWebViewActivity.this,
						"AndroidCallJS---插入内容到表格", Toast.LENGTH_SHORT).show();

				String json = buildJson(getContacts());
				mWebView.loadUrl("javascript:show('" + json + "')");
			}
		});

		android_invoke_js_setParams.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// int num = (int) (Math.random() * 100 + 1);
				// Toast.makeText(TestWebViewActivity.this,
				// "AndroidCallJS---传入一段内容", Toast.LENGTH_SHORT).show();
				// mWebView.loadUrl("javascript:invokedByJava('这是Android传进去的参数  ===='+'"
				// + num + "')");
				photo();

			}

		});

	}

	public List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact(1, "张三", "5554"));
		contacts.add(new Contact(2, "李四", "5555"));
		contacts.add(new Contact(3, "王五", "5556"));
		contacts.add(new Contact(4, "police", "110"));

		return contacts;
	}

	public String buildJson(List<Contact> contacts) {
		JSONArray jsonArray = new JSONArray();

		for (Contact contact : contacts) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("id", contact.getId());
				jsonObject.put("name", contact.getName());
				jsonObject.put("phone", contact.getPhone());
				jsonArray.put(jsonObject);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray.toString();
	}
	
	
	private void photo() {
		Toast.makeText(TestWebViewActivity.this, "JsCallAndroid----photo",
				Toast.LENGTH_SHORT).show();
		String endfileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date()) + ".jpg";
		imageFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File imgFile = new File(imageFilePath + "/" + endfileName);
		Log.i("WEBVIEW", "imgFile = " + imgFile.getPath());
		photoUri = Uri.fromFile(imgFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}
	

	/****
	 * JAVA 和 JS 交互的 接口对象
	 * 
	 * @author Administrator
	 *
	 */
	class SharpJavaScript {

		public void contactlist() {
			Toast.makeText(TestWebViewActivity.this,
					"JsCallAndroid----contactlist", Toast.LENGTH_SHORT).show();
			try {
				String json = buildJson(getContacts());
				mWebView.loadUrl("javascript:show(" + json + ")");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void takePhoto() {
			Toast.makeText(TestWebViewActivity.this, "JsCallAndroid----photo",
					Toast.LENGTH_SHORT).show();
			String endfileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
					.format(new Date()) + ".jpg";
			imageFilePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File imgFile = new File(imageFilePath + "/", endfileName);
			Log.i("WEBVIEW", "imgFile = " + imgFile.getPath());
			photoUri = Uri.fromFile(imgFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, REQUEST_CAMERA);
		}

		public void call(String phone) {
			Toast.makeText(TestWebViewActivity.this, "JsCallAndroid----call",
					Toast.LENGTH_SHORT).show();
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone)));

		}

		// 显示通知的方法
		public void showNotification() {
			Toast.makeText(TestWebViewActivity.this,
					"JsCallAndroid----showNotification 您有新的通知请看桌面",
					Toast.LENGTH_SHORT).show();

			NotificationManager manager = (NotificationManager) TestWebViewActivity.this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			PendingIntent pendIntent = PendingIntent.getActivity(
					TestWebViewActivity.this, 0, new Intent(
							TestWebViewActivity.this, SecondActivity.class),
					PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					TestWebViewActivity.this);
			builder.setContentTitle("Demo_2016")
					.setSmallIcon(R.drawable.charge_zhifubao_icon)
					.setLargeIcon(
							BitmapFactory.decodeResource(getResources(),
									R.drawable.charge_zhifubao_icon))
					.setContentText("您有新的消息，请注意查收")
					.setPriority(Notification.PRIORITY_DEFAULT)
					.setAutoCancel(true).setContentIntent(pendIntent);

			Notification notification = builder.build();
			notification.when = System.currentTimeMillis();
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;
			manager.notify(666, notification);
		}

		// js 传递android参数显示土司
		@JavascriptInterface
		public void jsInvokeAndoidShowToast(final String params) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(TestWebViewActivity.this,
							"JsCallAndroid=====" + params, 2000).show();
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							mWebView.loadUrl("http://www.sohu.com");
						}
					}, 100);
				}
			});
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 判断是否可以返回操作
		if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			if (loadHistoryUrls.size() > 1
					&& !loadHistoryUrls.get(loadHistoryUrls.size() - 1)
							.toString().contains(".html")) {
				Log.i("WEBVIEW", "onKeyDown===loadHistoryUrls.size() > 1");

				// 移除加载栈中的最后链接
				loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls
						.size() - 1));

				// 加载重定向之前的页
				mWebView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
			} else {
				Log.i("WEBVIEW", "onKeyDown===finish()");
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		/****
		 * @author wangjingwei
		 *   调用系统的拍照  两种情况  
		 *   >>>>> 1   调用系统拍照intent  携带参数 调用系统拍照intentintent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)  
		 *            此时返回的 intent==null
		 *            需要手动去赋值URI。从而获取图片 (Uri uri = Uri.fromFile(文件对象)) 
		 *   
		 *   >>>>> 2  调用系统拍照intent  没有携带参数  
		 *           此时返回的 intent 不为空。
		 *           获取返回的数据有两种 :
		 *   							(1)URI uri = intent.getData()
		 *   							(2)Bitmap bitmap = (Bitmap)intent.getExtra.get("data")
		 */
		switch (requestCode) {
		case REQUEST_CAMERA:
			if (resultCode == Activity.RESULT_OK) {

				Uri uri = null;
				if (intent != null && intent.getData() != null) {
					uri = intent.getData();
				}
				// 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
				if (uri == null) {
					uri = photoUri;
				}
				doCropPhoto(uri);
			}

			break;
		case REQUEST_PICKED:
			if (resultCode == Activity.RESULT_OK) {

				final Bitmap photo = intent.getParcelableExtra("data");
				final String bitmapToBase64 = SafeUtil.bitmapToBase64(photo);
				try {
					FileUtils.saveBitmap2File_AbsolutePath(photo, imageFilePath);
					// 保存后开启广播刷新操作
					File myTargetFile = new File(imageFilePath);
					Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri uri = Uri.fromFile(myTargetFile);
					intent.setData(uri);
					this.sendBroadcast(intent);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(photo!=null){
						photo.recycle();
					}
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//设置图片显示
							Bitmap base64ToBitmap = SafeUtil.base64ToBitmap(bitmapToBase64);
							img.setImageBitmap(base64ToBitmap);
						}
					});
				}
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 截取图片 o
	 * 
	 * @param uri
	 */
	public void doCropPhoto(Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 120);
			intent.putExtra("outputY", 120);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, REQUEST_PICKED);

		} catch (android.content.ActivityNotFoundException e) {
			return;
		}
	}
}
