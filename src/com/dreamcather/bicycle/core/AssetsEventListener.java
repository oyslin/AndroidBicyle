package com.dreamcather.bicycle.core;

import java.util.Vector;

import com.dreamcather.bicycle.interfaces.IAssetsEvent;

public class AssetsEventListener implements IAssetsEvent{
	private Vector<IAssetsEvent> mEvents = null;
	
	public AssetsEventListener(){
		mEvents = new Vector<IAssetsEvent>();
	}

	public void onCitySettingLoaded( int resultCode) {
		for(IAssetsEvent event : mEvents){
			event.onCitySettingLoaded(resultCode);
		}
		
	}

	public void onBicyclesInfoLoaded(int resultCode) {
		for(IAssetsEvent event : mEvents){
			event.onBicyclesInfoLoaded(resultCode);
		}		
	}
	
	public void addEvent(IAssetsEvent event){
		if(!mEvents.contains(event)){
			mEvents.add(event);
		}
	}
	
	public void removeEvent(IAssetsEvent event){
		if(mEvents.contains(event)){
			mEvents.remove(event);
		}
	}
}
