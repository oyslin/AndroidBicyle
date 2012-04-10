package com.dreamcather.bicycle.core;

import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.interfaces.IHttpService;
import com.dreamcather.bicycle.interfaces.ISettingService;

public class BicycleService {
	private static BicycleService mInstance = null;
	private IHttpService mHttpService = null;
	private HttpEventListener mHttpEvent = null;
	private IAssetsService mAssertsService = null;
	private AssetsEventListener mAssetsEventListener = null;
	private ISettingService mSettingService = null;
	private SettingEventListener mSettingEvent = null;
	
	private BicycleService(){
		mHttpService = new HttpService();
		mHttpEvent = new HttpEventListener();
		mAssertsService = new AssetsService();
		mAssetsEventListener = new AssetsEventListener();
		mSettingService = new SettingService();
		mSettingEvent = new SettingEventListener();
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
	
}
