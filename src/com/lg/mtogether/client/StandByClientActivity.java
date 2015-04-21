package com.lg.mtogether.client;

import java.net.InetAddress;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Utils;
import com.lg.mtogether.receiver.WiFiDirectBroadcastReceiver;

public class StandByClientActivity extends Activity implements ConnectionInfoListener {

	
	IntentFilter mIntentFilter;

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stand_by_client);		
		setMyP2pManager();
		
		i=getIntent();
		InetAddress ServerIP = i.getParcelableExtra("groupOwnerAddress");
		Log.i("SERVER IP ",ServerIP.toString());
		
		
		
		TextView title = (TextView)findViewById(R.id.tv_stanby_client_title);
		/** �ӽ� ȭ����ȯ�� ��ư */
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
		
		title.setText("�׷쿬�� �������Դϴ�.");
		
	}

	
	private void setMyP2pManager(){

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		
		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// TODO ���������ÿ��� �� �ݹ�޼ҵ尡 ȣ��˴ϴ�. �������� ���� �ڵ�� �̰���
		Log.i("STANDBYCLIENTACTIVITY", "��������!!!!");
		
		
		Utils.makeToast(getApplicationContext(), "������ �����Ǿ����ϴ�.");
		finish();
		
	}




	
}
