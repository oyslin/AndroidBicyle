package com.dreamcatcher.bicycle.interfaces;


public interface IAssetsEvent {
	/**
	 * called when city setting loaded
	 * @param citySetting
	 * @param resultCode
	 */
	public void onCitySettingLoaded(int resultCode);
	
	/**
	 * called when bicycles info loaded
	 * @param resultCode
	 */
	public void onBicyclesInfoLoaded(int resultCode);
}
