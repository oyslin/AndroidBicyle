package com.dreamcatcher.bicycle.core;

import java.util.Vector;

import com.dreamcatcher.bicycle.interfaces.IAdEvent;

public class AdEventListener implements IAdEvent {
	private Vector<IAdEvent> mEvents = null;
	
	public AdEventListener(){
		mEvents = new Vector<IAdEvent>();
	}
	
	
	@Override
	public void onPointsUpdated(String currencyName, int totalPoint) {
		for(IAdEvent event : mEvents){
			event.onPointsUpdated(currencyName, totalPoint);
		}
	}

	@Override
	public void onPointsUpdateFailed(String error) {
		for(IAdEvent event : mEvents){
			event.onPointsUpdateFailed(error);
		}
	}
	
	public void addEvent(IAdEvent event){
		if(!mEvents.contains(event)){
			mEvents.add(event);
		}
	}
	
	public void removeEvent(IAdEvent event){
		if(mEvents.contains(event)){
			mEvents.remove(event);
		}
	}

}
