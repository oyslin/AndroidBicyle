package com.walt.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.walt.R;
import com.walt.dataset.BicycleDataset;
import com.walt.util.Constants;
import com.walt.util.Utils;
import com.walt.vo.BicycleStationInfo;

public class SplashScreen extends Activity {
	private Handler mHandler = null;
	private final static int SUCESS = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		init();
	}
	
	private void init(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == SUCESS){
					startActivity(new Intent(SplashScreen.this, Main.class));
					finish();
				}				
			}			
		};
		
		new Thread(new Runnable() {
			public void run() {
				getBicyleInfo();
				mHandler.sendEmptyMessage(SUCESS);
			}
		}).start();	
	}
	
	private void getBicyleInfo(){
		boolean success = loadFromLocal();
		if(!success){
			getBicyleInfoFromAssets();
		}
	}
	
	private boolean loadFromLocal(){
		String jsonStr = Utils.getDataFromLocal(Constants.LocalStoreTag.ALL_BICYLE);
		if(jsonStr == null || jsonStr.equals("")){
			return false;
		}		
		setToDataset(jsonStr);
		return true;
	}	
	
	private void getBicyleInfoFromAssets(){
		AssetManager assetManager = getAssets();
		try {
			InputStream inputStream = assetManager.open("bicycles.json", AssetManager.ACCESS_BUFFER);
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
	
	private void setToDataset(String jsonStr){
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
				
				Log.e("SplashScreen", "Address = " + Utils.convertToUtf8Str(address));
				
				BicycleStationInfo bicyleInfo = new BicycleStationInfo(id, name, latitude, longitude, capacity, available, address);
				dataset.addBicyleInfo(id, bicyleInfo);
			}
		} catch (Exception e) {			
			Log.d("SplashScreen", "Set to Dataset failed!");
			e.printStackTrace();
		}		
	}	
}
