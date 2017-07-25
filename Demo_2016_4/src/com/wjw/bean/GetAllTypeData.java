package com.wjw.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ddddd.R;
public class GetAllTypeData {
	
	private static GetAllTypeData mGetAllTypeData = null;

	public static GetAllTypeData getInstance() {
		if (mGetAllTypeData == null) {
			mGetAllTypeData = new GetAllTypeData();
		}
		return mGetAllTypeData;
	}


	public int[] getImgIDs() {
		int[] imgIDs = new int[] { R.drawable.dog8, R.drawable.dog12,
				R.drawable.dog13, R.drawable.dog11, R.drawable.dog14,
				R.drawable.dog15, R.drawable.dog16, R.drawable.dog20,
				R.drawable.dog21, R.drawable.face10 };

		return imgIDs;
	}
	

}
