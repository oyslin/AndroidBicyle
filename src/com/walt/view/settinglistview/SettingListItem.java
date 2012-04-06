package com.walt.view.settinglistview;

import android.view.View;

public class SettingListItem{	
	private View mView = null;
	private ISettingItemClickEvent mClickEvent = null;
	
	public SettingListItem(View view, ISettingItemClickEvent clickEvent){
		this.mView = view;
		this.mClickEvent = clickEvent;
	}
	
	public View getView() {
		return mView;
	}

	public void setView(View view) {
		this.mView = view;
	}
	public ISettingItemClickEvent getClickEvent() {
		return mClickEvent;
	}
	public void setClickEvent(ISettingItemClickEvent clickEvent) {
		this.mClickEvent = clickEvent;
	}
}
