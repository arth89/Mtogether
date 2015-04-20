package com.lg.mtogether.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Utils;
import com.lg.mtogether.receiver.WiFiDirectBroadcastReceiver;

public class DeviceListActivity extends Activity implements PeerListListener{
	int num=2;

	IntentFilter mIntentFilter;

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;

	ArrayList<String> peerNameList;
	ArrayList<WifiP2pDevice> peerList;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devicelist);

		peerNameList = new ArrayList<String>();
		peerList = new ArrayList<WifiP2pDevice>();
		setMyP2pManager();




		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peerNameList);
		ListView contactlist = (ListView)findViewById(R.id.lv_device_contactlist);
		contactlist.setAdapter(adapter);

		contactlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//컨트롤러에서 기기 개수를 선택하지 않은 경우 넘어가면 안됨.
				//통신완료되면 다음 activity로 넘어감.
				//통신과정에서 컨트롤러에서 선택한 기기 개수 정보가 함께 넘어와야 함.
				if (num!=0) {
					Intent i = new Intent(DeviceListActivity.this, StandByClientActivity.class);
					i.putExtra("device", 1);
					i.putExtra("num", num);
					startActivity(i);
				} else {
					Toast.makeText(DeviceListActivity.this, "wating...", Toast.LENGTH_SHORT).show();
				}
			}});
	}

	private void setMyP2pManager(){

		//와이파이 온
		Utils.setWifiEnable(this,true);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		//필요없는 인텐트필터제외
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		//mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		//mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);

		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(DeviceListActivity.this, "Mtogether 컨트롤러를 검색합니다.",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int reasonCode) {
				Toast.makeText(DeviceListActivity.this, 
						"연결상태가 원활하지 않습니다.\n잠시 후 다시 시도해 주세요." + 
								"\nErrorCode:"+reasonCode,Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// TODO Auto-generated method stub
		peerNameList.clear();
		peerList.clear();


		peerList.addAll(peers.getDeviceList());

		for(int i = 0; i < peerList.size();i++){
			peerNameList.add(peerList.get(i).deviceName);

		}

		adapter.notifyDataSetChanged();



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
		
		mManager.stopPeerDiscovery(mChannel, new ActionListener() {
			
			@Override
			public void onSuccess() {
				Utils.makeToast(getApplicationContext(), "기기검색을 종료합니다.");
			}
			
			@Override
			public void onFailure(int reason) {
				
			}
		});
	}
}
