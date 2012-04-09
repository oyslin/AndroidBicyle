package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.adapter.BicycleListAdapter;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.interfaces.IHttpEvent;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.dreamcather.bicycle.vo.BicycleStationInfo;

public class BicycleList extends Activity implements IHttpEvent{
	private ActivityTitle mActivityTitle = null;
	private BicycleListAdapter mAdapter = null;
	private ListView mListView = null;
	private Handler mHandler = null;
	private final static int BICYCLES_INFO_LOAD_SUCCESS = 0;
	private final static int BICYCLES_INFO_LOAD_FAILED = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_list);
		init();
	}
	
	private void init(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case BICYCLES_INFO_LOAD_SUCCESS:
					mAdapter.updateDataset();
					break;
				case BICYCLES_INFO_LOAD_FAILED:
					//TODO show error dialog
					break;
				default:
					break;
				}
			}			
		};
		
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
	
	private void loadAllBicyclesInfoFromServer(){
		BicycleService.getInstance().getHttpService().getAllBicyclesInfo();
	}

	public void onAllBicyclesInfoReceived(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mHandler.sendEmptyMessage(BICYCLES_INFO_LOAD_SUCCESS);
		}else {
			mHandler.sendEmptyMessage(BICYCLES_INFO_LOAD_FAILED);
		}		
	}

	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {		
		
	}
}
