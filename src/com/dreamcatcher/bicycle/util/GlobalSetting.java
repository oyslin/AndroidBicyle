package com.dreamcatcher.bicycle.util;

import com.dreamcatcher.bicycle.vo.CitySetting;

public class GlobalSetting {
	private static GlobalSetting mInstance = null;
	private CitySetting mCitySetting = null;
	
	private GlobalSetting(){
		
	}
	
	public static GlobalSetting getInstance(){
		if(mInstance == null){
			mInstance = new GlobalSetting();
		}
		return mInstance;
	}

	public CitySetting getCitySetting() {
		return mCitySetting;
	}

	public void setCitySetting(CitySetting citySetting) {
		this.mCitySetting = citySetting;
	}
}
