package com.walt.dataset;

import java.util.ArrayList;
import java.util.HashMap;

import com.walt.vo.BicycleStationInfo;

public class BicycleDataset {
	private HashMap<Integer, BicycleStationInfo> mBicycleMap = null;
	private static BicycleDataset mInstance = null;
	
	private BicycleDataset(){
		mBicycleMap = new HashMap<Integer, BicycleStationInfo>();		
	}
	
	public static BicycleDataset getInstance(){
		if(mInstance == null){
			mInstance = new BicycleDataset();
		}
		return mInstance;
	}
	
	public void addBicyleInfo(int id, BicycleStationInfo bicyleInfo){
		if(!mBicycleMap.containsKey(id)){
			mBicycleMap.put(id, bicyleInfo);
		}		
	}
	
	public void delBicyleInfo(int id){
		if(mBicycleMap.containsKey(id)){
			mBicycleMap.remove(id);
		}
	}
	
	public void updateBicycleInfo(int id, BicycleStationInfo newBicycleInfo){
		if(mBicycleMap.containsKey(id)){
			mBicycleMap.remove(id);
		}
		mBicycleMap.put(id, newBicycleInfo);
	}
	
	public BicycleStationInfo getBicyleInfo(int id){
		return mBicycleMap.get(id);
	}
	
	public int getBicyleCount(){
		return mBicycleMap.keySet().size();
	}
	
	public ArrayList<BicycleStationInfo> getBicyleStationInfos(){
		ArrayList<BicycleStationInfo> arrayList = new ArrayList<BicycleStationInfo>();
		arrayList.addAll(mBicycleMap.values());		
		return arrayList;
	}
}
