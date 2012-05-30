<<<<<<< HEAD
package com.dreamcatcher.bicycle.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.dataset.BicycleDataset;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;

public class Utils {	
	private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BicycleApp.getInstance());
	private static Editor mEditor = mSharedPreferences.edit();
	private static BicycleApp mBicycleApp = BicycleApp.getInstance();
	private static Ringtone mRingtone = null;
	private static Vibrator mVibrator = null;
	
	public static PackageInfo getPackageInfo(){
		PackageInfo packageInfo = null;
		try {
			String packageName = mBicycleApp.getPackageName();
			packageInfo = mBicycleApp.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageInfo;
	}
	
	public static String getStringDataFromLocal(String tagName){
		String result = null;
		result = mSharedPreferences.getString(tagName, "");
		return result;
	}
	
	public static void storeStringDataToLocal(String tagName, String value){
		mEditor.putString(tagName, value);
		mEditor.commit();		
	}
	
	public static boolean getBooleanDataFromLocal(String tagName, boolean defaultValue){
		boolean result = false;
		result = mSharedPreferences.getBoolean(tagName, defaultValue);
		return result;
	}
	
	public static void storeBooleanDataToLocal(String tagName, boolean value){
		mEditor.putBoolean(tagName, value);
		mEditor.commit();
	}
	
	public static int getIntDataFromLocal(String tagName, int defaultValue){
		int result = -1;
		result = mSharedPreferences.getInt(tagName, defaultValue);
		return result;
	}
	
	public static void storeLongDataToLocal(String tagName, long value){
		mEditor.putLong(tagName, value);
		mEditor.commit();
	}
	
	public static long getLongDataFromLocal(String tagName, long defaultValue){
		long result = 0;
		result = mSharedPreferences.getLong(tagName, defaultValue);
		return result;
	}
	
	public static void storeIntDataToLocal(String tagName, int value){
		mEditor.putInt(tagName, value);
		mEditor.commit();
	}
	
	public static void clearLocalData(){
		mEditor.clear();
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
	
	public static void reminderReturnBicycle(){
	  Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	  if(alert == null){
		  alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		  if(alert == null){
			  alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		  }
	  }
	  if(mRingtone == null){
		  mRingtone = RingtoneManager.getRingtone(mBicycleApp, alert);
	  }
	  
	  mRingtone.play();
	}
	
	public static void stopReminder(){
		if(mRingtone != null){
			if(mRingtone.isPlaying()){
				mRingtone.stop();
			}
		}
		if(mVibrator != null){			
			mVibrator.cancel();
		}
	}
	
	public static void vibrate(){
		if(mVibrator == null){
			mVibrator = (Vibrator) mBicycleApp.getSystemService(Context.VIBRATOR_SERVICE);
		}
		long[] patten = {1000, 2000, 1000, 2000, 1000, 2000};
		mVibrator.vibrate(patten, 2);		
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
	 * exit application
	 */
	public static void exitApplication(){
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}
	
	public static int dip2px(float dipValue){
		final float scale = mBicycleApp.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	
	public static int px2dip(float pxValue){
		final float scale = mBicycleApp.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	/**
	 * get current network info
	 * @return 0: network unavailable 1: wifi 2: mobile
	 */
	public static int getNetworkInfo(){
		int result = Constants.NetworkInfo.DISCONNECT;
		try {
			ConnectivityManager cm = (ConnectivityManager) mBicycleApp.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if(networkInfo != null && networkInfo.isConnected()){
					if(networkInfo.getState() == State.CONNECTED){
						if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
							result = Constants.NetworkInfo.WIFI;
						}else {
							result = Constants.NetworkInfo.MOBILE;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * load city setting from local
	 */
	public static boolean loadCitySetting() throws Exception{		
		String cityName = Utils.getStringDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		if(cityName == null || cityName.equals("")){
			return false;
		}
		boolean result = false;		
		try {
			InputStream inputStream = BicycleApp.getInstance().getAssets().open(Constants.CitySetting.CITY_SETTING_FILENAME);
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
				boolean showBicycleNumber = cityJson.getBoolean(Constants.SettingJsonTag.SHOW_BICYCLE_NUMBER);
				boolean refreshPop = cityJson.getBoolean(Constants.SettingJsonTag.REFRESH_POP);
				boolean needDecode = cityJson.getBoolean(Constants.SettingJsonTag.NEED_DECODE);
				
				CitySetting citySetting = new CitySetting(tabs, allBicyclesUrl, bicycleDetailUrl, defaultLatitude, defaultLongitude, 
							offsetLatitude, offsetLongitude, assetsFileName, showBicycleNumber, refreshPop,needDecode);
				GlobalSetting.getInstance().setCitySetting(citySetting);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public static void loadBicyclesInfoFromAssets() throws Exception{
		AssetManager assetManager = BicycleApp.getInstance().getAssets();
		try {
			String assetFileName = GlobalSetting.getInstance().getCitySetting().getAssetsFileName();
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
			throw e;
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
	
	public static void clearDataset(){
		BicycleDataset.getInstance().clearData();
	}
	
	public static String getPinyinCaptalLetter(String bicycleName){
		return null;
	}
	
	public static String decodeSZCode(String str) {
	    int len = str.length() / 2;	    
	    int i = 1;
	    StringBuffer sb = new StringBuffer();
	    int key = getIntegerValue(str.charAt(0)) * 16  + getIntegerValue(str.charAt(1));
	    System.out.println("key = " + key);
	    int ivalue = 0;
	    int jvalue = 0;
	    while (i < len) {
	    	ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));	        
	    	ivalue = key ^ ivalue;
	    	System.out.println("ivalue = " + ivalue);
	        if (ivalue == 2) {
	            i = i + 1;
	            key = Getkey(key, ivalue);
	            ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));
	            ivalue = key ^ ivalue;
	            jvalue = ivalue;
	            i = i + 1;
	            key = Getkey(key, ivalue);
	            ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));
	            ivalue = key ^ ivalue;
	            jvalue = jvalue * 256 + ivalue;
	            sb.append((char)jvalue);
	        } else {
	        	sb.append((char)ivalue);
	        }
	        i = i + 1;	        
	        key = Getkey(key, ivalue);
	    }
	    return sb.toString();
	}
	private static int Getkey(int key, int sc) {
	    return (key * 0x0063 + sc) % 0x100;
	}
	
	private static int getIntegerValue(char ch){
		if(ch >= 65 && ch <= 70){
			return ch - 55;
		}else{
			return ch - 48;
		}
	}
	
}
=======
package com.dreamcatcher.bicycle.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.dataset.BicycleDataset;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;

public class Utils {	
	private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BicycleApp.getInstance());
	private static Editor mEditor = mSharedPreferences.edit();
	private static BicycleApp mBicycleApp = BicycleApp.getInstance();
	private static Ringtone mRingtone = null;
	private static Vibrator mVibrator = null;
	
	public static PackageInfo getPackageInfo(){
		PackageInfo packageInfo = null;
		try {
			String packageName = mBicycleApp.getPackageName();
			packageInfo = mBicycleApp.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageInfo;
	}
	
	public static String getStringDataFromLocal(String tagName){
		String result = null;
		result = mSharedPreferences.getString(tagName, "");
		return result;
	}
	
	public static void storeStringDataToLocal(String tagName, String value){
		mEditor.putString(tagName, value);
		mEditor.commit();		
	}
	
	public static boolean getBooleanDataFromLocal(String tagName, boolean defaultValue){
		boolean result = false;
		result = mSharedPreferences.getBoolean(tagName, defaultValue);
		return result;
	}
	
	public static void storeBooleanDataToLocal(String tagName, boolean value){
		mEditor.putBoolean(tagName, value);
		mEditor.commit();
	}
	
	public static int getIntDataFromLocal(String tagName, int defaultValue){
		int result = -1;
		result = mSharedPreferences.getInt(tagName, defaultValue);
		return result;
	}
	
	public static void storeLongDataToLocal(String tagName, long value){
		mEditor.putLong(tagName, value);
		mEditor.commit();
	}
	
	public static long getLongDataFromLocal(String tagName, long defaultValue){
		long result = 0;
		result = mSharedPreferences.getLong(tagName, defaultValue);
		return result;
	}
	
	public static void storeIntDataToLocal(String tagName, int value){
		mEditor.putInt(tagName, value);
		mEditor.commit();
	}
	
	public static void clearLocalData(){
		mEditor.clear();
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
	
	public static void reminderReturnBicycle(){
	  Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	  if(alert == null){
		  alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		  if(alert == null){
			  alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		  }
	  }
	  if(mRingtone == null){
		  mRingtone = RingtoneManager.getRingtone(mBicycleApp, alert);
	  }
	  
	  mRingtone.play();
	}
	
	public static void stopReminder(){
		if(mRingtone != null){
			if(mRingtone.isPlaying()){
				mRingtone.stop();
			}
		}
		if(mVibrator != null){			
			mVibrator.cancel();
		}
	}
	
	public static void vibrate(){
		if(mVibrator == null){
			mVibrator = (Vibrator) mBicycleApp.getSystemService(Context.VIBRATOR_SERVICE);
		}
		long[] patten = {1000, 2000, 1000, 2000, 1000, 2000};
		mVibrator.vibrate(patten, 2);		
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
	 * exit application
	 */
	public static void exitApplication(){
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}
	
	public static int dip2px(float dipValue){
		final float scale = mBicycleApp.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	
	public static int px2dip(float pxValue){
		final float scale = mBicycleApp.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	/**
	 * get current network info
	 * @return 0: network unavailable 1: wifi 2: mobile
	 */
	public static int getNetworkInfo(){
		int result = Constants.NetworkInfo.DISCONNECT;
		try {
			ConnectivityManager cm = (ConnectivityManager) mBicycleApp.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if(networkInfo != null && networkInfo.isConnected()){
					if(networkInfo.getState() == State.CONNECTED){
						if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
							result = Constants.NetworkInfo.WIFI;
						}else {
							result = Constants.NetworkInfo.MOBILE;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * load city setting from local
	 */
	public static boolean loadCitySetting() throws Exception{		
		String cityName = Utils.getStringDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		if(cityName == null || cityName.equals("")){
			return false;
		}
		boolean result = false;		
		try {
			InputStream inputStream = BicycleApp.getInstance().getAssets().open(Constants.CitySetting.CITY_SETTING_FILENAME);
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
				boolean showBicycleNumber = cityJson.getBoolean(Constants.SettingJsonTag.SHOW_BICYCLE_NUMBER);
				boolean refreshPop = cityJson.getBoolean(Constants.SettingJsonTag.REFRESH_POP);
				boolean needDecode = cityJson.getBoolean(Constants.SettingJsonTag.NEED_DECODE);
				
				CitySetting citySetting = new CitySetting(tabs, allBicyclesUrl, bicycleDetailUrl, defaultLatitude, defaultLongitude, 
							offsetLatitude, offsetLongitude, assetsFileName, showBicycleNumber, refreshPop,needDecode);
				GlobalSetting.getInstance().setCitySetting(citySetting);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public static void loadBicyclesInfoFromAssets() throws Exception{
		AssetManager assetManager = BicycleApp.getInstance().getAssets();
		try {
			String assetFileName = GlobalSetting.getInstance().getCitySetting().getAssetsFileName();
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
			throw e;
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
	
	public static void clearDataset(){
		BicycleDataset.getInstance().clearData();
	}
	
	public static String getPinyinCaptalLetter(String bicycleName){
		return null;
	}
	
	public static String decodeSZCode(String str) {
	    int len = str.length() / 2;	    
	    int i = 1;
	    StringBuffer sb = new StringBuffer();
	    int key = getIntegerValue(str.charAt(0)) * 16  + getIntegerValue(str.charAt(1));
	    System.out.println("key = " + key);
	    int ivalue = 0;
	    int jvalue = 0;
	    while (i < len) {
	    	ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));	        
	    	ivalue = key ^ ivalue;
	    	System.out.println("ivalue = " + ivalue);
	        if (ivalue == 2) {
	            i = i + 1;
	            key = Getkey(key, ivalue);
	            ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));
	            ivalue = key ^ ivalue;
	            jvalue = ivalue;
	            i = i + 1;
	            key = Getkey(key, ivalue);
	            ivalue = getIntegerValue(str.charAt(i * 2)) * 16 + getIntegerValue(str.charAt(i * 2 + 1));
	            ivalue = key ^ ivalue;
	            jvalue = jvalue * 256 + ivalue;
	            sb.append((char)jvalue);
	        } else {
	        	sb.append((char)ivalue);
	        }
	        i = i + 1;	        
	        key = Getkey(key, ivalue);
	    }
	    return sb.toString();
	}
	private static int Getkey(int key, int sc) {
	    return (key * 0x0063 + sc) % 0x100;
	}
	
	private static int getIntegerValue(char ch){
		if(ch >= 65 && ch <= 70){
			return ch - 55;
		}else{
			return ch - 48;
		}
	}
	
}
>>>>>>> e154b104753b732324c632006e4ab804ef800c33
