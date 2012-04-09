package com.dreamcather.bicycle.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.view.settinglistview.SettingListAdapter;
import com.dreamcather.bicycle.view.settinglistview.SettingListview;

public class BicycleSetting extends Activity {
	private SettingListview mListview = null;
	private SettingListAdapter mAdapter = null;
	private LayoutInflater mInflater = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_setting);
		init();
	}
	
	private void init(){
		mInflater = getLayoutInflater();
		mListview = (SettingListview) findViewById(R.id.bicycle_setting_listview);
		mAdapter = new SettingListAdapter(getSettingItems());
		mListview.setAdapter(mAdapter);
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(getText(R.string.title_setting));
	}
	
	private ArrayList<View> getSettingItems(){
		ArrayList<View> arrayList = new ArrayList<View>();		
		for(int i = 0, n = Constants.SettingListViewItem.SETTING_ITEM_IMAGE.length; i < n; i++){
			View view = mInflater.inflate(R.layout.setting_listview_item, mListview, false);
			ImageView imageView = (ImageView) view.findViewById(R.id.setting_listview_item_image);
			TextView textView = (TextView) view.findViewById(R.id.setting_listview_item_text);
			ImageView indicator = (ImageView) view.findViewById(R.id.setting_listview_item_next_indicator);
			
			imageView.setImageResource(Constants.SettingListViewItem.SETTING_ITEM_IMAGE[i]);
			textView.setText(Constants.SettingListViewItem.SETTING_ITEM_TEXT[i]);
			indicator.setImageResource(Constants.SettingListViewItem.SETTING_ITEM_NEXT_INDICATOR);			
			
			view.setOnClickListener(getOnClickListener(i));
			
			arrayList.add(view);
		}
		return arrayList;
	}
	
	private OnClickListener getOnClickListener(int index){
		OnClickListener listener = null;
		switch (index) {
		case 0:
		case 1:
		case 2:
			final Intent intent = new Intent(this, Constants.SettingListViewItem.NEXT_ACTIVITY_ARRAY[index]);
			listener = new OnClickListener() {				
				public void onClick(View v) {
					startActivity(intent);					
				}
			};
			break;
		case 3:
			listener = new OnClickListener() {				
				public void onClick(View v) {					
					shareToFriend();
				}
			};
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
