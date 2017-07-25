package com.wjw.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.ddddd.R;
import com.wjw.customview.AutoTextView;
import com.wjw.customview.MarqueeView;

public class ScrollVerticalTextViewActivity extends Activity implements
		OnClickListener {

	private Button textview_button;
	private Button recycleview_button;
	private AutoTextView scrolltextview_textview;
	private MarqueeView recycle_textview;
	
	private Button button_img;
	private Button button_music;

	private static final int TEXTVIEW = 1;
	private static final int RECYCLE = 2;

	private String[] textArrays = new String[] { "11111111111111111","22222222222222222",
			"33333333333333333", "4444444444444444","5555555555555555555","666666666666666666" };
	private String[] textUrls = new String[] { "http://www.baidu.com",
			"http://www.hao123.com", "http://www.sohu.com","http://www.sina.com"};
	
	private boolean textViewIsClick = true;
	
	private int currentSelectPosition = 0;
	private String currentSelectNewsUrl;
	

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TEXTVIEW:
				scrolltextview_textview.setText(textArrays[currentSelectPosition%textArrays.length]);
				currentSelectNewsUrl = textUrls[currentSelectPosition%textUrls.length];
				currentSelectPosition++;
				mHandler.sendEmptyMessageDelayed(TEXTVIEW, 2500);
				break;
			case RECYCLE:

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scroll_vertical_textview);

		textview_button = (Button) findViewById(R.id.textview_button);
		recycleview_button = (Button) findViewById(R.id.recycleview_button);
		
		button_img = (Button)findViewById(R.id.button_img);
		button_music = (Button)findViewById(R.id.button_music);
		
		scrolltextview_textview = (AutoTextView)findViewById(R.id.scrolltextview_textview);
		recycle_textview = (MarqueeView)findViewById(R.id.recycle_textview);

		textview_button.setOnClickListener(this);
		recycleview_button.setOnClickListener(this);
		scrolltextview_textview.setOnClickListener(this);
		button_img.setOnClickListener(this);
		button_music.setOnClickListener(this);
		
			
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textview_button:
			if(textViewIsClick){
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						textViewIsClick =false;
						mHandler.sendEmptyMessage(TEXTVIEW);
					}
				});
			}
			break;
		case R.id.recycleview_button:
			ArrayList<String>descArrayList = new ArrayList<String>();
			ArrayList<String>urlArrayList = new ArrayList<String>();
			for(int x = 0;x<textArrays.length;x++){
				descArrayList.add(textArrays[x]);
			}
			for(int x = 0;x<textUrls.length;x++){
				urlArrayList.add(textUrls[x]);
			}
			
			recycle_textview.setListDesc(descArrayList);
			recycle_textview.setListUrl(urlArrayList);
			recycle_textview.startScroll();

			break;
		case R.id.scrolltextview_textview:
			Intent intent = new Intent(ScrollVerticalTextViewActivity.this,
					WebViewActivity.class);
			intent.putExtra("url", currentSelectNewsUrl);
			intent.putExtra("isShowTitle", 1);
			startActivity(intent);
			
			break;
			
		case R.id.button_img: //系统gallery相册意图
			
			Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, null);
			choosePictureIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(choosePictureIntent, 0);
			
//			Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(choosePictureIntent, 0);
			break;
			
		case R.id.button_music: // 系统音乐播放器意图
			Intent musicIntent = new Intent(Intent.ACTION_VIEW	);
			File sdcard = Environment.getExternalStorageDirectory();
			File audioFile =  new File(sdcard.getPath()+"/kgmusic/download/我们的歌.mp3");
			musicIntent.setDataAndType(Uri.fromFile(audioFile),"audio/mp3");
			startActivity(musicIntent);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		
	}

}
