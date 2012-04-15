package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.adapter.CityListAdapter;
import com.dreamcather.bicycle.adapter.CityListAdapter.ICityListEvent;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.ISettingEvent;
import com.dreamcather.bicycle.interfaces.ISettingService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

public class ChangeCityActivity extends Activity implements ISettingEvent{
	private int mSelectedCityIndex = -1;
	private ISettingService mSettingService = null;
	private ProgressDialog mProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_city);
		this.init();
	}
	
	private void init(){		
		mSettingService = BicycleService.getInstance().getSettingService();
		this.addEvent();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_change_city);
		
		ListView listView = (ListView) findViewById(R.id.change_city_list);
		
		ICityListEvent citySelectEvent = new ICityListEvent() {			
			public void onCityItemClicked(int index) {
				mSelectedCityIndex = index;				
			}
		};
		
		CityListAdapter adapter = new CityListAdapter(citySelectEvent, getCurrentCityIndex());
		
		listView.setAdapter(adapter);
		
		Button reloadBtn = (Button) findViewById(R.id.change_city_reload_btn);
		reloadBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onReloadtBtnClicked();				
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}

	private void addEvent(){
		BicycleService.getInstance().getSettingEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getSettingEventListener().removeEvent(this);
	}
	
	private int getCurrentCityIndex(){
		String currentCity = Utils.getStringDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		for(int i = 0, n = Constants.CitySetting.CITY_TAG.length; i < n; i++){
			if(currentCity.equalsIgnoreCase(Constants.CitySetting.CITY_TAG[i])){
				return i;
			}
		}
		return -1;
	}
	
	private void onReloadtBtnClicked(){
		String currentCity = Utils.getStringDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		String selectedCity = Constants.CitySetting.CITY_TAG[mSelectedCityIndex];
		if(currentCity.equalsIgnoreCase(selectedCity)){
			Toast.makeText(this, R.string.change_city_same_city_toast_msg, Toast.LENGTH_SHORT).show();
		}else {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getText(R.string.change_city_progress_dialog_msg));
			mProgressDialog.show();
			mSettingService.changeCitySetting(selectedCity);			
		}
	}

	public void onCitySettingChanged(int resultCode) {
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
		}
		if(resultCode == Constants.ResultCode.CHANGE_CITY_FAILED){
			Toast.makeText(ChangeCityActivity.this, getText(R.string.change_city_reload_failed_msg), Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(ChangeCityActivity.this, getText(R.string.change_city_reload_success_msg), Toast.LENGTH_SHORT).show();
		}
	}

}
