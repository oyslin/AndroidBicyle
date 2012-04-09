package com.dreamcather.bicycle.core;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import com.dreamcather.bicycle.interfaces.IHttpService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.vo.BicycleStationInfo;

public class HttpService implements IHttpService {
	private ExecutorService mExecutorService = null;
	
	public HttpService(){
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	public void getAllBicyclesInfo() {
			mExecutorService.execute(new Runnable() {
				HttpEventListener eventListener = BicycleService.getInstance().getHttpEventListener();
				public void run() {
					try {
						HttpUtils.getAllBicyclesInfoFromServer();
						eventListener.onAllBicyclesInfoReceived(Constants.ResultCode.SUCCESS);
					} catch (IOException e) {
						eventListener.onAllBicyclesInfoReceived(Constants.ResultCode.HTTP_REQUEST_FAILED);
					}					
				}
			});		
	}

	public void getSingleBicycleInfo(final int bicycleId) {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				HttpEventListener eventListener = BicycleService.getInstance().getHttpEventListener();
				try {
					BicycleStationInfo bicycleStationInfo = HttpUtils.getSingleBicycleInfoFromHttp(bicycleId);
					eventListener.onSingleBicycleInfoReceived(bicycleStationInfo, Constants.ResultCode.SUCCESS);
				} catch (IOException e) {
					eventListener.onSingleBicycleInfoReceived(null, Constants.ResultCode.HTTP_REQUEST_FAILED);
				} catch (JSONException e) {
					eventListener.onSingleBicycleInfoReceived(null, Constants.ResultCode.JSON_PARSER_FAILED);
				}				
			}
		});
	}

}
