package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.view.ActivityTitle;

public class MapSettingActivity extends Activity {
	
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
	}
}
