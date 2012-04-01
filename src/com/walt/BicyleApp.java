package com.walt;

import android.app.Application;

public class BicyleApp extends Application {
	private static BicyleApp mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
	
	public static BicyleApp getInstance(){
		return mInstance;
	}
}
