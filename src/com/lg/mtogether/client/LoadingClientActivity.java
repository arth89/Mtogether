package com.lg.mtogether.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.mtogether.R;
import com.lg.mtogether.constant.Constant;

public class LoadingClientActivity extends Activity {


	ProgressBar pb;
	TextView	tv;
	
	int progress = 0;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_client);

		mContext = getApplicationContext();
		pb = (ProgressBar)findViewById(R.id.loading_pb_client);
		tv = (TextView)findViewById(R.id.loading_tv_client);
		pb.setMax(Constant.progressMax);
		loadingThread.start();
		
		Toast.makeText(mContext, "loadingCla", Toast.LENGTH_LONG).show();

	}

	Handler mHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {

			if(msg.what==0){

				pb.setProgress(progress++);

				if(progress==50){
					tv.setText("°ð À½¾ÇÀÌ Àç»ýµË´Ï´Ù..");
				}
				
				if(progress==Constant.progressMax){
					Intent mIntent = new Intent(mContext,PlayActivity.class);
					startActivity(mIntent);
					finish();
				}

			}
		};
	};

	Thread loadingThread =  new Thread(){

		public void run() {


			while(progress<Constant.progressMax){
				try {
					sleep(Constant.loadingSpeed);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Message msg = Message.obtain();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}

		};
	};



}
