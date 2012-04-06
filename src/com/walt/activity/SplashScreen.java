package com.walt.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.walt.R;
import com.walt.util.Constants;
import com.walt.util.Utils;

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
		boolean localSuccess = loadFromLocal();		
		if(!localSuccess){			 
			getBicyleInfoFromAssets();
		}
	}
	
	private boolean loadFromLocal(){
		String jsonStr = Utils.getDataFromLocal(Constants.LocalStoreTag.ALL_BICYLE);
		if(jsonStr == null || jsonStr.equals("")){
			return false;
		}
		Log.e("SplashScreen", "Local jsonStr = " + jsonStr);
		Utils.setToDataset(jsonStr);
		return true;
	}	
	
	private void getBicyleInfoFromAssets(){
		AssetManager assetManager = getAssets();
		try {
			InputStream inputStream = assetManager.open("bicycles.json", AssetManager.ACCESS_BUFFER);
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.HttpSetting.HTTP_CONT_ENCODE));
			String line = null;
			while((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			String jsonStr = stringBuilder.toString();
			Log.e("SplashScreen", "Assets jsonStr = " + jsonStr);
			Utils.setToDataset(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
