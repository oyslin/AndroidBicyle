package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;

public class BicycleSetting extends Activity {
	private LayoutInflater mInflater = null;
	private LinearLayout mListContainer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_setting);
		init();
	}
	
	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}
	
	private void init(){
		mInflater = getLayoutInflater();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(getText(R.string.title_setting));
		
		mListContainer = (LinearLayout) findViewById(R.id.bicycle_setting_list_container);
		this.addSettingItem();
	}
	
	private void addSettingItem(){
		for(int i = 0, n = Constants.SettingListViewItemFunction.SETTING_ITEM_IMAGE.length; i < n; i++){
			View view = mInflater.inflate(R.layout.setting_listview_item, mListContainer, false);
			
			ImageView imageView = (ImageView) view.findViewById(R.id.setting_listview_item_image);
			TextView textView = (TextView) view.findViewById(R.id.setting_listview_item_text);
			ImageView indicator = (ImageView) view.findViewById(R.id.setting_listview_item_next_indicator);
			
			imageView.setImageResource(Constants.SettingListViewItemFunction.SETTING_ITEM_IMAGE[i]);
			textView.setText(Constants.SettingListViewItemFunction.SETTING_ITEM_TEXT[i]);
			indicator.setImageResource(Constants.SettingListViewItemFunction.SETTING_ITEM_NEXT_INDICATOR);			
			
			view.setOnClickListener(getOnFunctionSettingItemClickListener(i));
			
			view.setBackgroundResource(Constants.SettingListViewItemFunction.BACKGROUND_IMAGE[i]);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, Utils.dip2px(Constants.SettingListViewItemFunction.MARGIN_TOP_IN_DIP[i]), 0, 0);
			view.setLayoutParams(params);
			
			mListContainer.addView(view);
		}
	}
	
	private OnClickListener getOnFunctionSettingItemClickListener(int index){
		OnClickListener listener = null;
		switch (index) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 7:
				final Intent intent = new Intent(this, Constants.SettingListViewItemFunction.NEXT_ACTIVITY_ARRAY[index]);
				listener = new OnClickListener() {				
					public void onClick(View v) {
						startActivity(intent);					
					}
				};
				break;
			case 5:
				listener = new OnClickListener() {
					public void onClick(View v) {					
						shareToFriend();
					}
				};
				break;
			case 6:
				break;
			default:
				break;
		}
		return listener;
	}
	
	private void shareToFriend(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_message));
		intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.share_title));
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, getText(R.string.share_chooser_title)));
	}
}
