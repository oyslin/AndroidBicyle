package com.dreamcather.bicycle.core;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dreamcather.bicycle.exception.NetworkException;
import com.dreamcather.bicycle.interfaces.IHttpService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.vo.BicycleStationInfo;

public class HttpService implements IHttpService {
	private ExecutorService mExecutorService = null;
	private Handler mHandler = null;
	private final static int ALL_SUCCESS = 0;
	private final static int ALL_HTTP_FAILED = 1;
	private final static int ALL_DISCONNECT = 2;
	private final static int SINGLE_SUCCESS = 3;
	private final static int SINGLE_HTTP_FAILED = 4;
	private final static int SINGLE_JSON_FAILED = 5;
	private final static int SINGLE_DISCONNECT = 6;
	
	
	public HttpService(){
		mExecutorService = Executors.newCachedThreadPool();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ALL_SUCCESS:
					BicycleService.getInstance().getHttpEventListener().onAllBicyclesInfoReceived(Constants.ResultCode.SUCCESS);
					break;
				case ALL_HTTP_FAILED:
					BicycleService.getInstance().getHttpEventListener().onAllBicyclesInfoReceived(Constants.ResultCode.HTTP_REQUEST_FAILED);
					break;
				case ALL_DISCONNECT:
					BicycleService.getInstance().getHttpEventListener().onAllBicyclesInfoReceived(Constants.ResultCode.NETWORK_DISCONNECT);	
					break;
				case SINGLE_SUCCESS:
					BicycleStationInfo bicycleStationInfo = null;
					bicycleStationInfo = msg.getData().getParcelable(Constants.ParcelableTag.BICYCLE_STATION_INFO);
					
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleInfoReceived(bicycleStationInfo, Constants.ResultCode.SUCCESS);
					break;
				case SINGLE_HTTP_FAILED:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleInfoReceived(null, Constants.ResultCode.HTTP_REQUEST_FAILED);
					break;
				case SINGLE_JSON_FAILED:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleInfoReceived(null, Constants.ResultCode.JSON_PARSER_FAILED);
					break;
				case SINGLE_DISCONNECT:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleInfoReceived(null, Constants.ResultCode.NETWORK_DISCONNECT);
					break;
				default:
					break;
				}
			}			
		};
	}
	
	public void getAllBicyclesInfo() {
			mExecutorService.execute(new Runnable() {
				public void run() {
					try {
						HttpUtils.getAllBicyclesInfoFromServer();
						mHandler.sendEmptyMessage(ALL_SUCCESS);						
					} catch (IOException e) {
						mHandler.sendEmptyMessage(ALL_HTTP_FAILED);						
					} catch (NetworkException e) {
						mHandler.sendEmptyMessage(ALL_DISCONNECT);											
					}
				}
			});				
	}

	public void getSingleBicycleInfo(final int bicycleId) {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				try {
					BicycleStationInfo bicycleStationInfo = HttpUtils.getSingleBicycleInfoFromHttp(bicycleId);
					Bundle data = new Bundle();
					data.putParcelable(Constants.ParcelableTag.BICYCLE_STATION_INFO, bicycleStationInfo);
					Message message = Message.obtain();
					message.setData(data);
					message.what = SINGLE_SUCCESS;
					mHandler.sendMessage(message);
				} catch (IOException e) {
					mHandler.sendEmptyMessage(SINGLE_HTTP_FAILED);
				} catch (JSONException e) {
					mHandler.sendEmptyMessage(SINGLE_JSON_FAILED);
				} catch (NetworkException e) {
					mHandler.sendEmptyMessage(SINGLE_DISCONNECT);
				}
			}
		});
	}

}
