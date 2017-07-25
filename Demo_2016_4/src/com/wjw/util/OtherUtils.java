package com.wjw.util;

public class OtherUtils {
	
	private static long lastClickTime;
	
	/**
	 * 通过两次点击毫秒值间隔判断是否可以点击
	 * 
	 * @param lastTime
	 * @return
	 */
	public static boolean isAllowClickable() {
		long currentTime = System.currentTimeMillis();
		long time = currentTime - lastClickTime	;
		if (0 < time && time< 800) {
			return false;
		}
		lastClickTime = time;
		return true;
	}


}
