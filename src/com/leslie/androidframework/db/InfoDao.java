package com.leslie.androidframework.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.leslie.androidframework.entity.News;

public class InfoDao extends BaseDao {
	public InfoDao(Context context) {
		super(context);
	}

	public long addNews(News item, String userId) {
		if (item == null) {
			return -1000;
		}

		ContentValues values = new ContentValues();
		values.put(MySQLiteOpenHelper.NEWS_ID, item.getId());
		values.put(MySQLiteOpenHelper.NEWS_TITLE, item.getTitle());

		long rowNum = -1;

		try {
			rowNum = getDB().insert(MySQLiteOpenHelper.NEWS_TABLE_NAME, null,
					values);
		} catch (Exception e) {

		}

		return rowNum;
	}

	public List<News> getAllInfo() {
		List<News> list = new ArrayList<News>();

		try {
			Cursor cursor = getDB().query(MySQLiteOpenHelper.NEWS_TABLE_NAME,
					null, null, null, null, null, null);

			while (cursor.moveToNext()) {
				News news = new News();
				news.setId(cursor.getInt(cursor
						.getColumnIndex(MySQLiteOpenHelper.NEWS_ID)));
				news.setTitle(cursor.getString(cursor
						.getColumnIndex(MySQLiteOpenHelper.NEWS_TITLE)));
				list.add(news);
			}

			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
