package com.dreamcather.bicycle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcather.bicycle.BicycleApp;
import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;

public class CityListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private int[] cityNameResIdArray = null;
	private View selectedView = null;
	private ICityListEvent mCityListEvent;
	
	public CityListAdapter(ICityListEvent cityListEvent){
		cityNameResIdArray = Constants.CitySetting.CITY_NAME_RESID;
		mLayoutInflater = LayoutInflater.from(BicycleApp.getInstance());
		mCityListEvent = cityListEvent;
	}		
	
	public int getCount() {
		return cityNameResIdArray.length;
	}

	public String getItem(int position) {
		return Utils.getText(cityNameResIdArray[position]).toString();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			final int index = position;
			convertView = mLayoutInflater.inflate(R.layout.select_city_item, parent, false);
			convertView.setOnClickListener(new OnClickListener() {					
				public void onClick(View v) {
					CityListAdapter.this.onItemClicked(v, index);
				}
			});
		}
		TextView cityTextView = (TextView) convertView.findViewById(R.id.select_city_item_name);
		cityTextView.setText(Utils.getText(cityNameResIdArray[position]));			
		
		return convertView;
	}
	
	private void onItemClicked(View view, int index){
		if(selectedView != null){
			ImageView selectImageView = (ImageView) selectedView.findViewById(R.id.select_city_item_check);
			selectImageView.setSelected(false);
		}
		selectedView = view;
		ImageView selectImageView = (ImageView) selectedView.findViewById(R.id.select_city_item_check);
		selectImageView.setSelected(true);
		mCityListEvent.onCityItemClicked(index);
	}
	
	public interface ICityListEvent{
		void onCityItemClicked(int index);
	}

}
