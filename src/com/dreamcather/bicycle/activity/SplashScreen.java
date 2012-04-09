package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.IAssetsEvent;
import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.GlobalSetting;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.vo.CitySetting;

public class SplashScreen extends Activity implements IAssetsEvent{
	private Handler mHandler = null;	
	private final static int BICYCLES_INFO_LOAD_SUCCESS = 0;
	private final static int CITY_SETTING_LOAD_FAILED = 1;
	private IAssetsService mAssetsService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		init();
	}	
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}
	
	private void addEvent(){
		BicycleService.getInstance().getAssetsEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getAssetsEventListener().removeEvent(this);
	}



	private void init(){
		this.addEvent();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case BICYCLES_INFO_LOAD_SUCCESS:
						startActivity(new Intent(SplashScreen.this, Main.class));
						finish();
						break;
					case CITY_SETTING_LOAD_FAILED:
						startActivity(new Intent(SplashScreen.this, SelectCityActivity.class));
						finish();
						break;
					default:
						break;
				}
			}
		};
		
		mAssetsService = BicycleService.getInstance().getAssertsService();		
		mAssetsService.loadCitySetting();
	}
	
	private void getBicycleInfo(){
		boolean success = loadAllBicyclesInfoFromLocal();		
		if(!success){			 
			mAssetsService.loadBicyclesInfo();
		}else {
			startActivity(new Intent(SplashScreen.this, Main.class));
			finish();
		}
	}
	
	private boolean loadAllBicyclesInfoFromLocal(){
		String jsonStr = Utils.getDataFromLocal(Constants.LocalStoreTag.ALL_BICYCLE);
		if(jsonStr == null || jsonStr.equals("")){
			return false;
		}
		Utils.setToDataset(jsonStr);
		return true;
	}

	/**
	 * load city setting result
	 */
	public void onCitySettingLoaded(CitySetting citySetting, int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			GlobalSetting.getInstance().setCitySetting(citySetting);
			getBicycleInfo();
		}else {
			mHandler.sendEmptyMessage(CITY_SETTING_LOAD_FAILED);
		}
	}

	/**
	 * load bicycles info result
	 */
	public void onBicyclesInfoLoaded(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mHandler.sendEmptyMessage(BICYCLES_INFO_LOAD_SUCCESS);
		}		
	}
}
