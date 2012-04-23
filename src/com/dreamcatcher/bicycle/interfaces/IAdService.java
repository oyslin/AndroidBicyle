package com.dreamcatcher.bicycle.interfaces;

public interface IAdService {
	/**
	 * get point from server
	 */
	public void getPoints();
	
	/**
	 * spend some point
	 * @param point
	 */
	public void spendPoints(int point);
	
	/**
	 * award some point to user
	 * @param point
	 */
	public void awardPoint(int point);
}
