package com.walt.activity;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walt.BicycleApp;
import com.walt.R;
import com.walt.dataset.BicycleDataset;
import com.walt.util.Utils;
import com.walt.vo.BicycleStationInfo;

public class BicycleListAdapter extends BaseAdapter {
	private ArrayList<BicycleStationInfo> mBicycleStationInfos = null;
	private BicycleDataset mBicycleDataset = null;
	private LayoutInflater mInflater;
	
	public BicycleListAdapter(){
		mBicycleDataset = BicycleDataset.getInstance();
		mBicycleStationInfos = mBicycleDataset.getBicyleStationInfos();
		mInflater = LayoutInflater.from(BicycleApp.getInstance());
	}
	
	public void updateDataset(){
		mBicycleStationInfos = mBicycleDataset.getBicyleStationInfos();
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return mBicycleStationInfos.size();
	}

	public Object getItem(int position) {		
		return mBicycleStationInfos.get(position);
	}

	public long getItemId(int position) {		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.bicycle_listview, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		BicycleStationInfo bicycleStationInfo = mBicycleStationInfos.get(position);
		if(bicycleStationInfo != null){
			holder.bicycleIndex.setText(String.valueOf(bicycleStationInfo.getId()));
			holder.bicycleName.setText(bicycleStationInfo.getName());
			String avaibike = Utils.getText(R.string.list_avaibike);
			String avaipark = Utils.getText(R.string.list_avaipark);
			holder.availableBicycles.setText(avaibike + bicycleStationInfo.getAvailable());
			holder.availableParks.setText(avaipark + String.valueOf(bicycleStationInfo.getCapacity() - bicycleStationInfo.getAvailable()));
			holder.address.setText(bicycleStationInfo.getAddress());
		}			
		
		return convertView;
	}
	
	private static class ViewHolder{
		public TextView bicycleIndex;
		public TextView bicycleName;
		public TextView availableBicycles;
		public TextView availableParks;
		public TextView address;
		
		
		public ViewHolder(View parent){
			bicycleIndex = (TextView) parent.findViewById(R.id.bicycle_listview_index);
			bicycleName = (TextView) parent.findViewById(R.id.bicycle_listview_name);
			availableBicycles = (TextView) parent.findViewById(R.id.bicycle_listview_avaibike);
			availableParks = (TextView) parent.findViewById(R.id.bicycle_listview_avaipark);
			address = (TextView) parent.findViewById(R.id.bicycle_listview_address);			
		}
	}

}
