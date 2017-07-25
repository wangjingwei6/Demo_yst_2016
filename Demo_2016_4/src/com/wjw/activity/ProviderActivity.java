package com.wjw.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ddddd.R;
import com.wjw.activity.MainActivity.ViewHolder;

public class ProviderActivity extends Activity{

	private ListView listView;
	private List<Map<String, String>> allList = new ArrayList<Map<String,String>>();
	private static final String[] PHONES_PROJECTION = new String[] {
		Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.provider);
		
		initData();
		
		listView = (ListView)findViewById(R.id.list);
		MyAdapter adapter = new MyAdapter();
		listView.setAdapter(adapter);
		
	}
	
	private void initData() {
		
		Map<String,String> map = null;
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
		
		if(cursor!=null){
			while(cursor.moveToNext()){
			 String phoneNum = cursor.getString(1);
				if(TextUtils.isEmpty(phoneNum)){
					continue;
				}
				String userName  = cursor.getString(0);
				
				Log.i("ProviderActivity", "userId = "+ cursor.getLong(2));
				map = new HashMap<String, String>();
				map.put("phoneNum", phoneNum);
				map.put("userName", userName);
				Log.i("ProviderActivity", "****"+allList.add(map));
			}
		}
		cursor.close();
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return allList.size();
		}

		@Override
		public Object getItem(int position) {
			return allList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyViewHolder viewholder = null;
			if(convertView==null){
				convertView = LayoutInflater.from(ProviderActivity.this).inflate(R.layout.provider_item,null);
			}
			viewholder = (MyViewHolder) getHolder(viewholder,convertView);
			
			viewholder.phoneNum.setText(allList.get(position).get("phoneNum"));
			viewholder.userName.setText(allList.get(position).get("userName"));
			
			return convertView;
		}

		private ViewHolder getHolder(ViewHolder viewHolder,View view) {
			if(viewHolder==null){
				viewHolder  = new MyViewHolder(view);
			}
			return viewHolder;
		}
		
		class MyViewHolder extends ViewHolder{
			private TextView phoneNum;
			private TextView userName;
			
			public MyViewHolder(View view){
				phoneNum = (TextView) view.findViewById(R.id.phonenum);
				userName = (TextView) view.findViewById(R.id.username);
			}
		}
		
		
	}
}
