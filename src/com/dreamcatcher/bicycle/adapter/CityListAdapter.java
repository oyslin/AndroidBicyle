package com.dreamcatcher.bicycle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.Utils;

public class CityListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private int[] cityNameResIdArray = null;
	private ViewHolder mSelectedViewHolder = null;
	private ICityListEvent mCityListEvent;
	private int mDefaultSelection = -1;
	
	public CityListAdapter(ICityListEvent cityListEvent, int defaultSelectiono){
		cityNameResIdArray = Constants.CitySetting.CITY_NAME_RESID;
		mLayoutInflater = LayoutInflater.from(BicycleApp.getInstance());
		mCityListEvent = cityListEvent;
		mDefaultSelection = defaultSelectiono;
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
		ViewHolder holder = null;
		if(convertView == null){			
			convertView = mLayoutInflater.inflate(R.layout.select_city_item, parent, false);			
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.cityTextView.setText(Utils.getText(cityNameResIdArray[position]));
		if(position == mDefaultSelection){
			holder.selectImageView.setSelected(true);
			mSelectedViewHolder = holder;
			
		}else {
			holder.selectImageView.setSelected(false);
		}		
		
		final int index = position;		
		convertView.setOnClickListener(new OnClickListener() {					
			public void onClick(View v) {
				CityListAdapter.this.onItemClicked(v, index);
			}
		});
	
		return convertView;
	}
	
	private void onItemClicked(View view, int index){
		if(mSelectedViewHolder != null){			
			mSelectedViewHolder.selectImageView.setSelected(false);
		}
		mSelectedViewHolder = (ViewHolder)view.getTag();
		mSelectedViewHolder.selectImageView.setSelected(true);
		
		mCityListEvent.onCityItemClicked(index);
	}
	
	public interface ICityListEvent{
		void onCityItemClicked(int index);
	}
	
	private static class ViewHolder{
		public TextView cityTextView;
		public ImageView selectImageView;
		
		public ViewHolder(View parent){
			cityTextView = (TextView) parent.findViewById(R.id.select_city_item_name);
			selectImageView = (ImageView) parent.findViewById(R.id.select_city_item_check);
		}
	}
}
