package com.wjw.textviewhightdemo;

import com.example.ddddd.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreTextView extends LinearLayout {
	

	protected TextView contentView;
	protected ImageView expandView;
	protected TextView expandTextView;
	protected int textColor;
	protected float textSize;
	protected int maxLine;
	protected String text;
	protected boolean isExpand = false;

	public int defaultTextColor = Color.BLACK;
	public int defaultTextSize = 12;
	public int defaultLine = 2;
	
	public MoreTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initalize();
		initWithAttrs(context, attrs);
		bindListener();
	}

	protected void initWithAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MoreTextStyle);
		int textColor = a.getColor(R.styleable.MoreTextStyle_textColor,
				defaultTextColor);
		textSize = a.getDimensionPixelSize(R.styleable.MoreTextStyle_textSize,
				defaultTextSize);
		maxLine = a.getInt(R.styleable.MoreTextStyle_maxLine, defaultLine);
		text = a.getString(R.styleable.MoreTextStyle_text);
		bindTextView(textColor, textSize, maxLine, text);
		a.recycle();
	}

	protected void initalize() {
		setOrientation(VERTICAL);
		setGravity(Gravity.LEFT);
		contentView = new TextView(getContext());
		LinearLayout.LayoutParams llpc = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llpc.setMargins(0, 0, 0, 0);
		addView(contentView, llpc);
		expandTextView = new TextView(getContext());
		int padding = dip2px(getContext(), 5);
		expandTextView.setText("��ʾȫ��");
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, dip2px(getContext(), 8), padding, 0);
		addView(expandTextView, llp);
	}

	public void setExpandTextViewColor(int textColor) {
		expandTextView.setTextColor(textColor);
	}

	public void setToFalse(String text, int position) {
		contentView.setText(text);
		int h = contentView.getLineCount();
		Log.i("MORE_TEXT_VIEW_TAG", "POSITION:" + position);
		Log.i("MORE_TEXT_VIEW_TAG", "line:" + h);
		Log.i("MORE_TEXT_VIEW_TAG", "-------------------");
		if (h > defaultLine) {
			contentView.setHeight(contentView.getLineHeight() * defaultLine);
			expandTextView.setVisibility(View.VISIBLE);
			expandTextView.setText("չ����ʾ");
		}else {
			expandTextView.setVisibility(View.GONE);
		}
//		expandTextView.setText("չ����ʾ");
	}

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

	public void setToFalse() {
		isExpand = false;
		post(new Runnable() {

			@Override
			public void run() {
				expandTextView
						.setVisibility(contentView.getLineCount() > defaultLine ? View.VISIBLE
								: View.GONE);

			}
		});
		// judgeExpandOrNot();
		if (contentView.getLineCount() > defaultLine) {
			contentView.setHeight(contentView.getLineHeight() * defaultLine);
		} else {
			if (contentView.getLineCount() != 0) {
				contentView.setHeight(contentView.getLineHeight()
						* contentView.getLineCount());
			} else {
				contentView.setHeight(contentView.getLineHeight());
			}
		}
		expandTextView.setText("��ʾȫ��");
	}

	protected void bindTextView(int color, float size, final int line,
			String text) {
		contentView.setTextColor(color);
		contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		contentView.setText(text);
		post(new Runnable() {

			@Override
			public void run() {
				expandTextView
						.setVisibility(contentView.getLineCount() > line ? View.VISIBLE
								: View.GONE);

			}
		});
	}

	protected void bindListener() {
		expandTextView.setOnClickListener(new View.OnClickListener() {
			// boolean isExpand;

			@Override
			public void onClick(View v) {
				isExpand = !isExpand;
				judgeExpandOrNot();
			}
		});
	}

	private void judgeExpandOrNot() {
		contentView.clearAnimation();
		final int deltaValue;
		final int startValue = contentView.getHeight();
		int durationMillis = 350;
		if (isExpand) {
			deltaValue = contentView.getLineHeight()
					* contentView.getLineCount() - startValue;
			expandTextView.setText("����");
		} else {
			deltaValue = contentView.getLineHeight() * maxLine - startValue;
			expandTextView.setText("��ʾȫ��");
		}
		Animation animation = new Animation() {
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				contentView.setHeight((int) (startValue + deltaValue
						* interpolatedTime));
			}

		};
		animation.setDuration(durationMillis);
		contentView.startAnimation(animation);
	}

	public TextView getTextView() {
		return contentView;
	}

	public void setText(CharSequence charSequence) {
		contentView.setText(charSequence);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
