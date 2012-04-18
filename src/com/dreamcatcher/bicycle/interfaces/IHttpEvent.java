package com.dreamcatcher.bicycle.interfaces;

import com.dreamcatcher.bicycle.vo.BicycleStationInfo;

public interface IHttpEvent {
	/**
	 * called on all bicycle infos received
	 * @param resultCode
	 */
	public void onAllBicyclesInfoReceived(int resultCode);
	
	/**
	 * call on single bicycle info received
	 * @param bicycleStationInfo
	 * @param resultCode
	 */
	public void onSingleBicycleInfoReceived(BicycleStationInfo bicycleStationInfo, int resultCode);
}
