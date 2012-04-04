package com.walt.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.ConsoleHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.walt.R;
import com.walt.dataset.BicycleDataset;
import com.walt.util.Constants;
import com.walt.util.Utils;
import com.walt.vo.BicycleStationInfo;

public class BicycleMap extends MapActivity {
	private BMapManager mBMapManager = null;
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
		
		mMapView = (MapView) findViewById(R.id.bicycle_mapview);			
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		mMapView.setBuiltInZoomControls(true);
		
		mMapController = mMapView.getController();
		mGeoPoint = new GeoPoint((int)(Constants.LocalSetting.DEFAULT_LATITUDE * RAT), (int) (Constants.LocalSetting.DEFAULT_LONGITUDE * RAT));
		
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

        List<Overlay> list = mMapView.getOverlays();
        
        mMarker = getResources().getDrawable(R.drawable.ic_marker);
        mMarkerHeight = mMarker.getIntrinsicHeight();
        mMarkerWidth = mMarker.getIntrinsicWidth();
        mMarker.setBounds(0, 0, mMarkerWidth, mMarkerHeight);
        
        ItemizedBicycleOverlay bicycleOverlays = new ItemizedBicycleOverlay(mMarker, this);
        ArrayList<BicycleStationInfo> bicycleInfos = mDataset.getBicyleStationInfos();
        
        for(int i = 0, count = bicycleInfos.size(); i < count; i++){
        	BicycleStationInfo bicycleInfo = bicycleInfos.get(i);
        	GeoPoint point = new GeoPoint((int)(bicycleInfo.getLatitude() * RAT), (int)(bicycleInfo.getLongitude() * RAT));
        	OverlayItem overlayItem = new OverlayItem(point, String.valueOf(bicycleInfo.getId()), bicycleInfo.getName());
        	bicycleOverlays.addOverlayItem(overlayItem);
        }
        
        list.add(bicycleOverlays);
        
        //update bicycle info from server
        mThreadPool.execute(new Runnable() {			
			public void run() {
				getAllBicylesInfoFromServer();				
			}
		});       
	}
	
	
	@Override
	protected void onDestroy() {
		if(mBMapManager != null){
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if(mBMapManager != null){
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mBMapManager != null){
			mBMapManager.start();
		}
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * update bicycles info from server and save it to local
	 */
	private void getAllBicylesInfoFromServer(){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Constants.HttpSetting.ALL_BICYLE_URL);
		String jsonStr = null;
		try {			
			HttpResponse response = httpClient.execute(httpGet);
			jsonStr = EntityUtils.toString(response.getEntity());
			
			if(jsonStr == null || jsonStr.equals("")){
				throw new Exception();
			}
			
			int firstBrace = jsonStr.indexOf("{");
			jsonStr = jsonStr.substring(firstBrace);
			
			Utils.storeDataToLocal(Constants.LocalStoreTag.ALL_BICYLE, Utils.convertToUtf8Str(jsonStr));			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public BicycleStationInfo getSingleBicyleInfoFromHttp(int id){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Constants.HttpSetting.BICYLE_DETAIL_URL);
		String jsonStr = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			jsonStr = EntityUtils.toString(response.getEntity());
			
			if(jsonStr == null || jsonStr.equals("")){
				throw new Exception();
			}
			
			int firstBrace = jsonStr.indexOf("{");
			jsonStr = jsonStr.substring(firstBrace);
			
			JSONObject jsonObject = new JSONObject(Utils.convertToUtf8Str(jsonStr));	
			JSONArray jsonArray = jsonObject.getJSONArray(Constants.JsonTag.STATION);
			BicycleStationInfo bicycleInfo = null;
			for(int i = 0, total = jsonArray.length(); i < total; i++){
				JSONObject jsonItem = jsonArray.getJSONObject(i);				
				String name = jsonItem.getString(Constants.JsonTag.NAME);
				double latitude = jsonItem.getDouble(Constants.JsonTag.LATITUDE);
				double longitude = jsonItem.getDouble(Constants.JsonTag.LONGITUDE);
				int capacity = jsonItem.getInt(Constants.JsonTag.CAPACITY);
				int available = jsonItem.getInt(Constants.JsonTag.AVAIABLE);
				String address = jsonItem.getString(Constants.JsonTag.ADDRESS);
				
				bicycleInfo = new BicycleStationInfo(id, name, latitude, longitude, capacity, available, address);				
			}
			return bicycleInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	class ItemizedBicycleOverlay extends ItemizedOverlay<OverlayItem>{
		private ArrayList<OverlayItem> mOverlayItems = new ArrayList<OverlayItem>();
		private OverlayItem mSelectedOverlayItem = null;
		
		public ItemizedBicycleOverlay(Drawable defaultMarker) {
			super(boundCenter(defaultMarker));
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
			Log.e("BicycleMap", "Address = " + bicycleStationInfo.getAddress());
			
			mMapPopAddress.setText(bicycleStationInfo.getAddress());
			
			GeoPoint geoPoint = this.mSelectedOverlayItem.getPoint();
			Point point = mMapView.getProjection().toPixels(geoPoint, null);
			int posX = point.x - mMarkerWidth,
				poxY = point.y - mMarkerHeight;
			GeoPoint toShowPoint = mMapView.getProjection().fromPixels(posX, poxY);
			
			mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					toShowPoint, MapView.LayoutParams.BOTTOM));
			mPopView.setVisibility(View.VISIBLE);
			Log.e("BicycleMap", "showPopContent: lat ");
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
			BicycleStationInfo bicycleInfo = getSingleBicyleInfoFromHttp(id);
			return bicycleInfo;
		}		
	}
	
	class BicyleOverlay extends Overlay{			
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
            super.draw(canvas, mapView, shadow);
            Paint paint = new Paint();
            Point myScreenCoords = new Point();
           
            mapView.getProjection().toPixels(mGeoPoint, myScreenCoords);
            paint.setStrokeWidth(1);
            paint.setARGB(255, 255, 0, 0);
            paint.setStyle(Paint.Style.STROKE);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pin);
            canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
            canvas.drawText("天府广场", myScreenCoords.x, myScreenCoords.y, paint);
            return true;
        }

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			
			return super.onTap(p, mapView);
			
		}        
    }
	
	

}
