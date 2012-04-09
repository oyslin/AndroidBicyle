package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.adapter.CityListAdapter;
import com.dreamcather.bicycle.adapter.CityListAdapter.ICityListEvent;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

public class ChangeCityActivity extends Activity {
	private int mSelectedCityIndex = -1;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_city);
		this.init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_select_city);
		
		ListView listView = (ListView) findViewById(R.id.select_city_list);
		
		ICityListEvent citySelectEvent = new ICityListEvent() {			
			public void onCityItemClicked(int index) {
				mSelectedCityIndex = index;				
			}
		};
		
		CityListAdapter adapter = new CityListAdapter(citySelectEvent, getCurrentCityIndex());
		
		listView.setAdapter(adapter);
		
		Button nextBtn = (Button) findViewById(R.id.select_city_next_btn);
		nextBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onNextBtnClicked();				
			}
		});
	}
	
	private int getCurrentCityIndex(){
		String currentCity = Utils.getDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		for(int i = 0, n = Constants.CitySetting.CITY_TAG.length; i < n; i++){
			if(currentCity.equalsIgnoreCase(Constants.CitySetting.CITY_TAG[i])){
				return i;
			}
		}
		return -1;
	}
	
	private void onNextBtnClicked(){
		String currentCity = Utils.getDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		String selectedCity = Constants.CitySetting.CITY_TAG[mSelectedCityIndex];
		if(currentCity.equalsIgnoreCase(selectedCity)){
			//TODO show dialog
		}else {
			//TODO restart
		}
	}

}
