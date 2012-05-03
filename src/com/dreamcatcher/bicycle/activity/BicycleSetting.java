package com.dreamcatcher.bicycle.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IAdEvent;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.ReminderNotification;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;
import com.dreamcatcher.bicycle.vo.Adsetting;
import com.uucun.adsdk.UUAppConnect;

public class BicycleSetting extends Activity implements IAdEvent{
	private LayoutInflater mInflater = null;
	private LinearLayout mListContainer = null;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private static long mReminderTimeValue = 0;
	private Handler mHandler = null;
	private final static int SPEND_SUCCESS = 0;
	
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
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}
	
	private void addEvent(){
		BicycleService.getInstance().getAdEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getAdEventListener().removeEvent(this);
	}
	
	private void init(){
		this.addEvent();
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == SPEND_SUCCESS){
					Toast.makeText(BicycleSetting.this, R.string.toast_msg_ad_remove_success, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		mInflater = getLayoutInflater();
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(getText(R.string.title_setting));
		
		mListContainer = (LinearLayout) findViewById(R.id.bicycle_setting_list_container);
		this.addSettingItem();
		
	}
	
	private void addSettingItem(){
		for(int i = 0, n = Constants.SettingListViewItem.SETTING_ITEM_IMAGE.length; i < n; i++){
			View view = mInflater.inflate(R.layout.setting_listview_item, mListContainer, false);
			
			ImageView imageView = (ImageView) view.findViewById(R.id.setting_listview_item_image);
			TextView textView = (TextView) view.findViewById(R.id.setting_listview_item_text);
			ImageView indicator = (ImageView) view.findViewById(R.id.setting_listview_item_next_indicator);
			
			imageView.setImageResource(Constants.SettingListViewItem.SETTING_ITEM_IMAGE[i]);
			textView.setText(Constants.SettingListViewItem.SETTING_ITEM_TEXT[i]);
			indicator.setImageResource(Constants.SettingListViewItem.SETTING_ITEM_NEXT_INDICATOR);			
			
			view.setOnClickListener(getOnFunctionSettingItemClickListener(i));
			
			view.setBackgroundResource(Constants.SettingListViewItem.BACKGROUND_IMAGE[i]);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, Utils.dip2px(Constants.SettingListViewItem.MARGIN_TOP_IN_DIP[i]), 0, 0);
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
				final Intent intent = new Intent(this, Constants.SettingListViewItem.NEXT_ACTIVITY_ARRAY[index]);
				listener = new OnClickListener() {				
					public void onClick(View v) {
						startActivity(intent);					
					}
				};
				break;			
			case 4:
				listener = new OnClickListener() {
					public void onClick(View v) {	
						showAdOffers();	
					}
				};
				break;
			case 3:
				listener = new OnClickListener() {				
					public void onClick(View v) {
						reminder();				
					}
				};				
				break;
			default:
				break;
		}
		return listener;
	}
	
	private void showAdOffers(){
		Adsetting adsetting = GlobalSetting.getInstance().getAdsetting();
		int currentPoint = adsetting.getPointTotal();
		long nextShowAdTime = adsetting.getNextShowAdTime();
		
		if(System.currentTimeMillis() > nextShowAdTime && currentPoint < Constants.AdSetting.REMOVE_AD_MIN_POINT ){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(String.format(getText(R.string.dialog_remove_ad_msg_not_enough).toString(), currentPoint))
				   .setPositiveButton(R.string.dialog_remove_ad_btn_earn_points, new DialogInterface.OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UUAppConnect.getInstance(BicycleSetting.this).showOffers();
							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();							
						}
					})
					.show();
			
		} else if(System.currentTimeMillis() > nextShowAdTime){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(String.format(getText(R.string.dialog_remove_ad_msg_enough).toString(), currentPoint))
					.setPositiveButton(R.string.dialog_remove_ad_btn_remove_ad, new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							removeAd();
							dialog.dismiss();
						}
					})
				   .setNeutralButton(R.string.dialog_remove_ad_btn_earn_points, new DialogInterface.OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UUAppConnect.getInstance(BicycleSetting.this).showOffers();
							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();							
						}
					})
					.show();
		} else {
			UUAppConnect.getInstance(BicycleSetting.this).showOffers();
		}
	}
	
	private void removeAd(){
		//set next show ad time
		long currentTime = System.currentTimeMillis();
		long nextShowAdTime = currentTime + Constants.AdSetting.MONTH_TIME;
		setNextShowAdTime(nextShowAdTime);
		BicycleService.getInstance().getAdService().spendPoints(Constants.AdSetting.REMOVE_AD_MIN_POINT);
	}
	
	private void reminder(){		
		View layout = mInflater.inflate(R.layout.return_bicycle_reminder, null, false);
		final TextView timeValueText = (TextView) layout.findViewById(R.id.return_bicycle_reminder_time_value);
		SeekBar seekBar = (SeekBar) layout.findViewById(R.id.return_bicycle_reminder_seekbar);
		RelativeLayout soundLine = (RelativeLayout) layout.findViewById(R.id.return_bicycle_reminder_sound_line);
		RelativeLayout vibrateLine = (RelativeLayout) layout.findViewById(R.id.return_bicycle_reminder_vibrate_line);
		RelativeLayout soundAndVibrateLine = (RelativeLayout) layout.findViewById(R.id.return_bicycle_reminder_sound_and_vibrate_line);
		final ImageView soundImage = (ImageView) layout.findViewById(R.id.return_bicycle_reminder_sound_image);
		final ImageView vibrateImage = (ImageView) layout.findViewById(R.id.return_bicycle_reminder_vibrate_image);
		final ImageView soundAndVibrateImage = (ImageView) layout.findViewById(R.id.return_bicycle_reminder_sound_and_vibrate_image);
		soundImage.setSelected(true);
		
		if(mReminderTimeValue != 0){
			int timeValue = Math.round((mReminderTimeValue - System.currentTimeMillis())/(1000 * 60));
			timeValueText.setText(String.valueOf(timeValue));
		}
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				timeValueText.setText(String.valueOf(progress));
			}
		});
		
		soundLine.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				soundImage.setSelected(true);
				vibrateImage.setSelected(false);
				soundAndVibrateImage.setSelected(false);
			}
		});
		
		vibrateLine.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				soundImage.setSelected(false);
				vibrateImage.setSelected(true);
				soundAndVibrateImage.setSelected(false);
			}
		});
		
		soundAndVibrateLine.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				soundImage.setSelected(false);
				vibrateImage.setSelected(false);
				soundAndVibrateImage.setSelected(true);				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.return_bicycle_reminder_dialog_title)
				.setView(layout)
				.setNegativeButton(R.string.return_bicycle_reminder_dialog_negative_btn,new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						cancelReminder();
						dialog.dismiss();						
					}
				})
				.setPositiveButton(R.string.return_bicycle_reminder_dialog_positive_btn, new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						int timeValue = Integer.parseInt(timeValueText.getText().toString());
						boolean soundSelected = soundImage.isSelected();
						boolean vibrateSelected = vibrateImage.isSelected();
						boolean soundAndVibrateSelected = soundAndVibrateImage.isSelected();
						startReminder(timeValue, soundSelected, vibrateSelected, soundAndVibrateSelected);
						dialog.dismiss();						
					}
				});
		
		builder.show();		
	}
	
	private void startReminder(int timeValue, boolean soundSelected, boolean vibrateSelected, boolean soundAndVibrateSelected){
		if(mTimer == null){
			mTimer = new Timer();
		}
		if(mTimerTask != null){
			mTimerTask.cancel();
		}
		mTimerTask = getTimerTask(soundSelected, vibrateSelected, soundAndVibrateSelected);
		long delay = 1000 * 60 * timeValue;
		
		mTimer.schedule(mTimerTask, delay);
		mReminderTimeValue = System.currentTimeMillis() + delay;
	}
	
	private TimerTask getTimerTask(final boolean soundSelected, final boolean vibrateSelected, final boolean soundAndVibrateSelected){
		return new TimerTask() {			
			@Override
			public void run() {
				if(soundAndVibrateSelected){
					Utils.reminderReturnBicycle();
					Utils.vibrate();
				}else if(soundSelected){
					Utils.reminderReturnBicycle();
				}else {
					Utils.vibrate();
				}
				ReminderNotification.startNotification();
			}
		};
	}
	
	private void cancelReminder(){
		if(mTimer != null){
			mTimer.cancel();
		}
	}
	
	private void setNextShowAdTime(long nextShowAdTime){
		Utils.storeLongDataToLocal(Constants.LocalStoreTag.NEXT_AD_SHOWN_TIME, nextShowAdTime);
		GlobalSetting.getInstance().getAdsetting().setNextShowAdTime(nextShowAdTime);
	}

	@Override
	public void onPointsUpdated(String currencyName, int totalPoint) {
		//setting current point
		GlobalSetting.getInstance().getAdsetting().setPointTotal(totalPoint);	
		
		mHandler.sendEmptyMessage(SPEND_SUCCESS);		
	}

	@Override
	public void onPointsUpdateFailed(String error) {
		//if error, set next show ad time to 0
		setNextShowAdTime(0);
	}
}
