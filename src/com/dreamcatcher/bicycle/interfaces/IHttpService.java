package com.dreamcatcher.bicycle.interfaces;

public interface IHttpService {
	/**
	 * get all bicycle infos from server
	 */
	public void getAllBicyclesInfo();
	
	/**
	 * get single bicycle info from server
	 * @param bicycleId
	 */
	public void getSingleBicycleInfo(int bicycleId);
}
