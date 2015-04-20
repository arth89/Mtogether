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
		builder.setTitle("���� ����")
			.setMessage("������ �����Ͻðڽ��ϱ�?")
			.setCancelable(false)
			.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					//TODO ���������۾�
					
					
					finish();
				}
			})
			.setNegativeButton("���", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
