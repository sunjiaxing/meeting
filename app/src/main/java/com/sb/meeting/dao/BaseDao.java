package com.sb.meeting.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sb.meeting.common.DBHelper;
import com.sb.meeting.dao.entity.CheckingGoods;
import com.sb.meeting.dao.entity.Company;
import com.sb.meeting.dao.entity.Goods;
import com.sb.meeting.dao.entity.News;
import com.sb.meeting.dao.entity.NewsChannel;
import com.sb.meeting.dao.entity.Student;


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
    public void clearAllTableData() {
        try {
            beginTransaction();
            db.execSQL(NewsChannel.DELETE_TABLE_DATA);
            db.execSQL(News.DELETE_TABLE_DATA);
            db.execSQL(Goods.DELETE_TABLE_DATA);
            db.execSQL(CheckingGoods.DELETE_TABLE_DATA);
            db.execSQL(Company.DELETE_TABLE_DATA);
            db.execSQL(Student.DELETE_TABLE_DATA);
            setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }
    }

    /**
     * 释放 cursor
     */
    protected void releaseConnection() {
        if (cursor != null) {
            cursor.close();
        }
    }
}
