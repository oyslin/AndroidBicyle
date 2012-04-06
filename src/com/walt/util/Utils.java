package com.walt.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.walt.BicycleApp;
import com.walt.dataset.BicycleDataset;
import com.walt.vo.BicycleStationInfo;

public class Utils {	
	private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BicycleApp.getInstance());
	private static Editor mEditor = mSharedPreferences.edit();	
	
	public static String getDataFromLocal(String tagName){
		String result = null;
		result = mSharedPreferences.getString(tagName, "");		
		return result;
	}
	
	public static void storeDataToLocal(String tagName, String value){
		mEditor.putString(tagName, value);
		mEditor.commit();
	}
	
	public static String getText(int resId){
		return BicycleApp.getInstance().getText(resId).toString();
	}
	
	public static Drawable getDrawable(int resId){
		if(resId != 0){
			return BicycleApp.getInstance().getResources().getDrawable(resId);
		}
		return null;
	}
	
	public static int getColor(int resId){
		return BicycleApp.getInstance().getResources().getColor(resId);
	}
	
	public static void setToDataset(String jsonStr){
		try {
			BicycleDataset dataset = BicycleDataset.getInstance();
			JSONObject jsonObject = new JSONObject(jsonStr);	
			JSONArray jsonArray = jsonObject.getJSONArray(Constants.JsonTag.STATION);
			for(int i = 0, total = jsonArray.length(); i < total; i++){
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				int id = jsonItem.getInt(Constants.JsonTag.ID);
				String name = jsonItem.getString(Constants.JsonTag.NAME);
				double latitude = jsonItem.getDouble(Constants.JsonTag.LATITUDE);
				double longitude = jsonItem.getDouble(Constants.JsonTag.LONGITUDE);
				int capacity = jsonItem.getInt(Constants.JsonTag.CAPACITY);
				int available = jsonItem.getInt(Constants.JsonTag.AVAIABLE);
				String address = jsonItem.getString(Constants.JsonTag.ADDRESS);				
				
				BicycleStationInfo bicyleInfo = new BicycleStationInfo(id, name, latitude, longitude, capacity, available, address);
				dataset.addBicyleInfo(id, bicyleInfo);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}		
	}
	
}
