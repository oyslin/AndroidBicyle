package com.dreamcatcher.bicycle.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.dataset.BicycleDataset;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;

public class BicycleListAdapter extends BaseAdapter {
	private ArrayList<BicycleStationInfo> mBicycleStationInfos = null;
	private BicycleDataset mBicycleDataset = null;
	private LayoutInflater mInflater;
	private CitySetting mCitySetting = null;
	
	public BicycleListAdapter(){
		mBicycleDataset = BicycleDataset.getInstance();
		mBicycleStationInfos = mBicycleDataset.getBicycleStationInfos();
		mInflater = LayoutInflater.from(BicycleApp.getInstance());
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
	}
	
	public void updateDataset(){
		mBicycleStationInfos = mBicycleDataset.getBicycleStationInfos();
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
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
			convertView = mInflater.inflate(R.layout.bicycle_listitem, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		BicycleStationInfo bicycleStationInfo = mBicycleStationInfos.get(position);
		if(bicycleStationInfo != null){
			holder.bicycleIndex.setText(String.valueOf(position + 1));
			holder.bicycleName.setText(bicycleStationInfo.getName());
			
			if(mCitySetting.isShowBicycleNumber()){
				String avaibike = Utils.getText(R.string.list_avaibike);
				String avaipark = Utils.getText(R.string.list_avaipark);
				holder.availableBicycles.setText(avaibike + bicycleStationInfo.getAvailable());
				holder.availableParks.setText(avaipark + String.valueOf(bicycleStationInfo.getCapacity() - bicycleStationInfo.getAvailable()));
				holder.bicycleNumberLine.setVisibility(View.VISIBLE);
			}else{
				holder.bicycleNumberLine.setVisibility(View.GONE);
			}			
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
		public LinearLayout bicycleNumberLine;
		
		
		public ViewHolder(View parent){
			bicycleIndex = (TextView) parent.findViewById(R.id.bicycle_listview_index);
			bicycleName = (TextView) parent.findViewById(R.id.bicycle_listview_name);
			availableBicycles = (TextView) parent.findViewById(R.id.bicycle_listview_avaibike);
			availableParks = (TextView) parent.findViewById(R.id.bicycle_listview_avaipark);
			address = (TextView) parent.findViewById(R.id.bicycle_listview_address);
			bicycleNumberLine = (LinearLayout) parent.findViewById(R.id.bicycle_listview_count_line);
		}
	}

}
