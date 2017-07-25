package com.wjw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class LoginCheckUtil {
	
	    // 匹配手机号
		public static final String SHOUJIHAO = "^1[34589]\\d{9}$";
		// 移动
		public static final String YIDONG = "^1((34[0-8]|(3[5-9]|47|5[012789]|8[23478])[0-9]))\\d{7}$";
		// 联通
		public static final String LIANTONG = "^1(3[0-2]|5[56]|8[56]|45)\\d{8}$";
		// 电信
		public static final String DIANXIN = "^1(33|53|8[019])\\d{8}$";
	

	/**
	 * 检验是否是手机号 
	 * @param mobiles 
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,3,6,7,8]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 登陆本地校验
	 * @param mAccountStr
	 * @param mPasswordStr
	 * @return
	 */
	public static String checkUserLogin(String mAccountStr, String mPasswordStr){
		if(TextUtils.isEmpty(mAccountStr)&&TextUtils.isEmpty(mPasswordStr)){
				return "请输入手机号和密码";
		}else{
			if(!TextUtils.isEmpty(mAccountStr) && !TextUtils.isEmpty(mPasswordStr) && mAccountStr.length()==11 && mPasswordStr.length() == 6){
				if(mAccountStr.matches(SHOUJIHAO)){
					if (mAccountStr.matches(YIDONG)||mAccountStr.matches(LIANTONG)||mAccountStr.matches(DIANXIN)){
						return "200";
					} else {
						return "非三大运营商充值号码？";
					}
				}else{
					return "请输入有效的手机号";
				}
			}else{
				if(TextUtils.isEmpty(mAccountStr)){
					return "请输入手机号!";
				}else if(mAccountStr.length()!=11){
					return "手机号码输入有误";
				}else if(TextUtils.isEmpty(mPasswordStr)|| mPasswordStr.length() != 6){
					return "密码格式不正确";
				}else{
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 注册本地校验
	 * @param mAccountStr
	 * @param mPasswordStr
	 * @param mRePasswordStr
	 * @return
	 */
	public static String checkUserRegister(String mAccountStr, String mPasswordStr,String mRePasswordStr){
		
		if (TextUtils.isEmpty(mAccountStr) || !isMobileNO(mAccountStr)) {
			return "请输入有效的手机号!";
		}

		if (TextUtils.isEmpty(mPasswordStr)) {
			return "请输入密码!";
		}
		
		if(mPasswordStr.length() !=6){
			return "密码格式不对,请输入6位!";
		}
		
		if (!mPasswordStr.equals(mRePasswordStr)) {
			return "2次输入的密码不一致，请重新输入!";
		}
		
		return "200";
	}
	
	
	
}
