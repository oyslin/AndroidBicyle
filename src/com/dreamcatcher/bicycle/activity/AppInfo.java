package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;

public class AppInfo extends Activity {
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
		TextView versionInfoText = (TextView) findViewById(R.id.bicycle_info_version);
		PackageInfo packageInfo = Utils.getPackageInfo();
		if(packageInfo != null){
			String version = packageInfo.versionName + "." + packageInfo.versionCode;
			versionInfoText.setText(version);
		}		
	}
}
