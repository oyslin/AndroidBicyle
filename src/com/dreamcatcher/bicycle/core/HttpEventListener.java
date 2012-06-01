package com.dreamcatcher.bicycle.core;

import java.util.Vector;

import com.dreamcatcher.bicycle.interfaces.IHttpEvent;
import com.dreamcatcher.bicycle.vo.BicycleNumberInfo;

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

	public void onSingleBicycleNumberInfoReceived(
			BicycleNumberInfo bicycleNumberInfo, int resultCode) {
		for(IHttpEvent event : mEvents){
			event.onSingleBicycleNumberInfoReceived(bicycleNumberInfo, resultCode);
		}
	}
	
	public void onNewVersionCheckCompleted(boolean needUpdate, int resultCode) {
		for(IHttpEvent event : mEvents){
			event.onNewVersionCheckCompleted(needUpdate, resultCode);
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
