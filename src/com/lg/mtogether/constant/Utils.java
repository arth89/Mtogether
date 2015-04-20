package com.lg.mtogether.constant;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class Utils {

	
	
	public static String getBtName(){
		
		BluetoothAdapter bluetoothAdapter = null;
	    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    return bluetoothAdapter.getName();
	}
	
	public static void setWifiEnable(Context context,boolean b){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		wifiManager.setWifiEnabled(b);

	}
	
	public static void makeToast(Context context,String text){
		
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		
	}
	
}
