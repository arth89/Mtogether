package com.lg.mtogether.client;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Utils;
import com.lg.mtogether.receiver.WiFiDirectBroadcastReceiver;

public class DeviceListActivity extends Activity implements PeerListListener,WifiP2pManager.ConnectionInfoListener{
	int num=2;

	IntentFilter mIntentFilter;

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;

	ArrayList<String> peerNameList;
	ArrayList<WifiP2pDevice> peerList;
	ArrayAdapter<String> adapter;
	
	ProgressDialog pd;

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
				
				String title = "���̷�Ʈ ����Ȯ��";
				String content = peerList.get(position).deviceName+" �� �����Ͻðڽ��ϱ�?\n("+
						peerList.get(position).deviceAddress+")";
				DialogSimple(title,content,position);
				
			}});
	}

	private void setMyP2pManager(){

		Utils.setWifiEnable(this,true);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);

		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

		

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
	protected void onPause() {
		
		super.onPause();

		Log.i("TAG", "onpause");
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
	
	private void DialogSimple(String strTitle, String strContent,final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(strTitle)
			.setMessage(strContent)
			.setCancelable(false)
			.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					pd = ProgressDialog.show(DeviceListActivity.this,"Connect", "���� �� �Դϴ�.", true);
					
					WifiP2pConfig config = new WifiP2pConfig();
					config.deviceAddress = peerList.get(position).deviceAddress;
					config.groupOwnerIntent=0;
					mManager.connect(mChannel, config, new ActionListener(){

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onFailure(int reason) {
							// TODO Auto-generated method stub
							Utils.makeToast(DeviceListActivity.this, "������ �����Ͽ����ϴ�. ��� �� �ٽ� �õ��� �ּ���.");
							pd.dismiss();
						}
					});
					
					
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

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// TODO Auto-generated method stub
		
		if(pd==null) return;
		
		
		
		pd.dismiss();
		Intent i = new Intent(DeviceListActivity.this,StandByClientActivity.class);
		i.putExtra("groupOwnerAddress", info.groupOwnerAddress);
		startActivity(i);
		finish();
	}
}
