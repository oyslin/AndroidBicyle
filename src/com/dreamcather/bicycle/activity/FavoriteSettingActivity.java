package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.view.ActivityTitle;

public class FavoriteSettingActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_setting);
		init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_favorite_setting);
	}
}
