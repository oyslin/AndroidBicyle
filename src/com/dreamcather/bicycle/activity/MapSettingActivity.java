package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.BMapManager;
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void onAutoLocateLineClicked(){
		boolean selected =  mAutoLocateImage.isSelected();
		mAutoLocateImage.setSelected(!selected);
		Utils.storeBooleanDataToLocal(Constants.LocalStoreTag.AUTO_LOCATE_ON_STARTUP, !selected);
	}
	
	private void downloadOfflineMap(){
		
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

	public void onGetOfflineMapState(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
