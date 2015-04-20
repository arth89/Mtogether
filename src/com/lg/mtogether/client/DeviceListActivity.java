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
				//��Ʈ�ѷ����� ��� ������ �������� ���� ��� �Ѿ�� �ȵ�.
				//��ſϷ�Ǹ� ���� activity�� �Ѿ.
				//��Ű������� ��Ʈ�ѷ����� ������ ��� ���� ������ �Բ� �Ѿ�;� ��.
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

		//�������� ��
		Utils.setWifiEnable(this,true);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		//�ʿ���� ����Ʈ��������
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		//mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		//mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);

		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(DeviceListActivity.this, "Mtogether ��Ʈ�ѷ��� �˻��մϴ�.",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int reasonCode) {
				Toast.makeText(DeviceListActivity.this, 
						"������°� ��Ȱ���� �ʽ��ϴ�.\n��� �� �ٽ� �õ��� �ּ���." + 
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
				Utils.makeToast(getApplicationContext(), "���˻��� �����մϴ�.");
			}
			
			@Override
			public void onFailure(int reason) {
				
			}
		});
	}
}
