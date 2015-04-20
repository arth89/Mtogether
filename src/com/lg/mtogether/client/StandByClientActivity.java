package com.lg.mtogether.client;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lg.mtogether.R;

public class StandByClientActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stand_by_client);
		
		Intent receive = getIntent();
		int control = receive.getIntExtra("control", 0);
		int device = receive.getIntExtra("device", 0);
		int num = receive.getIntExtra("num", 0);
		
		TextView title = (TextView)findViewById(R.id.tv_stanby_client_title);
		LinearLayout device1 = (LinearLayout)findViewById(R.id.ll_stanby_client_device1);
		LinearLayout device2 = (LinearLayout)findViewById(R.id.ll_stanby_client_device2);
		LinearLayout device3 = (LinearLayout)findViewById(R.id.ll_stanby_client_device3);
		LinearLayout device4 = (LinearLayout)findViewById(R.id.ll_stanby_client_device4);
		LinearLayout device5 = (LinearLayout)findViewById(R.id.ll_stanby_client_device5);
		
		/** 임시 화면전환용 버튼 */
		////////////////////////////////////
		Button btn = (Button)findViewById(R.id.btn_temp1);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(StandByClientActivity.this,LoadingClientActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		///////////////////////////////////
		
		if (num!=0) {
			switch(num) {
			case 2:
				device3.setVisibility(View.GONE);
			case 3:
				device4.setVisibility(View.GONE);
			case 4:
				device5.setVisibility(View.GONE);
			case 5:
				break;
			}
		}
		
		if (control == 1) {
			title.setText("waiting devices...");
			control=0;
			//연결이 완료되면 음악파일 선택 모드로 넘어감.
		}
		if (device == 1) {
			title.setText("contact ok.. "+"\n"+"waiting other devices"+"\n");
			device=0;
			//연결이 완료되면 재생대기 상태로 넘어감.
		}
		
		
	}

	
}
