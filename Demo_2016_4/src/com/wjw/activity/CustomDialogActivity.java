package com.wjw.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ddddd.R;
import com.wjw.customview.MyImageView;
import com.wjw.util.CustomProgressDialogUtil;

public class CustomDialogActivity extends Activity implements OnClickListener {
	
	private Button progress_honrizontal_dialog__bt;
	private Button progress_vertical_xuanzhuang_but;
	private Button progress_honrizontal_color_bt;
	private Button progress_honrizontal_micai_bt;

	private ProgressBar progress;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MESSAGE_PROGRESS_FINISH:
				Toast.makeText(CustomDialogActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_PROGRESS_COLOR_HORIZONTAL:
				progress.setVisibility(View.GONE);
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.custom_all_dialog);

		progress_vertical_xuanzhuang_but = (Button) findViewById(R.id.progress_vertical_xuanzhuang_but);
		progress_honrizontal_dialog__bt = (Button) findViewById(R.id.progress_honrizontal_dialog__bt);
		progress_honrizontal_color_bt = (Button) findViewById(R.id.progress_honrizontal_color_bt);
		progress_honrizontal_micai_bt = (Button) findViewById(R.id.progress_honrizontal_micai_bt);
		
		progress_vertical_xuanzhuang_but.setOnClickListener(this);
		progress_honrizontal_dialog__bt.setOnClickListener(this);
		progress_honrizontal_color_bt.setOnClickListener(this);
		progress_honrizontal_micai_bt.setOnClickListener(this);
		
	}

	private int allProgress = 100;
	private int loadProgress = 7;
	private int currentProgress = 0;
	private static final int MESSAGE_PROGRESS_FINISH = 110;

	private void showProgressDialog() {
		final ProgressDialog mDialog = new ProgressDialog(this);
		mDialog.setTitle("九秀美女直播");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage("正在更新中~");
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgressDrawable(getResources().getDrawable(R.drawable.ns_horizontal_progress));
		mDialog.setProgress(currentProgress);
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i <= allProgress / loadProgress; i++) {
					try {
						Thread.sleep(500);
						// dialog.incrementProgressBy(6);
						currentProgress += loadProgress;

						if (currentProgress < 100) {
							mDialog.setProgress(currentProgress);
						} else {
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.dismiss();
							}
							mHandler.sendEmptyMessage(MESSAGE_PROGRESS_FINISH);
							currentProgress = 0;
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

		mDialog.show();

	}

	private void showAlterDialog() {

		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("九秀直播");
		builder.setMessage("九秀美女直播更新啦，下载九秀美女直播随时随地玩主播");
		builder.setIcon(R.drawable.charge_zhifubao_icon);
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "罢了", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "我要更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
				}
				showProgressDialog();
			}
		});
		dialog.show();
	}

	private int mCount;
	private static final int MESSAGE_PROGRESS_COLOR_HORIZONTAL = 111;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.progress_vertical_xuanzhuang_but:
			CustomProgressDialogUtil.remindUserDialog(CustomDialogActivity.this, "支付加载中...").show();
			break;
		case R.id.progress_honrizontal_dialog__bt:
			showAlterDialog();
			break;
		case R.id.progress_honrizontal_color_bt:

			showProgressbar(R.id.progress_horizontal_color);
			break;

		case R.id.progress_honrizontal_micai_bt:
			
			showProgressbar(R.id.progress_horizontal_micai);
			break;

		default:
			break;
		}
	}

	private void showProgressbar(int id) {
		// 当前进度值
		mCount = 0;

		progress = (ProgressBar) findViewById(id);
		progress.setVisibility(View.VISIBLE);
		progress.setMax(100);
		progress.setProgress(0);
		progress.setIndeterminate(false);

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					while (mCount <= 100) {
						progress.setProgress(mCount++);
						Thread.sleep(100);
					}

					if (mCount > 100) {
						mHandler.sendEmptyMessage(MESSAGE_PROGRESS_COLOR_HORIZONTAL);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
