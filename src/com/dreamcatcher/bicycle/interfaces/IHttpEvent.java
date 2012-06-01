package com.dreamcatcher.bicycle.interfaces;

import com.dreamcatcher.bicycle.vo.BicycleNumberInfo;

public interface IHttpEvent {
	/**
	 * called on all bicycle infos received
	 * @param resultCode
	 */
	public void onAllBicyclesInfoReceived(int resultCode);
	
	/**
	 * called on single bicycle info received
	 * @param bicycleStationInfo
	 * @param resultCode
	 */
	public void onSingleBicycleNumberInfoReceived(BicycleNumberInfo bicycleNumberInfo, int resultCode);
	
	/**
	 * called on new version check completed
	 * @param needUpdate true: need update, false: do not need update
	 */
	public void onNewVersionCheckCompleted(boolean needUpdate, int resultCode);
}
