package com.walt.view.settinglistview;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.walt.R;

public class SettingListAdapter extends BaseAdapter {
	ArrayList<View> mArrayList = null;
	
	public SettingListAdapter(ArrayList<View> arrayList){
		mArrayList = arrayList;
	}

	public int getCount() {
		return mArrayList.size();
	}

	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(convertView == null){
			view = mArrayList.get(position);
			if(mArrayList.size() > 1){
				if(position == 0){
					view.setBackgroundResource(R.drawable.setting_listitem_bg_top);
				}else if(position < mArrayList.size() -1){
					view.setBackgroundResource(R.drawable.setting_listitem_bg_middle);
				}else {
					view.setBackgroundResource(R.drawable.setting_listitem_bg_bottom);
				}
			}else {
				view.setBackgroundResource(R.drawable.setting_listitem_bg_whole);
			}
		}else {
			view = convertView;
		}		
		return view;
	}

}
