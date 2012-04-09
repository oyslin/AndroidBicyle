package com.dreamcather.bicycle.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.vo.CitySetting;

public class AssetsService implements IAssetsService {
	private ExecutorService mExecutorService = null;
	
	public AssetsService(){
		mExecutorService = Executors.newCachedThreadPool();		
	}
	
	public void loadCitySetting() {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				AssetsEventListener eventListener = BicycleService.getInstance().getAssetsEventListener();
				try {
					CitySetting citySetting = Utils.loadCitySetting();
					if(citySetting != null){
						eventListener.onCitySettingLoaded(citySetting, Constants.ResultCode.SUCCESS);
					}else {
						eventListener.onCitySettingLoaded(null, Constants.ResultCode.LOAD_ASSETS_FAILED);
					}					
				} catch (Exception e) {
					eventListener.onCitySettingLoaded(null, Constants.ResultCode.LOAD_ASSETS_FAILED);
				}				
			}
		});
	}

	public void loadBicyclesInfo() {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				AssetsEventListener eventListener = BicycleService.getInstance().getAssetsEventListener();
				try {
					Utils.loadBicyclesInfoFromAssets();
					eventListener.onBicyclesInfoLoaded(Constants.ResultCode.SUCCESS);
				} catch (Exception e) {
					eventListener.onBicyclesInfoLoaded(Constants.ResultCode.LOAD_ASSETS_FAILED);
				}
				
			}
		});
	}

}
