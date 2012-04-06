package com.walt.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
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
import com.walt.R;
import com.walt.dataset.BicycleDataset;
import com.walt.util.Constants;
import com.walt.util.HttpUtils;
import com.walt.view.ActivityTitle;
import com.walt.view.ActivityTitle.IActivityTitleRightImageClickEvent;
import com.walt.vo.BicycleStationInfo;

public class BicycleMap extends MapActivity {
	private BMapManager mBMapManager = null;
	private MKLocationManager mLocationManager = null;
	private LocationListener mLocationListener = null;
	private MapView mMapView = null;
	private View mPopView = null;
	private MapController mMapController = null;
	private GeoPoint mGeoPoint = null;
	private BicycleDataset mDataset = null;
	private final static int RAT = 1000000;
	private TextView mMapPopName = null;
	private TextView mMapPopAvailBicyles = null;
	private TextView mMapPopAvailParks = null;
	private TextView mMapPopAddress = null;
	private ExecutorService mThreadPool = Executors.newCachedThreadPool();
	private Drawable mMarker = null;
	private int mMarkerWidth = 0;
	private int mMarkerHeight = 0;
	private MyLocationOverlay mMyLocationOverlay = null;
	private ActivityTitle mActivityTitle = null;
	private boolean mMyLocationAdded = false;
	private boolean mMyLocationEnabled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bicycle_map);
		
		init();
	}
	
	private void init(){
		mBMapManager = new BMapManager(this);
		mBMapManager.init(Constants.BaiduApi.KEY, null);
		super.initMapActivity(mBMapManager);
		
		mDataset = BicycleDataset.getInstance();
		
		mActivityTitle = (ActivityTitle) findViewById(R.id.bicycle_title);
		mActivityTitle.setActivityTitle(getText(R.string.title_map));
		
		mMapView = (MapView) findViewById(R.id.bicycle_mapview);			
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		mMapView.setBuiltInZoomControls(true);
		
		mMapController = mMapView.getController();
		mGeoPoint = new GeoPoint(
				(int) ((Constants.LocalSetting.DEFAULT_LATITUDE + Constants.LocalSetting.OFFSET_LATITUDE) * RAT),
				(int) ((Constants.LocalSetting.DEFAULT_LONGITUDE + Constants.LocalSetting.OFFSET_LONGITUDE) * RAT));

		mMapController.setCenter(mGeoPoint);
		mMapController.setZoom(15);
		
		mPopView = getLayoutInflater().inflate(R.layout.map_pop, null);
		mMapPopName = (TextView) mPopView.findViewById(R.id.map_pop_name);
		mMapPopAvailBicyles = (TextView) mPopView.findViewById(R.id.map_pop_available_bicycles);
		mMapPopAvailParks = (TextView) mPopView.findViewById(R.id.map_pop_available_parks);
		mMapPopAddress = (TextView) mPopView.findViewById(R.id.map_pop_address);		
		
		mMapView.addView(mPopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM));
		mPopView.setVisibility(View.GONE);
		

        List<Overlay> overlayList = mMapView.getOverlays();
        
        addAddBicycleMarks(overlayList);
        
        //update bicycle info from server
        mThreadPool.execute(new Runnable() {			
			public void run() {
				HttpUtils.getAllBicylesInfoFromServer();
			}
		});        

		IActivityTitleRightImageClickEvent rightImageClickEvent = new IActivityTitleRightImageClickEvent() {			
			public void onRightImageClicked() {
				BicycleMap.this.onRightImageClicked();		
			}
		};
		mActivityTitle.setRightImage(R.drawable.ic_titlebar_locate, rightImageClickEvent);
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
        ArrayList<BicycleStationInfo> bicycleInfos = mDataset.getBicyleStationInfos();
        
        for(int i = 0, count = bicycleInfos.size(); i < count; i++){
        	BicycleStationInfo bicycleInfo = bicycleInfos.get(i);
        	GeoPoint point = new GeoPoint((int)((Constants.LocalSetting.OFFSET_LATITUDE + bicycleInfo.getLatitude()) * RAT), (int)((Constants.LocalSetting.OFFSET_LONGITUDE + bicycleInfo.getLongitude()) * RAT));
        	OverlayItem overlayItem = new OverlayItem(point, String.valueOf(bicycleInfo.getId()), bicycleInfo.getName());
        	bicycleOverlays.addOverlayItem(overlayItem);
        }
        
        overlayList.add(bicycleOverlays);
	}
	
	
	@Override
	protected void onDestroy() {
		if(mBMapManager != null){
			if(mLocationManager != null){
				mLocationManager.removeUpdates(mLocationListener);
			}			
			mBMapManager.destroy();
			mBMapManager = null;
		}
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
		private OverlayItem mSelectedOverlayItem = null;
		
		public ItemizedBicycleOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}
		
		public ItemizedBicycleOverlay(Drawable marker, Context context){
			super(boundCenterBottom(marker));
		}		

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mOverlayItems.get(i);
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem overlayItem = mOverlayItems.get(index);
			this.mSelectedOverlayItem = overlayItem;
			setFocus(overlayItem);
			
			int bicycleId = Integer.parseInt(overlayItem.getTitle());
//			BicycleStationInfo bicycleStationInfo = mDataset.getBicyleInfo(bicycleId);
//			
//			showPopContent(bicycleStationInfo);
			
			refreshBicyleInfo(bicycleId);
			
			return true;
		}
		
		private void showPopContent(BicycleStationInfo bicycleStationInfo){
			mMapPopName.setText(bicycleStationInfo.getName());
			mMapPopAvailBicyles.setText(String.valueOf(bicycleStationInfo.getAvailable()));
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
		
		private void refreshBicyleInfo(final int id) {
			GetBicycleInfoTask task = new GetBicycleInfoTask(id);
			Future<BicycleStationInfo> future = mThreadPool.submit(task);
			try {
				BicycleStationInfo bicycleStationInfo = future.get();
				showPopContent(bicycleStationInfo);
				mDataset.updateBicycleInfo(id, bicycleStationInfo);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			mPopView.setVisibility(View.GONE);
			
			return super.onTap(point, mapView);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mOverlayItems.size();
		}
		
		public void addOverlayItem(OverlayItem overlayItem){
			mOverlayItems.add(overlayItem);
			this.populate();
		}
	}
	
	class GetBicycleInfoTask implements Callable<BicycleStationInfo>{
		private int id;
		public GetBicycleInfoTask(int id){
			this.id = id;
		}

		public BicycleStationInfo call() throws Exception {
			BicycleStationInfo bicycleInfo = HttpUtils.getSingleBicyleInfoFromHttp(id);			
			return bicycleInfo;
		}		
	}
	
	class BicycleOverlay extends Overlay{	
		private GeoPoint mGeo;
        public BicycleOverlay() {
			super();
			mGeo = new GeoPoint((int) (31.663098 * RAT), (int)(120.75511 * RAT));
		}

		@Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            Point myScreenCoords = new Point();
           
            mapView.getProjection().toPixels(mGeo, myScreenCoords);
            paint.setStrokeWidth(1);
            paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.STROKE);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pin);
            canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
            canvas.drawText("87", myScreenCoords.x, myScreenCoords.y, paint);
            return true;
        }       
    }
	
	

}
