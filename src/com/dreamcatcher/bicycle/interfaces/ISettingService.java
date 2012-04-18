package com.dreamcatcher.bicycle.interfaces;

public interface ISettingService {
	/**
	 * change city setting
	 * @param cityTag
	 */
	void changeCitySetting(final String cityTag);
	
	/**
	 * change favorite ids
	 * @param favoriteIds
	 */
	void changeFavoriteIds(String favoriteIds);
}
