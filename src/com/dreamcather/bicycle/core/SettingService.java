package com.dreamcather.bicycle.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

import com.dreamcather.bicycle.interfaces.ISettingService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.util.Utils;

public class SettingService implements ISettingService {
	private ExecutorService mExecutorService = null;
	private Handler mHandler = null;
	private final static int SETTING_SUCCESS = 0;
	private final static int SETTING_FIALED = 0;
	
	public SettingService(){
		mExecutorService = Executors.newCachedThreadPool();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == SETTING_SUCCESS){
					BicycleService.getInstance().getSettingEventListener().onCitySettingChanged(Constants.ResultCode.SUCCESS);
				}else {
					BicycleService.getInstance().getSettingEventListener().onCitySettingChanged(Constants.ResultCode.CHANGE_CITY_FAILED);
				}
			}
		};
	}
	
	public void changeCitySetting(final String cityTag) {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				try {
					Utils.storeStringDataToLocal(Constants.LocalStoreTag.CITY_NAME, cityTag);//set city tag
					Utils.loadCitySetting();
					Utils.clearDataset();
					Utils.loadBicyclesInfoFromAssets();
					HttpUtils.getAllBicyclesInfoFromServer();
					mHandler.sendEmptyMessage(SETTING_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(SETTING_FIALED);
				}
			}
		});
	}

}
