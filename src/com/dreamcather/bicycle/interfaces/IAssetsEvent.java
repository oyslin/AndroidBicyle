package com.dreamcather.bicycle.interfaces;

import com.dreamcather.bicycle.vo.CitySetting;

public interface IAssetsEvent {
	/**
	 * called when city setting loaded
	 * @param citySetting
	 * @param resultCode
	 */
	public void onCitySettingLoaded(CitySetting citySetting, int resultCode);
	
	/**
	 * called when bicycles info loaded
	 * @param resultCode
	 */
	public void onBicyclesInfoLoaded(int resultCode);
}
