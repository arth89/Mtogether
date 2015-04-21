package com.lg.mtogether.server;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lg.mtogether.R;

public class ContolActivity extends Activity {

	Button bit1, bit2, bit3, scratch;
	SoundPool sound;
	int[] soundId = {0,0,0,0};
	int[] streamId = {0,0,0,0};
	int num1 = 0, num2 = 0, num3 = 0; // 첫번째 클릭과 두번째 클릭 구분을 위한 값
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_contol);
		
		bit1=(Button)findViewById(R.id.bit1);
		bit2=(Button)findViewById(R.id.bit2);
		bit3=(Button)findViewById(R.id.bit3);
		scratch=(Button)findViewById(R.id.scratch);
		
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contol, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
