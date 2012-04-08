package com.walt.activity;

import com.walt.R;
import com.walt.view.ActivityTitle;

import android.app.Activity;
import android.os.Bundle;

public class BicycleQuery extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_query);
		init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_query);
	}

}
