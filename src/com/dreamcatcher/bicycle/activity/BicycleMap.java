package com.dreamcatcher.bicycle.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.R;
import com.dreamcatcher.bicycle.core.BicycleService;
import com.dreamcatcher.bicycle.dataset.BicycleDataset;
import com.dreamcatcher.bicycle.interfaces.IAdEvent;
import com.dreamcatcher.bicycle.interfaces.IHttpEvent;
import com.dreamcatcher.bicycle.interfaces.IHttpService;
import com.dreamcatcher.bicycle.interfaces.ISettingEvent;
import com.dreamcatcher.bicycle.util.Constants;
import com.dreamcatcher.bicycle.util.GlobalSetting;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.view.ActivityTitle;
import com.dreamcatcher.bicycle.view.ActivityTitle.IActivityTitleLeftImageClickEvent;
import com.dreamcatcher.bicycle.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.dreamcatcher.bicycle.vo.Adsetting;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;
import com.dreamcatcher.bicycle.vo.CitySetting;
import com.waps.AdView;

public class BicycleMap extends MapActivity implements IHttpEvent, ISettingEvent, IAdEvent{
	private BMapManager mBMapManager = null;
	private MKLocationManager mLocationManager = null;
	private LocationListener mLocationListener = null;
	private MapView mMapView = null;
	private List<Overlay> mMapOverLayList = null;
	private View mBicyclePopView = null;
	private MapController mMapController = null;
	private BicycleDataset mDataset = null;
	private final static int RAT = 1000000;
	private TextView mMapPopName = null;
	private TextView mMapPopAvailBicycles = null;
	private TextView mMapPopAvailParks = null;
	private TextView mMapPopAddress = null;
	private Button mMapPopBtnTo = null;
	private Drawable mMarker = null;
	private LinearLayout mAdLine = null;
	private ItemizedBicycleOverlay mMarkersOverlay = null;
	private int mMarkerWidth = 0;
	private int mMarkerHeight = 0;
	private Drawable mFavoriteMarker = null;
	private ItemizedBicycleOverlay mFavoriteMarkersOverlay = null;
	private MyLocationOverlay mMyLocationOverlay = null;
	private boolean mMyLocationAdded = false;
	private boolean mMyLocationAnimationEnabled = false;
	private CitySetting mCitySetting = null;
	private IHttpService mHttpService = null;
	private OverlayItem mSelectedBicycleItem = null;
	private long mCurrentTime = 0;
	private int mSelectedId = -1;
	private boolean mAutoLocate = false;
	private LinearLayout mProgressbarLine = null;
	private boolean mNeedUpdateMyGeo = false;
	private GeoPoint mMyPoint = null;
	private MKSearch mMkSearch = null;
	private RouteOverlay mRouteOverlay = null;
	private PoiOverlay mPoiOverlay = null;
	private ArrayList<MKPoiInfo> mPoiInfos = null;
	private RelativeLayout mSearchBar = null;
	private EditText mSearchInput = null;
	private String mCityName = "";	
	private boolean mNeedUpdateCity = false;
	private View mPoiPopview = null;
	private TextView mPopBtn = null;
	private OverlayItem mSelectedPoiItem = null;
	
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
		mHttpService.getAllBicyclesInfo();
		
		mBMapManager = BicycleApp.getInstance().getMapManager();
		if(mBMapManager == null){
			BicycleApp.getInstance().initBaiduMap();
		}
		mBMapManager.start();
		super.initMapActivity(mBMapManager);
		//init search
		mMkSearch = new MKSearch();
		mMkSearch.init(mBMapManager, new MySearchListener());
		
		mDataset = BicycleDataset.getInstance();
		
		mProgressbarLine = (LinearLayout) findViewById(R.id.bicycle_map_progress_line);
		
		ActivityTitle activityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		activityTitle.setActivityTitle(getText(R.string.title_map));
		
		activityTitle.setRightImage(R.drawable.ic_titlebar_locate, new IActivityTitleRightImageClickEvent() {			
			@Override
			public void onRightImageClicked() {
				BicycleMap.this.toggleMyLocation();
			}
		});
		
		activityTitle.setLeftImage(R.drawable.ic_titlebar_search, new IActivityTitleLeftImageClickEvent() {			
			@Override
			public void onLeftImageClicked() {
				BicycleMap.this.showSearchBar();				
			}
		});
		
