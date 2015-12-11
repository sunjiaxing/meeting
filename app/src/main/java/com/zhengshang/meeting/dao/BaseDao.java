package com.zhengshang.meeting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhengshang.meeting.common.DBHelper;
import com.zhengshang.meeting.dao.entity.News;
import com.zhengshang.meeting.dao.entity.NewsChannel;


public class BaseDao {
	protected SQLiteDatabase db;
	protected Cursor cursor = null;
	protected String sql = null;

	public BaseDao(Context context) {
		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
		} catch (Exception e) {
			db = DBHelper.getInstance(context).getWritableDatabase();
		}
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}
	/**
	 * 清除所有表的数据
	 */
	public void clearAllTableData(){
		try {
			beginTransaction();
			db.execSQL(NewsChannel.DELETE_TABLE_DATA);
			db.execSQL(News.DELETE_TABLE_DATA);
			setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endTransaction();
		}
	}

	protected void releaseConnection(){
		if (cursor != null) {
			cursor.close();
		}
	}
}
