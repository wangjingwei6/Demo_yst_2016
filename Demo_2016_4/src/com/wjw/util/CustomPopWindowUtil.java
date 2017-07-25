package com.wjw.util;

import java.util.ArrayList;

import com.example.ddddd.R;
import com.wjw.adapter.PopupWindowGroupAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class CustomPopWindowUtil {
	private static View mView;
	private static PopupWindow mPopupWindow;
	private static ListView mListView;
	
	/**
	 * 显示popwindow
	 * 
	 * @param parent
	 */
	public static void showWindow(final Context context,View parent,final ArrayList<String> groups) {

		if (mPopupWindow == null) {
			mView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popwindow_group_list, null);
		}
		
		mListView = (ListView) mView.findViewById(R.id.lvGroup);
		PopupWindowGroupAdapter groupAdapter = new PopupWindowGroupAdapter(context, groups);
		mListView.setAdapter(groupAdapter);
		mPopupWindow = new PopupWindow(mView, 320, 400);

		// 使其聚集
		mPopupWindow.setFocusable(true);
		// 设置允许在外点击消失
		mPopupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 如果有edittext，阻挡输入法遮挡
		
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		int xPos = windowManager.getDefaultDisplay().getWidth() / 2- mPopupWindow.getWidth() / 2; 偏移量 x
		mPopupWindow.showAsDropDown(parent, 0, 0); //x 横坐标偏移 y纵坐标偏移 

		mPopupWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Toast.makeText(context,"groups.get(position)" + groups.get(position), 1000)
						.show();

				if (mPopupWindow != null) {
					mPopupWindow.dismiss();
				}
			}
		});
	}

}
