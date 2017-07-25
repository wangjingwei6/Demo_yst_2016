package com.wjw.activity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddddd.R;
import com.wjw.customtest.camera.CustomCameraActivity;
import com.wjw.customview.CustomTitleView;
import com.wjw.customview.MyImageView;
import com.wjw.customview.PerfectGridView;
import com.wjw.customviewpager.BaseCustomViewpagerActivity;
import com.wjw.lockview.LockViewActivity;
import com.wjw.prefect.swiperefreshlayout.SwipeRefreshListViewActivity;
import com.wjw.sildeswipeactivity.BaseSlideStyleActivity;
import com.wjw.textviewhightdemo.ExtendTextViewActivity;
import com.wjw.util.CustomPopWindowUtil;
import com.wjw.util.OtherUtils;
import com.wjw.util.SPUtil;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends BaseActivity implements OnClickListener {

	private ArrayList<String> popupwindowData;
	private MyImageView myimageview;
	private String str = "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
			+ "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
			+"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
			+ "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
	private TextView content, index;
	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_3;
	private TextView tv_4;
	private TextView tv_5;
	private TextView tv_6;
	private TextView tvHint;
	
	private Button lockview_but;
	private Button viewpager_allstyle_bt;
	private Button slide_swipe_act_bt;
	private Button custom_dialog_bt;
	private Button custom_horizontalscrollview_bt;
	private Button xianshi;
	private Button yinshi;
	private Button noti;
	private Button provider;
	private Button swipemenu;
	private Button extend_tv;
	private Button vebview_sim;
	private Button vebview;
	private Button recycleview;
	private Button listview_swipe;
	private Button listview_style1;
	private Button gridview_style;
	private Button expandablelist_style;
	private Button loop_page_one;
	private Button loop_page_two;
	private Button scroll_vertical_text;
	private Button custom_camera;
	private Button custom_canvas;
	private Button videoview;

	private ViewStub viewstub;

	private LinearLayout open_linear;
	private boolean hasMesure = false;
	private boolean isShow = false;
	private int lineC;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				initExtendView();
				break;
			case 666:
				tv_3.setText("服务计算的数据是 :" + MyService.m);
				tv_3.setTextColor(Color.GREEN);
				mHandler.sendEmptyMessageDelayed(888, 3000);
				break;
			case 888:
				tv_4.setText("服务计算的数据是 :" + MyService.y);
				tv_4.setTextColor(Color.RED);
				mHandler.removeMessages(888);
				mHandler.removeMessages(666);
				break;
			default:
				break;
			}
		}
	};
	private boolean isFirst = true;

	public void extend_b(View view) {
		isFirst = false;
		if (viewstub != null && isFirst) {
			viewstub.inflate();
		} else {
			viewstub.setVisibility(View.VISIBLE);
		}

	}

	public void hidden_b(View view) {
		viewstub.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//覆盖在上面一层的动画布局控件
		tvHint = (TextView) findViewById(R.id.tv_hint);
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		tvHint.startAnimation(ani);
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.main);
	}
	@Override
	public void initView() {
		myimageview = (MyImageView) findViewById(R.id.myimageview);
		
		content = (TextView) findViewById(R.id.content);
		index = (TextView) findViewById(R.id.index);
		open_linear = (LinearLayout) findViewById(R.id.open_linear);
		
		lockview_but = (Button)findViewById(R.id.lockview_but);
		viewpager_allstyle_bt = (Button)findViewById(R.id.viewpager_allstyle_bt);
		slide_swipe_act_bt = (Button)findViewById(R.id.slide_swipe_act_bt);
		custom_dialog_bt = (Button)findViewById(R.id.custom_dialog_bt);
		custom_horizontalscrollview_bt = (Button)findViewById(R.id.custom_horizontalscrollview_bt);
		xianshi = (Button)findViewById(R.id.xianshi);
		yinshi = (Button)findViewById(R.id.yinshi);
		noti = (Button)findViewById(R.id.noti);
		provider = (Button)findViewById(R.id.provider);
		swipemenu = (Button)findViewById(R.id.swipemenu);
		extend_tv = (Button)findViewById(R.id.extend_tv);
		vebview_sim = (Button)findViewById(R.id.vebview_sim);
		vebview = (Button)findViewById(R.id.vebview);
		recycleview = (Button)findViewById(R.id.recycleview);
		listview_swipe = (Button)findViewById(R.id.listview_swipe);
		listview_style1 = (Button)findViewById(R.id.listview_style1);
		gridview_style = (Button)findViewById(R.id.gridview_style);
		expandablelist_style = (Button)findViewById(R.id.expandablelist_style);
		loop_page_one = (Button)findViewById(R.id.loop_page_one);
		loop_page_two = (Button)findViewById(R.id.loop_page_two);
		scroll_vertical_text = (Button)findViewById(R.id.scroll_vertical_text);
		custom_camera = (Button)findViewById(R.id.custom_camera);
		custom_canvas = (Button)findViewById(R.id.custom_canvas);
		videoview = (Button)findViewById(R.id.videoview);
		viewstub = (ViewStub) findViewById(R.id.stub);

		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		
		SpannableString sbs1 = new SpannableString(tv_1.getText());
		SpannableString sbs2 = new SpannableString(tv_2.getText());
		SpannableString sbs3 = new SpannableString(tv_3.getText());
		SpannableString sbs4 = new SpannableString(tv_4.getText());
		SpannableString sbs5 = new SpannableString(tv_5.getText());

		CharSequence text = "妓院妓@院妓院@妓院妓院@妓院妓院@xxxxxxxx,妓院妓院妓@院妓院妓院@xxxxxxxxxxxxxxx";
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		String rexgString = "@";
		Pattern pattern = Pattern.compile(rexgString);
		Matcher matcher = pattern.matcher(text);

		sbs1.setSpan(new BackgroundColorSpan(Color.RED), 0, 2,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		sbs1.setSpan(new BackgroundColorSpan(Color.GREEN), 2, 6,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		sbs1.setSpan(new BackgroundColorSpan(Color.BLUE), 6, tv_1.getText()
				.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_1.setText(sbs1);

		sbs2.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 1,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		sbs2.setSpan(new ForegroundColorSpan(Color.YELLOW), 1, 4,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		sbs2.setSpan(new ForegroundColorSpan(Color.RED), 4, tv_2.getText()
				.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_2.setText(sbs2);

		sbs3.setSpan(new StyleSpan(Typeface.ITALIC), 0,
				tv_3.getText().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_3.setText(sbs3);

		sbs4.setSpan(new StrikethroughSpan(), 2, 6,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_4.setText(sbs4);

		sbs5.setSpan(new UnderlineSpan(), 2, 6,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_5.setText(sbs5);

		while (matcher.find()) {
			builder.setSpan(
					new ImageSpan(this, R.drawable.lanbojini_title),
					matcher.start(), matcher.end(),
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			builder.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			builder.setSpan(new ForegroundColorSpan(Color.RED), 5, 10,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			builder.setSpan(new ForegroundColorSpan(Color.GREEN), 10, text.length(),
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		tv_6.setText(builder);

		content.setText(str);

		getMeasureLine();
		
		lockview_but.setOnClickListener(this);
		viewpager_allstyle_bt.setOnClickListener(this);
		slide_swipe_act_bt.setOnClickListener(this);
		myimageview.setOnClickListener(this);
		custom_dialog_bt.setOnClickListener(this);
		custom_horizontalscrollview_bt.setOnClickListener(this);
		yinshi.setOnClickListener(this);
		xianshi.setOnClickListener(this);
		noti.setOnClickListener(this);
		provider.setOnClickListener(this);
		swipemenu.setOnClickListener(this);
		extend_tv.setOnClickListener(this);
		vebview_sim.setOnClickListener(this);
		vebview.setOnClickListener(this);
		recycleview.setOnClickListener(this);
		listview_swipe.setOnClickListener(this);
		listview_style1.setOnClickListener(this);
		gridview_style.setOnClickListener(this);
		expandablelist_style.setOnClickListener(this);
		loop_page_one.setOnClickListener(this);
		loop_page_two.setOnClickListener(this);
		scroll_vertical_text.setOnClickListener(this);
		custom_camera.setOnClickListener(this);
		custom_canvas.setOnClickListener(this);
		videoview.setOnClickListener(this);
	}
	
	private ArrayList<String> initPopupWindowData(){
		
		popupwindowData = new ArrayList<String>();
		popupwindowData.add("我的微博");
		popupwindowData.add("好友");
		popupwindowData.add("亲人");
		popupwindowData.add("陌生人");
		popupwindowData.add("1111");
		popupwindowData.add("2222");
		popupwindowData.add("3333");
		popupwindowData.add("4444");
		popupwindowData.add("5555");
		
		return popupwindowData;
		
	}
	
	
	private void initExtendView() {
		if (lineC > 6) {
			index.setVisibility(View.VISIBLE);
			index.setText("展开全文");
			content.setEllipsize(TruncateAt.END);
			content.setLines(6);

			open_linear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isShow) {
						isShow = true;
						content.setEllipsize(null);
						content.setLines(lineC);
						index.setText("收回");
					} else {
						isShow = false;
						content.setEllipsize(TruncateAt.END);
						index.setText("全文");
						content.setLines(6);
					}
				}
			});

		} else {
			content.setText(str);
			Toast.makeText(MainActivity.this, "没有超过6行", Toast.LENGTH_LONG).show();
			;
		}

	}

	private void getMeasureLine() {
		ViewTreeObserver viewTreeObserver = content.getViewTreeObserver();
		viewTreeObserver
				.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						if (!hasMesure) {
							hasMesure = true;
							lineC = content.getLineCount();
							Log.e("CNM", "line = " + lineC);

							Message message = Message.obtain();
							message.what = 1;
							mHandler.sendMessage(message);
						}

						return true;
					}
				});

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(manager!=null){
			manager.cancel(666);
		}
	}
	
	@Override
	protected void onRestart() {
		Log.i("MainActivity", "***onRestart");
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		Log.i("MainActivity", "***onStart");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("MainActivity", "***onResume");

		PerfectGridView gridview = (PerfectGridView) findViewById(R.id.gridview);
		GridViewAdapter mGridViewAdapter = new GridViewAdapter(this, strIds);
		gridview.setAdapter(mGridViewAdapter);

		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
		scrollView.smoothScrollTo(0, 0);

		Intent intent1 = new Intent("com.wjw.service");
		intent1.putExtra("num", 666);
		startService(intent1);

		Intent intent2 = new Intent("com.wjw.service");
		intent2.putExtra("num", 888);
		startService(intent2);

		mHandler.sendEmptyMessageDelayed(666, 3000);
		gridview.post(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
					}
				});
			}
		});
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});

	}

	private String[] strIds = new String[] { "四川", "吉林", "北京", "福建", "黑龙江",
			"天津", "上海", "河北", "山东", "江苏", "浙江", "湖北" };

	class GridViewAdapter extends BaseAdapter {
		private String[] strs;
		private Context mContext;

		public GridViewAdapter(Context context, String[] strIds) {
			this.strs = strIds;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return strs.length;
		}

		@Override
		public Object getItem(int position) {
			return strs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			final ViewHolder mViewHolder;
			if (convertView == null) {// 没有缓存
				view = View.inflate(mContext, R.layout.grid_view, null);
				mViewHolder = new ViewHolder();
				mViewHolder.mTextView = (TextView) view.findViewById(R.id.tv);
				view.setTag(mViewHolder);
			} else {
				view = convertView;
				mViewHolder = (ViewHolder) view.getTag();
			}

			mViewHolder.mTextView.setText(strs[position]);

			return view;
		}
	};

	static class ViewHolder {
		private TextView mTextView;
	}

	@Override
	public void onClick(View v) {
		
		if(!OtherUtils.isAllowClickable()){
			return;
		}
		
		switch (v.getId()) {
		
		case R.id.lockview_but:
			
			startActivity(new Intent(this,LockViewActivity.class));
			
			break;
		
		case R.id.viewpager_allstyle_bt:
			
			startActivity(new Intent(this,BaseCustomViewpagerActivity.class));
			
			break;
		
		case R.id.slide_swipe_act_bt:
			
			startActivity(new Intent(this,BaseSlideStyleActivity.class));
			
			break;
		case R.id.myimageview:
//			Toast.makeText(MainActivity.this, "事件触发", 0).show();
			
			CustomPopWindowUtil.showWindow(MainActivity.this,myimageview, initPopupWindowData());
			
			break;
		case R.id.custom_dialog_bt:
			
			startActivity(new Intent(this,CustomDialogActivity.class));
			
			break;
		case R.id.custom_horizontalscrollview_bt:
			Intent scrollviewIntent = new Intent(this,CustomHorizontalScrollviewActivity.class);
			startActivity(scrollviewIntent);
			break;
		case R.id.yinshi:
			/**隐式意图**/
			Intent yIntent = new Intent();
			yIntent.setAction("SECONDACTIVITY");
//			startActivity(yIntent);
			startActivityForResult(yIntent, 1001);
			break;
		case R.id.xianshi:
			/**显示意图**/
			Intent intent = new Intent(this,SecondActivity.class);
			startActivityForResult(intent, 1002);
			break;
		case R.id.noti:
			manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
			
			PendingIntent pendIntent =PendingIntent.getActivity
					(this, 0, new Intent(this,SecondActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder  builder= new NotificationCompat.Builder(this);
			builder.setContentTitle("Demo_2016")
			.setSmallIcon(R.drawable.charge_zhifubao_icon)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.charge_zhifubao_icon))
			.setContentText("您有新的消息，请注意查收")
			.setPriority(Notification.PRIORITY_DEFAULT)
			.setAutoCancel(true)
			.setContentIntent(pendIntent);
			
			Notification notification = builder.build(); 
			notification.when = System.currentTimeMillis();
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;	
			manager.notify(666, notification);
			break;
		case R.id.provider:
			
			startActivity(new Intent(this,ProviderActivity.class));
			
			break;
		case R.id.swipemenu:
			
			startActivity(new Intent(this,SwipeMenuActivity.class));
			
			break;
			
		case R.id.extend_tv:
			
			startActivity(new Intent(this,ExtendTextViewActivity.class));
			
			break;
		case R.id.vebview_sim:
			startActivity(new Intent(this,WebViewActivity.class));
			break;
		case R.id.vebview:
			startActivity(new Intent(this,TestWebViewActivity.class));
			break;
		case R.id.recycleview:
			startActivity(new Intent(this,RecycleViewActivity.class));
			break;
		case R.id.listview_swipe:
			startActivity(new Intent(this,SwipeRefreshListViewActivity.class));
			break;
		case R.id.listview_style1:
			startActivity(new Intent(this,GooleListViewStyleActivity.class));
			break;
		case R.id.gridview_style:
			startActivity(new Intent(this,GooleGridViewStyleActivity.class));
			break;
		case R.id.expandablelist_style:
			startActivity(new Intent(this,GoogleExpandableListActivity.class));
			break;
		case R.id.loop_page_one:
			startActivity(new Intent(this,AAALoopViewPageActivity.class));
			break;
		case R.id.loop_page_two:
			startActivity(new Intent(this,BBBLoopViewPageActivity.class));
			break;
		case R.id.scroll_vertical_text:
			startActivity(new Intent(this,ScrollVerticalTextViewActivity.class));
			break;
		case R.id.custom_camera:
			startActivity(new Intent(this,CustomCameraActivity.class));
			break;
		case R.id.custom_canvas:
			startActivity(new Intent(this,CustomCanvasActivity.class));
			break;
		case R.id.videoview:
			
			startActivity(new Intent(this,CustomVideoViewActivity.class));
			break;
		default:
			break;
		}
		
	}
	

	private NotificationManager manager;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.i("MainActivity", "***onActivityResult");
		
		switch (requestCode) {
		case 1001:
			if(resultCode==10086){
				Toast.makeText(this,data.getStringExtra("Result").toString()+"_1001",
						Toast.LENGTH_SHORT).show();
			}
			
			break;
		case 1002:
			if(resultCode==10086){
				Toast.makeText(this,data.getStringExtra("Result").toString()+"_1002",
						Toast.LENGTH_SHORT).show(); 
			}
				
			break;
		default:
			break;
		}
		
	}


}