		mSearchBar = (RelativeLayout) findViewById(R.id.bicycle_map_search_bar);
		mSearchInput = (EditText) findViewById(R.id.bicycle_map_search_input);
		
		ImageButton searchBtn = (ImageButton) findViewById(R.id.bicycle_map_search_btn);
		ImageButton hideBtn = (ImageButton) findViewById(R.id.bicycle_map_search_btn_hide);
		
		searchBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				searchPoi();				
			}
		});
		
		hideBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				hideSearchBar();				
			}
		});
		
		mMapView = (MapView) findViewById(R.id.bicycle_mapview);			
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		mMapView.setBuiltInZoomControls(true);
		
		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		
		setMapSenter();		
		
		mBicyclePopView = getLayoutInflater().inflate(R.layout.map_pop, null);
		mMapPopName = (TextView) mBicyclePopView.findViewById(R.id.map_pop_name);
		mMapPopAvailBicycles = (TextView) mBicyclePopView.findViewById(R.id.map_pop_available_bicycles);
		mMapPopAvailParks = (TextView) mBicyclePopView.findViewById(R.id.map_pop_available_parks);
		mMapPopAddress = (TextView) mBicyclePopView.findViewById(R.id.map_pop_address);
		mMapPopBtnTo = (Button) mBicyclePopView.findViewById(R.id.map_pop_btn_to);
		
		mMapPopBtnTo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onMapPopBtnToClicked();				
			}
		});
		
		mMapView.addView(mBicyclePopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM));
		mBicyclePopView.setVisibility(View.GONE);
		
		mPoiPopview = getLayoutInflater().inflate(R.layout.poi_bubble, null);
		mPopBtn = (TextView) mPoiPopview.findViewById(R.id.poi_bubble_btn);
		
		mPopBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				searchPoiRouter();				
			}
		});
		
		mMapView.addView(mPoiPopview, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM));
		mPoiPopview.setVisibility(View.GONE);
		
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
        
		mAutoLocate = Utils.getBooleanDataFromLocal(Constants.LocalStoreTag.AUTO_LOCATE_ON_STARTUP, false);
		
		//add my location if auto locate set
		if(mAutoLocate){
			addMyLocation();
			enalbeMyLocationAnimation();
		}
		//check whether need show ad
		checkAd(true);
	}
	
	private void checkAd(boolean create){
		Adsetting adsetting = GlobalSetting.getInstance().getAdsetting();
		boolean showAdd = adsetting.isShowAd();
		long nextShowAdTime = adsetting.getNextShowAdTime();
		
		if(showAdd && System.currentTimeMillis() > nextShowAdTime){
			if(create){
				mAdLine = (LinearLayout)findViewById(R.id.AdLinearLayout);
				new AdView(this, mAdLine).DisplayAd();
			}
		}else {
			if(mAdLine != null){
				mAdLine.setVisibility(View.GONE);				
			}
		}
	}
	
	private void showSearchBar(){
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
		mSearchBar.setVisibility(View.VISIBLE);
		mSearchBar.startAnimation(animation);
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
		BicycleService.getInstance().getAdEventListener().addEvent(this);
	}
	
	private void removeEvent(){
		BicycleService.getInstance().getHttpEventListener().removeEvent(this);
		BicycleService.getInstance().getSettingEventListener().removeEvent(this);
		BicycleService.getInstance().getAdEventListener().removeEvent(this);
	}
	
	private void onMapPopBtnToClicked(){
		Toast.makeText(this, R.string.toast_msg_searching_router, Toast.LENGTH_SHORT).show();
		
		GeoPoint geoPoint = this.mSelectedBicycleItem.getPoint();
		if(mMyPoint == null){			
			mNeedUpdateMyGeo = true;
			getMyLocation();
		}else {
			MKPlanNode start = new MKPlanNode();
			start.pt = mMyPoint;
			MKPlanNode end = new MKPlanNode();
			end.pt = geoPoint;
			
			mMkSearch.walkingSearch(null, start, null, end);			
			mNeedUpdateMyGeo = false;
		}
	}
	
	//search poi
	private void searchPoi(){
		String searchKey = mSearchInput.getText().toString();
		if("".equals(searchKey.trim())){
			return;
		}
		
		if(mPoiOverlay != null){
			mMapOverLayList.remove(mPoiOverlay);
			mMapView.invalidate();
		}
		
		if("".equals(mCityName)){
			mNeedUpdateCity = true;
			getMyLocation();			
		}else {
			mProgressbarLine.setVisibility(View.VISIBLE);
			mMkSearch.poiSearchInCity(mCityName, searchKey);
			mNeedUpdateCity = false;
		}		
	}
	
	private void hideSearchBar(){
		Animation animation = AnimationUtils.loadAnimation(BicycleMap.this, R.anim.slide_up);
		mSearchBar.startAnimation(animation);
		mSearchBar.setVisibility(View.GONE);
	}
	
	private void getMyLocation(){
		if (!mMyLocationAdded) {
			addMyLocation();
		}
	}
	
	private void toggleMyLocation(){
		if(!mMyLocationAdded){
			addMyLocation();
		}
		if(mMyLocationAnimationEnabled){
			disableMyLocationAnimation();
		}else {
			enalbeMyLocationAnimation();
		}
	}
	
	private void enalbeMyLocationAnimation(){
		mMyLocationAnimationEnabled = true;
		if(mMyPoint != null){
			mMapController.animateTo(mMyPoint);
		}
	}
	
	private void disableMyLocationAnimation(){
		mMyLocationAnimationEnabled = false;	
	}
	
	private void addMyLocation(){
		//Add my location
        mLocationManager = mBMapManager.getLocationManager();
        
        mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
        mLocationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
        
        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
        
        mLocationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mMyPoint = new GeoPoint((int)(location.getLatitude() * RAT), (int)(location.getLongitude() * RAT));
				if(mMyLocationAnimationEnabled){
					mMapController.animateTo(mMyPoint);
				}				
				
				if(mNeedUpdateMyGeo){
					onMapPopBtnToClicked();
				}
				if("".equals(mCityName)){
					mMkSearch.reverseGeocode(mMyPoint);
				}
			}
		};
		
        mMapView.getOverlays().add(mMyLocationOverlay);
        
        mMyLocationOverlay.enableCompass();
		mMyLocationOverlay.enableMyLocation();
        
        mBMapManager.stop();
		mLocationManager.requestLocationUpdates(mLocationListener);
		mBMapManager.start();
        
        mMyLocationAdded = true;
	}
	
	
	/**
	 * add all the bicycle marks in map
	 * @param overlayList
	 */
	private void addAllBicycleMarkers(){		
        ArrayList<BicycleStationInfo> bicycleInfos = mDataset.getBicycleStationInfos();
        String[] favoriteIds = getFavoriteIds();
        if(mMarkersOverlay != null){
        	mMarkersOverlay.clearAllOverlayItem();
        }
        if(mFavoriteMarkersOverlay != null){
        	mFavoriteMarkersOverlay.clearAllOverlayItem();
        }
        
        
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
		if(mMarkersOverlay.size() >= 0){
        	mMapOverLayList.remove(mMarkersOverlay);
        }
        
        if(mFavoriteMarkersOverlay.size() >= 0){        	
        	mMapOverLayList.remove(mFavoriteMarkersOverlay);
        }
	}
		
	private void reLoadUI(){
		//set map center
		mCitySetting = GlobalSetting.getInstance().getCitySetting();
		mBicyclePopView.setVisibility(View.GONE);
		
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
			if (mLocationManager != null && mMyLocationAdded) {
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
			mSelectedBicycleItem = overlayItem;
			setFocus(overlayItem);
			
			int bicycleId = Integer.parseInt(overlayItem.getTitle());
			
			if(System.currentTimeMillis() - mCurrentTime > 2000){
				mCurrentTime = System.currentTimeMillis();
				mSelectedId = bicycleId;
				mProgressbarLine.setVisibility(View.VISIBLE);
				mHttpService.getSingleBicycleInfo(bicycleId);				
			}
			return true;
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			mBicyclePopView.setVisibility(View.GONE);			
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
		
		public void clearAllOverlayItem(){
			mOverlayItems.clear();
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
		
		GeoPoint geoPoint = this.mSelectedBicycleItem.getPoint();
		Point point = mMapView.getProjection().toPixels(geoPoint, null);
		int posX = point.x - mMarkerWidth,
			poxY = point.y - mMarkerHeight;
		GeoPoint toShowPoint = mMapView.getProjection().fromPixels(posX, poxY);
		
		mMapView.updateViewLayout(mBicyclePopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				toShowPoint, MapView.LayoutParams.BOTTOM));
		mBicyclePopView.setVisibility(View.VISIBLE);			
	}
	
	private class MySearchListener implements MKSearchListener{

		@Override
		public void onGetAddrResult(MKAddrInfo result, int error) {
			if(error != 0 || result == null){
				return;
			}
			if(result.poiList != null && result.poiList.size() > 0){
				MKPoiInfo poiInfo = result.poiList.get(0);
				mCityName = poiInfo.city;
				if(mNeedUpdateCity){
					searchPoi();
				}
			}
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int error) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int error) {
			mProgressbarLine.setVisibility(View.GONE);
			
			if(error != 0 || result == null){
				Toast.makeText(BicycleMap.this, R.string.toast_msg_no_poi_result, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mPoiOverlay != null){
				mMapOverLayList.remove(mPoiOverlay);
			}
			if(mRouteOverlay != null){
				mMapOverLayList.remove(mRouteOverlay);
			}
			
			mMapView.invalidate();
			
			if(result.getCurrentNumPois() > 0){
				mPoiOverlay = new MyPoiOverlay(BicycleMap.this, mMapView);
				mPoiInfos = result.getAllPoi();
				mPoiOverlay.setData(mPoiInfos);
				
				mMapOverLayList.add(mPoiOverlay);
				mMapView.invalidate();
				mMapController.animateTo(result.getPoi(0).pt);
				hideSearchBar();
				hideKeyBoard();
			}else {
				hideKeyBoard();
				Toast.makeText(BicycleMap.this, R.string.toast_msg_no_poi_result, Toast.LENGTH_SHORT).show();
			}			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int error) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int error) {
			mProgressbarLine.setVisibility(View.GONE);
			
			if(error != 0 || result == null){
				Toast.makeText(BicycleMap.this, R.string.toast_msg_warking_router_search_failed, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(mRouteOverlay != null){
				mMapOverLayList.remove(mRouteOverlay);
			}			
			
			mRouteOverlay = new RouteOverlay(BicycleMap.this, mMapView);
			mRouteOverlay.setData(result.getPlan(0).getRoute(0));
			mMapOverLayList.add(mRouteOverlay);
			mMapView.invalidate();
			mBicyclePopView.setVisibility(View.GONE);
			mPoiPopview.setVisibility(View.GONE);			
		}		
	}

	private class MyPoiOverlay extends PoiOverlay{		
		public MyPoiOverlay(Activity avtivity, MapView mapView) {
			super(avtivity, mapView);
		}
		
		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			mPoiPopview.setVisibility(View.GONE);
			return super.onTap(point, mapView);
		}
		
		@Override
		protected boolean onTap(int index) {
			mSelectedPoiItem = getItem(index);
			int height = mSelectedPoiItem.getMarker(index).getIntrinsicWidth();
			showPoiContent(index, height);
			return true;
		}		
	}
	
	private void showPoiContent(int index, int height){
		GeoPoint geoPoint = mPoiInfos.get(index).pt;
		Point point = mMapView.getProjection().toPixels(geoPoint, null);
		int posX = point.x,
			poxY = point.y - height;
		GeoPoint toShowPoint = mMapView.getProjection().fromPixels(posX, poxY);
		
		mMapView.updateViewLayout(mPoiPopview, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				toShowPoint, MapView.LayoutParams.BOTTOM));
		
		mPoiPopview.setVisibility(View.VISIBLE);
	}
	
	private void searchPoiRouter(){
		if(mSelectedPoiItem == null){
			return;
		}
		
		GeoPoint geoPoint = mSelectedPoiItem.getPoint();
		
		MKPlanNode start = new MKPlanNode();
		start.pt = mMyPoint;
		MKPlanNode end = new MKPlanNode();
		end.pt = geoPoint;
		
		mProgressbarLine.setVisibility(View.VISIBLE);
		
		mMkSearch.walkingSearch(null, start, null, end);
	}
	
	private void hideKeyBoard(){
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus()
	       		.getWindowToken(), 0);
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
		mProgressbarLine.setVisibility(View.GONE);
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

	@Override
	public void onPointsUpdated(String currencyName, int totalPoint) {
		//check whether need to show ad
		checkAd(false);		
	}

	@Override
	public void onPointsUpdateFailed(String error) {		
		
	}
}
