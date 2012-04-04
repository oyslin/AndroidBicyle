package com.walt.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.walt.BicycleApp;

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
	
}
