package com.dreamcatcher.bicycle.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class BicycleNumberInfo implements Parcelable{
	private int mId = 1;	
	private int mCapacity = 0;
	private int mAvailable = 0;	
	
	public BicycleNumberInfo(int id, int capacity, int available) {
		this.mId = id;
		this.mCapacity = capacity;
		this.mAvailable = available;
	}
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	public int getCapacity() {
		return mCapacity;
	}
	public void setCapacity(int capacity) {
		this.mCapacity = capacity;
	}
	public int getAvailable() {
		return mAvailable;
	}
	public void setAvailable(int available) {
		this.mAvailable = available;
	}
	@Override
	public int describeContents() {		
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);		
		dest.writeInt(mCapacity);
		dest.writeInt(mAvailable);
	}
	
	public static final Parcelable.Creator<BicycleNumberInfo> CREATOR = new Parcelable.Creator<BicycleNumberInfo>() {

		public BicycleNumberInfo createFromParcel(Parcel source) {
			return new BicycleNumberInfo(source);
		}

		public BicycleNumberInfo[] newArray(int size) {
			return new BicycleNumberInfo[size];
		}		
	};
	
	public BicycleNumberInfo(Parcel in){
		readFromParce(in);
	}
	
	private void readFromParce(Parcel in){
		this.mId = in.readInt();		;
		this.mCapacity = in.readInt();
		this.mAvailable = in.readInt();
	}	
}
