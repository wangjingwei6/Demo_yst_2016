package com.wjw.customview;

import com.example.ddddd.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 自定义加载框. <br>
 * 类详细说明.
 * 
 * @author dingyj@c-platform.com
 * @version 1.0.0
 */
public class MProcessDialog extends Dialog {

	private String content = null;

	private ImageView animImg;
	private TextView tvProcess;
	private TextView loadTv;
	private TextView tvTitle;
	private ProgressBar proBar;
	private Button btnCancel;
	private TranslateAnimation tranAnim;

	/**
	 * @param context
	 */
	public MProcessDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		setCancelable(false);
		initUI2();
	}

	public MProcessDialog(Context context, String content) {
		super(context, R.style.CustomProgressDialog);
		setCancelable(false);
		initUI2();
	}

	public MProcessDialog(Context context, int id) {
		super(context, R.style.CustomProgressDialog);
		setCancelable(false);
		initUI2();
	}

	/**
	 * 初始化第一个动画效果
	 */
	private void initUI() {
		setContentView(R.layout.process_dlg); //process_dlg 是另外一个动画效果
		getWindow().getAttributes().gravity = Gravity.CENTER;
		animImg = (ImageView) findViewById(R.id.tv_process);
		loadTv = (TextView) findViewById(R.id.lodding);
		animImg.setBackgroundResource(R.drawable.anim_progress); //anim_progress 是另外一个动画效果
		AnimationDrawable anim = (AnimationDrawable) animImg.getBackground();
		anim.start();
		if (content != null && content.trim().length() > 0) {
			loadTv.setText(content);
		}
	}
	
	/**
	 *  初始化第一个动画效果
	 */
	private void initUI2() {
		setContentView(R.layout.process_dlg2); //process_dlg 是另外一个动画效果
		getWindow().getAttributes().gravity = Gravity.CENTER;
		animImg = (ImageView) findViewById(R.id.tv_process);
		animImg.setBackgroundResource(R.drawable.anim_progress2); //anim_progress 是另外一个动画效果
		AnimationDrawable anim = (AnimationDrawable) animImg.getBackground();
		anim.start();
		if (content != null && content.trim().length() > 0) {
			loadTv.setText(content);
		}
	}
	
	/**
	 * 设置新的内容
	 * 
	 * @param info
	 *            内容
	 */
	public void setMessage(String info) {
		if (info != null) {
			this.content = info;
			if (loadTv != null) {
				loadTv.setText(content);
			}
		}
	}
}
