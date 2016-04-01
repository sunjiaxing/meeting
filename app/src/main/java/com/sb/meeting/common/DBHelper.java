package com.sb.meeting.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.sb.meeting.dao.entity.Area;
import com.sb.meeting.dao.entity.CheckingGoods;
import com.sb.meeting.dao.entity.Company;
import com.sb.meeting.dao.entity.Goods;
import com.sb.meeting.dao.entity.News;
import com.sb.meeting.dao.entity.NewsChannel;
import com.sb.meeting.dao.entity.Student;
import com.sb.meeting.dao.entity.User;

/**
 * 数据库辅助类
 *
 * @author sun
 */
public class DBHelper extends SQLiteOpenHelper {
    public static DBHelper instance;

    private DBHelper(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
    }

    /**
     * 单例
     *
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new DBHelper(context,
                            BonConstants.DATABASE_NAME, null,
                            BonConstants.DATABASE_VERSION);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(News.CREAT_TABLE);
        db.execSQL(NewsChannel.CREAT_TABLE);
        db.execSQL(User.CREAT_TABLE);
        db.execSQL(Goods.CREAT_TABLE);
        db.execSQL(CheckingGoods.CREAT_TABLE);
        db.execSQL(Area.CREAT_TABLE);
        db.execSQL(Company.CREAT_TABLE);
        db.execSQL(Student.CREAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
