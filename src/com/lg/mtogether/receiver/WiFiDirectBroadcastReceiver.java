package com.lg.mtogether.receiver;

import com.lg.mtogether.client.DeviceListActivity;
import com.lg.mtogether.server.StandByActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private StandByActivity mSeverActivity;
	private DeviceListActivity mClientActivity;
	private int deviceNum;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			DeviceListActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mClientActivity = activity;
	}
	
	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			StandByActivity activity,int deviceNum) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mSeverActivity = activity;
		this.deviceNum = deviceNum;
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
				Log.i("ClientRecevier", "dd");
			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			// Respond to new connection or disconnections
			if (mManager != null){
				mManager.requestGroupInfo(mChannel, mSeverActivity);
			}
			

		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			// Respond to this device's wifi state changing
			if (mManager != null){
				WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);	
				mSeverActivity.setMyDevice(device.deviceName);
			}
		}
	}
	
}
