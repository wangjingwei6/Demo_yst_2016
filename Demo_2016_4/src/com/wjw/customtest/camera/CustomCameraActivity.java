package com.wjw.customtest.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.customview.CircularImageView;
import com.wjw.customview.FocusImageView;
import com.wjw.util.BitmapUtils;
import com.wjw.util.WindowUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class CustomCameraActivity extends Activity implements
		SurfaceHolder.Callback, Camera.PictureCallback,OnClickListener,OnInfoListener,OnErrorListener{

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;//照相
	private MediaRecorder mMediaRecorder;//录像
	private FocusImageView mFocusImageView;

	private CircularImageView camera_img;
	private Button takephoto;
	private ImageView btn_switch_camera;
	private ImageView btn_flash_mode;
	private Button recoder;

	private boolean mIsFrontCamera = false; //默认后置
	private FlashMode mFlashMode=FlashMode.ON;//当前闪光灯类型，默认为关闭  
	
	private boolean isRecoding = false;

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Toast.makeText(CustomCameraActivity.this, "surfaceCreated",
				Toast.LENGTH_SHORT).show();
		mCamera = Camera.open();
		try {
			setCarameraParameters();
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mCamera.release();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		closeCamera();
		mSurfaceHolder = null;
		mSurfaceView  = null;
	}

	/**
	 * 配置摄像头参数
	 */
	private void setCarameraParameters() {
		Camera.Parameters mParameters = mCamera.getParameters();
		
		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			mParameters.set("orientation", "portrait");
			mParameters.setRotation(90);
			mCamera.setDisplayOrientation(90);
		} else {
			mParameters.set("orientation", "landspace");
			mParameters.setRotation(0);
		}
		
		
		//设置颜色效果
		// List<String> supportedColorEffects =
		// mParameters.getSupportedColorEffects(); 
		// Iterator<String> iterator = supportedColorEffects.iterator();
		// while(iterator.hasNext()){
		// String currentEffect = iterator.next();
		// if(currentEffect.equals(Camera.Parameters.EFFECT_SOLARIZE)){
		// mParameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
		// //过度曝光效果
		// break;
		// }
		// }
		
		// 选择合适的预览尺寸   
		List<Camera.Size> sizeList = mParameters.getSupportedPreviewSizes();
		if (sizeList.size()>0) {
			Size cameraSize=sizeList.get(0);
			//预览图片大小
			mParameters.setPreviewSize(cameraSize.width, cameraSize.height);
		}
		
		//设置生成的图片大小
		 List<Size> supportedPictureSizes = mParameters.getSupportedPictureSizes();
		if (supportedPictureSizes.size()>0) {
			Size pictureSize=sizeList.get(0);
			for (Size size : sizeList) {
				//小于100W像素
				if (size.width*size.height<100*10000) {
					pictureSize=size;
					break;
				}
			}
			mParameters.setPictureSize(pictureSize.width, pictureSize.height);
		}

		
		//设置图片格式
		mParameters.setPictureFormat(ImageFormat.JPEG);       
		mParameters.setJpegQuality(100);
		mParameters.setJpegThumbnailQuality(100);
		//自动聚焦模式
		mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		//闪光灯模式
		mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		
		mCamera.setParameters(mParameters);
		
	}

	/**
	 * 关闭摄像头并且释放资源
	 */
	private void closeCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}		
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
	     imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		setContentView(R.layout.custom_camera);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		camera_img = (CircularImageView)findViewById(R.id.camera_img);
		takephoto = (Button)findViewById(R.id.takephoto);
		recoder = (Button)findViewById(R.id.recoder);
		btn_switch_camera=(ImageView)findViewById(R.id.btn_switch_camera);
		btn_flash_mode =(ImageView)findViewById(R.id.btn_flash_mode);
		
		mFocusImageView=(FocusImageView) findViewById(R.id.focusImageView);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
		
		
		takephoto.setOnClickListener(this);
		btn_switch_camera.setOnClickListener(this);
		btn_flash_mode.setOnClickListener(this);
		recoder.setOnClickListener(this);
		
		mSurfaceView.setFocusable(true);
		mSurfaceView.setFocusableInTouchMode(true);
		mSurfaceView.setClickable(true);
		mSurfaceView.setOnTouchListener(new TouchListener());
		
	}
	
	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(CustomCameraActivity.this, extra+"", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 摄像最长时间结束回调
	 */
	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
			initMediaRecoder();
		}
	}
	
	/**
	 * 配置摄像机参数
	 */
	private void initMediaRecoder() {
		if(mCamera==null){
			try {
				mCamera=Camera.open();
			} catch (Exception e) {
				mCamera =null;
			}
		}
		
		if(!isRecoding){
			 mCamera.unlock();
			 if(mMediaRecorder==null){
				 mMediaRecorder = new MediaRecorder();
				 mMediaRecorder.setOnErrorListener(this);
				 mMediaRecorder.setOnInfoListener(this);
			 }
			 mMediaRecorder.reset();
			 mMediaRecorder.setCamera(mCamera);
			 
			 mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//默认音频麦克风 MIC
			 mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//默认摄像头  CAMERA
//			 CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
			 
			 mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); //设置视频，音频的输出格式       
			 mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//默认音频编码器
			 mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);//默认视频编码器
			 
