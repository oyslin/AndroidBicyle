package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

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
		TextView homepage = (TextView) findViewById(R.id.bicycle_info_homepage);
		TextView serviceCall = (TextView) findViewById(R.id.bicycle_info_service_call);
		final String pageUrl = homepage.getText().toString();
		final String serviceNumber = serviceCall.getText().toString();
		
		homepage.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Utils.startBrowser(BicycleInfo.this, "http://" + pageUrl);
			}
		});
		
		serviceCall.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Utils.startCall(BicycleInfo.this, serviceNumber);
			}
		});
		
		
		
	}
}
