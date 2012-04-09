package com.dreamcather.bicycle.activity;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.view.ActivityTitle;

import android.app.Activity;
import android.os.Bundle;

public class FeedbackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		this.init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_feedback);
	}

}
