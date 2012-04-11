package com.dreamcather.bicycle.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dreamcather.bicycle.interfaces.ISettingService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.util.Utils;

public class SettingService implements ISettingService {
	private ExecutorService mExecutorService = null;
	
	public SettingService(){
		mExecutorService = Executors.newCachedThreadPool();
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
					BicycleService.getInstance().getSettingEventListener().onCitySettingChanged(Constants.ResultCode.SUCCESS);
				} catch (Exception e) {
					BicycleService.getInstance().getSettingEventListener().onCitySettingChanged(Constants.ResultCode.CHANGE_CITY_FAILED);
				}
			}
		});
	}

}
