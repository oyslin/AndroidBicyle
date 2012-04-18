package com.dreamcatcher.bicycle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.util.Log;

import com.dreamcatcher.bicycle.exception.NetworkException;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;

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
		HttpGet httpGet = new HttpGet(citySetting.getAllBicyclesUrl());		
		
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		httpGet.setParams(params);
		
		String jsonStr = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);					
			jsonStr = getJsonDataFromInputStream(response.getEntity().getContent());
			
			if(jsonStr != null && !jsonStr.trim().equals("")){
				int firstBrace = jsonStr.indexOf("{");
				if(firstBrace < 0){
					throw new IOException();
				}
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
		CitySetting citySetting = GlobalSetting.getInstance().getCitySetting();
		
		HttpGet httpGet = new HttpGet(citySetting.getBicycleDetailUrl() + String.valueOf(id));
		String jsonStr = null;
		BicycleStationInfo bicycleInfo = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			jsonStr = getJsonDataFromInputStream(response.getEntity().getContent());	
			
			if(jsonStr != null && !jsonStr.equals("")){
				int firstBrace = jsonStr.trim().indexOf("{");
				if(firstBrace < 0){
					throw new IOException();
				}
				
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
	
	public static boolean checkVersion(PackageInfo packageInfo) throws NetworkException{
		if(Utils.getNetworkInfo() == Constants.NetworkInfo.DISCONNECT){
			throw new NetworkException();
		}
		boolean needUpdate = false;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Constants.HttpUrl.VERSION_INFO_URL);
		try {			
			HttpResponse response = httpClient.execute(httpGet);
			String jsonStr = getJsonDataFromInputStream(response.getEntity().getContent());
			if(jsonStr != null && !jsonStr.equals("")){
				JSONObject jsonObject = new JSONObject(jsonStr);
				String versionName = jsonObject.getString("versionName");
				int versionCode = jsonObject.getInt("versionCode");
				
				int versionNameCompareResult = compareStr(versionName, packageInfo.versionName);
				if(versionNameCompareResult > 0){
					needUpdate = true;
				}else if(versionNameCompareResult == 0){
					if(versionCode > packageInfo.versionCode){
						needUpdate = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return needUpdate;
	}
	
	private static int compareStr(String first, String second){
		int result = 0;
		byte[] firstBytes = first.getBytes();
		byte[] secondeBytes = second.getBytes();
		int count = Math.min(firstBytes.length, secondeBytes.length);
		int index = 0;
		for(; index < count; index++){
			if(firstBytes[index] > secondeBytes[index]){
				result = 1;
				break;
			}else if(firstBytes[index] < secondeBytes[index]){
				result = -1;
				break;
			}
		}
		if(index == count){
			if(firstBytes.length > secondeBytes.length){
				result = 1;
			}else if(firstBytes.length == secondeBytes.length){
				result = 0;
			}else {
				result = -1;
			}
		}
		
		return result;
	}
}
