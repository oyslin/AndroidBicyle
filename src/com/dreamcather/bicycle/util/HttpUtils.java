package com.dreamcather.bicycle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dreamcather.bicycle.exception.NetworkException;
import com.dreamcather.bicycle.vo.BicycleStationInfo;
import com.dreamcather.bicycle.vo.CitySetting;

public class HttpUtils {	
	/**
	 * update bicycles info from server and save it to local
	 */
	public static boolean getAllBicyclesInfoFromServer() throws IOException, NetworkException{
		if(Utils.getNetworkInfo() == Constants.NetworkInfo.DISCONNECT){
			throw new NetworkException();
		}
		
		boolean success = false;
		HttpClient httpClient = new DefaultHttpClient();
		CitySetting citySetting = GlobalSetting.getInstance().getCitySetting();
		if(citySetting == null){
			return false;
		}
		HttpGet httpGet = new HttpGet(GlobalSetting.getInstance().getCitySetting().getAllBicyclesUrl());
		String jsonStr = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			jsonStr = getJsonDataFromInputStream(response.getEntity().getContent());
			
			if(jsonStr != null && !jsonStr.trim().equals("")){
				Log.d("HttpUtils", "jsonStr = " + jsonStr);
				int firstBrace = jsonStr.indexOf("{");
				
				jsonStr = jsonStr.substring(firstBrace);
				
				Utils.setToDataset(jsonStr);
				Utils.storeStringDataToLocal(Constants.LocalStoreTag.ALL_BICYCLE, jsonStr);
				success = true;			
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return success;
	}
	
	/**
	 * get single bicycle station info
	 * @param id
	 * @return
	 */
	public static BicycleStationInfo getSingleBicycleInfoFromHttp(int id) throws IOException, JSONException, NetworkException{
		if(Utils.getNetworkInfo() == Constants.NetworkInfo.DISCONNECT){
			throw new NetworkException();
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(GlobalSetting.getInstance().getCitySetting().getBicycleDetailUrl() + String.valueOf(id));
		String jsonStr = null;
		BicycleStationInfo bicycleInfo = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			jsonStr = getJsonDataFromInputStream(response.getEntity().getContent());	
			
			if(jsonStr != null && !jsonStr.equals("")){
				Log.d("HttpUtils", "jsonStr = " + jsonStr);
				int firstBrace = jsonStr.trim().indexOf("{");
				jsonStr = jsonStr.substring(firstBrace);
				
				JSONObject jsonObject = new JSONObject(jsonStr);	
				JSONArray jsonArray = jsonObject.getJSONArray(Constants.BicycleJsonTag.STATION);
				
				for(int i = 0, total = jsonArray.length(); i < total; i++){
					JSONObject jsonItem = jsonArray.getJSONObject(i);				
					String name = jsonItem.getString(Constants.BicycleJsonTag.NAME);
					double latitude = jsonItem.getDouble(Constants.BicycleJsonTag.LATITUDE);
					double longitude = jsonItem.getDouble(Constants.BicycleJsonTag.LONGITUDE);
					int capacity = jsonItem.getInt(Constants.BicycleJsonTag.CAPACITY);
					int available = jsonItem.getInt(Constants.BicycleJsonTag.AVAIABLE);
					String address = jsonItem.getString(Constants.BicycleJsonTag.ADDRESS);
					
					bicycleInfo = new BicycleStationInfo(id, name, latitude, longitude, capacity, available, address);				
				}
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 

		return bicycleInfo;
	}
	
	private static String getJsonDataFromInputStream(InputStream inputStream){
		String jsonStr = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.HttpSetting.HTTP_CONT_ENCODE));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while((line= reader.readLine()) != null){
				stringBuilder.append(line);
			}
			jsonStr = stringBuilder.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return jsonStr;
	}
}
