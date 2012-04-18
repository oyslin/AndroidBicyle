package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.view.ActivityTitle;

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
