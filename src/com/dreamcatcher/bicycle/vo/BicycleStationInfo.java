package com.dreamcatcher.bicycle.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class BicycleStationInfo implements Parcelable{
	private int mId = 1;
	private String mName = "";
	private String mNameCapital = "";
	private double mLatitude = 0.0;
	private double mLongitude = 0.0;
	private int mCapacity = 0;
	private int mAvailable = 0;
	private String mAddress = "";
	
	public BicycleStationInfo(int id, String name, String nameCapital, double latitude, double longitude, int capacity, int available, String address){
		this.mId = id;
		this.mName = name;
		//if null, do not update
		if(!nameCapital.equals("")){
			this.mNameCapital = nameCapital;
		}
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
	
	public String getNameCapital(){
		return mNameCapital;
	}
	
	public void setNameCapital(String nameCapital){
		this.mNameCapital = nameCapital;
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

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mName);
		dest.writeString(mNameCapital);
		dest.writeDouble(mLatitude);
		dest.writeDouble(mLongitude);
		dest.writeInt(mCapacity);
		dest.writeInt(mAvailable);
		dest.writeString(mAddress);		
	}
	
	public static final Parcelable.Creator<BicycleStationInfo> CREATOR = new Parcelable.Creator<BicycleStationInfo>() {

		public BicycleStationInfo createFromParcel(Parcel source) {
			return new BicycleStationInfo(source);
		}

		public BicycleStationInfo[] newArray(int size) {
			return new BicycleStationInfo[size];
		}		
	};
	
	public BicycleStationInfo(Parcel in){
		readFromParce(in);
	}
	
	private void readFromParce(Parcel in){
		this.mId = in.readInt();
		this.mName = in.readString();
		this.mNameCapital = in.readString();
		this.mLatitude = in.readDouble();
		this.mLongitude = in.readDouble();
		this.mCapacity = in.readInt();
		this.mAvailable = in.readInt();
		this.mAddress = in.readString();
	}
}
