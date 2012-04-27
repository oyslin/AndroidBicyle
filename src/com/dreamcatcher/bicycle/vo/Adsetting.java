package com.dreamcatcher.bicycle.vo;

public class Adsetting {
	private boolean mShowAd = true;
	private int mPointTotal = 0;
	private long mNextShowAdTime = 0;
	
	public Adsetting(){
		
	}
	
	public Adsetting(boolean mShowAd, int mPointTotal, int nextShowAdTime) {		
		this.mShowAd = mShowAd;
		this.mPointTotal = mPointTotal;
		this.mNextShowAdTime = nextShowAdTime;
	}
	public boolean isShowAd() {
		return mShowAd;
	}
	public void setShowAd(boolean showAd) {
		this.mShowAd = showAd;
	}
	public int getPointTotal() {
		return mPointTotal;
	}
	public void setPointTotal(int pointTotal) {
		this.mPointTotal = pointTotal;
	}

	public long getNextShowAdTime() {
		return mNextShowAdTime;
	}

	public void setNextShowAdTime(long nextShowAdTime) {
		this.mNextShowAdTime = nextShowAdTime;
	}
	
	
}
