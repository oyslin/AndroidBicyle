package com.walt;

import com.walt.vo.CitySetting;

import android.app.Application;

public class BicycleApp extends Application {
	private static BicycleApp mInstance = null;
	private static CitySetting mCitySetting = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		getCitySettings();
	}
	
	public static BicycleApp getInstance(){
		return mInstance;
	}
	
	public static CitySetting getCitySetting(){
		return mCitySetting;
	}
	
	private void getCitySettings(){
		
	}
}
