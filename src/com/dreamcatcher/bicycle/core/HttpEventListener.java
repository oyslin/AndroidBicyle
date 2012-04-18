package com.dreamcatcher.bicycle.core;

import java.util.Vector;

import com.dreamcatcher.bicycle.interfaces.IHttpEvent;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;

public class HttpEventListener implements IHttpEvent {
	private Vector<IHttpEvent> mEvents = null;
	
	public HttpEventListener(){
		mEvents = new Vector<IHttpEvent>();
	}

	public void onAllBicyclesInfoReceived(int resultCode) {
		for(IHttpEvent event : mEvents){
			event.onAllBicyclesInfoReceived(resultCode);
		}
	}

	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {
		for(IHttpEvent event : mEvents){
			event.onSingleBicycleInfoReceived(bicycleStationInfo, resultCode);
		}
	}
	
	public void addEvent(IHttpEvent event){
		if(!mEvents.contains(event)){
			mEvents.add(event);
		}
	}
	
	public void removeEvent(IHttpEvent event){
		if(mEvents.contains(event)){
			mEvents.remove(event);
		}
	}

}
