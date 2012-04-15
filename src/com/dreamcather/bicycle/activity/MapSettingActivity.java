package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKOLUpdateElement;
import com.baidu.mapapi.MKOfflineMap;
import com.baidu.mapapi.MKOfflineMapListener;
import com.dreamcather.bicycle.BicycleApp;
import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

public class MapSettingActivity extends Activity implements MKOfflineMapListener{
	private ImageView mAutoLocateImage = null;
	private ImageView mShowFavoriteImage = null;
	private TextView mOfflineMapPercentage = null;
	
	private BMapManager mMapManger = null;
	private MKOfflineMap mOfflineMap = null;
	private int mCityId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_setting);
		init();
	}
	
	private void init(){
		mMapManger = BicycleApp.getInstance().getMapManager();
		if(mMapManger == null){
			BicycleApp.getInstance().initBaiduMap();
		}
		mMapManger.start();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_map_setting);
		
		mOfflineMapPercentage = (TextView) findViewById(R.id.map_setting_download_offline_map_percentage);
		int offlinePercentage = Utils.getIntDataFromLocal(Constants.LocalStoreTag.OFFLINE_MAP_PERCENTAGE, 0);
		if(offlinePercentage > 0){
			showOfflineMapPercentage(offlinePercentage);
		}
		
		RelativeLayout autoLocateLine = (RelativeLayout) findViewById(R.id.map_setting_auto_locate);
		mAutoLocateImage = (ImageView) findViewById(R.id.map_setting_auto_locate_image);
		
		boolean autoLocate = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.AUTO_LOCATE_ON_STARTUP, false);		
		mAutoLocateImage.setSelected(autoLocate);
		
		autoLocateLine.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				onAutoLocateLineClicked();				
			}
		});
		
		RelativeLayout downloadOfflineMap = (RelativeLayout) findViewById(R.id.map_setting_download_offline_map);
		downloadOfflineMap.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				downloadOfflineMap();				
			}
		});
		
		RelativeLayout showNearSpot = (RelativeLayout) findViewById(R.id.map_setting_show_nearest_spots);
		
//		int showNearestSpot = Utils.getIntDataFromLocal(Constants.LocalStoreTag.SHOW_NEAREST_SPOTS, -1);
		
		
		showNearSpot.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowNearSpotsClicked();				
			}
		});
		
		RelativeLayout showFavorites = (RelativeLayout) findViewById(R.id.map_setting_show_favorites);
		mShowFavoriteImage = (ImageView) findViewById(R.id.map_setting_show_favorites_image);
		
		boolean showFavorite = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.SHOW_FAVORITE_SPOTS, false);
		mShowFavoriteImage.setSelected(showFavorite);
		
		showFavorites.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowFavoritesClicked();				
			}
		});
	}
		
	@Override
	protected void onResume() {		
		super.onResume();
		if(mMapManger != null){
			mMapManger.start();
		}
	}

	@Override
	protected void onPause() {
		if(mMapManger != null){
			mMapManger.stop();
		}
		super.onPause();
	}
	
	private void showOfflineMapPercentage(int percentage){
		mOfflineMapPercentage.setText(String.format(getText(R.string.map_setting_download_offline_map_percentage).toString(), percentage));
	}

	private void onAutoLocateLineClicked(){
		boolean selected =  mAutoLocateImage.isSelected();
		mAutoLocateImage.setSelected(!selected);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.AUTO_LOCATE_ON_STARTUP, !selected);
	}
	
	/**
	 * download offline map
	 */
	private void downloadOfflineMap(){
		mOfflineMap =  new MKOfflineMap();
		mOfflineMap.init(mMapManger, this);
		
		int networkInfo = Utils.getNetworkInfo();
		//network unavailable
		if(networkInfo == Constants.NetworkInfo.DISCONNECT){
			Toast.makeText(this, R.string.toast_msg_network_error, Toast.LENGTH_SHORT).show();
			return;
		}
		//not wifi
		if(networkInfo != Constants.NetworkInfo.WIFI){
			Toast.makeText(this, R.string.map_setting_download_offline_map_network_not_wifi_msg, Toast.LENGTH_SHORT).show();
			return;
		}
		
		mCityId = Constants.CitySetting.CITY_MAP_ID[getCityIndex()];
		
		MKOLUpdateElement mapInfo = mOfflineMap.getUpdateInfo(mCityId);
		
		String startMsg = "";
		
		if(mapInfo != null){
			if(mapInfo.ratio == 100){
				Toast.makeText(this, getText(R.string.map_setting_map_already_complete), Toast.LENGTH_LONG).show();
				return;
			}else if(mapInfo.ratio != 0){
				startMsg = getText(R.string.map_setting_download_offline_map_already_downloaded).toString();
			}else {
				startMsg = getText(R.string.map_setting_download_offline_map_started).toString();
			}
		}
		
		if(mOfflineMap.start(mCityId)){
			Toast.makeText(this, startMsg, Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(this, getText(R.string.map_setting_download_offline_map_start_failed), Toast.LENGTH_SHORT).show();
		}
	}
	
	private int getCityIndex(){
		String currentCityTag = Utils.getStringDataFromLocal(Constants.LocalStoreTag.CITY_NAME);
		int index = -1;
		for(int i = 0, n = Constants.CitySetting.CITY_TAG.length; i < n; i++){
			if (currentCityTag.equalsIgnoreCase(Constants.CitySetting.CITY_TAG[i])){
				index = i;
				break;
			}
		}
		return index;
	}
	
	private void onShowNearSpotsClicked(){
		
	}
	
	
	private void onShowFavoritesClicked(){
		boolean selected = mShowFavoriteImage.isSelected();
		mShowFavoriteImage.setSelected(!selected);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.SHOW_FAVORITE_SPOTS, false);
	}

	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:				
				MKOLUpdateElement update = mOfflineMap.getUpdateInfo(state);
				if (update.ratio == 100) {
					Toast.makeText(BicycleApp.getInstance(),getText(R.string.map_setting_download_offline_map_complete), Toast.LENGTH_SHORT).show();
				}else {
					showOfflineMapPercentage(update.ratio);
					Utils.storeIntDataToLocal(Constants.LocalStoreTag.OFFLINE_MAP_PERCENTAGE, update.ratio);
				}
	
				break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				Log.d("OfflineDemo", String.format("new offlinemap ver"));
				break;
			}
		
	}
}
