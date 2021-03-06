package com.dreamcatcher.bicycle.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.interfaces.IAdEvent;
import com.dreamcatcher.bicycle.interfaces.ISettingEvent;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.vo.CitySetting;
import com.kuguo.ad.PushAdsManager;
import com.uucun.adsdk.UUAppConnect;

public class Main extends TabActivity implements ISettingEvent, IAdEvent{
	private TabHost mTabHost;
	private LayoutInflater mLayoutInflater;
	private long mCurrentTime = 0;
	private static final int SPLASH_SCREEN_CODE = 1;
	private static final int SELECT_CITY_CODE = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(this, SplashScreen.class), SPLASH_SCREEN_CODE);        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == SPLASH_SCREEN_CODE){
    		if(resultCode == RESULT_OK){
    			boolean loadCompleted = data.getBooleanExtra("load_completed", false);
    			if(loadCompleted){
    				setContentView(R.layout.main);
    	            this.init();
    			}else {
					startActivityForResult(new Intent(this, SelectCityActivity.class), SELECT_CITY_CODE);
				}
    		}
    	}else if(requestCode == SELECT_CITY_CODE) {
			if(requestCode == RESULT_OK){
				setContentView(R.layout.main);
	            this.init();
			}
		}
    }
    
    @Override
	protected void onDestroy() {
		this.removeEvent();
		UUAppConnect.getInstance(this).exitSdk();
		super.onDestroy();
	}    

	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis() - mCurrentTime < 2000){
			Utils.exitApplication();
		}else {
			try {
				Toast.makeText(this, getText(R.string.exit_app_inform_msg), Toast.LENGTH_SHORT).show();
				mCurrentTime = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}	

	private void init(){
    	this.addEvent();
    	
    	mTabHost = getTabHost();
    	mLayoutInflater = LayoutInflater.from(this);
    	
    	CitySetting citySetting = GlobalSetting.getInstance().getCitySetting();
    	if(citySetting == null){
    		return;
    	}
    	int[] tabs = citySetting.getTabs();
    	int childrenCount = Constants.TabSetting.IMAGE_ARRAY.length;
    	
    	for(int i = 0; i < childrenCount; i++){    		
    		TabSpec tabSpec = mTabHost.newTabSpec(getString(Constants.TabSetting.TEXT_ARRAY[i])).setIndicator(getTabItemView(i)).setContent(getTabItemIntent(i));
    		
    		mTabHost.addTab(tabSpec);
    		mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
    		if(!inArray(i, tabs)){
    			mTabHost.getTabWidget().getChildAt(i).setVisibility(View.GONE);
    		}
    	}
    	
		UUAppConnect.getInstance(this).initSdk();
		
		//get ad config from server
		BicycleService.getInstance().getAdService().getPoints();
		
		//get push ad
		PushAdsManager paManager = PushAdsManager.getInstance();
		paManager.receivePushMessage(this, true);
    }
    
    private void addEvent(){
    	BicycleService.getInstance().getSettingEventListener().addEvent(this);
    	BicycleService.getInstance().getAdEventListener().addEvent(this);
    }
    
    private void removeEvent(){
    	BicycleService.getInstance().getSettingEventListener().removeEvent(this);
    	BicycleService.getInstance().getAdEventListener().removeEvent(this);
    }
    
    private boolean inArray(int data, int[] array){
    	boolean result = false;
    	for(int i = 0; i < array.length; i++){
    		if(data == array[i]){
    			result = true;
    			break;
    		}
    	}
    	return result;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	processIntent(intent);
    	super.onNewIntent(intent);
    }

    
    private void processIntent(Intent intent){
    	boolean isFromReminderNotificaiton = intent.getBooleanExtra(Constants.IntentExtraTag.MAIN_REMINDER_FROM_NOTIFICATION, false);
    	if(isFromReminderNotificaiton){
    		new AlertDialog.Builder(this)
    						.setTitle(R.string.return_bicycle_reminder_cancel_dialog_title)
    						.setMessage(R.string.return_bicycle_reminder_cancel_dialog_msg)
    						.setPositiveButton(R.string.return_bicycle_reminder_cancel_dialog_btn, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									closeReturnBicycleReminder();									
								}
							}).show();
    	}
    }
    
    private void closeReturnBicycleReminder(){
    	Utils.stopReminder();
    }
    
    private void reloadUI(){
    	CitySetting citySetting = GlobalSetting.getInstance().getCitySetting();
    	if(citySetting == null){
    		return;
    	}
    	int[] tabs = citySetting.getTabs();
    	int tabCount = mTabHost.getChildCount();
    	for(int i = 0; i < tabCount; i++){
    		if(inArray(i, tabs)){
    			mTabHost.getTabWidget().getChildAt(i).setVisibility(View.VISIBLE);
    		}else {
    			mTabHost.getTabWidget().getChildAt(i).setVisibility(View.VISIBLE);
			}
    	}
    }
    
    private View getTabItemView(int index){
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);	
		ImageView tabImage = (ImageView) view.findViewById(R.id.tab_item_image);
		if(tabImage != null){
			tabImage.setImageResource(Constants.TabSetting.IMAGE_ARRAY[index]);
		}
		
		TextView textView = (TextView) view.findViewById(R.id.tab_item_text);		
		textView.setText(Constants.TabSetting.TEXT_ARRAY[index]);
	
		return view;
    }
    
    private Intent getTabItemIntent(int index){
    	Intent intent = new Intent(this, Constants.TabSetting.CONTENT_ARRAY[index]);    	
    	return intent;
    }

	public void onCitySettingChanged(int resultCode) {
		reloadUI();	
	}
	
	public void onFavoriteIdsChanged() {
		
	}

	@Override
	public void onPointsUpdated(String currencyName, int totalPoint) {
		GlobalSetting.getInstance().getAdsetting().setPointTotal(totalPoint);		
	}

	@Override
	public void onPointsUpdateFailed(String error) {	
		
	}
    
}