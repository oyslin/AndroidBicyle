package com.dreamcatcher.bicycle.interfaces;

public interface IAdEvent {
	/**
	 * get point successful
	 * @param currencyName
	 * @param totalPoint
	 */
	public void onPointsUpdated(String currencyName, int totalPoint);
	
	/**
	 * get point failed
	 * @param error
	 */
	public void onPointsUpdateFailed(String error);	
}
