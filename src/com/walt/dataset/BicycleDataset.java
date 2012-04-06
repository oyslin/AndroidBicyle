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
	 * @param bicyleInfo
	 */
	public synchronized void addBicyleInfo(int id, BicycleStationInfo bicyleInfo){
		if(!mBicycleMap.containsKey(id)){
			mBicycleMap.put(id, bicyleInfo);
		}else {
			mBicycleMap.remove(id);
			mBicycleMap.put(id, bicyleInfo);
		}
	}
	
	/**
	 * remove a bicycle station info from map
	 * @param id
	 */
	public synchronized void delBicyleInfo(int id){
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
	
	public synchronized BicycleStationInfo getBicyleInfo(int id){
		return mBicycleMap.get(id);
	}
	
	public synchronized int getBicyleCount(){
		return mBicycleMap.keySet().size();
	}
	
	public synchronized ArrayList<BicycleStationInfo> getBicyleStationInfos(){
		ArrayList<BicycleStationInfo> arrayList = new ArrayList<BicycleStationInfo>();
		arrayList.addAll(mBicycleMap.values());		
		return arrayList;
	}
}
