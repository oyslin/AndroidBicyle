package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IHttpEvent;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;

public class BicycleMore extends Activity implements IHttpEvent{
	private LayoutInflater mInflater = null;
	private LinearLayout mListContainer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_more);
		init();
	}
	
	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}
	
	private void init(){
		mInflater = getLayoutInflater();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(getText(R.string.title_more));
		
		mListContainer = (LinearLayout) findViewById(R.id.bicycle_more_list_container);
		this.addSettingItem();
		this.addEvent();
	}
	
	private void addSettingItem(){
		for(int i = 0, n = Constants.MoreListviewItem.SETTING_ITEM_IMAGE.length; i < n; i++){
			View view = mInflater.inflate(R.layout.setting_listview_item, mListContainer, false);
			
			ImageView imageView = (ImageView) view.findViewById(R.id.setting_listview_item_image);
			TextView textView = (TextView) view.findViewById(R.id.setting_listview_item_text);
			ImageView indicator = (ImageView) view.findViewById(R.id.setting_listview_item_next_indicator);
			
			imageView.setImageResource(Constants.MoreListviewItem.SETTING_ITEM_IMAGE[i]);
			textView.setText(Constants.MoreListviewItem.SETTING_ITEM_TEXT[i]);
			indicator.setImageResource(Constants.MoreListviewItem.SETTING_ITEM_NEXT_INDICATOR);			
			
			view.setOnClickListener(getOnFunctionSettingItemClickListener(i));
			
			view.setBackgroundResource(Constants.MoreListviewItem.BACKGROUND_IMAGE[i]);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, Utils.dip2px(Constants.MoreListviewItem.MARGIN_TOP_IN_DIP[i]), 0, 0);
			view.setLayoutParams(params);
			
			mListContainer.addView(view);
		}
	}
	private OnClickListener getOnFunctionSettingItemClickListener(int index){
		OnClickListener listener = null;
		switch (index) {
			case 0:			
			case 4:			
				final Intent intent = new Intent(this, Constants.MoreListviewItem.NEXT_ACTIVITY_ARRAY[index]);
				listener = new OnClickListener() {
					public void onClick(View v) {
						startActivity(intent);					
					}
				};
				break;
			case 1:
				listener = new OnClickListener() {
					public void onClick(View v) {					
						shareToFriend();
					}
				};
				break;
			case 2:
				listener = new OnClickListener() {					
					public void onClick(View v) {
						checkVersion();						
					}
				};
				break;
			case 3:
				listener = new OnClickListener() {					
					public void onClick(View v) {
						goToMarket();
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
	
	private void goToMarket(){
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HttpUrl.APP_URI)));
		} catch (Exception e) {
			Toast.makeText(this, R.string.toast_msg_version_no_market, Toast.LENGTH_SHORT).show();
		}		
	}
	
	private void checkVersion(){
		Toast.makeText(this, R.string.toast_msg_version_is_checking, Toast.LENGTH_SHORT).show();		
		PackageInfo packageInfo = Utils.getPackageInfo();
		BicycleService.getInstance().getHttpService().checkNewVersion(packageInfo.versionName, packageInfo.versionCode);
	}
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}
	
	private void addEvent(){
		BicycleService.getInstance().getHttpEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getHttpEventListener().removeEvent(this);
	}

	public void onAllBicyclesInfoReceived(int resultCode) {
		
	}

	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {
		
	}

	public void onNewVersionCheckCompleted(boolean needUpdate, int resultCode) {
		if(resultCode == Constants.ResultCode.NETWORK_DISCONNECT){
			Toast.makeText(this, R.string.toast_msg_network_error, Toast.LENGTH_SHORT).show();
		}else if(resultCode == Constants.ResultCode.SUCCESS){
			if(needUpdate){
				goToMarket();
			}else {
				Toast.makeText(this, R.string.toast_msg_version_is_up_to_date, Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(this, R.string.toast_msg_server_unavailable, Toast.LENGTH_SHORT).show();
		}
	}
}
