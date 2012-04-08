package com.walt.activity;

import com.walt.R;
import com.walt.view.ActivityTitle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class SelectCityActivity extends Activity {
	private ListView mListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		init();
	}

	private void init(){
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(R.string.title_select_city);
		
		mListView = (ListView) findViewById(R.id.select_city_list);
	}
	
	private static class CityListAdapter extends BaseAdapter{
		
		

		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
