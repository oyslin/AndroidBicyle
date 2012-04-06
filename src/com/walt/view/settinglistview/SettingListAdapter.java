package com.walt.view.settinglistview;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		return mArrayList.get(position);
	}

}
