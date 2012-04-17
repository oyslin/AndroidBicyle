package com.dreamcather.bicycle.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcather.bicycle.BicycleApp;
import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.dataset.BicycleDataset;
import com.dreamcather.bicycle.interfaces.ISettingService;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.Utils;
import com.dreamcather.bicycle.vo.BicycleStationInfo;

public class FavoriteSettingAdapter extends BaseAdapter {
	private ArrayList<BicycleStationInfo> mBicycleStationInfos = null;
	private BicycleDataset mBicycleDataset = null;
	private LayoutInflater mInflater;
	private boolean[] mCheckResult = null;
	private int[] mAllIds = null;
	private ISettingService mSettingService = null;
	
	public FavoriteSettingAdapter(){
		mBicycleDataset = BicycleDataset.getInstance();
		mBicycleStationInfos = mBicycleDataset.getBicycleStationInfos();
		mInflater = LayoutInflater.from(BicycleApp.getInstance());
		mCheckResult = new boolean[mBicycleStationInfos.size()];
		mAllIds = new int[mBicycleStationInfos.size()];
		mSettingService = BicycleService.getInstance().getSettingService();
		initFavoriteIds();
		initSelected();
	}
	
	private void initFavoriteIds(){
		for(int i = 0, n = mBicycleStationInfos.size(); i < n; i++){
			mAllIds[i] = mBicycleStationInfos.get(i).getId();
		}
	}
	
	private void initSelected(){
		String favoriteIds = Utils.getStringDataFromLocal(Constants.LocalStoreTag.FAVORITE_IDS);
		if(favoriteIds == null || favoriteIds.equals("")){
			return;
		}
		String[] favoriteIdArray = favoriteIds.split("\\|");
		for(int i = 0, n = mAllIds.length; i < n; i++){
			if(isInArray(mAllIds[i], favoriteIdArray)){
				mCheckResult[i] = true;
			}
		}
	}
	
	private boolean isInArray(int index, String[] idArray){
		boolean result = false;
		for(String id : idArray){
			if(Integer.parseInt(id) == index){
				result = true;
				break;
			}
		}
		return result;
	}

	public int getCount() {		
		return mBicycleStationInfos.size();
	}

	public BicycleStationInfo getItem(int position) {
		
		return mBicycleStationInfos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.favorite_setting_listitem, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		BicycleStationInfo bicycleStationInfo = mBicycleStationInfos.get(position);
		if(bicycleStationInfo != null){
			viewHolder.bicycleIndex.setText(String.valueOf(position + 1));
			viewHolder.bicycleName.setText(bicycleStationInfo.getName());
			viewHolder.bicycleAddress.setText(bicycleStationInfo.getAddress());
			if(mCheckResult[position]){
				viewHolder.checkImage.setSelected(true);
			}else {
				viewHolder.checkImage.setSelected(false);
			}
		}
		return convertView;
	}
	
	public void setAllSelected(boolean selected){
		for(int i = 0, n = mCheckResult.length; i < n;i++){
			mCheckResult[i] = selected;
		}		
		this.notifyDataSetChanged();
		this.updateFavoriteIdsToLocal();
	}
	
	public void setSingleSelected(int index){
		mCheckResult[index] = !mCheckResult[index];
		this.notifyDataSetChanged();
		this.updateFavoriteIdsToLocal();
	}
	
	private void updateFavoriteIdsToLocal(){
		StringBuilder stringBuilder = new StringBuilder();
		if(mCheckResult.length > 0){
			if(mCheckResult[0]){
				stringBuilder.append(mAllIds[0]);
			}
		}
		for(int i = 1, n = mCheckResult.length; i < n; i++){
			if(mCheckResult[i]){
				stringBuilder.append(mAllIds[i]).append("|");
			}
		}
		mSettingService.changeFavoriteIds(stringBuilder.toString());
	}
	
	
	private static class ViewHolder{
		public TextView bicycleIndex;
		public TextView bicycleName;
		public TextView bicycleAddress;
		public ImageView checkImage;
		
		public ViewHolder(View parent){
			bicycleIndex = (TextView) parent.findViewById(R.id.favorite_setting_listitem_index);
			bicycleName = (TextView) parent.findViewById(R.id.favorite_setting_listitem_name);
			bicycleAddress = (TextView) parent.findViewById(R.id.favorite_setting_listitem_address);
			checkImage = (ImageView) parent.findViewById(R.id.favorite_setting_listitem_favorite_image);
		}
	}
}
