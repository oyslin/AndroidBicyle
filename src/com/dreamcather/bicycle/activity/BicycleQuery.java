package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.view.ActivityTitle;

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
	
	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}
}
