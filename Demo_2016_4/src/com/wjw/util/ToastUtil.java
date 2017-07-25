package com.wjw.util;

import com.example.ddddd.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast;
	private static View view; //弹出吐司提醒的view
	
	/**
	 *  其他吐丝
	 * @param context
	 * @param message
	 */
	public static void MakeToast(Context context, String message) {
		try {
			if(context!=null){
				LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflate.inflate(R.layout.custom_blue_toast, null);
				if(toast==null){
					toast = new Toast(context);
					toast.setView(view);
					toast.setText(message);
					toast.setDuration(Toast.LENGTH_SHORT);
				}else{
					toast.setView(view);
					toast.setText(message);
				}
				toast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 		信息提示涉及的吐丝
	 * @param context
	 * @param message
	 */
	public static void MakeToast_Center(Context context, String message) {
		try {
			if(context!=null){
				LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflate.inflate(R.layout.ns_remind_toast, null);
			//	view.setPadding(100, 75, 100, 75);
				if(toast==null){
					toast = new Toast(context);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.setText(message);
					toast.setDuration(Toast.LENGTH_SHORT);
				}else{
					toast.setView(view);
					toast.setText(message);
				}
				toast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}