package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

public class SelectCityActivity extends Activity {
	private LayoutInflater mInflater = null;
	private int mSelectedCityIndex = -1;	
	private Handler mHandler = null;
	private int LOAD_SUCCESS = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		init();
	}

	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_select_city);
		
		mInflater = LayoutInflater.from(this);		
		ListView listView = (ListView) findViewById(R.id.select_city_list);
		
		CityListAdapter adapter = new CityListAdapter();
		listView.setAdapter(adapter);
		
		Button nextBtn = (Button) findViewById(R.id.select_city_next_btn);
		nextBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onNextBtnClicked();				
			}
		});
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == LOAD_SUCCESS){
					startActivity(new Intent(SelectCityActivity.this, Main.class));
					finish();
				}
			}			
		};
	}	
	
	private void onNextBtnClicked(){
		if(mSelectedCityIndex != -1){
			final String cityTag = Constants.CitySetting.CITY_TAG[mSelectedCityIndex];
			new Thread(new Runnable() {				
				public void run() {
					Utils.storeDataToLocal(Constants.LocalStoreTag.CITY_NAME, cityTag);
					Utils.loadCitySetting();
					Utils.getBicycleInfoFromAssets();
					mHandler.sendEmptyMessage(LOAD_SUCCESS);
				}
			}).start();			
		}
	}
	
	private class CityListAdapter extends BaseAdapter{
		private int[] cityNameResIdArray = null;
		private View selectedView = null;
		public CityListAdapter(){
			cityNameResIdArray = Constants.CitySetting.CITY_NAME_RESID;
		}		
		
		public int getCount() {
			return cityNameResIdArray.length;
		}

		public String getItem(int position) {
			return getText(cityNameResIdArray[position]).toString();
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.select_city_item, parent, false);
				final int index = position;
				convertView.setOnClickListener(new OnClickListener() {					
					public void onClick(View v) {
						CityListAdapter.this.onItemClicked(v);
						mSelectedCityIndex = index;
					}
				});
			}
			TextView cityTextView = (TextView) convertView.findViewById(R.id.select_city_item_name);
			cityTextView.setText(getText(cityNameResIdArray[position]));			
			
			return convertView;
		}
		
		private void onItemClicked(View view){
			if(selectedView != null){
				ImageView selectImageView = (ImageView) selectedView.findViewById(R.id.select_city_item_check);
				selectImageView.setSelected(false);
			}
			selectedView = view;
			ImageView selectImageView = (ImageView) selectedView.findViewById(R.id.select_city_item_check);
			selectImageView.setSelected(true);			
		}
	}
}
