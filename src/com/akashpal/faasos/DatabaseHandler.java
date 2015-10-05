package com.akashpal.faasos;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// DataBase info:
	private static final int DATABASE_VERSION = 3 ;
	// The version number must be incremented each time a change to DB structure occurs.
	private static final String DATABASE_NAME = "LikedItems";
	private static final String TABLE_NAME = "Likes";
	
	// Field Names: 
	public static final String KEY_NAME = Constants.TAG_NAME;
	public static final String KEY_PRICE = Constants.TAG_PRICE;
	public static final String KEY_SPICE = Constants.TAG_SPICE_METER;
	public static final String KEY_IMAGE_URL = Constants.TAG_IMAGE;
	
	// Column Numbers for each Field Name:
	public static final int COL_NAME = 0;
	public static final int COL_PRICE = 1;
	public static final int COL_SPICE = 2;
    public static final int COL_IMAGE_URL = 3;

	
	private SQLiteDatabase db;

	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("DATA","onCreate db");
		
		String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_NAME +
		" ( " + 
	    KEY_NAME + " TEXT PRIMARY KEY, " +
	    KEY_PRICE + " INTEGER, " +
	    KEY_SPICE + " INTEGER, " +
	    KEY_IMAGE_URL + " TEXT " +
	    " );" ;
		
		db.execSQL(CREATE_BOOK_TABLE);
		
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("DATA","onUpgrade db");
		
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
		
		onCreate(db);
	}
	
	

	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;

		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db =  this.getReadableDatabase();
		//Cursor c = 	db.query(true, TABLE_NAME, ALL_KEYS, where, null, null, null, null, null);
		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	
	void addLike(String name,int price,int spice,String img){
		
		Log.d("DATA","like added");
		
		SQLiteDatabase db =  this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME,name);
		values.put(KEY_PRICE,price);
		values.put(KEY_SPICE,spice);
		values.put(KEY_IMAGE_URL,img);
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	
	
	
	public ArrayList<HashMap<String, String>> getLikedItems() 
	{
		Log.d("DATA","get Like");
		
		ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>(); 
		HashMap<String,String> map;
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		SQLiteDatabase db =  this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String n = cursor.getString(COL_NAME);
				String p = cursor.getString(COL_PRICE);
				String s = cursor.getString(COL_SPICE);
				String img = cursor.getString(COL_IMAGE_URL);
				
				// creating new HashMap
				map = new HashMap<String, String>();
			    map.put(KEY_NAME, n);
			    map.put(KEY_PRICE, p);
			    map.put(KEY_SPICE, s);
			    map.put(KEY_IMAGE_URL, img);
			    al.add(map);
					
			}while(cursor.moveToNext());
		}
		
		return al;
	}
	
	

}
