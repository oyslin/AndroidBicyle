package com.walt.util;

import com.walt.R;
import com.walt.activity.BicycleList;
import com.walt.activity.BicycleMap;
import com.walt.activity.BicycleQuery;
import com.walt.activity.BicycleSetting;
import com.walt.activity.FavoriteSettingActivity;
import com.walt.activity.MapSettingActivity;
import com.walt.activity.SearchSettingActivity;


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
	
	public interface CityTabSetting{
		int[] SUZHOU = new int[]{0, 1, 2, 3};
		int[] CHANGSHU = new int[]{0, 1, 3};
		int[] KUNSHAN = new int[]{0, 1, 3};
		int[] NANTONG = new int[]{0, 1, 2, 3};
		int[] SONGJIANG = new int[]{0, 1, 2, 3};
		int[] ZHONGSHAN = new int[]{0, 1, 2, 3};
		int[] SHAOXING = new int[]{0, 1, 2, 3};
		int[] WUJIANG = new int[]{0, 1, 2, 3};
	}
	
	public interface CityTagSetting{
		String SUZHOU = "suzhou";
		String CHANGSHU = "changshu";
		String KUNSHAN = "kunshan";
		String NANTONG = "nantong";
		String SONGJIANG = "songjiang";
		String ZHONGSHAN = "zhongshan";
		String SHAOXING = "shaoxing";
		String WUJIANG = "wujiang";		
	}
	
	public interface HttpSetting{
		interface Suzhou{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Changshu{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Kunshan{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Nantong{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Songjiang{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Zhongshan{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Shaoxing{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		
		interface Wujiang{
			String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
			String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		}
		String ALL_BICYCLE_URL = "http://www.csbike01.com/csmap/ibikestation.asp";
		String ALL_BICYCLE_URL_SZ = "http://www.subicycle.com/szmap/ibikestation.asp";
		String BICYCLE_DETAIL_URL = "http://www.csbike01.com/csmap/ibikestation.asp?id=";
		String BICYCLE_DETAIL_URL_SZ = "http://www.subicycle.com/szmap/ibikestation.asp?id=";
		String HTTP_CONT_ENCODE = "UTF-8";
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
