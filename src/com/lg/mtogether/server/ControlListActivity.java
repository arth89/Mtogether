package com.lg.mtogether.server;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.mtogether.R;

public class ControlListActivity extends Activity implements OnItemClickListener{




	ListView lv;
	MusicAdapter adapter;
	ArrayList<String> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_music);

		lv  = (ListView)findViewById(R.id.list_music);
		mList = new ArrayList<String>();
		
		
		adapter = new MusicAdapter(this);

		
		
		lv.setAdapter(adapter);

		
		//TODO 
		

		mList.add("Music1_test");
		mList.add("Music2_test");
		mList.add("Music3_test");

		adapter.notifyDataSetChanged();
		
		lv.setOnItemClickListener(this);

	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(), mList.get(position), Toast.LENGTH_SHORT).show();
		startActivity(new Intent(ControlListActivity.this,LoadingActivity.class));
		finish();
	}
	
	class MusicAdapter extends BaseAdapter{

		Context mContext;


		public MusicAdapter(Context context) {
			this.mContext =context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if(convertView == null){
				LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.list_one_music, null);
			}
			
			
			TextView musicName= (TextView)convertView.findViewById(R.id.list_one_mname);
			//TODO 나중사용
			//TextView artistName= (TextView)convertView.findViewById(R.id.list_one_aname);
			//ImageView iv;
			
			musicName.setText(mList.get(position));
			
			return convertView;
		}

	}

	

}





