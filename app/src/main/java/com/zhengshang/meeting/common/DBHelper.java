package com.zhengshang.meeting.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhengshang.meeting.dao.entity.News;
import com.zhengshang.meeting.dao.entity.NewsChannel;
import com.zhengshang.meeting.dao.entity.User;

/**
 * 数据库辅助类
 *
 * @author sun
 */
public class DBHelper extends SQLiteOpenHelper {
    public static DBHelper instance;
    private Context context;

    private DBHelper(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
        this.context = context;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
