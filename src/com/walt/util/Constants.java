package com.walt.util;

import android.app.Activity;

import com.walt.R;
import com.walt.activity.BicycleInfo;
import com.walt.activity.BicycleList;
import com.walt.activity.BicycleMap;
import com.walt.activity.BicycleSetting;
import com.walt.activity.FavoriteSettingActivity;
import com.walt.activity.MapSettingActivity;
import com.walt.activity.SearchSettingActivity;


public class Constants {
	public interface TabSetting{
		int IMAGE_ARRAY[] = {R.drawable.ic_tab_bicycle,
							 R.drawable.ic_tab_list,
							 R.drawable.ic_tab_setting,
							 R.drawable.ic_tab_info};
		int TEXT_ARRAY[] = {R.string.tab_map,
							R.string.tab_list,
							R.string.tab_settings,
							R.string.tab_info};
		@SuppressWarnings("rawtypes")
		Class CONTENT_ARRAY[] = {BicycleMap.class,
								 BicycleList.class,
								 BicycleSetting.class,
								 BicycleInfo.class};
	}
	
	public interface HttpSetting{
		String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
		String ALL_BICYCLE_URL_SZ = "http://www.subicycle.com/szmap/ibikestation.asp";
		String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		String BICYCLE_DETAIL_URL_SZ = "http://www.subicycle.com/szmap/ibikestation.asp?id=";
	}
	
	public interface LocalSetting{
		double DEFAULT_LATITUDE = 31.653893;
		double DEFAULT_LONGITUDE = 120.754509;
		double OFFSET_LATITUDE = 0.005983; //Latitude offset need to add
		double OFFSET_LONGITUDE = 0.006450; //Longitude offset need to add
	}
	
	public interface JsonTag{
		String STATION = "station";
		String ID = "id";
		String NAME = "name";
		String LATITUDE = "lat";
		String LONGITUDE = "lng";
		String CAPACITY = "capacity";		
		String AVAIABLE = "availBike";
		String ADDRESS = "address";
	}
	
	public interface LocalStoreTag{
		String ALL_BICYLE = "all_bicyle";
	}
	
	public interface BaiduApi{
		String KEY = "A5FD8EB09C29A97B537DC78BB3A29608F1873BA4";
	}
	
	public interface AssetsFileName{
		String BICYLE_JSON = "bicycles.json";
	}
	
	public interface SettingListViewItem{
		int[] SETTING_ITEM_IMAGE = {
									R.drawable.ic_setting_map,
									R.drawable.ic_setting_search,
									R.drawable.ic_setting_favorite
		};
		String[] SETTING_ITEM_TEXT = {
									Utils.getText(R.string.setting_map),
									Utils.getText(R.string.setting_search),
									Utils.getText(R.string.setting_favorite)
		};
		int SETTING_ITEM_NEXT_INDICATOR = R.drawable.ic_setting_next_indicator;
		
		@SuppressWarnings("rawtypes")
		Class NEXT_ACTIVITY_ARRAY[] = {
			MapSettingActivity.class,
			SearchSettingActivity.class,
			FavoriteSettingActivity.class
		};
	}
}
