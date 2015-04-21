package com.lg.mtogether.server;

import com.lg.mtogether.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;



public class BitmapTurntable extends View{
	Bitmap bitmap;
	int imageX, imageY, width, height;

	public BitmapTurntable(Context context, int imageX, int imageY, int width,
			int height) {
		super(context);
		// mWidth = context.getResources().getDisplayMetrics().widthPixels;
		// mHeight = context.getResources().getDisplayMetrics().heightPixels;
		this.imageX = imageX;
		this.imageY = imageY;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.turntable);
		bitmap = bitmap.createScaledBitmap(bitmap, width, height, true);
		canvas.drawBitmap(bitmap, imageX, imageY, paint);
	}
}
