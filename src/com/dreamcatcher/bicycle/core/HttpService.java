package com.dreamcatcher.bicycle.core;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dreamcatcher.bicycle.exception.NetworkException;
import com.dreamcatcher.bicycle.interfaces.IHttpService;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.HttpUtils;
import com.dreamcatcher.bicycle.vo.BicycleNumberInfo;

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
	private final static int CHECK_VERSION_COMPLETE = 7;
	private final static int CHECK_VERSION_DISCONNECT = 8;
	private final static int CHECK_VERSION_HTTP_FAILED = 9;
	
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
					BicycleNumberInfo bicycleNumberInfo = null;
					bicycleNumberInfo = msg.getData().getParcelable(Constants.ParcelableTag.BICYCLE_STATION_INFO);
					
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleNumberInfoReceived(bicycleNumberInfo, Constants.ResultCode.SUCCESS);
					break;
				case SINGLE_HTTP_FAILED:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleNumberInfoReceived(null, Constants.ResultCode.HTTP_REQUEST_FAILED);
					break;
				case SINGLE_JSON_FAILED:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleNumberInfoReceived(null, Constants.ResultCode.JSON_PARSER_FAILED);
					break;
				case SINGLE_DISCONNECT:
					BicycleService.getInstance().getHttpEventListener().onSingleBicycleNumberInfoReceived(null, Constants.ResultCode.NETWORK_DISCONNECT);
					break;
				case CHECK_VERSION_COMPLETE:
					boolean needUpdate = msg.getData().getBoolean(Constants.ParcelableTag.VERSION_NEED_UPDATE);
					BicycleService.getInstance().getHttpEventListener().onNewVersionCheckCompleted(needUpdate, Constants.ResultCode.SUCCESS);
					break;
				case CHECK_VERSION_DISCONNECT:
					BicycleService.getInstance().getHttpEventListener().onNewVersionCheckCompleted(false, Constants.ResultCode.NETWORK_DISCONNECT);
					break;
				case CHECK_VERSION_HTTP_FAILED:
					BicycleService.getInstance().getHttpEventListener().onNewVersionCheckCompleted(false, Constants.ResultCode.HTTP_REQUEST_FAILED);
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
					BicycleNumberInfo bicycleNumberInfo = HttpUtils.getSingleBicycleInfoFromHttp(bicycleId);
					Bundle data = new Bundle();
					data.putParcelable(Constants.ParcelableTag.BICYCLE_STATION_INFO, bicycleNumberInfo);
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

	public void checkNewVersion(final String currentVersionName, final int currentVersionCode) {
		mExecutorService.execute(new Runnable() {			
			public void run() {
				try {
					boolean needUpdate = HttpUtils.checkVersion(currentVersionName, currentVersionCode);
					Bundle data = new Bundle();
					data.putBoolean(Constants.ParcelableTag.VERSION_NEED_UPDATE, needUpdate);
					Message message = Message.obtain();
					message.setData(data);
					message.what = CHECK_VERSION_COMPLETE;
					mHandler.sendMessage(message);
				} catch (NetworkException e) {
					mHandler.sendEmptyMessage(CHECK_VERSION_DISCONNECT);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(CHECK_VERSION_HTTP_FAILED);
				}
			}
		});		
	}
}
