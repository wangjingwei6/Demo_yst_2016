package com.wjw.util;

import com.example.ddddd.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class CustomProgressDialogUtil {
	
	/**
	 * 旋转进度条提示框
	 * @param context
	 * @param desc 文本内容
	 * @param loginDialogListenner
	 */
    public static Dialog remindUserDialog(Context context,String desc) {
    	final Dialog dialog = new Dialog(context,R.style.custom_loading_dialog);
		
		Window window = dialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.remind_user_progress_dialog, null);
		TextView descTv = (TextView) view.findViewById(R.id.desc);
		descTv.setText(desc);
		
		window.setContentView(view);
		window.setGravity(Gravity.CENTER);
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		WindowManager.LayoutParams lp = window.getAttributes();  
		lp.width = WindowUtils.getScreenWidth((Activity)context)*3/5;
		lp.height = WindowUtils.getScreenHeight((Activity)context)*1/5;
		window.setAttributes(lp);
		
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		
		return dialog;
    }
}
