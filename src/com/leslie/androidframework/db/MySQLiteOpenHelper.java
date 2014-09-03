package com.leslie.androidframework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * 数据库版本升级问题要注意
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "mydb";
	public static final int DB_VERSION = 1;

	/**
	 * 资讯表
	 */
	public static final String NEWS_TABLE_NAME = "news";
	public static final String NEWS_ID = "id";
	public static final String NEWS_TITLE = "title";

	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public MySQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * <p>
	 * 每次出新版本所有的表都应该在这里创建
	 * </p>
	 * <p>
	 * 如果用户是第一次安装某个版本，这样就保证了所有的表都在
	 * </p>
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + NEWS_TABLE_NAME + "(" + NEWS_ID
				+ " integer primary key," + NEWS_TITLE + " text not null);");
	}

	// 这里处理数据库升级,非第一安装
	// 假设用户装了 版本1，现在要升级成 版本3 要保证 版本2，版本3 的修改都在
	// 假设用户装了 版本2，现在要升级成 版本3 要保证 版本3 的修改在
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("leslie", "oldVersion " + oldVersion);
		Log.i("leslie", "newVersion " + newVersion);

		if (oldVersion < 2) {
			String sqlString = "create table table2";
			db.execSQL(sqlString);
		}

		if (oldVersion < 3) {
			String sqlString = "create table table3";
			db.execSQL(sqlString);
		}
	}
}
