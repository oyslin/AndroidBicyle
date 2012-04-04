package com.walt.util;

import java.io.UnsupportedEncodingException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.walt.BicycleApp;

public class Utils {	
	private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BicycleApp.getInstance());
	private static Editor mEditor = mSharedPreferences.edit();
	private static String[] encoderArray = {"UTF-8","GB2312", "GBK", "Unicode", "BIG5"};
	
	public static String getDataFromLocal(String tagName){
		String result = null;
		result = mSharedPreferences.getString(tagName, "");		
		return result;
	}
	
	public static void storeDataToLocal(String tagName, String value){
		mEditor.putString(tagName, value);
		mEditor.commit();
	}
	
	public static String convertToUtf8Str(String str){
		String result = null;
		try {
			byte[] bytes = str.getBytes("UTF8");
			result = new String(bytes, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	//
	//
	
}
