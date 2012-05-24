package com.dreamcatcher.bicycle.util;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.activity.AppInfo;
import com.dreamcatcher.bicycle.activity.BicycleList;
import com.dreamcatcher.bicycle.activity.BicycleMap;
import com.dreamcatcher.bicycle.activity.BicycleMore;
import com.dreamcatcher.bicycle.activity.BicycleSetting;
import com.dreamcatcher.bicycle.activity.ChangeCityActivity;
import com.dreamcatcher.bicycle.activity.FavoriteSettingActivity;
import com.dreamcatcher.bicycle.activity.FeedbackActivity;
import com.dreamcatcher.bicycle.activity.MapSettingActivity;


public class Constants {
	public interface TabSetting{
		int IMAGE_ARRAY[] = {R.drawable.ic_tab_bicycle,
							 R.drawable.ic_tab_list,
							 R.drawable.ic_tab_setting,
							 R.drawable.ic_tab_more
							 };
		int TEXT_ARRAY[] = {R.string.tab_map,
							R.string.tab_list,
							R.string.tab_settings,
							R.string.tab_more
							};
		@SuppressWarnings("rawtypes")
		Class CONTENT_ARRAY[] = {BicycleMap.class,
								 BicycleList.class,
								 BicycleSetting.class,
								 BicycleMore.class
								};
		
	}
	
	public interface CitySetting{
		String CITY_SETTING_FILENAME = "setting/citysetting.json";
		String CITY_TAG[] = {"suzhou", "changshu", "kunshan", "nantong", "wujiang", "zhongshan", "shaoxing", "wuhan"};
		int CITY_NAME_RESID[] = {
				R.string.city_name_suzhou,
				R.string.city_name_changshu,
				R.string.city_name_kunshan,
				R.string.city_name_nantong,
				R.string.city_name_wujiang,				
				R.string.city_name_zhongshan,
				R.string.city_name_shaoxing,
				R.string.city_name_wuhan
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
		String SHOW_BICYCLE_NUMBER = "showBicycleNumber";
		String REFRESH_POP = "refreshPop";
		String NEED_DECODE = "needDecode";
	}
	
	public interface LocalStoreTag{
		String ALL_BICYCLE = "all_bicycle";
		String CITY_NAME = "city_name";
		String AUTO_LOCATE_ON_STARTUP = "auto_locate";
		String SHOW_NEAREST_SPOTS = "show_nearest_spot";
		String SHOW_FAVORITE_SPOTS = "show_favorite_spots";
		String FAVORITE_IDS = "favorite_ids";
		String OFFLINE_MAP_PERCENTAGE = "offline_map_percentage";
		String NEXT_AD_SHOWN_TIME = "next_ad_shown_time";
	}
	
	public interface IntentExtraTag{
		String MAIN_REMINDER_FROM_NOTIFICATION = "is_from_reminder_notifaction";
	}
	
	public interface BaiduApi{
		String KEY = "59CC96DE7DFC977509A4F484D9E48D6B66E6FBCA";
	}
	
	public interface SettingListViewItem{
		int[] SETTING_ITEM_IMAGE = {
									R.drawable.ic_setting_map,
									R.drawable.ic_setting_changecity,
									R.drawable.ic_setting_favorite,
									R.drawable.ic_reminder,
									R.drawable.ic_setting_recommend
		};
		String[] SETTING_ITEM_TEXT = {
									Utils.getText(R.string.setting_map),
									Utils.getText(R.string.setting_changecity),
									Utils.getText(R.string.setting_favorite),
									Utils.getText(R.string.setting_return_reminder),
									Utils.getText(R.string.setting_recommend)									
		};
		int SETTING_ITEM_NEXT_INDICATOR = R.drawable.ic_setting_next_indicator;
		
		@SuppressWarnings("rawtypes")
		Class NEXT_ACTIVITY_ARRAY[] = {
									MapSettingActivity.class,
									ChangeCityActivity.class,
									FavoriteSettingActivity.class,
									null,
									null,
		};
		
		int[] MARGIN_TOP_IN_DIP = {
									0,
									-1,
									-1,
									-1,
									-1
		};
		
		int[] BACKGROUND_IMAGE ={
									R.drawable.setting_listitem_bg_top,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_bottom				
		};
	}
	
	public interface MoreListviewItem{
		int[] SETTING_ITEM_IMAGE = {						
									R.drawable.ic_setting_feedback,
									R.drawable.ic_setting_share,
									R.drawable.ic_setting_update,
									R.drawable.ic_setting_rating,
									R.drawable.ic_setting_about
		};
		String[] SETTING_ITEM_TEXT = {						
									Utils.getText(R.string.setting_feedback),
									Utils.getText(R.string.setting_share),
									Utils.getText(R.string.setting_update),
									Utils.getText(R.string.setting_rating),
									Utils.getText(R.string.setting_about)
		};
		int SETTING_ITEM_NEXT_INDICATOR = R.drawable.ic_setting_next_indicator;
		
		@SuppressWarnings("rawtypes")
		Class NEXT_ACTIVITY_ARRAY[] = {						
									FeedbackActivity.class,
									null,
									null,
									null,
									AppInfo.class
		};
		
		int[] MARGIN_TOP_IN_DIP = {
									0,
									-1,
									-1,
									-1,
									-1
		};
		
		int[] BACKGROUND_IMAGE ={
									R.drawable.setting_listitem_bg_top,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_middle,
									R.drawable.setting_listitem_bg_bottom
		};
	}
	
	public interface ResultCode{
		int SUCCESS = 0;
		int HTTP_REQUEST_FAILED = 1;
		int JSON_PARSER_FAILED = 2;
		int LOAD_ASSETS_FAILED = 3;
		int CHANGE_CITY_FAILED = 4;
		int NETWORK_DISCONNECT = 5;
	}
	
	public interface NetworkInfo{
		int DISCONNECT = 0;
		int WIFI = 1;
		int MOBILE = 2;
	}
	
	public interface ParcelableTag{
		String BICYCLE_STATION_INFO = "bicycle_station_info";
		String VERSION_NEED_UPDATE = "need_update";
		String CURRENCY_NAME = "currency_name";
		String TOTAL_POINT = "total_point";
		String GET_POINT_ERROR_MSG = "get_point_error_msg";
	}
	
	public interface HttpUrl{
		String VERSION_INFO_URL = "http://waltoy.cnodejs.net/versioninfo";
		String FEEDBACK_URL = "http://waltoy.cnodejs.net/feedback";
		String APP_URI = "market://details?id=com.dreamcatcher.bicycle";
	}
	
	public interface AdSetting{
		String SHOW_AD = "showAd";
		int REMOVE_AD_MIN_POINT = 200;
		long MONTH_TIME = 2592000000L;
	}
}
