package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.IAssetsEvent;
import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;

public class SplashScreen extends Activity implements IAssetsEvent{
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
		String jsonStr = Utils.getStringDataFromLocal(Constants.LocalStoreTag.ALL_BICYCLE);
		if(jsonStr == null || jsonStr.equals("")){
			return false;
		}
		Utils.setToDataset(jsonStr);
		return true;
	}

	/**
	 * load city setting result
	 */
	public void onCitySettingLoaded(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){			
			getBicycleInfo();
		}else {
			startActivity(new Intent(SplashScreen.this, SelectCityActivity.class));
			finish();
		}
	}

	/**
	 * load bicycles info result
	 */
	public void onBicyclesInfoLoaded(int resultCode) {
		if (resultCode == Constants.ResultCode.SUCCESS) {
			startActivity(new Intent(SplashScreen.this, Main.class));
			finish();
		}
	}
	
}
