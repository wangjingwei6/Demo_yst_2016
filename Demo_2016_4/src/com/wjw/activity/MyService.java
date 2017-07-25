package com.wjw.activity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyService extends IntentService {

	int j;
	public static int m = 0;
	public static int y = 0;

	public MyService() {
		super("MyService");
	}

	
	public MyService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int x= intent.getIntExtra("num",0);
		Log.i("Serivce","num== "+x);
		j = x;

		switch (x) {
		case 666:
			Log.i("Serivce","	case 666");
			m = j+1000;
			break;

		case 888:
			Log.i("Serivce","	case 888");
			y = j+1000;
			break;

		default:
			break;
		}

	}

}
