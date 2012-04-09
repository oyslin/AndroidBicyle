package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.vo.CitySetting;

public class SplashScreen extends Activity {
	private Handler mHandler = null;
	private final static int SUCESS = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		init();
	}
	
	private void init(){
		CitySetting citySetting = Utils.loadCitySetting();
		
		//If setting exists, load setting, otherwise load select city activity
		if(citySetting != null){
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if(msg.what == SUCESS){
						startActivity(new Intent(SplashScreen.this, Main.class));
						finish();
					}
				}
			};
			
			new Thread(new Runnable() {
				public void run() {
					getBicycleInfo();
					mHandler.sendEmptyMessage(SUCESS);
				}
			}).start();
		}else {
			startActivity(new Intent(this, SelectCityActivity.class));
			finish();
		}		
	}	
	
	private void getBicycleInfo(){
		boolean localSuccess = loadFromLocal();		
		if(!localSuccess){			 
			Utils.getBicycleInfoFromAssets();
		}
	}
	
	private boolean loadFromLocal(){
		String jsonStr = Utils.getDataFromLocal(Constants.LocalStoreTag.ALL_BICYCLE);
		if(jsonStr == null || jsonStr.equals("")){
			return false;
		}
		Utils.setToDataset(jsonStr);
		return true;
	}	
}
