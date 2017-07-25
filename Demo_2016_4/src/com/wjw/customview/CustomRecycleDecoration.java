package com.wjw.customview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomRecycleDecoration extends RecyclerView.ItemDecoration {

	private int space;

	
	public static int Orientation = -1; //方向
	public int HORIZONTAL = 6;
	public int VERTICAL = 8;
	
	public static int Column = 2; // 布局列数
	
	public static int Type = 0;
	public int LinearLayoutManager = 0;
	public int GridLayoutManager = 1;
	public int StaggeredGridLayoutManager = 2;

	public CustomRecycleDecoration(int space) {
		this.space = space;
		initType();
	}

	private void initType() {
		if(Column==1){
			Type = LinearLayoutManager;
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
			RecyclerView.State state) {
		

		switch (Type) {
		case 0:// TYPE_LinearLayoutManager
			if(Orientation==VERTICAL){
				
				outRect.top = space;
				outRect.left = space/2; 
				outRect.right = space/2;
				outRect.bottom = 0;
				
			}else if(Orientation==HORIZONTAL){
				outRect.top = 0;
				outRect.left = space; 
				outRect.bottom = 0;
				outRect.right = 0;
			}else{
				
			}

			break;
		case 1:// TYPE_GridLayoutManager

			if (parent.getChildPosition(view) % Column == 0) {
				outRect.left = space;
			} else {
				outRect.left = space/3; 
			}
			outRect.right = space;
			outRect.top = space;

			break;
		case 2:// TYPE_StaggeredGridLayoutManager
			if (parent.getChildPosition(view) % Column == 0) {
				outRect.left = space/3;
			} else {
				outRect.left = space;
			}
			outRect.right = space;
			outRect.top = space;

			break;

		default:
			break;
		}

	}
}
