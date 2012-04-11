package com.dreamcather.bicycle.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

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
import com.dreamcather.bicycle.BicycleApp;
import com.dreamcather.bicycle.R;
import com.dreamcather.bicycle.core.BicycleService;
import com.dreamcather.bicycle.dataset.BicycleDataset;
import com.dreamcather.bicycle.interfaces.IHttpEvent;
import com.dreamcather.bicycle.interfaces.IHttpService;
import com.dreamcather.bicycle.interfaces.ISettingEvent;
import com.dreamcather.bicycle.util.Constants;
import com.dreamcather.bicycle.util.GlobalSetting;
import com.dreamcather.bicycle.view.ActivityTitle;
import com.dreamcather.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.dreamcather.bicycle.vo.BicycleStationInfo;
import com.dreamcather.bicycle.vo.CitySetting;

public class BicycleMap extends MapActivity implements IHttpEvent, ISettingEvent{
	private BMapManager mBMapManager = null;
	private MKLocationManager mLocationManager = null;
	private LocationListener mLocationListener = null;
	private MapView mMapView = null;
	private View mPopView = null;
	private MapController mMapController = null;
	private BicycleDataset mDataset = null;
	private final static int RAT = 1000000;
	private TextView mMapPopName = null;
	private TextView mMapPopAvailBicycles = null;
	private TextView mMapPopAvailParks = null;
	private TextView mMapPopAddress = null;
	private Drawable mMarker = null;
	private int mMarkerWidth = 0;
	private int mMarkerHeight = 0;
	private MyLocationOverlay mMyLocationOverlay = null;
	private ActivityTitle mActivityTitle = null;
	private boolean mMyLocationAdded = false;
	private boolean mMyLocationEnabled = false;
	private CitySetting mCitySetting = null;
	private Handler mHandler = null;
	private final static int BICYCLE_INFO_LOAD_SUCCESS = 0;
	private final static int BICYCLE_INFO_LOAD_FAILED = 1;
	private final static int CITY_SETTING_RELOAD_SUCCESS = 3;
	private IHttpService mHttpService = null;
	private OverlayItem mSelectedOverlayItem = null;
	private boolean mHttpCallReturned = true;
	private BicycleStationInfo mReturnedBicycleInfo = null;
	private int mSelectedId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_map);
		
		init();
	}
	
	private void init(){
		this.addEvent();
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case BICYCLE_INFO_LOAD_SUCCESS:
						mHttpCallReturned = true;
						
						//update pop view
						showPopContent(mReturnedBicycleInfo);
						mDataset.updateBicycleInfo(mSelectedId, mReturnedBicycleInfo);
						break;
					case BICYCLE_INFO_LOAD_FAILED:
						mHttpCallReturned = false;
						break;
					case CITY_SETTING_RELOAD_SUCCESS:
						reLoadUI();
						break;	
					default:
						break;
				}				
			}			
		};
		
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
		mHttpService = BicycleService.getInstance().getHttpService();
		
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
		
		GeoPoint centerPoint = new GeoPoint(
				(int) ((mCitySetting.getDefaultLatitude() + mCitySetting.getOffsetLatitude()) * RAT),
				(int) ((mCitySetting.getDefaultLongitude() + mCitySetting.getOffsetLongitude()) * RAT));

		mMapController.setCenter(centerPoint);
		mMapController.setZoom(15);
		
		mPopView = getLayoutInflater().inflate(R.layout.map_pop, null);
		mMapPopName = (TextView) mPopView.findViewById(R.id.map_pop_name);
		mMapPopAvailBicycles = (TextView) mPopView.findViewById(R.id.map_pop_available_bicycles);
		mMapPopAvailParks = (TextView) mPopView.findViewById(R.id.map_pop_available_parks);
		mMapPopAddress = (TextView) mPopView.findViewById(R.id.map_pop_address);		
		
		mMapView.addView(mPopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM));
		mPopView.setVisibility(View.GONE);
		

        List<Overlay> overlayList = mMapView.getOverlays();
        
        addAddBicycleMarks(overlayList);        

		IActivityTitleRightImageClickEvent rightImageClickEvent = new IActivityTitleRightImageClickEvent() {			
			public void onRightImageClicked() {
				BicycleMap.this.onRightImageClicked();		
			}
		};
		mActivityTitle.setRightImage(R.drawable.ic_titlebar_locate, rightImageClickEvent);

	}	
	
	private void addEvent(){
		BicycleService.getInstance().getHttpEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getHttpEventListener().removeEvent(this);
	}
	
	private void onRightImageClicked(){
		if (!mMyLocationAdded) {
			addMyLocation();
		}

		if (mMyLocationEnabled) {
			mMyLocationOverlay.disableMyLocation();
			mMyLocationOverlay.disableCompass();
			mLocationManager.removeUpdates(mLocationListener);
			mMyLocationEnabled = false;
		} else {
			mMyLocationOverlay.enableCompass();
			mMyLocationOverlay.enableMyLocation();
			mLocationManager.requestLocationUpdates(mLocationListener);
			mMyLocationEnabled = true;
		}
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
	private void addAddBicycleMarks(List<Overlay> overlayList){
		mMarker = getResources().getDrawable(R.drawable.ic_marker);
        mMarkerHeight = mMarker.getIntrinsicHeight();
        mMarkerWidth = mMarker.getIntrinsicWidth();
        mMarker.setBounds(0, 0, mMarkerWidth, mMarkerHeight);
        
        ItemizedBicycleOverlay bicycleOverlays = new ItemizedBicycleOverlay(mMarker, this);
        ArrayList<BicycleStationInfo> bicycleInfos = mDataset.getBicycleStationInfos();
        
        for(int i = 0, count = bicycleInfos.size(); i < count; i++){
        	BicycleStationInfo bicycleInfo = bicycleInfos.get(i);
        	GeoPoint point = new GeoPoint((int)((mCitySetting.getOffsetLatitude() + bicycleInfo.getLatitude()) * RAT), (int)((mCitySetting.getOffsetLongitude() + bicycleInfo.getLongitude()) * RAT));
        	OverlayItem overlayItem = new OverlayItem(point, String.valueOf(bicycleInfo.getId()), bicycleInfo.getName());
        	bicycleOverlays.addOverlayItem(overlayItem);
        }
        
        overlayList.add(bicycleOverlays);
	}
	
	private void reLoadUI(){
		
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
			
			if(mHttpCallReturned){
				mHttpCallReturned = false;
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
		//do nothing		
	}

	/**
	 * on single bicycle info received
	 */
	public void onSingleBicycleInfoReceived(
			BicycleStationInfo bicycleStationInfo, int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mReturnedBicycleInfo = bicycleStationInfo;
			mHandler.sendEmptyMessage(BICYCLE_INFO_LOAD_SUCCESS);
		}else {
			mHandler.sendEmptyMessage(BICYCLE_INFO_LOAD_FAILED);
		}		
	}

	/**
	 * when city setting changed, reload UI
	 */
	public void onCitySettingChanged(int resultCode) {
		if(resultCode == Constants.ResultCode.SUCCESS){
			mHandler.sendEmptyMessage(CITY_SETTING_RELOAD_SUCCESS);
		}		
	}
}
