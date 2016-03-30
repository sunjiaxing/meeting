package com.sb.meeting.dao;

import android.content.Context;

import com.sb.meeting.dao.entity.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域dao
 * Created by sun on 2016/3/29.
 */
public class AreaDao extends BaseDao {

    public AreaDao(Context context) {
        super(context);
    }

    /**
     * 判断是否有数据
     *
     * @return
     */
    public boolean hasData() {
        try {
            sql = "select count(" + Area.KEY_COLUMN_ID + ") as count from " + Area.KEY_TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(cursor.getColumnIndex("count"));
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return false;
    }

    /**
     * 执行sql
     *
     * @param sql
     */
    public void execSQL(String sql) {
        db.execSQL(sql);
    }

    /**
     * 获取地区列表
     *
     * @param parentId 父级单位id
     * @return
     */
    public List<Area> getAreaList(int parentId) {
        List<Area> data = null;
        try {
            sql = "select " + Area.KEY_COLUMN_ID + "," + Area.KEY_COLUMN_NAME + " from "
                    + Area.KEY_TABLE_NAME + " where "
                    + Area.KEY_COLUMN_STYLE + " = 0 and "
                    + Area.KEY_COLUMN_PARENT_ID + " = ? order by "
                    + Area.KEY_COLUMN_LIST_ORDER + " desc";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(parentId)});
            if (cursor != null && cursor.getCount() > 0) {
                data = new ArrayList<>();
                Area area;
                while (cursor.moveToNext()) {
                    area = new Area();
                    area.setAreaId(cursor.getInt(cursor.getColumnIndex(Area.KEY_COLUMN_ID)));
                    area.setName(cursor.getString(cursor.getColumnIndex(Area.KEY_COLUMN_NAME)));
                    data.add(area);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return data;
    }
}
