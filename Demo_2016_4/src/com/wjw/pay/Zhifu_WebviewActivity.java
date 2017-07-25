//package com.wjw.pay;
//
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import com.alipay.android.msp.demo.Keys;
//import com.alipay.android.msp.demo.Result;
//import com.alipay.android.msp.demo.Rsa;
//import com.alipay.sdk.app.PayTask;
//import com.dandelionlvfengli.lib.UI;
//import com.guohua.common.util.Contacts;
//import com.jack.contacts.AppContext;
//import com.yst.newbusiness.R;
//
///**
// * 支付宝支付
// * 
// * @author jack
// * 
// */
//public class Zhifu_WebviewActivity extends Activity {
//	/**
//	 * 显示支付宝
//	 */
//	private WebView zhifuWebView;
//
//	public static final String TAG = "alipay-sdk";
//
//	private static final int RQF_PAY = 1;
//
//	private static final int RQF_LOGIN = 2;
//
//	public static final String PAY_NO = "payno";
//	public static final String PAY_TYPE = "paytype";
//
//	public static Zhifu_WebviewActivity mInstace = null;
//
//	private String payOrderId;
//	private int zhifubaoChongzhiType;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mInstace = this;
//		UI.enter(this);
//		setContentView(R.layout.activity_zhifu_webview);
//		chushihuaWebview();
//		this.payOrderId = this.getIntent().getStringExtra(PAY_NO);
//		this.zhifubaoChongzhiType = Integer.parseInt(this.getIntent()
//				.getStringExtra(PAY_TYPE));
//		// AppContext.zhifubaoChongzhiType = 1;
//		this.chongzhi();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		UI.enter(this);
//	}
//
//	/**
//	 * 初始化支付宝
//	 */
//	public void chushihuaWebview() {
//		zhifuWebView = (WebView) findViewById(R.id.zhifuWebView);
//		zhifuWebView.getSettings().setJavaScriptEnabled(true);
//		zhifuWebView.getSettings().setBuiltInZoomControls(true);
//		zhifuWebView.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		});
//
//	}
//
//	Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			Result result = new Result((String) msg.obj);
//
//			switch (msg.what) {
//			case RQF_PAY:
//				if (result.getResult().equals("") || result.getResult() == null) {
//					Toast.makeText(Zhifu_WebviewActivity.this, "恭喜您，操作成功",
//							Toast.LENGTH_SHORT).show();
//					if (zhifubaoChongzhiType == 2) {
//						startActivity(new Intent(Zhifu_WebviewActivity.this,
//								CustomerOrdersActivity.class));
//					}
//				} else {
//					Toast.makeText(Zhifu_WebviewActivity.this,
//							result.getResult(), Toast.LENGTH_SHORT).show();
//				}
//
//				finish();
//				break;
//			case RQF_LOGIN: {
//				Toast.makeText(Zhifu_WebviewActivity.this, result.getResult(),
//						Toast.LENGTH_SHORT).show();
//
//			}
//				break;
//			default:
//				break;
//			}
//			AppContext.zhifujine = 0.0;
//		};
//	};
//
//	// 支付宝充值
//	public void chongzhi() {
//		try {
//			Log.i("ExternalPartner", "onItemClick");
//			String info = getNewOrderInfo();
//			String sign = Rsa.sign(info, Keys.PRIVATE);
//			sign = URLEncoder.encode(sign, "UTF-8");
//			info += "&sign=\"" + sign + "\"&" + getSignType();
//			Log.i(TAG, "==info:" + info);
//
//			final String orderInfo = info;
//			new Thread() {
//				public void run() {
//					// AliPay alipay = new AliPay(Zhifu_WebviewActivity.this,
//					// mHandler);
//					// 构造PayTask 对象
//					PayTask alipay = new PayTask(Zhifu_WebviewActivity.this);
//					// 设置为沙箱模式，不设置默认为线上环境
//					// alipay.setSandBox(true);
//
//					String result = alipay.pay(orderInfo);
//
//					Log.i(TAG, "result = " + result);
//					Message msg = new Message();
//					msg.what = RQF_PAY;
//					msg.obj = result;
//					mHandler.sendMessage(msg);
//				}
//			}.start();
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			Toast.makeText(Zhifu_WebviewActivity.this,
//					R.string.remote_call_failed, Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	private String getNewOrderInfo() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("partner=\"");
//		sb.append(Keys.DEFAULT_PARTNER);
//		sb.append("\"&out_trade_no=\"");
//		sb.append(payOrderId);
//		sb.append("\"&subject=\"");
//		sb.append(getTypeByString(zhifubaoChongzhiType) + "");
//		sb.append("\"&body=\"");
//		sb.append(getTypeByString(zhifubaoChongzhiType) + "");
//		sb.append("\"&total_fee=\"");
//		sb.append(AppContext.zhifujine);
//		sb.append("\"&notify_url=\"");
//
//		// 网址需要做URL编码
//		// sb.append(URLEncoder.encode("http://61.177.36.62:8801"+"/OnlineAlipay/notify_url.aspx"));
//		sb.append(URLEncoder.encode(Contacts.URL_ALIPAY_HEAD
//				+ "alipay!alipayNotify"));
//		sb.append("\"&service=\"mobile.securitypay.pay");
//		sb.append("\"&_input_charset=\"UTF-8");
//		sb.append("\"&return_url=\"");
//		sb.append(URLEncoder.encode("http://m.alipay.com"));
//		sb.append("\"&payment_type=\"1");
//		sb.append("\"&seller_id=\"");
//		sb.append(Keys.DEFAULT_SELLER);
//
//		// 如果show_url值为空，可不传
//		// sb.append("\"&show_url=\"");
//		sb.append("\"&it_b_pay=\"1m");
//		sb.append("\"");
//
//		return new String(sb);
//	}
//
//	public static String getTypeByString(int id) {
//		// 余额充值 = 1,
//		// 服务包充值 = 2,
//		// 在线咨询充值 = 3,
//		// 健康中心充值 = 4,
//		if (id == 1) {
//			return "余额充值";
//		}
//		if (id == 2) {
//			return "购物消费";
//		}
//
//		return "余额充值";
//	}
//
//	private String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
//		Date date = new Date();
//		String key = format.format(date);
//
//		java.util.Random r = new java.util.Random();
//		key += r.nextInt();
//		key = key.substring(0, 15);
//		Log.d(TAG, "outTradeNo: " + key);
//		return key;
//	}
//
//	private String getSignType() {
//		return "sign_type=\"RSA\"";
//	}
//
//	// 支付宝充值
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			this.finish();
//			AppContext.tongquanZaixianchongzhi = true;
//			AppContext.shoujiChongzhiCaozuo = true;
//		}
//		return true;
//	}
//
//}
