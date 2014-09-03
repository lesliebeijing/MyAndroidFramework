package com.leslie.androidframework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * CRUD base class
 */
public class BaseDao {
	private MySQLiteOpenHelper sqliteOpenHelper = null;
	private SQLiteDatabase db = null;

	public BaseDao(Context context) {
		sqliteOpenHelper = new MySQLiteOpenHelper(context);
	}

	SQLiteDatabase getDB() {
		try {
			if (db == null) {
				db = sqliteOpenHelper.getReadableDatabase();
			}
		} catch (Exception e) {

		}

		return db;
	}

	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
