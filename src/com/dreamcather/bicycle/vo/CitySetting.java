package com.dreamcather.bicycle.vo;

public class CitySetting {
	private int[] mTabs = null;
	private String mAllBicyclesUrl = "";
	private String mMapUrl = "";
	private String mBicycleDetailUrl = "";
	private double mDefaultLatitude = 0;
	private double mDefaultLongitude = 0;
	private double mOffsetLatitude = 0;
	private double mOffsetLongitude = 0;
	private String mAssetsFileName = "";
	
	public CitySetting(String tabStr, String allBicyclesUrl, String mapUrl,
			String bicycleDetailUrl, double defaultLatitude,
			double defaultLongitude, double offsetLatitude,
			double offsetLongitude, String assetsFileName) {		
		this.mTabs = convertToArray(tabStr);
		this.mAllBicyclesUrl = allBicyclesUrl;
		this.mMapUrl = mapUrl;
		this.mBicycleDetailUrl = bicycleDetailUrl;
		this.mDefaultLatitude = defaultLatitude;
		this.mDefaultLongitude = defaultLongitude;
		this.mOffsetLatitude = offsetLatitude;
		this.mOffsetLongitude = offsetLongitude;
		this.mAssetsFileName = assetsFileName;
	}
	
	private int[] convertToArray(String tabStr){
		String[] strArrayStrings = tabStr.split("\\|");
		int arrayLength = strArrayStrings.length;
		int[] result = new int[arrayLength];
		for(int i = 0; i < arrayLength; i++){
			result[i] = Integer.parseInt(strArrayStrings[i]);
		}
		return result;
	}
	
	public int[] getTabs() {
		return mTabs;
	}
	public void setTabs(int[] mTabs) {
		this.mTabs = mTabs;
	}
	public String getAllBicyclesUrl() {
		return mAllBicyclesUrl;
	}
	public void setAllBicyclesUrl(String mAllBicyclesUrl) {
		this.mAllBicyclesUrl = mAllBicyclesUrl;
	}	
	
	public String getMapUrl() {
		return mMapUrl;
	}

	public void setMapUrl(String mMapUrl) {
		this.mMapUrl = mMapUrl;
	}

	public String getBicycleDetailUrl() {
		return mBicycleDetailUrl;
	}
	public void setBicycleDetailUrl(String mBicycleDetailUrl) {
		this.mBicycleDetailUrl = mBicycleDetailUrl;
	}
	public double getDefaultLatitude() {
		return mDefaultLatitude;
	}
	public void setDefaultLatitude(double mDefaultLatitude) {
		this.mDefaultLatitude = mDefaultLatitude;
	}
	public double getDefaultLongitude() {
		return mDefaultLongitude;
	}
	public void setDefaultLongitude(double mDefaultLongitude) {
		this.mDefaultLongitude = mDefaultLongitude;
	}
	public double getOffsetLatitude() {
		return mOffsetLatitude;
	}
	public void setOffsetLatitude(double mOffsetLatitude) {
		this.mOffsetLatitude = mOffsetLatitude;
	}
	public double getOffsetLongitude() {
		return mOffsetLongitude;
	}
	public void setOffsetLongitude(double mOffsetLongitude) {
		this.mOffsetLongitude = mOffsetLongitude;
	}
	public String getAssetsFileName() {
		return mAssetsFileName;
	}
	public void setAssetsFileName(String mAssetsFileName) {
		this.mAssetsFileName = mAssetsFileName;
	}
	
	
}
