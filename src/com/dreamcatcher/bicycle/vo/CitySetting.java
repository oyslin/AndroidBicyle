package com.dreamcatcher.bicycle.vo;

public class CitySetting {
	private int[] mTabs = null;
	private String mAllBicyclesUrl = "";
	private String mBicycleDetailUrl = "";
	private double mDefaultLatitude = 0;
	private double mDefaultLongitude = 0;
	private double mOffsetLatitude = 0;
	private double mOffsetLongitude = 0;
	private String mAssetsFileName = "";
	private boolean mShowBicycleNumber = true;
	private boolean mRefreshSingle = true;
	private boolean mNeedDecode = false;
	
	public CitySetting(String tabStr, String allBicyclesUrl,
			String bicycleDetailUrl, double defaultLatitude,
			double defaultLongitude, double offsetLatitude,
			double offsetLongitude, String assetsFileName, 
			boolean showBicycleNumber, boolean refreshSingle,
			boolean needDecode) {		
		this.mTabs = convertToArray(tabStr);
		this.mAllBicyclesUrl = allBicyclesUrl;
		this.mBicycleDetailUrl = bicycleDetailUrl;
		this.mDefaultLatitude = defaultLatitude;
		this.mDefaultLongitude = defaultLongitude;
		this.mOffsetLatitude = offsetLatitude;
		this.mOffsetLongitude = offsetLongitude;
		this.mAssetsFileName = assetsFileName;
		this.mShowBicycleNumber = showBicycleNumber;
		this.mRefreshSingle = refreshSingle;
		this.mNeedDecode = needDecode;
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
	
		
	public boolean isNeedDecode() {
		return mNeedDecode;
	}

	public void setNeedDecode(boolean needDecode) {
		this.mNeedDecode = needDecode;
	}

	public boolean isShowBicycleNumber() {
		return mShowBicycleNumber;
	}

	public void setShowBicycleNumber(boolean showBicycleNumber) {
		this.mShowBicycleNumber = showBicycleNumber;
	}

	public boolean isRefreshSingle() {
		return mRefreshSingle;
	}

	public void setRefreshSingle(boolean refreshSingle) {
		this.mRefreshSingle = refreshSingle;
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
