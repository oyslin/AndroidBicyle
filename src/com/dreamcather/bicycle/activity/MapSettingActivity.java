package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
	private ImageView mShowNearSpotsImage = null;
	private ImageView mShowAllBicyclesImage = null;
	private ImageView mShowOnlyFavoriteImage = null;
	
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
		mShowNearSpotsImage = (ImageView) findViewById(R.id.map_setting_show_nearest_spots_image);
		
		boolean showNearestSpot = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.SHOW_NEAREST_SPOTS, false);
		mShowNearSpotsImage.setSelected(showNearestSpot);
		
		showNearSpot.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowNearSpotsClicked();				
			}
		});
		
		RelativeLayout showAllBicycles = (RelativeLayout) findViewById(R.id.map_setting_show_all_bicycles);
		mShowAllBicyclesImage = (ImageView) findViewById(R.id.map_setting_show_all_bicycles_image);
		
		RelativeLayout showOnlyFavorites = (RelativeLayout) findViewById(R.id.map_setting_show_only_favorites);
		mShowOnlyFavoriteImage = (ImageView) findViewById(R.id.map_setting_show_only_favorites_image);
		
		boolean showAll = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.SHOW_ALL_BICYCLES, true);
		mShowAllBicyclesImage.setSelected(showAll);
		mShowOnlyFavoriteImage.setSelected(!showAll);
		
		showAllBicycles.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowAllBicyclesClicked();				
			}
		});
		
		showOnlyFavorites.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowOnlyFavoritesClicked();				
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
			new AlertDialog.Builder(this).setTitle(R.string.network_disconnect_alert_title)
							.setMessage(R.string.network_disconnect_alert_msg)
							.show();
			return;
		}
		//not wifi
		if(networkInfo != Constants.NetworkInfo.WIFI){
			new AlertDialog.Builder(this).setTitle(R.string.map_setting_download_offline_map_network_not_wifi_title)
							.setMessage(R.string.map_setting_download_offline_map_network_not_wifi_msg)
							.show();
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
		boolean selected = mShowNearSpotsImage.isSelected();
		mShowNearSpotsImage.setSelected(!selected);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.SHOW_NEAREST_SPOTS, !selected);
	}
	
	private void onShowAllBicyclesClicked(){
		mShowAllBicyclesImage.setSelected(true);
		mShowOnlyFavoriteImage.setSelected(false);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.SHOW_ALL_BICYCLES, true);
	}
	
	private void onShowOnlyFavoritesClicked(){
		mShowOnlyFavoriteImage.setSelected(true);
		mShowAllBicyclesImage.setSelected(false);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.SHOW_ALL_BICYCLES, false);
	}

	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:				
				MKOLUpdateElement update = mOfflineMap.getUpdateInfo(state);
				if (update.ratio == 100) {
					Toast.makeText(BicycleApp.getInstance(),getText(R.string.map_setting_download_offline_map_complete), Toast.LENGTH_SHORT).show();
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
