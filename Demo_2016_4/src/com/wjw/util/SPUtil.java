package com.wjw.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtil {
	private SharedPreferences sharedPreferences;
	private Context context;
	private String preFileName;
	private Editor edit;
	private static SPUtil mSPUtil;

	// public SPUtil(Context context, String sharePreFileName){
	// this.context = context;
	// this.preFileName = sharePreFileName;
	// refresh();
	// getEditor();
	// }
	private SPUtil(Context context, String sharePreFileName) {
		this.context = context;
		this.preFileName = sharePreFileName;
		getSharedPreferences();
		getEditor();
	}

	public static synchronized SPUtil getSpUtil(Context context, String sharePreFileName) {

		if (mSPUtil == null) {
			synchronized (SPUtil.class) {

				if (mSPUtil == null) {
					mSPUtil = new SPUtil(context, sharePreFileName);
				}
			}
		}
		return mSPUtil;
	}

	@SuppressLint("CommitPrefEdits")
	public Editor getEditor() {
		edit = sharedPreferences.edit();
		return edit;
	}

	@SuppressWarnings("static-access")
	public void getSharedPreferences() {
		sharedPreferences = context.getSharedPreferences(preFileName, context.MODE_PRIVATE);
	}

	public String getStringValue(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}

	public boolean getBooleanValue(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}

	public float getFloatValue(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}

	public int getIntValue(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}

	public long getLongValue(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}

	public boolean writeBooleanValue(String key, boolean value) {
		return sharedPreferences.edit().putBoolean(key, value).commit();
	}

	public boolean writeStringValue(String key, String value) {
		return sharedPreferences.edit().putString(key, value).commit();
	}

	public boolean writeStringValueSimple(String key, String value) {
		return edit.putString(key, value).commit();
	}

	public boolean writeFloatValue(String key, float value) {
		return sharedPreferences.edit().putFloat(key, value).commit();
	}

	public boolean writeLongValue(String key, long value) {
		return sharedPreferences.edit().putLong(key, value).commit();
	}

	public boolean writeIntValue(String key, int value) {
		return sharedPreferences.edit().putInt(key, value).commit();
	}

	@SuppressWarnings("rawtypes")
	public Map getAll() {
		return sharedPreferences.getAll();
	}

	public boolean contains(String key) {
		return sharedPreferences.contains(key);
	}

	public boolean delete(String key) {
		return sharedPreferences.edit().remove(key).commit();
	}

	public boolean removeAll() {
		return sharedPreferences.edit().clear().commit();
	}

	
	
	
	
	// ========================================================================HY

	public static final String FILE_NAME = "share_data";
	

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}
