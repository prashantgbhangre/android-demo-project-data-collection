package com.prashantb.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final String DATABASE_NAME = "Demo";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_DATA = "tblData";

	public static final String KEY_DATA_COLUMN_ID = "DataId";
	public static final String KEY_DATA_COLUMN_NAME = "DdataName";
	public static final String KEY_DATA_COLUMN_NUMBER = "DataNumner";
	public static final String KEY_DATA_COLUMN_EMAIL = "DataEmail";

	public static final String QUERY_DATA = "CREATE TABLE " + TABLE_DATA + "("
			+ KEY_DATA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_DATA_COLUMN_NAME + " TEXT ," + KEY_DATA_COLUMN_NUMBER
			+ " TEXT ," + KEY_DATA_COLUMN_EMAIL + " TEXT );";

	final Context mContext;
	DatabaseHelper dbHelper;
	SQLiteDatabase db;

	public DBAdapter(Context context) {
		mContext = context;
		dbHelper = new DatabaseHelper(mContext);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				Log.d("ctrae", "here");
				db.execSQL(QUERY_DATA);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
				onCreate(db);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public DBAdapter open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public boolean IsDbNull() {
		Cursor mCursor = db.query(TABLE_DATA,
				new String[] { KEY_DATA_COLUMN_ID }, null, null, null, null,
				null);
		if (mCursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean insertDATADetail(String name, String number, String email) {
		try {
			ContentValues info = new ContentValues();
			info.put(KEY_DATA_COLUMN_NAME, name);
			info.put(KEY_DATA_COLUMN_NUMBER, number);
			info.put(KEY_DATA_COLUMN_EMAIL, email);
			return db.insert(TABLE_DATA, null, info) > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public Cursor getDATADetail() {
		try {
			return db.query(TABLE_DATA, new String[] { KEY_DATA_COLUMN_ID,
					KEY_DATA_COLUMN_NAME, KEY_DATA_COLUMN_NUMBER,
					KEY_DATA_COLUMN_EMAIL }, null, null, null, null, null);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean deleteAll(String id) {
		// TODO Auto-generated method stub
		 return db.delete(TABLE_DATA, KEY_DATA_COLUMN_ID+ "=" + id, null) > 0;
	}
}
