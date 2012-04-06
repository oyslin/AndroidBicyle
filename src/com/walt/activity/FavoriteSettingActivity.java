package com.walt.activity;

import com.walt.R;
import com.walt.view.ActivityTitle;

import android.app.Activity;
import android.os.Bundle;

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
