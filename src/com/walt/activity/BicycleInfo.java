package com.walt.activity;

import android.app.Activity;
import android.os.Bundle;

import com.walt.R;
import com.walt.view.ActivityTitle;

public class BicycleInfo extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_info);
		init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_info);
	}
}
