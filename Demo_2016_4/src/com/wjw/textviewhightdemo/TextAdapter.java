package com.wjw.textviewhightdemo;

import java.util.List;

import com.wjw.textviewhightdemo.MyTextView.OnStateChangeListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class TextAdapter extends BaseAdapter {

	private List<TModel> list;
	private Context context;

	public TextAdapter(Context context, List<TModel> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public TModel getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InlinedApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new MyTextView(context);
		}
		final TModel tmodel = getItem(position);
		((MyTextView) convertView).setText(getItem(position).content);
		((MyTextView) convertView).setOnStateChangeListener(new OnStateChangeListener() {
					@Override
					public void onStateChange(boolean isExpand) {
						tmodel.isExpand = isExpand;
					}
				});
		((MyTextView) convertView).setIsExpand(tmodel.isExpand);
		return convertView;
	}
}
