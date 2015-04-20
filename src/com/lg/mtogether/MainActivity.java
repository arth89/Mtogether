package com.lg.mtogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lg.mtogether.client.DeviceListActivity;
import com.lg.mtogether.constant.Utils;
import com.lg.mtogether.server.SettingActivity;


public class MainActivity extends Activity {

	Button btn,btn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Utils.setWifiEnable(this,false);
		Utils.setWifiEnable(this,true);

		btn = (Button)findViewById(R.id.button1);
		btn2 = (Button)findViewById(R.id.button2);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,SettingActivity.class));

			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,DeviceListActivity.class));

			}
		});




	}


}
