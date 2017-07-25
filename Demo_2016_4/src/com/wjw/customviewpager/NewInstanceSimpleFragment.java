package com.wjw.customviewpager;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NewInstanceSimpleFragment extends Fragment {

	private String mTiles;
	public static final String BUNDLE_TITLE = "title";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		if (bundle != null) {
			mTiles = bundle.getString(BUNDLE_TITLE);
		}

		TextView tv = new TextView(getActivity());
		tv.setTextSize(48);
		tv.setTextColor(Color.RED);
		Random r = new Random();
		tv.setBackgroundColor(Color.argb(r.nextInt(148), r.nextInt(255), r.nextInt(255), r.nextInt(255)));
		tv.setText(mTiles);
		tv.setGravity(Gravity.CENTER);

		return tv;

	}

	/**
	 * 通过title 初始化一个fragment
	 * 
	 * @param title
	 * @return
	 */
	public static NewInstanceSimpleFragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);

		NewInstanceSimpleFragment fragmengt = new NewInstanceSimpleFragment();
		fragmengt.setArguments(bundle);

		return fragmengt;

	}

}
