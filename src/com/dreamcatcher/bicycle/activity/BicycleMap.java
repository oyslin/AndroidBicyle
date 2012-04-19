package com.dreamcatcher.bicycle.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.dataset.BicycleDataset;
import com.dreamcatcher.bicycle.interfaces.IHttpEvent;
import com.dreamcatcher.bicycle.interfaces.IHttpService;
import com.dreamcatcher.bicycle.interfaces.ISettingEvent;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;
import com.dreamcatcher.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;

public class BicycleMap extends MapActivity implements IHttpEvent, ISettingEvent{
	private BMapManager mBMapManager = null;
	private MKLocationManager mLocationManager = null;
	private LocationListener mLocationListener = null;
	private MapView mMapView = null;
	private List<Overlay> mMapOverLayList = null;
	private View mPopView = null;
	private MapController mMapController = null;
	private BicycleDataset mDataset = null;
	private final static int RAT = 1000000;
	private TextView mMapPopName = null;
	private TextView mMapPopAvailBicycles = null;
	private TextView mMapPopAvailParks = null;
	private TextView mMapPopAddress = null;
	private Drawable mMarker = null;
	private ItemizedBicycleOverlay mMarkersOverlay = null;
	private int mMarkerWidth = 0;
	private int mMarkerHeight = 0;
	private Drawable mFavoriteMarker = null;
	private ItemizedBicycleOverlay mFavoriteMarkersOverlay = null;
	private MyLocationOverlay mMyLocationOverlay = null;
	private ActivityTitle mActivityTitle = null;
	private boolean mMyLocationAdded = false;
	private boolean mMyLocationEnabled = false;
	private CitySetting mCitySetting = null;
	private IHttpService mHttpService = null;
	private OverlayItem mSelectedOverlayItem = null;
	private long mCurrentTime = 0;
	private int mSelectedId = -1;
	private boolean mAutoLocate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_map);
		
		init();
	}
	
	private void init(){
		this.addEvent();
		
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
		mHttpService = BicycleService.getInstance().getHttpService();
		mHttpService.getAllBicyclesInfo(true);
		
		mBMapManager = BicycleApp.getInstance().getMapManager();
		if(mBMapManager == null){
			BicycleApp.getInstance().initBaiduMap();
		}
		mBMapManager.start();
		super.initMapActivity(mBMapManager);
		
		mDataset = BicycleDataset.getInstance();
		
		mActivityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		mActivityTitle.setActivityTitle(getText(R.string.title_map));
		
		mMapView = (MapView) findViewById(R.id.bicycle_mapview);			
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		mMapView.setBuiltInZoomControls(true);
		
		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		
		setMapSenter();		
		
		mPopView = getLayoutInflater().inflate(R.layout.map_pop, null);
		mMapPopName = (TextView) mPopView.findViewById(R.id.map_pop_name);
		mMapPopAvailBicycles = (TextView) mPopView.findViewById(R.id.map_pop_available_bicycles);
		mMapPopAvailParks = (TextView) mPopView.findViewById(R.id.map_pop_available_parks);
		mMapPopAddress = (TextView) mPopView.findViewById(R.id.map_pop_address);		
		
		mMapView.addView(mPopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM));
		mPopView.setVisibility(View.GONE);
		

		mMapOverLayList = mMapView.getOverlays();
        
		//init markers
		mMarker = getResources().getDrawable(R.drawable.ic_marker);
        mMarkerHeight = mMarker.getIntrinsicHeight();
        mMarkerWidth = mMarker.getIntrinsicWidth();
        mMarker.setBounds(0, 0, mMarkerWidth, mMarkerHeight);
        
        mMarkersOverlay = new ItemizedBicycleOverlay(mMarker, this);
		
        mFavoriteMarker = getResources().getDrawable(R.drawable.ic_marker_favorite);
        mFavoriteMarker.setBounds(0, 0, mMarkerWidth, mMarkerHeight);
        
        mFavoriteMarkersOverlay = new ItemizedBicycleOverlay(mFavoriteMarker, this);
        
        //add all bicycle marks
        addAllBicycleMarkers();        

		IActivityTitleRightImageClickEvent rightImageClickEvent = new IActivityTitleRightImageClickEvent() {			
			public void onRightImageClicked() {
				BicycleMap.this.onRightImageClicked();		
			}
		};
		mActivityTitle.setRightImage(R.drawable.ic_titlebar_locate, rightImageClickEvent);
		mAutoLocate = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.AUTO_LOCATE_ON_STARTUP, false);
		
		//add my location if auto locate set
		if(mAutoLocate){
			addMyLocation();
			enalbeMyLocation();
		}
	}
	
	private void setMapSenter(){
		GeoPoint centerPoint = new GeoPoint(
				(int) ((mCitySetting.getDefaultLatitude() + mCitySetting.getOffsetLatitude()) * RAT),
				(int) ((mCitySetting.getDefaultLongitude() + mCitySetting.getOffsetLongitude()) * RAT));

		mMapController.setCenter(centerPoint);
	}
	
	private void addEvent(){
		BicycleService.getInstance().getHttpEventListener().addEvent(this);
		BicycleService.getInstance().getSettingEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getHttpEventListener().removeEvent(this);
		BicycleService.getInstance().getSettingEventListener().removeEvent(this);
	}
	
	private void onRightImageClicked(){
		if (!mMyLocationAdded) {
			addMyLocation();
		}

		if (mMyLocationEnabled) {			
			disableMyLocation();
		} else {
			enalbeMyLocation();
		};
	}
	
	private void enalbeMyLocation(){
		mMyLocationOverlay.enableCompass();
		mMyLocationOverlay.enableMyLocation();
		//Baidu Map bug, need to stop first
		mBMapManager.stop();
		mLocationManager.requestLocationUpdates(mLocationListener);
		mBMapManager.start();
		mMyLocationEnabled = true;
	}
	
	private void disableMyLocation(){
		mMyLocationOverlay.disableMyLocation();
		mMyLocationOverlay.disableCompass();
		mLocationManager.removeUpdates(mLocationListener);			
		mMyLocationEnabled = false;	
	}
	
	private void addMyLocation(){
		//Add my location
        mLocationManager = mBMapManager.getLocationManager();
        
        mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
        mLocationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
        
        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
        
        mLocationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mMapController.animateTo(new GeoPoint((int)(location.getLatitude() * RAT), (int)(location.getLongitude() * RAT)));
			}
		};
		
        mMapView.getOverlays().add(mMyLocationOverlay);
        mMyLocationAdded = true;
	}
	
	
	/**
	 * add all the bicycle marks in map
	 * @param overlayList
	 */
	private void addAllBicycleMarkers(){		
        ArrayList<BicycleStationInfo> bicycleInfos = mDataset.getBicycleStationInfos();
        String[] favoriteIds = getFavoriteIds();
        
        for(int i = 0, count = bicycleInfos.size(); i < count; i++){
        	BicycleStationInfo bicycleInfo = bicycleInfos.get(i);
        	GeoPoint point = new GeoPoint((int)((mCitySetting.getOffsetLatitude() + bicycleInfo.getLatitude()) * RAT), (int)((mCitySetting.getOffsetLongitude() + bicycleInfo.getLongitude()) * RAT));
        	OverlayItem overlayItem = new OverlayItem(point, String.valueOf(bicycleInfo.getId()), bicycleInfo.getName());
        	int bicycleId = bicycleInfo.getId();
        	if(favoriteIds!= null && isInArray(bicycleId, favoriteIds)){
        		mFavoriteMarkersOverlay.addOverlayItem(overlayItem);
        	}else {
        		mMarkersOverlay.addOverlayItem(overlayItem);
			}
        }
        if(mMarkersOverlay.size() > 0){
        	mMapOverLayList.add(mMarkersOverlay);
        }
        
        if(mFavoriteMarkersOverlay.size() > 0){        	
        	mMapOverLayList.add(mFavoriteMarkersOverlay);
        }        
	}
	
	private String[] getFavoriteIds(){
		String favoriteIds = Utils.getStringDataFromLocal(Constants.LocalStoreTag.FAVORITE_IDS);
		if(favoriteIds == null || favoriteIds.equals("")){
			return null;
		}
		String[] favoriteIdArray = favoriteIds.split("\\|");
		return favoriteIdArray;
		
	}
	
	private boolean isInArray(int index, String[] idArray){
		boolean result = false;		
		for(String id : idArray){
			if(Integer.parseInt(id) == index){
				result = true;
				break;
			}
		}
		return result;
	}
	
	private void removeAllBicyleMarkers(){
		if(mMarkersOverlay.size() > 0){
        	mMapOverLayList.remove(mMarkersOverlay);
        }
        
        if(mFavoriteMarkersOverlay.size() > 0){        	
        	mMapOverLayList.remove(mFavoriteMarkersOverlay);
        }
	}
		
	private void reLoadUI(){
		//set map center
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
		mPopView.setVisibility(View.GONE);
		
		mMapOverLayList.clear();		
		setMapSenter();
		
		//add all overlay
		addAllBicycleMarkers();
		mMapView.invalidate();
	}	
	
	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
	}

	@Override
	protected void onDestroy() {
		if(mBMapManager != null){
			if(mLocationManager != null){
				mLocationManager.removeUpdates(mLocationListener);
			}			
		}
		
		this.removeEvent();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mBMapManager != null) {
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(mLocationListener);
			}
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mBMapManager != null) {
			if (mLocationManager != null && mMyLocationEnabled) {
				mLocationManager.requestLocationUpdates(mLocationListener);
			}
			mBMapManager.start();
		}
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
	
	private class ItemizedBicycleOverlay extends ItemizedOverlay<OverlayItem>{
		private ArrayList<OverlayItem> mOverlayItems = new ArrayList<OverlayItem>();
		
		public ItemizedBicycleOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}
		
		public ItemizedBicycleOverlay(Drawable marker, Context context){
			super(boundCenterBottom(marker));
		}

		@Override
		protected OverlayItem createItem(int i) {			
			return mOverlayItems.get(i);
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem overlayItem = mOverlayItems.get(index);
			mSelectedOverlayItem = overlayItem;
			setFocus(overlayItem);
			
			int bicycleId = Integer.parseInt(overlayItem.getTitle());
			
			if(System.currentTimeMillis() - mCurrentTime > 2000){
				mCurrentTime = System.currentTimeMillis();
				mSelectedId = bicycleId;
				mHttpService.getSingleBicycleInfo(bicycleId);				
			}
			return true;
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			mPopView.setVisibility(View.GONE);			
			return super.onTap(point, mapView);
		}

		@Override
		public int size() {
			return mOverlayItems.size();
		}
		
		public void addOverlayItem(OverlayItem overlayItem){
			mOverlayItems.add(overlayItem);
			this.populate();
		}
	}
	
	private void showPopContent(BicycleStationInfo bicycleStationInfo){
		if(bicycleStationInfo == null){
			return;
		}
		mMapPopName.setText(bicycleStationInfo.getName());
		mMapPopAvailBicycles.setText(String.valueOf(bicycleStationInfo.getAvailable()));
		mMapPopAvailParks.setText(String.valueOf(bicycleStationInfo.getCapacity() - bicycleStationInfo.getAvailable()));			
		mMapPopAddress.setText(bicycleStationInfo.getAddress());
		
		GeoPoint geoPoint = this.mSelectedOverlayItem.getPoint();
		Point point = mMapView.getProjection().toPixels(geoPoint, null);
		int posX = point.x - mMarkerWidth,
			poxY = point.y - mMarkerHeight;
		GeoPoint toShowPoint = mMapView.getProjection().fromPixels(posX, poxY);
		
		mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				toShowPoint, MapView.LayoutParams.BOTTOM));
		mPopView.setVisibility(View.VISIBLE);			
	}

	/**
	 * on All bicycles info received
	 */
	public void onAllBicyclesInfoReceived(int resultCode) {
		if(resultCode == Constants.ResultCode.NETWORK_DISCONNECT){
			Toast.makeText(this, R.string.toast_msg_network_error, Toast.LENGTH_SHORT);
		}
	}

	/**
	 * on single bicycle info received
	 */
	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			showPopContent(bicycleStationInfo);
			mDataset.updateBicycleInfo(mSelectedId, bicycleStationInfo);
		}else if(resultCode == Constants.ResultCode.NETWORK_DISCONNECT){
			Toast.makeText(this, R.string.toast_msg_network_error, Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, R.string.toast_msg_server_unavailable, Toast.LENGTH_SHORT).show();			
		}
	}
	
	public void onNewVersionCheckCompleted(boolean needUpdate, int resultCode) {
		
	}

	/**
	 * when city setting changed, reload UI
	 */
	public void onCitySettingChanged(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			reLoadUI();
		}
	}
	
	public void onFavoriteIdsChanged() {		
		removeAllBicyleMarkers();
		addAllBicycleMarkers();
		mMapView.invalidate();
	}
}
