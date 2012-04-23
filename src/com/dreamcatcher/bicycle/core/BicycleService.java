package com.dreamcatcher.bicycle.core;

import com.dreamcatcher.bicycle.interfaces.IAdService;
import com.dreamcatcher.bicycle.interfaces.IAssetsService;
import com.dreamcatcher.bicycle.interfaces.IHttpService;
import com.dreamcatcher.bicycle.interfaces.ISettingService;

public class BicycleService {
	private static BicycleService mInstance = null;
	private IHttpService mHttpService = null;
	private HttpEventListener mHttpEvent = null;
	private IAssetsService mAssertsService = null;
	private AssetsEventListener mAssetsEventListener = null;
	private ISettingService mSettingService = null;
	private SettingEventListener mSettingEvent = null;
	private IAdService mAdService = null;
	private AdEventListener mAdEventListener = null;
	
	private BicycleService(){
		mHttpService = new HttpService();
		mHttpEvent = new HttpEventListener();
		mAssertsService = new AssetsService();
		mAssetsEventListener = new AssetsEventListener();
		mSettingService = new SettingService();
		mSettingEvent = new SettingEventListener();
		mAdService = new AdService();
		mAdEventListener = new AdEventListener();
	}
	
	public static BicycleService getInstance(){
		if(mInstance == null){
			mInstance = new BicycleService();
		}
		return mInstance;
	}
	
	public IHttpService getHttpService(){
		return mHttpService;
	}
	
	public HttpEventListener getHttpEventListener(){
		return mHttpEvent;
	}

	public IAssetsService getAssertsService() {
		return mAssertsService;
	}

	public AssetsEventListener getAssetsEventListener() {
		return mAssetsEventListener;
	}

	public ISettingService getSettingService() {
		return mSettingService;
	}

	public SettingEventListener getSettingEventListener() {
		return mSettingEvent;
	}
	
	public IAdService getAdService(){
		return mAdService;
	}
	
	public AdEventListener getAdEventListener(){
		return mAdEventListener;
	}
	
}
