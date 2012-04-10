package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.view.ActivityTitle;

public class MapSettingActivity extends Activity {
	private ImageView mAutoLocateImage = null;
	private ImageView mShowNearSpotsImage = null;
	private ImageView mShowAllBicyclesImage = null;
	private ImageView mShowOnlyFavoriteImage = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_setting);
		init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_map_setting);
		
		RelativeLayout autoLocateLine = (RelativeLayout) findViewById(R.id.map_setting_auto_locate);
		mAutoLocateImage = (ImageView) findViewById(R.id.map_setting_auto_locate_image);
		
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
		
		showNearSpot.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onShowNearSpotsClicked();				
			}
		});
		
		RelativeLayout showAllBicycles = (RelativeLayout) findViewById(R.id.map_setting_show_all_bicycles);
		mShowAllBicyclesImage = (ImageView) findViewById(R.id.map_setting_show_all_bicycles_image);
		
		RelativeLayout showOnlyFavorites = (RelativeLayout) findViewById(R.id.map_setting_show_only_favorites);
		mShowOnlyFavoriteImage = (ImageView) findViewById(R.id.map_setting_show_only_favorites_image);
		
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
	
	private void onAutoLocateLineClicked(){
		boolean selected =  mAutoLocateImage.isSelected();
		mAutoLocateImage.setSelected(!selected);
	}
	
	private void downloadOfflineMap(){
		
	}
	
	private void onShowNearSpotsClicked(){
		boolean selected = mShowNearSpotsImage.isSelected();
		mShowNearSpotsImage.setSelected(!selected);
	}
	
	private void onShowAllBicyclesClicked(){
		mShowAllBicyclesImage.setSelected(true);
		mShowOnlyFavoriteImage.setSelected(false);
	}
	
	private void onShowOnlyFavoritesClicked(){
		mShowOnlyFavoriteImage.setSelected(true);
		mShowAllBicyclesImage.setSelected(false);
	}
}
