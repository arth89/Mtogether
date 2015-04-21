package com.lg.mtogether.server;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lg.mtogether.R;

@SuppressWarnings("deprecation")
public class ContolActivity extends Activity implements Callback {

	Button bit1, bit2, bit3, scratch, volUp, volDown, flashON, flashOFF;
	// bit sound 관련
	SoundPool sound;
	int[] soundId = {0,0,0,0};
	int[] streamId = {0,0,0,0};
	int num1 = 0, num2 = 0, num3 = 0; // 첫번째 클릭과 두번째 클릭 구분을 위한 값
	// flash 관련
	private Camera camera;
	Parameters parameters;
	SurfaceHolder mHolder;
	boolean flag;
	// volume 관련
	AudioManager audio;
	
	//TurnTable
	Turntable mTurntable;
	
	// bit sound 관련 method
	public void sound1(int start, int num, int replay) {
		if (start == 0) {
			Log.d("sound", "sound id " + soundId[num]);
			streamId[num] = sound.play(soundId[num], 1.0F, 1.0F, 1, replay, 0.5F);
			// sound1.play의 인자 :
				// soundId1 : load method의 결과. 어떤 음원을 출력할 것인지
				// 1.0F : left volume(0.0~1.0)
				// 1.0F : right volume(0.0~1.0)
				// 1 : priority
				// -1 : 반복횟수. -1은 무한반복을 의미
				// 0.5F : 반복속도(0.5~2.0). 반복 속도를 제어할 수 있음.
			Log.d("sound", "streamId :" + streamId[num]);
		} else if (start ==1) {
			sound.stop(streamId[num]);
		}
	}
	
	//flash 관련 method
	Runnable myThread = new Runnable() {
		@Override
		public void run() {
			while (flag) {
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				camera.startPreview();

				try {
					Thread.sleep(300, 0);
				} catch (InterruptedException e) {
				}

				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.stopPreview();
			}
		}
	};
	protected void onStop() {
		super.onStop();
		if (camera != null) {
			camera.release();
		}
	}
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
		}
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
//		camera.stopPreview();
//		mHolder = null;
//		camera.release();
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_contol);
		
		bit1=(Button)findViewById(R.id.bit1);
		bit2=(Button)findViewById(R.id.bit2);
		bit3=(Button)findViewById(R.id.bit3);
		scratch=(Button)findViewById(R.id.scratch);
		
		//Turntable
		LinearLayout layout = (LinearLayout)findViewById(R.id.layout_turntable);
		mTurntable = new Turntable(this, layout);
		layout.addView(mTurntable);

		
//bit sound
		 sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		 soundId[0] = sound.load(this, R.raw.music1, 1);
		 Log.d("sound", "music1 load :"+soundId[0]);
		 soundId[1] = sound.load(this, R.raw.music2, 1);
		 Log.d("sound", "music2 load :"+soundId[1]);
		 soundId[2] = sound.load(this, R.raw.music3, 1);
		 Log.d("sound", "music3 load :"+soundId[2]);
		 soundId[3] = sound.load(this, R.raw.scratch, 1);
		
		bit1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (num1 == 0) { // 첫번째 클릭인 경우 음악을 재생한다.
					sound1(0, 0, -1);
					num1 = 1;
				} else { // 두번째 클릭인 경우 음악을 멈춘다.
					Log.d("sound", "music1 stop");
					num1 = 0;
					sound1(1, 0, -1);
				}
			}
		});
		
		bit2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (num2 == 0) { // 첫번째 클릭인 경우 음악을 재생한다.
					sound1(0, 1, -1);
					num2 = 1;
				} else { // 두번째 클릭인 경우 음악을 멈춘다.
					Log.d("sound", "music2 stop");
					num2 = 0;
					sound1(1, 1, -1);
				}
			}
		});
		
		bit3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (num3 == 0) { // 첫번째 클릭인 경우 음악을 재생한다.
					sound1(0, 2, -1);
					num3 = 1;
				} else { // 두번째 클릭인 경우 음악을 멈춘다.
					Log.d("sound", "music3 stop");
					num3 = 0;
					sound1(1, 2, -1);
				}
			}
		});
		
		scratch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // 스크래치음은 한 번만 재생한다.
					sound1(0, 3, 1);
			}
		});
	
//volume
		volUp = (Button)findViewById(R.id.volup);
		volDown = (Button)findViewById(R.id.voldown);
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		volUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)+3), AudioManager.FLAG_PLAY_SOUND);
				Log.d("vol", (int)(audio.getStreamVolume(AudioManager.STREAM_RING))+"");
			}
		});
		volDown.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC)-3), AudioManager.FLAG_PLAY_SOUND);
				Log.d("vol", (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC))+"");
			}
		});

		
//flash 
		flashON = (Button)findViewById(R.id.flashon);
		flashOFF = (Button)findViewById(R.id.flashoff);
		
		SurfaceView preview = (SurfaceView) findViewById(R.id.PREVIEW);
		mHolder = preview.getHolder();
		mHolder.addCallback(this);
		
		Log.d("flash", "before" );
		camera = Camera.open();
		Log.d("flash", "after");
		
		parameters = camera.getParameters();
		
		
		flashON.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;
				new Thread(myThread).start();
			}
		});

		
		flashOFF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = false;
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.stopPreview();
			}
		});
		
		
	}


}
