package com.walt.vo;

public class BicycleStationInfo {
	private int mId = 1;
	private String mName = "";
	private double mLatitude = 0.0;
	private double mLongitude = 0.0;
	private int mCapacity = 0;
	private int mAvailable = 0;
	private String mAddress = "";
	
	public BicycleStationInfo(int id, String name, double latitude, double longitude, int capacity, int available, String address){
		this.mId = id;
		this.mName = name;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mCapacity = capacity;
		this.mAvailable = available;
		this.mAddress = address;
	}
	
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
	public double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}
	public int getCapacity() {
		return mCapacity;
	}
	public void setCapacity(int mCapacity) {
		this.mCapacity = mCapacity;
	}
	public int getAvailable() {
		return mAvailable;
	}
	public void setAvailable(int mAvailable) {
		this.mAvailable = mAvailable;
	}
	public String getAddress() {
		return mAddress;
	}
	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	
}
