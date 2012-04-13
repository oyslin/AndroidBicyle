package com.dreamcather.bicycle.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.ISettingEvent;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.GlobalSetting;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.vo.CitySetting;

public class Main extends TabActivity implements ISettingEvent{
	private TabHost mTabHost;
	private LayoutInflater mLayoutInflater;
	private long mCurrentTime = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.init();
    }    
    
    @Override
	protected void onDestroy() {
		this.removeEvent();
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
    	//first load bicycle station info from server
    	loadBicycleInfoFromServer();
    	
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
    }
    
    private void addEvent(){
    	BicycleService.getInstance().getSettingEventListener().addEvent(this);
    }
    
    private void removeEvent(){
    	BicycleService.getInstance().getSettingEventListener().removeEvent(this);
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
    
    /**
     * load bicycles info from server via thread
     */
    private void loadBicycleInfoFromServer(){
//    	BicycleService.getInstance().getHttpService().getAllBicyclesInfo();    	
    }

	public void onCitySettingChanged(int resultCode) {
		reloadUI();	
	}   
    
}