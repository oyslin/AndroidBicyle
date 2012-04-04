package com.walt;

import android.app.Application;

public class BicycleApp extends Application {
	private static BicycleApp mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
	
	public static BicycleApp getInstance(){
		return mInstance;
	}
}
