package com.walt.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.walt.BicycleApp;
import com.walt.dataset.BicycleDataset;
import com.walt.vo.BicycleStationInfo;
import com.walt.vo.CitySetting;

public class Utils {	
	private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BicycleApp.getInstance());
	private static Editor mEditor = mSharedPreferences.edit();
	private static BicycleApp mBicycleApp = BicycleApp.getInstance();
	private static CitySetting mCitySetting = null;
	
	public static String getDataFromLocal(String tagName){
		String result = null;
		result = mSharedPreferences.getString(tagName, "");
		return result;
	}
	
	public static void storeDataToLocal(String tagName, String value){
		mEditor.putString(tagName, value);
		mEditor.commit();
	}
	
	/**
	 * start a phone call
	 */
	public static void startCall(Activity activity, String phoneNum){		
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
		activity.startActivity(intent);
	}
	
	/**
	 * start to send SMS message
	 */
	public static void startSMS(Activity activity, String message){
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.putExtra("sms_body", message);
	    intent.setType("vnd.android-dir/mms-sms");
	    activity.startActivity(intent);
	}
	
	/**
	 * start to browser a url
	 */
	public static void startBrowser(Activity activity, String url){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));		
		activity.startActivity(intent);
	}
	
	/**
	 * start to share via Weibo etc
	 */
	public static void startShare(){
		
	}
	
	public static String getText(int resId){
		return mBicycleApp.getText(resId).toString();
	}
	
	public static Drawable getDrawable(int resId){
		if(resId != 0){
			return mBicycleApp.getResources().getDrawable(resId);
		}
		return null;
	}
	
	public static int getColor(int resId){
		return mBicycleApp.getResources().getColor(resId);
	}
	
	/**
	 * load city setting from local
	 */
	public static CitySetting loadCitySetting(){
		String cityName = Utils.getDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		if(cityName == null || cityName.equals("")){
			return null;
		}
		try {
			InputStream inputStream = BicycleApp.getInstance().getAssets().open(Constants.CITY_SETTING_FILENAME);
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.HttpSetting.HTTP_CONT_ENCODE));
			String line = null;
			while((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			String jsonStr = stringBuilder.toString();
			JSONObject jsonObject  = new JSONObject(jsonStr);
			JSONObject cityJson = jsonObject.getJSONObject(cityName);
			if(cityJson != null){
				String tabs = cityJson.getString(Constants.SettingJsonTag.TABS);
				String allBicyclesUrl = cityJson.getString(Constants.SettingJsonTag.ALL_BICYCLES_URL);
				String bicycleDetailUrl = cityJson.getString(Constants.SettingJsonTag.BICYCLE_DETAIL_URL);
				double defaultLatitude = cityJson.getDouble(Constants.SettingJsonTag.DEFAULT_LATITUDE);
				double defaultLongitude = cityJson.getDouble(Constants.SettingJsonTag.DEFAULT_LONGITUDE);
				double offsetLatitude = cityJson.getDouble(Constants.SettingJsonTag.OFFSET_LATITUDE);
				double offsetLongitude = cityJson.getDouble(Constants.SettingJsonTag.OFFSET_LONGITUDE);
				String assetsFileName = cityJson.getString(Constants.SettingJsonTag.ASSETS_FILE_NAME);
				mCitySetting = new CitySetting(tabs, allBicyclesUrl, bicycleDetailUrl, defaultLatitude, defaultLongitude, offsetLatitude, offsetLongitude, assetsFileName);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCitySetting;
	}
	
	public static CitySetting getCitySetting(){		
		return mCitySetting;
	}
	
	public static void getBicycleInfoFromAssets(){
		AssetManager assetManager = BicycleApp.getInstance().getAssets();
		try {
			String assetFileName = mCitySetting.getAssetsFileName();
			InputStream inputStream = assetManager.open(assetFileName, AssetManager.ACCESS_BUFFER);
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.HttpSetting.HTTP_CONT_ENCODE));
			String line = null;
			while((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			String jsonStr = stringBuilder.toString();
			setToDataset(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setToDataset(String jsonStr){
		try {
			BicycleDataset dataset = BicycleDataset.getInstance();
			JSONObject jsonObject = new JSONObject(jsonStr);			
			JSONArray jsonArray = jsonObject.getJSONArray(Constants.BicycleJsonTag.STATION);
			for(int i = 0, total = jsonArray.length(); i < total; i++){
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				int id = jsonItem.getInt(Constants.BicycleJsonTag.ID);
				String name = jsonItem.getString(Constants.BicycleJsonTag.NAME);
				double latitude = jsonItem.getDouble(Constants.BicycleJsonTag.LATITUDE);
				double longitude = jsonItem.getDouble(Constants.BicycleJsonTag.LONGITUDE);
				int capacity = jsonItem.getInt(Constants.BicycleJsonTag.CAPACITY);
				int available = jsonItem.getInt(Constants.BicycleJsonTag.AVAIABLE);
				String address = jsonItem.getString(Constants.BicycleJsonTag.ADDRESS);				
				
				BicycleStationInfo bicycleInfo = new BicycleStationInfo(id, name, latitude, longitude, capacity, available, address);
				dataset.addBicycleInfo(id, bicycleInfo);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}		
	}
	
}
