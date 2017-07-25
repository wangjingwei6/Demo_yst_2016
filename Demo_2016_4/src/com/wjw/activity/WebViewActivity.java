package com.wjw.activity;

import com.example.ddddd.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

	WebView webView;

	// 用来展示商品详情的头标题
	private RelativeLayout web_view_title_layout;
	private ImageView web_view_back_iv;
	private ProgressBar mProgressBar = null;

	private SwipeRefreshLayout id_swipe_ly;
	private String url = "http://www.51ltx.com/service/GetHtml.aspx?Type=IndexInfo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.web_view);
		id_swipe_ly = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		id_swipe_ly.setColorSchemeResources(R.color.blue, R.color.yellow,
				R.color.red, R.color.green);
		id_swipe_ly.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				id_swipe_ly.postDelayed(new Runnable() {

					@Override
					public void run() {
						webView.loadUrl(url);
						id_swipe_ly.setRefreshing(false);
					}
				}, 3000);

			}
		});

		// if(getIntent().getIntExtra("isShowTitle", 0) == 1) {
		// initTitle();
		// }
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		
		webView.loadUrl("http://www.51ltx.com/service/GetHtml.aspx?Type=IndexInfo");
		
		// 点击链接继续在当前browser中响应
		webView.setWebViewClient(new WebViewClient() {

			// 让新打开的网页在当前的webview中显示
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;

			}

			// 网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if (!id_swipe_ly.isRefreshing()) {
					id_swipe_ly.setRefreshing(true);
				}
			}

			// 网页加载完毕
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				id_swipe_ly.setRefreshing(false);
			}
		});

		// String url = this.getIntent().getStringExtra("url");
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				// if(mProgressBar != null) {
				// if (newProgress == 100) {
				// mProgressBar.setVisibility(View.GONE);
				// } else {
				// if (View.INVISIBLE == mProgressBar.getVisibility()) {
				// mProgressBar.setVisibility(View.VISIBLE);
				// }
				// mProgressBar.setProgress(newProgress);
				// }
				// }
				WebViewActivity.this.setProgress(newProgress * 100);
			}
		});

	}

	// 初始化标题栏
	private void initTitle() {
		web_view_title_layout = (RelativeLayout) findViewById(R.id.web_view_title_layout);
		web_view_title_layout.setVisibility(View.VISIBLE);
		web_view_back_iv = (ImageView) findViewById(R.id.web_view_back_iv);
		web_view_back_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebViewActivity.this.finish();
			}
		});
		// mProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
		// mProgressBar.setVisibility(View.VISIBLE);
	}

	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
		}else{
//			finish(); 也可以
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

}
