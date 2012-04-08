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
	
	public synchronized static BicycleDataset getInstance(){
		if(mInstance == null){
			mInstance = new BicycleDataset();
		}
		return mInstance;
	}
	
	/**
	 * add bicycle station info to map, if already in map, update it
	 * @param id
	 * @param BicycleInfo
	 */
	public synchronized void addBicycleInfo(int id, BicycleStationInfo BicycleInfo){
		if(!mBicycleMap.containsKey(id)){
			mBicycleMap.put(id, BicycleInfo);
		}else {
			mBicycleMap.remove(id);
			mBicycleMap.put(id, BicycleInfo);
		}
	}
	
	/**
	 * remove a bicycle station info from map
	 * @param id
	 */
	public synchronized void delBicycleInfo(int id){
		if(mBicycleMap.containsKey(id)){
			mBicycleMap.remove(id);
		}
	}
	
	/**
	 * update a bicycle station info
	 * @param id
	 * @param newBicycleInfo
	 */
	public synchronized void updateBicycleInfo(int id, BicycleStationInfo newBicycleInfo){
		if(mBicycleMap.containsKey(id)){
			mBicycleMap.remove(id);
		}
		mBicycleMap.put(id, newBicycleInfo);
	}
	
	public synchronized BicycleStationInfo getBicycleInfo(int id){
		return mBicycleMap.get(id);
	}
	
	public synchronized int getBicycleCount(){
		return mBicycleMap.keySet().size();
	}
	
	public synchronized ArrayList<BicycleStationInfo> getBicycleStationInfos(){
		ArrayList<BicycleStationInfo> arrayList = new ArrayList<BicycleStationInfo>();
		arrayList.addAll(mBicycleMap.values());		
		return arrayList;
	}
}
