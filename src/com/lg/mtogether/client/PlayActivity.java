package com.lg.mtogether.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lg.mtogether.R;

public class PlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		
		Button btn = (Button)findViewById(R.id.btn_play_disconnect);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogSimple();
			}
		});
		
		
		
	}

	private void DialogSimple() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("연결 해제")
			.setMessage("연결을 해제하시겠습니까?")
			.setCancelable(false)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					//TODO 연결해제작업
					
					
					finish();
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
