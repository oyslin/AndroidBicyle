package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IAdEvent;
import com.dreamcatcher.bicycle.interfaces.IAssetsEvent;
import com.dreamcatcher.bicycle.interfaces.IAssetsService;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;
import com.waps.AppConnect;

public class SplashScreen extends Activity implements IAssetsEvent, IAdEvent{
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
		BicycleService.getInstance().getAdEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getAssetsEventListener().removeEvent(this);
		BicycleService.getInstance().getAdEventListener().removeEvent(this);
	}



	private void init(){
		this.addEvent();
		AppConnect.getInstance(this);
		
		//get ad config from server
		new GetConfigTask().execute();		
	}	
	
	private class GetConfigTask extends AsyncTask<Void, Void, Boolean>{
		private boolean showAd = false;
		@Override
		protected Boolean doInBackground(Void... params) {
			String showAdSetting = AppConnect.getInstance(BicycleApp.getInstance()).getConfig(Constants.AdSetting.SHOW_AD);
			if("true".equalsIgnoreCase(showAdSetting)){
				showAd = true;
			}else {
				showAd = false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			GlobalSetting.getInstance().getAdsetting().setShowAd(showAd);
			//get points from server
			BicycleService.getInstance().getAdService().getPoints();
		}		
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

	@Override
	public void onPointsUpdated(String currencyName, int totalPoint) {
		GlobalSetting.getInstance().getAdsetting().setPointTotal(totalPoint);
		
		//get next ad shown time
	    long nextShowAdTime = Utils.getLongDataFromLocal(Constants.LocalStoreTag.NEXT_AD_SHOWN_TIME, 0);
	    GlobalSetting.getInstance().getAdsetting().setNextShowAdTime(nextShowAdTime);
	    
	    mAssetsService = BicycleService.getInstance().getAssertsService();		
		mAssetsService.loadCitySetting();
	}

	@Override
	public void onPointsUpdateFailed(String error) {		
		mAssetsService = BicycleService.getInstance().getAssertsService();		
		mAssetsService.loadCitySetting();
	}	
}
