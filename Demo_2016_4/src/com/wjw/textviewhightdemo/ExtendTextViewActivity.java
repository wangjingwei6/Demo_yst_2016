package com.wjw.textviewhightdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.ddddd.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ExtendTextViewActivity extends Activity {

	private ListView lvList;
	private List<TModel> txtList;
	private TextAdapter adapter;
	private Random random;
	private static final String ENTERKEY = "\n";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extend_text_main);
		random = new Random(12345);
		lvList = (ListView) findViewById(R.id.lv_list);

		txtList = new ArrayList<TModel>(50);
		for (int i = 0; i < 50; i++) {
			TModel t = new TModel();
			t.content = radomLinesCreator();
			t.isExpand = false;
			txtList.add(t);
		}

		adapter = new TextAdapter(this, txtList);
		lvList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private String radomLinesCreator() {
		int index = 0;

		while (true) {
			index = random.nextInt(4);
			if (index != 0) {
				break;
			}
		}
		String str = (index + 1) + "";

		for (int i = 0; i < index; i++) {
			str += ENTERKEY + "LINE";
		}
		// Log.i("RADOM_TAG", "index:" + random.nextInt(5));
		return str;
	}
}
