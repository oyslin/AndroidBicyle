package com.walt;


public class Constants {
	public interface TabSetting{
		int IMAGE_ARRAY[] = {R.drawable.ic_tab_map,
							 R.drawable.ic_tab_list,
							 R.drawable.ic_tab_setting,
							 R.drawable.ic_tab_info};
		int TEXT_ARRAY[] = {R.string.tab_map,
							R.string.tab_list,
							R.string.tab_settings,
							R.string.tab_info};
		@SuppressWarnings("rawtypes")
		Class CONTENT_ARRAY[] = {BicyleMap.class,
								 BicyleList.class,
								 BicyleSetting.class,
								 BicyleInfo.class};
	}
}
