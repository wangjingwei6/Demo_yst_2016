package com.wjw.activity;

import com.example.ddddd.R;
import com.wjw.customview.MyVideoViewPlayer;
import com.wjw.util.WindowUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * 这是使用自定义的VideoView视频播放器类
 * @author wangjingwei
 *
 */
public class CustomVideoViewActivity extends Activity{

	private MyVideoViewPlayer videoPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.rela_videoview);
		
		Log.i("PLAY", "onCreate");

		videoPlayer = (MyVideoViewPlayer) findViewById(R.id.videoview);
		String url = "android.resource://" + getPackageName() + "/"+ R.raw.wjw;
//		String url = "http://static.wezeit.com/o_1a9jjk9021fkt7vs1mlo16i91gvn9.mp4";
		videoPlayer.setPath(url);
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i("PLAY", "onConfigurationChanged");
		
		if(WindowUtils.isScreenOriatationPortrait(this)){
			videoPlayer.setOnScreenPortrait();
		}else{
			videoPlayer.setOnScreenHorizontal();
		}
		super.onConfigurationChanged(newConfig);
	}

}
