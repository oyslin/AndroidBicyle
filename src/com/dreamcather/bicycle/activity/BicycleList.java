package com.dreamcather.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.HttpUtils;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;

public class BicycleList extends Activity {
	private ActivityTitle mActivityTitle = null;
	private BicycleListAdapter mAdapter = null;
	private ListView mListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_list);
		init();
	}
	
	private void init(){
		mActivityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		mActivityTitle.setActivityTitle(getText(R.string.title_list));
		IActivityTitleRightImageClickEvent rightImageClickEvent = new IActivityTitleRightImageClickEvent() {			
			public void onRightImageClicked() {
				HttpUtils.getAllBicyclesInfoFromServer();
				mAdapter.updateDataset();
			}
		};
		
		mActivityTitle.setRightImage(R.drawable.ic_titlebar_refresh, rightImageClickEvent);
		
		mListView = (ListView) findViewById(R.id.bicycle_listview);		
		mAdapter = new BicycleListAdapter();
		mListView.setAdapter(mAdapter);
	}
}
