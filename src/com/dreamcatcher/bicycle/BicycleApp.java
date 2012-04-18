package com.dreamcatcher.bicycle;

import android.app.Application;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.dreamcatcher.bicycle.util.Constants;

public class BicycleApp extends Application {
	private static BicycleApp mInstance = null;
	private BMapManager mBMapMan = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initBaiduMap();
	}
	
	public static BicycleApp getInstance(){
		return mInstance;
	}
	
	public BMapManager getMapManager(){
		return mBMapMan;
	}

	public void initBaiduMap(){
		mBMapMan = new BMapManager(this);
		mBMapMan.init(Constants.BaiduApi.KEY, new MKGeneralListenerImp());
	}
	
	private static class MKGeneralListenerImp implements MKGeneralListener{
		public void onGetNetworkState(int resultCode) {
		}

		/**
		 * no baidu api perssion
		 */
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED){
				
			}				
		}
	}	
}
