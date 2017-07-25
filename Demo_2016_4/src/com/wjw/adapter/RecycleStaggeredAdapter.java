package com.wjw.adapter;

import java.util.List;

import com.example.ddddd.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wjw.bean.RecycleViewBean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//================设置Grid适配器   瀑布流的效果

public class RecycleStaggeredAdapter extends RecyclerView.Adapter<RecycleStaggeredAdapter.ItemViewHolder> {

	private List<RecycleViewBean> recycleViewBean;
	private int column;
	private Context mContext;
	private ImageLoader mImageLoader;
	
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view,int position);
	}

	public OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public RecycleStaggeredAdapter(List<RecycleViewBean> bean,Context context) {
		this.recycleViewBean = bean;
		this.mContext =context;
		initImageLoader();
	}

	@Override
	public int getItemCount() {
		return recycleViewBean.size();
	}

	public void onBindViewHolder(final ItemViewHolder staggeredHolder,  int position) {
		
		setUpItemClick(staggeredHolder);
		
		mImageLoader.displayImage("http://yst-images.img-cn-hangzhou.aliyuncs.com/"+recycleViewBean.get(position).getImgUrl(), staggeredHolder.imageView);
	}

	public ItemViewHolder onCreateViewHolder(ViewGroup viewgroup, int arg1) {
		// View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.recycle_item,null);
		View view = LayoutInflater.from(viewgroup.getContext()).inflate(
				R.layout.recycle_item, viewgroup, false);
		ItemViewHolder mGridItemViewHolder = new ItemViewHolder(view);
		return mGridItemViewHolder;
	}

	@SuppressWarnings("rawtypes")
	public class ItemViewHolder extends RecyclerView.ViewHolder {
		private ImageView imageView;
		private TextView textView;

		public ItemViewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.recycle_item_img);
		}

	}

	protected void setUpItemClick(final ItemViewHolder staggeredHolder) {
		if(mOnItemClickLitener!=null){
			staggeredHolder.itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int layoutPosition = staggeredHolder.getPosition();
					mOnItemClickLitener.onItemClick(staggeredHolder.itemView, layoutPosition);
				}
			});
			
			staggeredHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					int layoutPosition = staggeredHolder.getPosition();
					mOnItemClickLitener.onItemLongClick(staggeredHolder.itemView, layoutPosition);
					return false;
				}
			});
		}
	}

	
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.experience_default)
				.showImageOnFail(R.drawable.experience_default)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
		
	}
	
}
