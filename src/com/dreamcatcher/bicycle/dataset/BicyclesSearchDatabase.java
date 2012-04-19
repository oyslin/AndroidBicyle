package com.dreamcatcher.bicycle.dataset;

import java.util.ArrayList;
import java.util.HashMap;

import com.dreamcatcher.bicycle.BicycleApp;
import com.dreamcatcher.bicycle.util.Utils;
import com.dreamcatcher.bicycle.vo.BicycleStationInfo;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

public class BicyclesSearchDatabase {
	private static final String DATABASE_NAME = "bicyclestation";
    private static final String FTS_VIRTUAL_TABLE = "FTSbicyclestation";
    private static final int DATABASE_VERSION = 2;
    
    private static final String BICYCLE_ID = "bicycle_id";
    private static final String BICYCLE_NAME_PINYIN = "bicycle_name";
    private static final HashMap<String,String> mColumnMap = buildColumnMap();    
    private final BicycleSearchOpenHelper mBicycleSearchOpenHelper;
    
    public BicyclesSearchDatabase(){
    	mBicycleSearchOpenHelper = new BicycleSearchOpenHelper(BicycleApp.getInstance());
    }
    
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(BICYCLE_ID, BICYCLE_ID);
        map.put(BICYCLE_NAME_PINYIN, BICYCLE_NAME_PINYIN);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }
    
    public Cursor queryBicycleStationName(String query, String[] columns){
    	String selection = BICYCLE_NAME_PINYIN + " MATCH ?";
    	String[] selectionArgs = new String[] {query+"*"};
    	return query(selection, selectionArgs, columns);
    }
    
    
    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(mBicycleSearchOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    
    
    private static class BicycleSearchOpenHelper extends SQLiteOpenHelper{
        private SQLiteDatabase mDatabase;
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                " USING fts3 (" +
                BICYCLE_ID + ", " +
                BICYCLE_NAME_PINYIN + ");";
        
    	public BicycleSearchOpenHelper(Context context){
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			db.execSQL(FTS_TABLE_CREATE);
			addBicycleData();
		}
		
		private void addBicycleData(){
			new Thread(new Runnable() {				
				public void run() {
					ArrayList<BicycleStationInfo> arrayList = BicycleDataset.getInstance().getBicycleStationInfos();
					for(int i = 0, n = arrayList.size(); i < n; i++){
						BicycleStationInfo bicycleStationInfo = arrayList.get(i);
						int id = bicycleStationInfo.getId();
						String name = bicycleStationInfo.getName();
						name = Utils.getPinyinCaptalLetter(name);
						addBicycleName(id, name);
					}					
				}
			}).start();
		}
		
		private long addBicycleName(int id, String name){
			ContentValues contentValues = new ContentValues();
			contentValues.put(BICYCLE_ID, id);
			contentValues.put(BICYCLE_NAME_PINYIN, name);
			return mDatabase.insert(FTS_VIRTUAL_TABLE, null, contentValues);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);			
		}
    }
}
