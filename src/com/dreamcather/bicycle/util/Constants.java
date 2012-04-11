package com.dreamcather.bicycle.util;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.activity.AppInfo;
import com.dreamcather.bicycle.activity.BicycleList;
import com.dreamcather.bicycle.activity.BicycleMap;
import com.dreamcather.bicycle.activity.BicycleQuery;
import com.dreamcather.bicycle.activity.BicycleSetting;
import com.dreamcather.bicycle.activity.ChangeCityActivity;
import com.dreamcather.bicycle.activity.FavoriteSettingActivity;
import com.dreamcather.bicycle.activity.FeedbackActivity;
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
		String CITY_TAG[] = {"suzhou", "changshu", "kunshan", "nantong", "wujiang", "zhongshan", "shaoxing"};
		int CITY_NAME_RESID[] = {
				R.string.city_name_suzhou,
				R.string.city_name_changshu,
				R.string.city_name_kunshan,
				R.string.city_name_nantong,
				R.string.city_name_wujiang,				
				R.string.city_name_zhongshan,
				R.string.city_name_shaoxing
		};
		int CITY_MAP_ID[] = {
				224,
				2113,
				2581,
				161,
				2284,
				187,
				293
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
		String ALL_BICYCLE = "all_bicycle";
		String CITY_NAME = "city_name";
		String AUTO_LOCATE_ON_STARTUP = "auto_locate";
		String SHOW_NEAREST_SPOTS = "show_nearest_spot";
		String SHOW_ALL_BICYCLES = "show_all_bicycles";
	}
	
	public interface BaiduApi{
		String KEY = "1ABD83087C2F91F0F12E912BA87705648F714363";
	}
	
	public interface SettingListViewItem{
		int[] SETTING_ITEM_IMAGE = {
									R.drawable.ic_setting_map,
									R.drawable.ic_setting_changecity,
									R.drawable.ic_setting_favorite,
									R.drawable.ic_setting_search,
									R.drawable.ic_setting_feedback,
									R.drawable.ic_setting_share,
									R.drawable.ic_setting_update,
									R.drawable.ic_setting_about
		};
		String[] SETTING_ITEM_TEXT = {
									Utils.getText(R.string.setting_map),
									Utils.getText(R.string.setting_changecity),
									Utils.getText(R.string.setting_favorite),
									Utils.getText(R.string.setting_search),
									Utils.getText(R.string.setting_feedback),
									Utils.getText(R.string.setting_share),
									Utils.getText(R.string.setting_update),
									Utils.getText(R.string.setting_about)
		};
		int SETTING_ITEM_NEXT_INDICATOR = R.drawable.ic_setting_next_indicator;
		
		@SuppressWarnings("rawtypes")
		Class NEXT_ACTIVITY_ARRAY[] = {
									MapSettingActivity.class,
									ChangeCityActivity.class,
									FavoriteSettingActivity.class,
									SearchSettingActivity.class,
									FeedbackActivity.class,
									null,
									null,
									AppInfo.class
		};
		
		int[] MARGIN_TOP_IN_DIP = {
				0,
				-1,
				-1,
				-1,
				20,
				-1,
				-1,
				-1				
		};
		
		int[] BACKGROUND_IMAGE ={
				R.drawable.setting_listitem_bg_top,
				R.drawable.setting_listitem_bg_middle,
				R.drawable.setting_listitem_bg_middle,
				R.drawable.setting_listitem_bg_bottom,
				R.drawable.setting_listitem_bg_top,
				R.drawable.setting_listitem_bg_middle,
				R.drawable.setting_listitem_bg_middle,
				R.drawable.setting_listitem_bg_bottom,
		};
	}	
	
	public interface ResultCode{
		int SUCCESS = 0;
		int HTTP_REQUEST_FAILED = 1;
		int JSON_PARSER_FAILED = 2;
		int LOAD_ASSETS_FAILED = 3;
		int CHANGE_CITY_FAILED = 4;
	}
	
	public interface NetworkInfo{
		int DISCONNECT = 0;
		int WIFI = 1;
		int MOBILE = 2;
	}
	
}
