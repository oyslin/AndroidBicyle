package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.adapter.CityListAdapter;
import com.dreamcatcher.bicycle.adapter.CityListAdapter.ICityListEvent;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IAssetsEvent;
import com.dreamcatcher.bicycle.interfaces.IAssetsService;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;

public class SelectCityActivity extends Activity implements IAssetsEvent{
	private int mSelectedCityIndex = -1;	
	private IAssetsService mAssetsService = null;
	private ProgressDialog mProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		init();
	}
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}

	private void init(){
		this.addEvent();
		
		mAssetsService = BicycleService.getInstance().getAssertsService();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_select_city);
		
		ListView listView = (ListView) findViewById(R.id.select_city_list);
		
		ICityListEvent citySelectEvent = new ICityListEvent() {			
			public void onCityItemClicked(int index) {
				mSelectedCityIndex = index;				
			}
		};
		
		CityListAdapter adapter = new CityListAdapter(citySelectEvent, 0);
		
		listView.setAdapter(adapter);
		
		Button nextBtn = (Button) findViewById(R.id.select_city_next_btn);
		nextBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onNextBtnClicked();				
			}
		});
		
	}
	
	
	private void addEvent(){
		BicycleService.getInstance().getAssetsEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getAssetsEventListener().removeEvent(this);
	}
	
	private void onNextBtnClicked(){
		if(mSelectedCityIndex != -1){
			final String cityTag = Constants.CitySetting.CITY_TAG[mSelectedCityIndex];
			
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getText(R.string.progress_dialog_loading_msg));
			mProgressDialog.show();
			
			Utils.storeStringDataToLocal(Constants.LocalStoreTag.CITY_NAME, cityTag);
			mAssetsService.loadCitySetting();					
		}
	}
	
	public void onCitySettingLoaded(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mAssetsService.loadBicyclesInfo();
		}		
	}

	public void onBicyclesInfoLoaded(int resultCode) {
		if (resultCode == Constants.ResultCode.SUCCESS) {
			Intent data = new Intent();
			data.putExtra("load_completed", true);
			setResult(RESULT_OK, data);
			mProgressDialog.dismiss();
			finish();
		}		
	}
	
}