//		     mMediaRecorder.setVideoEncodingBitRate(150000);//视频比特率
//			 mMediaRecorder.setAudioEncodingBitRate(8000);//音频比特率
//		     mMediaRecorder.setAudioSamplingRate(8000);//音频采样率
//			 mMediaRecorder.setAudioChannels(1);//音频通道
			 
			 mMediaRecorder.setMaxDuration(20*1000);//最长时间10秒
//			 mMediaRecorder.setVideoFrameRate(20);//视频帧速率
//			 mMediaRecorder.setVideoSize(480, 720);
			 mMediaRecorder.setOrientationHint(90);
			 mMediaRecorder.setMaxFileSize(5000000);//5MB
			 
			 File file = new File(imageFilePath+"/wangjingwei.mp4");
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
			
			try {
				mMediaRecorder.prepare();
				mMediaRecorder.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			recoder.setEnabled(true);
			isRecoding = true;
		}else{
			isRecoding = false;
			if(mMediaRecorder!=null){
				mMediaRecorder.stop();
				mMediaRecorder.release();
				mMediaRecorder=null;
				recoder.setEnabled(false);
				finish();
			}
		}
		
		
		
	}

	
	/**
	 * 捕捉图回调 拍照功能
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
		String endfileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".jpg";
		try {
			File destDir = new File(imageFilePath+"/1111111");
			destDir.mkdir();
			File imgFile = new File(destDir,endfileName);
			Log.i("CAMERA", "createNewFile = = "+imgFile.getPath());
			if(imgFile.exists()){
				imgFile.delete();
			}
			boolean createNewFile = imgFile.createNewFile();
			Log.i("CAMERA", "createNewFile = = "+createNewFile);
			Uri uri = Uri.fromFile(imgFile);
			
			OutputStream openOutputStream = getContentResolver().openOutputStream(uri);
			openOutputStream.write(data);
			openOutputStream.flush();
			openOutputStream.close();
			
			camera_img.setImageBitmap(BitmapUtils.getImageFromFile(imgFile.getAbsolutePath(), this));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("CAMERA", "error : "+e.getMessage().toString());
		}
		camera.startPreview();
	}

	/**
	 * 调用Camera拍照
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.takephoto:
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mCamera.takePicture(null, null, null, CustomCameraActivity.this);
				}
			}, 1000);
			break;
		case R.id.btn_switch_camera:
			
			switchCameraMode();
			
			break;
			
		case R.id.btn_flash_mode:
			if(getFlashMode()==FlashMode.ON){
				setFlashMode(FlashMode.OFF);
				btn_flash_mode.setImageResource(R.drawable.btn_flash_off);
			}else if (getFlashMode()==FlashMode.OFF) {
				setFlashMode(FlashMode.AUTO);
				btn_flash_mode.setImageResource(R.drawable.btn_flash_auto);
			}
			else if (getFlashMode()==FlashMode.AUTO) {
				setFlashMode(FlashMode.TORCH);
				btn_flash_mode.setImageResource(R.drawable.btn_flash_torch);
			}
			else if (getFlashMode()==FlashMode.TORCH) {
				setFlashMode(FlashMode.ON);
				btn_flash_mode.setImageResource(R.drawable.btn_flash_on);
			}
			
			break;
			
		case R.id.recoder:
			initMediaRecoder();
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 切换摄像头模式   前置 后置
	 */
	private void switchCameraMode() {
		mIsFrontCamera=!mIsFrontCamera;
		closeCamera(); //先释放之前摄像头的资源
		
		if(mIsFrontCamera){ //前置
			Camera.CameraInfo cameraInfo=new CameraInfo();
			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, cameraInfo);
				if(cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
					try {
						mCamera=Camera.open(i);
					} catch (Exception e) {
						closeCamera(); //先释放之前摄像头的资源
					}
				}
			}
		}else {//后置
			
			try {
				mCamera=Camera.open();
			} catch (Exception e) {
				closeCamera(); //先释放之前摄像头的资源
			}

		}
		
		try {
			setCarameraParameters();
			mCamera.setPreviewDisplay(mSurfaceHolder);//通过surfaceview显示取景画
			mCamera.startPreview();//开始预览
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeCamera(); //先释放之前摄像头的资源
		}
		
	}

	
	/**  
	 *  获取当前闪光灯类型
	 *  @return   
	 */
	public FlashMode getFlashMode() {
		return mFlashMode;
	}
	
	/**  
	 *  设置闪光灯类型
	 *  @param flashMode   
	 */
	public void setFlashMode(FlashMode flashMode) {
		if(mCamera==null) return;
		mFlashMode = flashMode;
		Camera.Parameters parameters=mCamera.getParameters();
		switch (flashMode) {
		case ON:
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			break;
		case AUTO:
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			break;
		case TORCH:
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			break;
		default:
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			break;
		}
		mCamera.setParameters(parameters);
	}
	
	/** 
	 * @Description: 闪光灯类型枚举 默认为关闭
	 */
	public enum FlashMode{
		/** ON:拍照时打开闪光灯   */ 
		ON,
		/** OFF：不打开闪光灯  */ 
		OFF,
		/** AUTO：系统决定是否打开闪光灯  */ 
		AUTO,
		/** TORCH：一直打开闪光灯  */ 
		TORCH
	}
	
	@SuppressWarnings("unused")
	private void onFocus(Point point,AutoFocusCallback callback){
		Camera.Parameters parameters=mCamera.getParameters();
		//不支持设置自定义聚焦，则使用自动聚焦，返回
		if (parameters.getMaxNumFocusAreas()<=0) {
			mCamera.autoFocus(callback);
			return;
		}
		List<Area> areas=new ArrayList<Camera.Area>();
		int left=point.x-300;
		int top=point.y-300;
		int right=point.x+300;
		int bottom=point.y+300;
		left=left<-1000?-1000:left;
		top=top<-1000?-1000:top;
		right=right>1000?1000:right;
		bottom=bottom>1000?1000:bottom;
		areas.add(new Area(new Rect(left,top,right,bottom), 100));
		parameters.setFocusAreas(areas);
		try {
			//本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
			//目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		mCamera.autoFocus(autoFocusCallback);
	}
	
	private final AutoFocusCallback autoFocusCallback=new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			//聚焦之后根据结果修改图片
			if (success) {
				mFocusImageView.onFocusSuccess();
			}else {
				//聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
				mFocusImageView.onFocusFailed();

			}
		}
	};
	private String imageFilePath;
	
	private final class TouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
				// 手指离开屏幕
			case MotionEvent.ACTION_UP:
					//设置聚焦
					Point point=new Point((int)event.getX(), (int)event.getY());
					onFocus(point,autoFocusCallback);
					mFocusImageView.startFocus(point);
				break;
			}
			return true;
		}

	}

	
}
