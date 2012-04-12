package com.dreamcather.bicycle.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;

public class AssetsService implements IAssetsService {
	private ExecutorService mExecutorService = null;
	private Handler mHandler = null;
	private final static int CITY_SETTING_LOAD_SUCCESS = 0;
	private final static int CITY_SETTING_LOAD_FAILED = 1;
	private final static int BICYCLE_INFO_LOAD_SUCCESS = 2;
	private final static int BICYCLE_INFO_LOAD_FAILED = 3;
	
	public AssetsService(){
		mExecutorService = Executors.newCachedThreadPool();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CITY_SETTING_LOAD_SUCCESS:
					BicycleService.getInstance().getAssetsEventListener().onCitySettingLoaded(Constants.ResultCode.SUCCESS);
					break;
				case CITY_SETTING_LOAD_FAILED:
					BicycleService.getInstance().getAssetsEventListener().onCitySettingLoaded(Constants.ResultCode.LOAD_ASSETS_FAILED);
					break;
				case BICYCLE_INFO_LOAD_SUCCESS:
					BicycleService.getInstance().getAssetsEventListener().onBicyclesInfoLoaded(Constants.ResultCode.SUCCESS);
					break;
				case BICYCLE_INFO_LOAD_FAILED:
					BicycleService.getInstance().getAssetsEventListener().onBicyclesInfoLoaded(Constants.ResultCode.LOAD_ASSETS_FAILED);
					break;
				default:
					break;
				}
			}
		};
	}
	
	public void loadCitySetting() {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				try {
					boolean success = Utils.loadCitySetting();
					if(success){
						mHandler.sendEmptyMessage(CITY_SETTING_LOAD_SUCCESS);
					}else {
						mHandler.sendEmptyMessage(CITY_SETTING_LOAD_FAILED);
					}					
				} catch (Exception e) {
					mHandler.sendEmptyMessage(CITY_SETTING_LOAD_FAILED);
				}				
			}
		});
	}

	public void loadBicyclesInfo() {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				try {
					Utils.loadBicyclesInfoFromAssets();
					mHandler.sendEmptyMessage(BICYCLE_INFO_LOAD_SUCCESS);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(BICYCLE_INFO_LOAD_FAILED);
				}				
			}
		});
	}

}
