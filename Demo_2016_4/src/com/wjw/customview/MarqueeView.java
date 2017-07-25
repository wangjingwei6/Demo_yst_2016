package com.wjw.customview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.ddddd.R;
import com.wjw.activity.ScrollVerticalTextViewActivity;
import com.wjw.activity.WebViewActivity;

public class MarqueeView extends RecyclerView {

	private Context mContext;
	private InnerAdapter mAdapter;
	private static final Handler HANDLER = new Handler(Looper.getMainLooper());
	private Runnable mRunnable;

	public MarqueeView(Context context) {
		this(context, null);
	}

	public MarqueeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mAdapter = new InnerAdapter(mContext);
		setAdapter(mAdapter);
		setLayoutManager(new LinearLayoutManager(mContext,
				LinearLayoutManager.VERTICAL, false));

		mRunnable = new Runnable() {
			@Override
			public void run() {
				scrollBy(3, 3);
				HANDLER.postDelayed(this, 100);
			}
		};
	}

	public void setListDesc(List<String> list) {
		mAdapter.setList(list);
	}

	public void setListUrl(List<String> list) {
		mAdapter.setUrlList(list);
	}

	public void startScroll() {
		HANDLER.postDelayed(mRunnable, 100);
	}

	public void stopScroll() {
		HANDLER.removeCallbacks(mRunnable);
	}

	private static final class InnerAdapter extends Adapter {

		private Context mContext;
		private List<String> mList = new ArrayList<String>();
		private List<String> urlsList = new ArrayList<String>();

		public InnerAdapter(Context context) {
			mContext = context;
		}

		public void setList(List<String> list) {
			if (list != null) {
				mList.clear();
				mList.addAll(list);
				notifyDataSetChanged();
			}
		}

		public void setUrlList(List<String> list) {
			if (urlsList != null) {
				urlsList.clear();
				urlsList.addAll(list);
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.scroll_recycle_item, parent, false);
			return new InnerViewHolder(view);
		}

		@Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            ((InnerViewHolder) holder).getView().setText(mList.get(position % mList.size()));
            ((InnerViewHolder) holder).getView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	Intent intent = new Intent(mContext,
        					WebViewActivity.class);
        			intent.putExtra("url", urlsList.get(position%urlsList.size()));
        			intent.putExtra("isShowTitle", 1);
        			mContext.startActivity(intent);
                }
            });
        }

		@Override
		public int getItemCount() {
			return mList.size() == 0 ? 0 : Integer.MAX_VALUE;
		}
	}

	private static final class InnerViewHolder extends ViewHolder {
		private TextView mView;

		public InnerViewHolder(View itemView) {
			super(itemView);
			mView = (TextView) itemView.findViewById(R.id.item_tv);
		}

		public TextView getView() {
			return mView;
		}
	}
}
