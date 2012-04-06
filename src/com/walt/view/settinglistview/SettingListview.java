package com.walt.view.settinglistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.walt.BicycleApp;
import com.walt.R;

public class SettingListview extends LinearLayout {
	private ListView mListView = null;	

	public SettingListview(Context context) {
		super(context);
		init();
	}	
	
	public SettingListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		LayoutInflater inflater = LayoutInflater.from(BicycleApp.getInstance());
		inflater.inflate(R.layout.setting_listview, this, true);
		mListView = (ListView) findViewById(R.id.setting_listview);
	}


	public void setAdapter(ListAdapter adapter){
		mListView.setAdapter(adapter);
	}
}
