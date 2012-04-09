package com.dreamcather.bicycle.util;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.activity.BicycleList;
import com.dreamcather.bicycle.activity.BicycleMap;
import com.dreamcather.bicycle.activity.BicycleQuery;
import com.dreamcather.bicycle.activity.BicycleSetting;
import com.dreamcather.bicycle.activity.FavoriteSettingActivity;
import com.dreamcather.bicycle.activity.MapSettingActivity;
import com.dreamcather.bicycle.activity.SearchSettingActivity;


public class Constants {
	public interface TabSetting{
		int IMAGE_ARRAY[] = {R.drawable.ic_tab_bicycle,
							 R.drawable.ic_tab_list,
							 R.drawable.ic_tab_query,
							 R.drawable.ic_tab_setting
							 };
		int TEXT_ARRAY[] = {R.string.tab_map,
							R.string.tab_list,
							R.string.tab_query,
							R.string.tab_settings
							};
		@SuppressWarnings("rawtypes")
		Class CONTENT_ARRAY[] = {BicycleMap.class,
								 BicycleList.class,
								 BicycleQuery.class,
								 BicycleSetting.class
								};
		
	}
	
	public interface CitySetting{
		String CITY_TAG[] = {"suzhou", "changshu", "kunshan", "nantong", "wujiang", "songjiang", "zhongshan", "shaoxing"};
		int CITY_NAME_RESID[] = {
				R.string.city_name_suzhou,
				R.string.city_name_changshu,
				R.string.city_name_kunshan,
				R.string.city_name_nantong,
				R.string.city_name_wujiang,
				R.string.city_name_songjiang,
				R.string.city_name_zhongshan,
				R.string.city_name_shaoxing
		};
	}

	public static final String CITY_SETTING_FILENAME = "setting/citysetting.json";
	
	public interface HttpSetting{		
		String HTTP_CONT_ENCODE = "UTF-8";
	}

	public interface BicycleJsonTag{
		String STATION = "station";
		String ID = "id";
		String NAME = "name";
		String LATITUDE = "lat";
		String LONGITUDE = "lng";
		String CAPACITY = "capacity";
		String AVAIABLE = "availBike";
		String ADDRESS = "address";
	}
	
	public interface SettingJsonTag{
		String TABS = "tabs";
		String ALL_BICYCLES_URL = "allBicyclesUrl";
		String BICYCLE_DETAIL_URL= "bicycleDetailUrl";
		String DEFAULT_LATITUDE = "defaultLatitude";
		String DEFAULT_LONGITUDE= "defaultLongitude";
		String OFFSET_LATITUDE= "offsetLatitude";
		String OFFSET_LONGITUDE= "offsetLongitude";
		String ASSETS_FILE_NAME= "assetsFileName";
	}
	
	public interface LocalStoreTag{
		String ALL_BICYCLE = "all_bicyCle";
		String CITY_NAME = "city_name";
	}
	
	public interface BaiduApi{
		String KEY = "1ABD83087C2F91F0F12E912BA87705648F714363";
	}
	
	public interface SettingListViewItem{
		int[] SETTING_ITEM_IMAGE = {
									R.drawable.ic_setting_map,
									R.drawable.ic_setting_search,
									R.drawable.ic_setting_favorite,
									R.drawable.ic_setting_share
		};
		String[] SETTING_ITEM_TEXT = {
									Utils.getText(R.string.setting_map),
									Utils.getText(R.string.setting_search),
									Utils.getText(R.string.setting_favorite),
									Utils.getText(R.string.setting_share)
		};
		int SETTING_ITEM_NEXT_INDICATOR = R.drawable.ic_setting_next_indicator;
		
		@SuppressWarnings("rawtypes")
		Class NEXT_ACTIVITY_ARRAY[] = {
			MapSettingActivity.class,
			SearchSettingActivity.class,
			FavoriteSettingActivity.class,
			null
		};
	}
}
