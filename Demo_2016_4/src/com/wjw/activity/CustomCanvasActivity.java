package com.wjw.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.example.ddddd.R;
import com.wjw.util.WindowUtils;

@SuppressLint("ClickableViewAccessibility")
public class CustomCanvasActivity extends Activity {

	private ImageView canvas_img1;
	private ImageView canvas_img2;

	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas);

		canvas_img1 = (ImageView) findViewById(R.id.canvas_img1);
		canvas_img2 = (ImageView) findViewById(R.id.canvas_img2);

		
//		initCanvas1();
		initCanvas2();
		
	}

	private void initCanvas2() {
		
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.c);
		
		Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		canvas = new Canvas(createBitmap);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getResources().getColor(R.color.white));
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(6);
		Matrix matrix = new Matrix();
		canvas.drawBitmap(bitmap, matrix, paint);
		canvas.drawLine(800, 100,1000, 200, paint);
		
		canvas_img2.setImageBitmap(createBitmap);
		
		canvas_img2.setOnTouchListener(new MyOnTouchListener());
	}

	private void initCanvas1() {
		bitmap = Bitmap.createBitmap(WindowUtils.getScreenWidth(this),
				WindowUtils.getScreenHeight(this) / 2, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.white));
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4);
		canvas_img1.setImageBitmap(bitmap);
		canvas_img1.setOnTouchListener(new MyOnTouchListener());
		
	}

	class MyOnTouchListener implements OnTouchListener {

		private float downX = 0;
		private float downY = 0;
		private float upX = 0;
		private float upY = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				downX = event.getX();
				downY = event.getY();
				canvas.save();
				Log.i("Canvas", "ACTION_DOWN "+"downX= "+downX+"  downY= "+downY);
				
				break;
			case MotionEvent.ACTION_MOVE:
				
				upX = event.getX();
				upY = event.getY();
				
				Log.i("Canvas", "ACTION_MOVE "+"upX= "+upX+"  upY= "+upY);
				
				canvas.drawLine(downX, downY, upX, upY, paint);
				canvas_img2.invalidate();
				downX = upX;
				downY = upY;

				Log.e("Canvas", "ACTION_MOVE "+"downX= "+downX+"  downY= "+downY);
				break;
			case MotionEvent.ACTION_UP:
				
				upX = event.getX();
				upY = event.getY();
				canvas.drawLine(downX, downY, upX, upY, paint);
				canvas_img2.invalidate();
				canvas.restore();

				Log.i("Canvas", "ACTION_UP "+"upX= "+upX+"  upY= "+upY);
				
				break;
			case MotionEvent.ACTION_CANCEL:
				
				break;
			default:
				break;
			}
			return true;
		}

	}

}
