package com.dreamcatcher.bicycle.core;

import java.util.Vector;

import com.dreamcatcher.bicycle.interfaces.ISettingEvent;

public class SettingEventListener implements ISettingEvent {
	private Vector<ISettingEvent> mEvents = null;
	
	public SettingEventListener(){
		mEvents = new Vector<ISettingEvent>();
	}
	
	public void onCitySettingChanged(int resultCode) {
		for(ISettingEvent event : mEvents){
			event.onCitySettingChanged(resultCode);
		}
	}
	
	public void onFavoriteIdsChanged() {
		for(ISettingEvent event : mEvents){
			event.onFavoriteIdsChanged();
		}
		
	}
	
	public void addEvent(ISettingEvent event){
		if(!mEvents.contains(event)){
			mEvents.add(event);
		}
	}
	
	public void removeEvent(ISettingEvent event){
		if(mEvents.contains(event)){
			mEvents.remove(event);
		}
	}

}
