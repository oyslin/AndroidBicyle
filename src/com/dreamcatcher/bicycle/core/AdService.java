package com.dreamcatcher.bicycle.core;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.interfaces.IAdService;
import com.dreamcatcher.bicycle.util.Constants;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

public class AdService implements IAdService, UpdatePointsNotifier {
	private BicycleApp mApp = BicycleApp.getInstance();
	private Handler mHandler = null;
	private final static int GET_POINTS_SUCCESS = 0;
	private final static int GET_POINTS_FAILED = 1;
	
	public AdService(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GET_POINTS_SUCCESS:
					String currencyName = msg.getData().getString(Constants.ParcelableTag.CURRENCY_NAME);
					int totalPoint = msg.getData().getInt(Constants.ParcelableTag.TOTAL_POINT);
					
					BicycleService.getInstance().getAdEventListener().onPointsUpdated(currencyName, totalPoint);
					break;
				case GET_POINTS_FAILED:
					String error = msg.getData().getString(Constants.ParcelableTag.CURRENCY_NAME);
					BicycleService.getInstance().getAdEventListener().onPointsUpdateFailed(error);
					break;
				default:
					break;
				}
			}
		};
	}
	
	
	@Override
	public void getPoints() {
		AppConnect.getInstance(mApp).getPoints(this);
	}

	@Override
	public void spendPoints(int point) {
		AppConnect.getInstance(mApp).spendPoints(point, this);
	}

	@Override
	public void awardPoint(int point) {
		AppConnect.getInstance(mApp).awardPoints(point, this);
	}

	@Override
	public void getUpdatePoints(String currencyName, int totalPoint) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString(Constants.ParcelableTag.CURRENCY_NAME, currencyName);
		data.putInt(Constants.ParcelableTag.TOTAL_POINT, totalPoint);
		msg.setData(data);
		mHandler.sendMessage(msg);
	}

	@Override
	public void getUpdatePointsFailed(String error) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString(Constants.ParcelableTag.GET_POINT_ERROR_MSG, error);
		msg.setData(data);
		mHandler.sendMessage(msg);		
	}
}
