package com.dreamcatcher.bicycle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.adapter.FavoriteSettingAdapter;
import com.dreamcatcher.bicycle.view.ActivityTitle;

public class FavoriteSettingActivity extends Activity {
	private ListView mListView = null;
	private FavoriteSettingAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_setting);
		init();
	}
	
	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_favorite_setting);
		
		mListView = (ListView) findViewById(R.id.favorite_setting_listview);
		mAdapter = new FavoriteSettingAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onFavoriteItemClick(position);				
			}			
		});
	}
	
	private void onFavoriteItemClick(int position){
		mAdapter.setSingleSelected(position);
	}
}
