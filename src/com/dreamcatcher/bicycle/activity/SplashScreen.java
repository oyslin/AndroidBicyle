package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IAssetsEvent;
import com.dreamcatcher.bicycle.interfaces.IAssetsService;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;

public class SplashScreen extends Activity implements IAssetsEvent{
	private IAssetsService mAssetsService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
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

	@Override
	protected void onResume() {		
		super.onResume();
		this.init();
	}
	
	private void init(){
		this.addEvent();
		
		mAssetsService = BicycleService.getInstance().getAssertsService();		
		mAssetsService.loadCitySetting();
		
		//get next ad shown time
	    long nextShowAdTime = Utils.getLongDataFromLocal(Constants.LocalStoreTag.NEXT_AD_SHOWN_TIME, 0);
	    
	    GlobalSetting.getInstance().getAdsetting().setNextShowAdTime(nextShowAdTime);		
	}	
		
	private void getBicycleInfo(){
		boolean success = loadAllBicyclesInfoFromLocal();		
		if(!success){			 
			mAssetsService.loadBicyclesInfo();
		}else {
			Intent data = new Intent();
			data.putExtra("load_completed", true);
			setResult(RESULT_OK, data);
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
			Intent data = new Intent();
			data.putExtra("load_completed", false);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	/**
	 * load bicycles info result
	 */
	public void onBicyclesInfoLoaded(int resultCode) {
		if (resultCode == Constants.ResultCode.SUCCESS) {
			Intent data = new Intent();
			data.putExtra("load_completed", true);
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
