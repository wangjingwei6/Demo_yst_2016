package com.wjw.textviewhightdemo;

import com.example.ddddd.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyTextView extends FrameLayout {

	TextView contentView; // 内容文本
	TextView openView; // “显示全文”或“收起”文本
	protected boolean isExpand = false; // 显示或收起标记
	private int defaultLine = 2; // 显示的行数,超过就隐藏

	public MyTextView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.extend_text_layout, this);
		contentView = (TextView) findViewById(R.id.content_text);
		openView = (TextView) findViewById(R.id.open_view);

		// 监听显示或收起按钮事件
		openView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isExpand = !isExpand;
				if (onStateChangeListener != null) {
					onStateChangeListener.onStateChange(isExpand);
				}
				if (isExpand) {
					contentView.setLines(contentView.getLineCount());
					openView.setText("收起");
				} else {
					contentView.setLines(defaultLine);
					openView.setText("展开显示");
				}
			}
		});
	}

	// 改变当前标记的值，并判断当前处于何种状态
		public void setIsExpand(boolean isExpand) {
			this.isExpand = isExpand;
			if (isExpand) {
				contentView.setLines(contentView.getLineCount());
				openView.setText("收起");
			} else {
				contentView.setLines(defaultLine);
				openView.setText("展开显示");
			}
		}
	
	// 给内容文本框赋值
	public void setText(String str) {
		contentView.setText(str);

		int count = contentView.getLayout() == null ? getLineNumber()
				: contentView.getLineCount();
		if (count > defaultLine) {
			contentView.setLines(defaultLine);
			openView.setVisibility(View.VISIBLE);
		} else {
			openView.setVisibility(View.GONE);
		}
	}

	// 计算内容文本框的占用的行数
	private int getLineNumber() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.AT_MOST);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		contentView.measure(widthMeasureSpec, heightMeasureSpec);
		int lineHeight = contentView.getLineHeight();
		int lineNumber = contentView.getMeasuredHeight() / lineHeight;
		return lineNumber;
	}

	// 改变状态的接口
	public interface OnStateChangeListener {
		void onStateChange(boolean isExpand);
	}

	public OnStateChangeListener onStateChangeListener;

	public void setOnStateChangeListener(
			OnStateChangeListener onStateChangeListener) {
		this.onStateChangeListener = onStateChangeListener;
	}

	
}
