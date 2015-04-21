package com.lg.mtogether.receiver;

import com.lg.mtogether.client.DeviceListActivity;
import com.lg.mtogether.client.StandByClientActivity;
import com.lg.mtogether.server.StandByActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	static final String TAG = "WIFIDirectBroadcastReceiver";

	private WifiP2pManager mManager;
	private Channel mChannel;
	private StandByActivity mSeverActivity;
	private DeviceListActivity mClientActivity;
	private StandByClientActivity mClientWaitActivity;
	private int deviceNum;
	int activityFlag = 0;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			StandByClientActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mClientWaitActivity = activity;
		this.activityFlag = 2;
	}
	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			DeviceListActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mClientActivity = activity;
		this.activityFlag = 1;
	}

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			StandByActivity activity,int deviceNum) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mSeverActivity = activity;
		this.deviceNum = deviceNum;
		this.activityFlag = 0;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi P2P is enabled
			} else {
				// Wi-Fi P2P is not enabled
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			if (mManager != null) {
				mManager.requestPeers(mChannel, mClientActivity);
				Log.i(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			// Respond to new connection or disconnections
			if (mManager != null){

				if(activityFlag==0){
					mManager.requestGroupInfo(mChannel, mSeverActivity);
				}
				else if(activityFlag==1){
					mManager.requestConnectionInfo(mChannel, mClientActivity);
				}
				else if(activityFlag==2){
					NetworkInfo netInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
					if(!netInfo.isConnected()){
						mManager.requestConnectionInfo(mChannel, mClientWaitActivity);
					}
				}
				Log.i(TAG,"WIFI_P2P_CONNECTION_CHANGED_ACTION");
			}

			



		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			// Respond to this device's wifi state changing
			if (mManager != null){
				if(activityFlag==0){
					WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);	
					mSeverActivity.setMyDevice(device.deviceName);
				}
				else{

				}
				Log.i(TAG,"WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
			}
		}
	}

}
