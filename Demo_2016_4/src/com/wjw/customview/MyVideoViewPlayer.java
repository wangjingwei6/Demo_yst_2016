package com.wjw.customview;

import com.example.ddddd.R;
import com.wjw.util.OtherUtils;
import com.wjw.util.WindowUtils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

@SuppressLint("NewApi")
public class MyVideoViewPlayer extends FrameLayout implements OnPreparedListener, OnCompletionListener, OnErrorListener, View.OnClickListener,
		OnSeekBarChangeListener, Animator.AnimatorListener {

	private MyVideoView videoview;
	private Context mContext;

	private FrameLayout videoFrameLayout; // 全屏幕控制布局
	private LinearLayout videoControllerLayout_bottom;// 底部控制栏
	private RelativeLayout videoControllerLayout_top; // 顶部控制栏
	private Button video_start_but; // 开始播放按钮
	private Button back_but;// 后退
	private Button foward_but;// 前进
	private ImageView video_backimg;
	private CircularImageView video_play_but;// 播放器播放主按钮
	private ProgressBar loading__progress;
	private SeekBar seekbar;
	private TextView videoTotalTime, videoCurTime;

	private boolean videoControllerShow = false;// 底部状态栏的显示状态
	private static String videoStatus = "STOP"; // STOP PLAY COMPLETE
	private boolean isFirstMeasure = true;
	private boolean isFirstPlay = true;

	private int protraitHeight = 0;
	private int protraitWidth = 0;
	private int horizongtalWidth = 0;
	private int horizontalHeight = 0;
	private static final int UPDATE_VIDEO_SEEKBAR = 1;
	private String mUrl = null;
	private Uri mUri = null;

	private String type = "normal";
	private int mCurrentPosition = 0;
	private int seekTo = 0;
	private int duration;

	public MyVideoViewPlayer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public MyVideoViewPlayer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyVideoViewPlayer(Context context) {
		this(context, null);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Log.i("PLAY", "onFinishInflate");
		initView();
	}

	private Handler videoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_VIDEO_SEEKBAR:

				if ("PLAY".equals(videoStatus)) {

					if ("normal".equals(type)) {
						mCurrentPosition = videoview.getCurrentPosition();
						seekbar.setProgress(mCurrentPosition);
					} else if (type.equals("back")) {
						mCurrentPosition = videoview.getCurrentPosition() - 1000 * 5;
						videoview.seekTo(mCurrentPosition);
						seekbar.setProgress(mCurrentPosition);
						type = "normal";

					} else if (type.equals("foward")) {
						mCurrentPosition = videoview.getCurrentPosition() + 1000 * 5;
						videoview.seekTo(mCurrentPosition);
						seekbar.setProgress(mCurrentPosition);
						type = "normal";
					} else if (type.equals("seek")) {
						videoview.seekTo(seekTo);
						seekbar.setProgress(seekTo);
						type = "normal";

					} else {}

					flushVideoStatus(); // 刷新播放器状态
					videoHandler.sendEmptyMessageDelayed(UPDATE_VIDEO_SEEKBAR, 1000);

				} else if ("STOP".equals(videoStatus)) { // 播放器暂停状态
					mCurrentPosition = videoview.getCurrentPosition();

					if (type.equals("seek")) {
						seekbar.setProgress(seekTo);
					} else {
						seekbar.setProgress(mCurrentPosition);
						videoview.seekTo(mCurrentPosition);
					}
					flushVideoStatus(); // 刷新播放器状态

				} else { // complete 播放完成状态
					flushVideoStatus(); // 刷新播放器状态
				}

				break;

			}

			Log.i("PLAY", "handler()= " + videoview.getCurrentPosition() + "******" + videoview.getDuration() + "****" + seekbar.getProgress());
		}
	};

	/**
	 * 初始化播放器相关操作
	 */
	private void initView() {

		View layout = LayoutInflater.from(mContext).inflate(R.layout.videoview, null);

		videoview = (MyVideoView) layout.findViewById(R.id.custom_videoview);
		// MediaController mMediaController = new MediaController(mContext);
		// videoview.setMediaController(mMediaController);

		videoFrameLayout = (FrameLayout) layout.findViewById(R.id.video_framlayout);
		videoControllerLayout_bottom = (LinearLayout) layout.findViewById(R.id.videoControllerLayout_bottom);
		videoControllerLayout_top = (RelativeLayout) layout.findViewById(R.id.videoControllerLayout_top);
		
		video_backimg = (ImageView) layout.findViewById(R.id.video_backimg);
		initVideoViewParams();

		video_start_but = (Button) layout.findViewById(R.id.video_start_but);

		back_but = (Button) layout.findViewById(R.id.back_but);
		foward_but = (Button) layout.findViewById(R.id.foward_but);
		video_play_but = (CircularImageView) layout.findViewById(R.id.video_play_but);
		loading__progress = (ProgressBar) layout.findViewById(R.id.loading__progress);
		seekbar = (SeekBar) layout.findViewById(R.id.seekbar);
		videoTotalTime = (TextView) layout.findViewById(R.id.videoTotalTime);
		videoCurTime = (TextView) layout.findViewById(R.id.videoCurTime);

		videoFrameLayout.setOnClickListener(this);
		video_start_but.setOnClickListener(this);
		back_but.setOnClickListener(this);
		foward_but.setOnClickListener(this);
		video_play_but.setOnClickListener(this);

		videoview.setOnPreparedListener(this);
		videoview.setOnCompletionListener(this);
		videoview.setOnErrorListener(this);

		seekbar.setOnSeekBarChangeListener(this);

		addView(layout);
	}

	/**
	 * 初始化VideoView播放器大小参数
	 */
	private void initVideoViewParams() {

		exitScreenAnamaiton();

		protraitWidth = WindowUtils.getScreenWidth((Activity) mContext);
		protraitHeight = WindowUtils.getScreenHeight((Activity) mContext);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(protraitWidth, protraitHeight * 2 / 5);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		videoview.setLayoutParams(lp);
		video_backimg.setLayoutParams(lp);
		videoview.getHolder().setFixedSize(protraitWidth, protraitHeight * 2 / 5);
	}

	// 设置播放器横屏
	public void setOnScreenHorizontal() {
		if (isFirstMeasure) {
			isFirstMeasure = !isFirstMeasure;
			horizongtalWidth = WindowUtils.getScreenWidth((Activity) mContext);
			horizontalHeight = WindowUtils.getScreenHeight((Activity) mContext);
		}

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(horizongtalWidth, horizontalHeight);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		videoview.setLayoutParams(layoutParams);
		videoview.getHolder().setFixedSize(horizongtalWidth, horizontalHeight);
	}

	// 设置播放器竖屏
	public void setOnScreenPortrait() {

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(protraitWidth, protraitHeight * 2 / 5);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		videoview.setLayoutParams(lp);

		((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		videoview.getHolder().setFixedSize(protraitWidth, protraitHeight * 2 / 5);
	}

	/**
	 * 各个按钮的点击监听事件
	 */
	@Override
	public void onClick(View v) {
		Log.i("PLAY", "onClick");
		if (!OtherUtils.isAllowClickable()) {
			return;
		}
		switch (v.getId()) {
		case R.id.video_framlayout:// 屏幕点击监听

			if (!"PLAY".equals(videoStatus)) {
				return;
			}
			if (videoControllerShow) {
				exitScreenAnamaiton();
				videoControllerShow = false;
			} else {
				entryScreenAnamaiton();
				videoControllerShow = true;
			}

			break;
		case R.id.video_start_but:
			if ("PLAY".equals(videoStatus)) {
				videoStatus = "STOP";
			} else {
				videoStatus = "PLAY";
			}
			videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);

			break;
		case R.id.back_but:
			type = "back";

			if (videoview.getCurrentPosition() > 0) {
				videoHandler.removeMessages(UPDATE_VIDEO_SEEKBAR);
				videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
			}

			break;
		case R.id.foward_but:
			type = "foward";

			if (videoview.getCurrentPosition() > 0 && videoview.getCurrentPosition() < videoview.getDuration()) {
				videoHandler.removeMessages(UPDATE_VIDEO_SEEKBAR);
				videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
			}

			break;
		case R.id.video_play_but:
			videoStatus = "PLAY";
			if (mUrl != null && isFirstPlay) { // 首次进来，开始初始化并且打开播放器
				start(mUrl);
				loading__progress.setVisibility(View.VISIBLE);
			} else {

			}
			videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);

			break;
		default:
			break;
		}

	}

	/**
	 * 刷新播放器状态
	 */
	private void flushVideoStatus() {

		if ("PLAY".equals(videoStatus)) {

			video_start_but.setBackgroundResource(R.drawable.icon_video_pause);
			video_play_but.setVisibility(View.GONE);
			startVideo();

		} else if ("STOP".equals(videoStatus)) {
			video_start_but.setBackgroundResource(R.drawable.icon_video_play);
			stopVideo();

		} else if ("COMPLETE".equals(videoStatus)) {

			videoview.seekTo(0);
			seekbar.setProgress(0);
			isFirstPlay = false;
			video_play_but.setVisibility(View.VISIBLE);
			video_backimg.setVisibility(View.VISIBLE);
			stopVideo();
			exitScreenAnamaiton();
		}

	}

	/**
	 * 显示控制栏动画
	 */
	private void entryScreenAnamaiton() {
		videoControllerLayout_bottom.setVisibility(View.VISIBLE);
		videoControllerLayout_top.setVisibility(View.VISIBLE);

		ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout_bottom, "alpha", 0, 1);
		animator.setDuration(200);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.start();
		animator.addListener(this);

		ObjectAnimator animator2 = ObjectAnimator.ofFloat(videoControllerLayout_top, "alpha", 0, 1);
		animator2.setDuration(200);
		animator2.setInterpolator(new AccelerateInterpolator());
		animator2.start();
		animator2.addListener(this);
	}

	/**
	 * 隐藏控制栏动画
	 */
	private void exitScreenAnamaiton() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout_bottom, "alpha", 1, 0);
		animator.setDuration(200);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.start();
		animator.addListener(this);

		ObjectAnimator animator2 = ObjectAnimator.ofFloat(videoControllerLayout_top, "alpha", 1, 0);
		animator2.setDuration(200);
		animator2.setInterpolator(new AccelerateInterpolator());
		animator2.start();
		animator2.addListener(this);

		videoControllerLayout_top.setVisibility(View.GONE);
		videoControllerLayout_bottom.setVisibility(View.GONE);

	}

	@Override
	public void onAnimationStart(Animator animation) {
		Log.i("PLAY", "onAnimationStart");
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		Log.i("PLAY", "onAnimationEnd");

	}

	@Override
	public void onAnimationCancel(Animator animation) {
		Log.i("PLAY", "onAnimationCancel");
	}

	@Override
	public void onAnimationRepeat(Animator animation) {

	}

	private int[] getMinuteAndSecond(int mils) {
		mils /= 1000;
		int[] time = new int[2];
		time[0] = mils / 60;
		time[1] = mils % 60;
		return time;
	}

	// ================================================================================================

	public void setPath(String url) {
		mUrl = url;
	}

	// VideoView 各种功能调用

	// 播放文件 URI
	public void start(String url) {
		videoview.setVideoURI(Uri.parse(url));
		videoview.requestFocus();
		videoview.start();
	}

	// 开始播放
	public void startVideo() {
		if (videoview != null && !videoview.isPlaying()) {
			videoview.start();
		}
	}

	// 暂停播放
	public void stopVideo() {
		if (videoview != null && videoview.isPlaying()) {
			videoview.pause();
			video_start_but.setBackgroundResource(R.drawable.icon_video_play);
		}
	}

	// 销毁播放器
	public void destoryVideo() {
		if (videoview != null) {
			videoview.stopPlayback();
			videoview = null;
		}

	}

	/**
	 * 注册的媒体文件加载完毕，可以播放时的回调监听事件
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.i("PLAY", "onPrepared");

		duration = videoview.getDuration();

		int[] time = getMinuteAndSecond(duration);
		videoTotalTime.setText(String.format("%02d:%02d", time[0], time[1]));

		Log.i("Canvas", "****" + String.format("%02d==%02d==%02d", 333, 44, 5));
		Log.i("Canvas", "****" + String.format("%02d:%02d:%02d", 33333, 44444, 5555));
		Log.i("Canvas", "****" + String.format("%03d:%03d:%03d", 333, 44, 5555));
		Log.i("Canvas", "****" + String.format("%04d:%04d:%04d", 333, 44, 5));

		loading__progress.setVisibility(View.GONE);
		video_backimg.setVisibility(View.GONE);
		seekbar.setMax(duration);
		mp.start();

		videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.i("PLAY", "onCompletion");
		Log.i("PLAY",
				"Progress == " + seekbar.getProgress() + "******" + videoview.getDuration() + "****currposition" + videoview.getCurrentPosition());

		videoStatus = "COMPLETE";
		videoHandler.sendEmptyMessageDelayed(UPDATE_VIDEO_SEEKBAR, 500);

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.i("PLAY", "onError");
		return false;
	}

	/**
	 * Seekbar 的变化监听事件回调
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Log.i("PLAY", "onProgressChanged ==== progress :" + progress);

		int[] time = getMinuteAndSecond(progress);
		videoCurTime.setText(String.format("%02d:%02d", time[0], time[1]));

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		type = "seek";
		seekTo = seekbar.getProgress();
		videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
	}

}
