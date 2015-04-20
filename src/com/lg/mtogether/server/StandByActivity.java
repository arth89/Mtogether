package com.lg.mtogether.server;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Utils;
import com.lg.mtogether.receiver.WiFiDirectBroadcastReceiver;

public class StandByActivity extends Activity implements GroupInfoListener{

	IntentFilter mIntentFilter;

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;

	TextView groupName;
	TextView title;
	TextView[] device = new TextView[5];

	LinearLayout device1,device2,device3,device4,device5;

	int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stand_by);

		Intent receive = getIntent();
		int control = receive.getIntExtra("control", 0);
		int device = receive.getIntExtra("device", 0);
		num = receive.getIntExtra("num", 0);

		initView();
		setMyP2pManager();
		//WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
		//와이파이다이렉트 그룹만들기
		createWifiP2pGroup();
		//연결대기 &  
		//백키 막기


		/** 임시 화면전환용 버튼 */
		////////////////////////////////////
		Button btn = (Button)findViewById(R.id.btn_temp2);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(StandByActivity.this,LoadingActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		///////////////////////////////////

		


	}

	private void initView(){
		title = (TextView)findViewById(R.id.tv_stanby_server_title);
		groupName = (TextView)findViewById(R.id.tv_stanby_server_groupname);
		device1 = (LinearLayout)findViewById(R.id.ll_stanby_server_device1);
		device2 = (LinearLayout)findViewById(R.id.ll_stanby_server_device2);
		device3 = (LinearLayout)findViewById(R.id.ll_stanby_server_device3);
		device4 = (LinearLayout)findViewById(R.id.ll_stanby_server_device4);
		device5 = (LinearLayout)findViewById(R.id.ll_stanby_server_device5);
		device[0] = (TextView)findViewById(R.id.tv_stand_by_device1);
		device[1] = (TextView)findViewById(R.id.tv_stand_by_device2);
		device[2] = (TextView)findViewById(R.id.tv_stand_by_device3);
		device[3] = (TextView)findViewById(R.id.tv_stand_by_device4);
		device[4] = (TextView)findViewById(R.id.tv_stand_by_device5);

		groupName.setText("[]");
		
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
	}

	private void setMyP2pManager(){

		//와이파이 온
		Utils.setWifiEnable(this,true);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		//필요없는 인텐트필터제외
		//mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		
		setGroupName();
		
		
		
		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this,num);
	}

	private void createWifiP2pGroup(){

		mManager.createGroup(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				//groupName.setText(Utils.getBtName());
				title.setText("연결을 대기중입니다.");
				Toast.makeText(getApplicationContext(),"연결 그룹 구성을 완료하였습니다.", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int reason) {
				title.setText("그룹구성이 원활하지 않습니다.\n잠시후 다시 시도해 주세요.");
			}
		});
	}

	@Override
	public void onGroupInfoAvailable(WifiP2pGroup group) {

		
		
		
		if(group==null){
			return;
		}
		
		//groupName.setText(group.getOwner().deviceName);
		
		ArrayList<WifiP2pDevice> clientList = new ArrayList<WifiP2pDevice>();
		clientList.addAll(group.getClientList());
		
		for(int i=0; i < num;i++){
			device[i].setText("device"+i);
		}
		
		for(int i=0; i < clientList.size();i++){
			device[i].setText(clientList.get(i).deviceName);
		}
		
		if(clientList.size()==num){
			Utils.makeToast(getApplicationContext(), "연결완료");
		}

	}
	
	public void setGroupName(){
		
		String newBtName = "Mtogether_";
		int i = (int)(Math.random()*100.0);
		
		newBtName = newBtName + i;
	    
	    Log.d("UTILS", newBtName);
	    
	    try {
	        Method m = mManager.getClass().getMethod(
	                "setDeviceName",
	                new Class[] { WifiP2pManager.Channel.class, String.class,
	                        WifiP2pManager.ActionListener.class });

	        m.invoke(mManager,mChannel,newBtName, new WifiP2pManager.ActionListener() {
	            public void onSuccess() {
	                //Code for Success in changing name
	            }

	            public void onFailure(int reason) {
	                //Code to be done while name change Fails
	            }
	        });
	    } catch (Exception e) {

	        e.printStackTrace();
	    }
	}
	
	public void setMyDevice(String myDevice){
		groupName.setText(myDevice);
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
