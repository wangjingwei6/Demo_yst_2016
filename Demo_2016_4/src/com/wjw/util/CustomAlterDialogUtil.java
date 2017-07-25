package com.wjw.util;

import com.example.ddddd.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomAlterDialogUtil {
	
	
	public interface DialogClickListenner {
		public void confirm();

		public void cancle();
	}
	
	
	/**
	 * 用户提醒弹出框
	 * @param context
	 * @param confirmStr 确定按钮文本内容
	 * @param cancelStr  取消按钮文本内容
	 * @param describe   提醒内容
	 * @param dialogType 提醒类型
	 * @param loginDialogListenner
	 */
    public static void remindUserDialog(Context context,String cancelStr ,String confirmStr,String describe,
    		final DialogClickListenner listener) {
    	final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		View view = LayoutInflater.from(context).inflate(R.layout.remind_user_pop, null);
		window.setContentView(view);
		window.setGravity(Gravity.CENTER);
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		WindowManager.LayoutParams lp = window.getAttributes();  
		lp.width = WindowUtils.getScreenWidth((Activity)context)*3/4;
		lp.height = WindowUtils.getScreenHeight((Activity)context)*1/4;
		window.setAttributes(lp);
		
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		cancel.setText(TextUtils.isEmpty(cancelStr)?"取消":cancelStr);
		confirm.setText(TextUtils.isEmpty(confirmStr)?"确定":confirmStr);
		
		
		cancel.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			dialog.cancel();
    			listener.cancle();
    		}
    	});
		confirm.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			if(dialog != null){
    				dialog.dismiss();
    			}
    			listener.confirm();
    		}
    	});
    }
    
}
