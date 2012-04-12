package com.dreamcather.bicycle;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;

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
//			Log.e("BicycleApp", "onGetNetworkState resultCode = " + resultCode);
//			if(resultCode == MKEvent.ERROR_NETWORK_CONNECT || resultCode == MKEvent.ERROR_NETWORK_DATA){
//				Toast.makeText(mInstance, Utils.getText(R.string.toast_msg_network_error), Toast.LENGTH_LONG).show();
//			}
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
