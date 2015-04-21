package com.lg.mtogether.server;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lg.mtogether.R;

public class Turntable extends ImageView {

	private float mThreshold = 3f;
	String TAG = "TurnTable";
	// private float mFromX, mFromY, mToX, mToY;
	private float mCentX, mCentY, mRadius;
	private Animation mAnimation;
	float degree;
	// private float mDegree;
	// private long mFromTime;
	// private long mToTime;
	SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
	int soundId;
	int streamId;

	Context context;
	BitmapTurntable bitmapTurntable;
	int imageHeight, imageWidth;
	int imageXPosition, imageYPosition;
	LinearLayout layout;

	public Turntable(Context context, LinearLayout layout) {
		super(context);
		setImageResource(R.drawable.turntable);
		setOnTurntableTouchListener();
		this.context = context;
		this.layout = layout;
		mAnimation = AnimationUtils.loadAnimation(context,
				R.anim.turntable_rotate);
		startAnimation(mAnimation);
		soundId = sound.load(context, R.raw.scratch, 1);

		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				getViewTreeObserver().removeOnPreDrawListener(this);
				imageHeight = getMeasuredHeight();
				imageWidth = getMeasuredWidth();
				return true;
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		getTurntableLocationInfo();
	}

	private void setOnTurntableTouchListener() {

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				float x = event.getX();
				float y = event.getY();
				if (!isInTurntable(event.getX(), event.getY())) {

					return false;

				}
				imageXPosition = getLeft();
				imageYPosition = getTop();

				// Log.d(TAG, "Degree test : " + getDegree(event.getX(),
				// event.getY()));

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:

					// mFromTime = mToTime = System.currentTimeMillis();
					// mFromX = mToX = event.getX();
					// mFromY = mToY = event.getY();
					setVisibility(View.GONE);
					bitmapTurntable = new BitmapTurntable(context,
							imageXPosition, imageYPosition, imageWidth,
							imageHeight);
					layout.addView(bitmapTurntable);
					streamId = sound.play(soundId, 1.0F, 1.0F, 1, -1, 0.8F);
					return true;

				case MotionEvent.ACTION_MOVE:

					// mToTime = System.currentTimeMillis();
					// mToX = event.getX();
					// mToY = event.getY();
					//
					// if( (mToTime - mFromTime) < mThreshold )
					// return false;

					// mFromTime = mToTime;
					// float degree = getDegree(mToX, mToY) - getDegree(mFromX,
					// mFromY);
					// if( Math.abs(degree) < mThreshold )
					// return false;
					// if( degree > 0 ) {
					//
					//
					// Log.d(TAG, "turntable up");
					// } else {
					//
					// Log.d(TAG, "turntable down");
					// }
					//
					// mFromX = mToX;
					// mFromY = mToY;
					//
					// float rotate = mDegree + degree;
					
					if (x > imageXPosition && x < imageXPosition + imageWidth
							&& y > imageYPosition
							&& y < imageYPosition + imageHeight) {
						degree = getDegree(event.getX(), event.getY());
						bitmapTurntable.setRotation(degree);
					}
					// mDegree = getRotation();

					return true;

				case MotionEvent.ACTION_UP:
					bitmapTurntable.setVisibility(View.GONE);
					sound.stop(streamId);
					return true;
				}
				return false;
			}
		});
	}

	private void getTurntableLocationInfo() {

		mRadius = (float) this.getWidth() / 2;
		mCentY = this.getY() + mRadius;
		mCentX = mCentY;

		Log.d(TAG, "(x,y) : (" + mCentX + "," + mCentY + ")," + "radius : "
				+ mRadius);
	}

	private boolean isInTurntable(float x, float y) {

		float sqrX = (x - mCentX) * (x - mCentX);
		float sqrY = (y - mCentY) * (y - mCentY);
		float sqrR = mRadius * mRadius;

		if (sqrX + sqrY > sqrR)
			return false;

		sqrR = (mRadius / 5f) * (mRadius / 5f);

		if (sqrX + sqrY < sqrR)
			return false;

		return true;
	}

	private float getDegree(float x, float y) {

		float startPointX = (imageXPosition + imageWidth) / 2;
		float startPointY = (imageYPosition + imageHeight) / 2;
		float gapX = x - startPointX;
		float gapY = y - startPointY;

		float degree = Math
				.abs((float) (Math.atan2(gapX, gapY) / Math.PI * 180 - 180));

		return degree;
	}

}
