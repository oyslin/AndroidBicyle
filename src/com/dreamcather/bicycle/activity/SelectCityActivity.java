package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.adapter.CityListAdapter;
import com.dreamcather.bicycle.adapter.CityListAdapter.ICityListEvent;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.IAssetsEvent;
import com.dreamcather.bicycle.interfaces.IAssetsService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.GlobalSetting;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.vo.CitySetting;

public class SelectCityActivity extends Activity implements IAssetsEvent{
	private int mSelectedCityIndex = -1;	
	private IAssetsService mAssetsService = null;
	private ProgressDialog mProgressDialog = null;
	private WebView mWebView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		init();
	}
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}

	private void init(){
		this.addEvent();
		
		mAssetsService = BicycleService.getInstance().getAssertsService();
		
		mWebView = (WebView) findViewById(R.id.select_city_webview);
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_select_city);
		
		ListView listView = (ListView) findViewById(R.id.select_city_list);
		
		ICityListEvent citySelectEvent = new ICityListEvent() {			
			public void onCityItemClicked(int index) {
				mSelectedCityIndex = index;				
			}
		};
		
		CityListAdapter adapter = new CityListAdapter(citySelectEvent, 0);
		
		listView.setAdapter(adapter);
		
		Button nextBtn = (Button) findViewById(R.id.select_city_next_btn);
		nextBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onNextBtnClicked();				
			}
		});
		
	}
	
	
	private void addEvent(){
		BicycleService.getInstance().getAssetsEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getAssetsEventListener().removeEvent(this);
	}
	
	private void onNextBtnClicked(){
		if(mSelectedCityIndex != -1){
			final String cityTag = Constants.CitySetting.CITY_TAG[mSelectedCityIndex];
			
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getText(R.string.progress_dialog_loading_msg));
			mProgressDialog.show();
			
			Utils.storeStringDataToLocal(Constants.LocalStoreTag.CITY_NAME, cityTag);
			mAssetsService.loadCitySetting();					
		}
	}
	
	public void onCitySettingLoaded(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mAssetsService.loadBicyclesInfo();
		}		
	}

	public void onBicyclesInfoLoaded(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
//			CitySetting citySetting = GlobalSetting.getInstance().getCitySetting();
//			//get cookie info
//			if(!"".equals(citySetting.getMapUrl())){				
//				WebSettings settings = mWebView.getSettings();
//		    	settings.setJavaScriptEnabled(true);
//		    	settings.setBlockNetworkImage(true);
//		    	mWebView.setWebViewClient(new MyWebViewClient());
//		    	mWebView.loadUrl(citySetting.getMapUrl());
//			}else {
				startActivity(new Intent(SelectCityActivity.this, Main.class));
				mProgressDialog.dismiss();
				finish();
//			}			
		}		
	}
	
	/**
	 * webview event
	 * @author Administrator
	 *
	 */
	private class MyWebViewClient extends WebViewClient {		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
			CookieManager cookieManager = CookieManager.getInstance();
			String cookieStr = cookieManager.getCookie(url);
			HttpUtils.updateCookie(cookieStr);
			
			startActivity(new Intent(SelectCityActivity.this, Main.class));
			mProgressDialog.dismiss();
			finish();
		}
	}
}
