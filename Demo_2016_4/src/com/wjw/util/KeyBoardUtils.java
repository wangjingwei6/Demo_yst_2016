package com.wjw.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 * 
 * @author wjw
 * 
 */
public class KeyBoardUtils {
	/**
	 * 打卡软键盘
	 * 
	 * @param mEditText
	 *            输入框
	 * @param mContext
	 *            上下文
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mEditText
	 *            输入框
	 * @param mContext
	 *            上下文
	 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 * @author by_wsc
	 * @email wscnydx@gmail.com
	 * @date 日期：2013-6-9 下午4:52:33
	 */
	public static void hiddenKeyBoard(Context context) {

		try {
			if (context == null)
				return;
			// 取消弹出的对话框
			InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示软键盘
	 * 
	 * @param activity
	 * @author by_wsc
	 * @email wscnydx@gmail.com
	 * @date 日期：2013-6-9 下午4:52:33
	 */
	public static void showKeyBoard(Context context) {

		try {
			if (context == null)
				return;
			// 取消弹出的对话框
			InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.toggleSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_NOT_ALWAYS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}