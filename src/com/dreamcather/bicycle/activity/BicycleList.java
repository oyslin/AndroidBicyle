package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.adapter.BicycleListAdapter;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.IHttpEvent;
import com.dreamcather.bicycle.interfaces.ISettingEvent;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.dreamcather.bicycle.vo.BicycleStationInfo;

public class BicycleList extends Activity implements IHttpEvent, ISettingEvent{
	private ActivityTitle mActivityTitle = null;
	private BicycleListAdapter mAdapter = null;
	private ListView mListView = null;
	private ProgressDialog mProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_list);
		init();
	}
	
	private void init(){
		this.addEvent();
		
		mActivityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		mActivityTitle.setActivityTitle(getText(R.string.title_list));
		
		IActivityTitleRightImageClickEvent rightImageClickEvent = new IActivityTitleRightImageClickEvent() {			
			public void onRightImageClicked() {
				loadAllBicyclesInfoFromServer();
			}
		};
		
		mActivityTitle.setRightImage(R.drawable.ic_titlebar_refresh, rightImageClickEvent);
		
		mListView = (ListView) findViewById(R.id.bicycle_listview);		
		mAdapter = new BicycleListAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	
	@Override
	protected void onDestroy() {
		this.removeEvent();
		super.onDestroy();
	}

	private void addEvent(){
		BicycleService.getInstance().getHttpEventListener().addEvent(this);
		BicycleService.getInstance().getSettingEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getHttpEventListener().removeEvent(this);
		BicycleService.getInstance().getSettingEventListener().removeEvent(this);
	}
	
	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}
	
	private void loadAllBicyclesInfoFromServer(){
		if(Utils.getNetworkInfo() == Constants.NetworkInfo.DISCONNECT){
			Toast.makeText(this, R.string.toast_msg_network_error, Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getText(R.string.list_progress_dialog_msg));
		mProgressDialog.show();
		BicycleService.getInstance().getHttpService().getAllBicyclesInfo();
	}

	public void onAllBicyclesInfoReceived(int resultCode) {
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
		}
		if(resultCode == Constants.ResultCode.SUCCESS){									
			mAdapter.updateDataset();
		}else {
			Toast.makeText(this, R.string.toast_msg_server_unavailable, Toast.LENGTH_SHORT).show();
		}
	}

	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {		
		
	}

	/**
	 * 
	 */
	public void onCitySettingChanged(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mAdapter.updateDataset();
		}
	}

	@Override
	protected void onResume() {		
		super.onResume();
	}

	@Override
	protected void onPause() {		
		super.onPause();
	}
	
}
