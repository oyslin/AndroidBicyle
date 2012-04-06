package com.walt.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.walt.R;
import com.walt.util.Constants;

public class Main extends TabActivity {
	private TabHost mTabHost;
	private LayoutInflater mLayoutInflater;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    
    private void init(){
    	mTabHost = getTabHost();
    	mLayoutInflater = LayoutInflater.from(this);    	
    	
    	int childrenCount = Constants.TabSetting.IMAGE_ARRAY.length;
    	for(int i = 0; i < childrenCount; i++){
    		TabSpec tabSpec = mTabHost.newTabSpec(getString(Constants.TabSetting.TEXT_ARRAY[i])).setIndicator(getTabItemView(i)).setContent(getTabItemIntent(i));
    		mTabHost.addTab(tabSpec);
    		mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
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
//		Drawable tabIcon = getResources().getDrawable(Constants.TabSetting.IMAGE_ARRAY[index]);
//		textView.setCompoundDrawables(null, tabIcon, null, null);
	
		return view;
    }
    
    private Intent getTabItemIntent(int index){
    	Intent intent = new Intent(this, Constants.TabSetting.CONTENT_ARRAY[index]);    	
    	return intent;
    }
}