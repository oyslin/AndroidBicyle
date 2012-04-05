package com.walt.util;

import com.walt.R;
import com.walt.activity.BicycleInfo;
import com.walt.activity.BicycleList;
import com.walt.activity.BicycleMap;
import com.walt.activity.BicycleSetting;


public class Constants {
	public interface TabSetting{
		int IMAGE_ARRAY[] = {R.drawable.ic_tab_bicycle_selected,
							 R.drawable.ic_tab_list_selected,
							 R.drawable.ic_tab_setting_selected,
							 R.drawable.ic_tab_info_selected};
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
		String KEY = "C65DFF635413A9E83E638C6EBA2C414CDF4E77E8";
	}
	
	public interface AssetsFileName{
		String BICYLE_JSON = "bicycles.json";
	}
}
