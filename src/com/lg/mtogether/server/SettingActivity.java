package com.lg.mtogether.server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Utils;

public class SettingActivity extends Activity {

	int num;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		RadioGroup radiogroup = (RadioGroup)findViewById(R.id.tv_setting_deviceNumber);
		RadioButton two = (RadioButton)findViewById(R.id.tv_setting_two);
		RadioButton three = (RadioButton)findViewById(R.id.tv_setting_three);
		RadioButton four = (RadioButton)findViewById(R.id.tv_setting_four);
		RadioButton five = (RadioButton)findViewById(R.id.tv_setting_five);
		Button start = (Button)findViewById(R.id.tv_setting_start);
		
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.tv_setting_two:
					num=2;
					break;
				case R.id.tv_setting_three:
					num=3;
					break;
				case R.id.tv_setting_four:
					num=4;
					break;
				case R.id.tv_setting_five:
					num=5;
					break;
				}
			}
		});
		
		
		start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(num) {
				case 0:
					Toast.makeText(SettingActivity.this, "select number of device", Toast.LENGTH_SHORT).show();
					break;
				default:
					Intent i = new Intent(SettingActivity.this, StandByActivity.class);
					i.putExtra("control", 1);
					i.putExtra("num", num);
					startActivity(i);
					
				}
			}
		});
	}
}
