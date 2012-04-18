package com.dreamcatcher.bicycle.interfaces;

public interface ISettingEvent {
	/**
	 * called when city setting changed
	 */
	void onCitySettingChanged(int resultCode);
	
	void onFavoriteIdsChanged();
}
