package com.sb.meeting.dao.entity;

import java.io.Serializable;

/**
 * 地区
 * Created by sun on 2016/3/29.
 */
public class Area implements Serializable{
    public static final String KEY_COLUMN_ID = "area_id";
    public static final String KEY_COLUMN_NAME = "name";
    public static final String KEY_COLUMN_STYLE = "style";
    public static final String KEY_COLUMN_PARENT_ID = "parent_id";
    public static final String KEY_COLUMN_CHILD = "child";
    public static final String KEY_COLUMN_ARR_CHILD_ID = "arr_child_id";
    public static final String KEY_COLUMN_KEY_ID = "key_id";
    public static final String KEY_COLUMN_LIST_ORDER = "list_order";
    public static final String KEY_COLUMN_DESCRIPTION = "description";
    public static final String KEY_COLUMN_SETTING = "setting";
    public static final String KEY_COLUMN_SITE_ID = "site_id";

    public static final String KEY_TABLE_NAME = "area";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_ID + " INTEGER PRIMARY KEY ,"
            + KEY_COLUMN_NAME + " TEXT,"
            + KEY_COLUMN_STYLE + " TEXT,"
            + KEY_COLUMN_PARENT_ID + " INTEGER,"
            + KEY_COLUMN_CHILD + " INTEGER,"
            + KEY_COLUMN_ARR_CHILD_ID + " TEXT,"
            + KEY_COLUMN_KEY_ID + " INTEGER ,"
            + KEY_COLUMN_LIST_ORDER + " INTEGER ,"
            + KEY_COLUMN_DESCRIPTION + " TEXT ,"
            + KEY_COLUMN_SETTING + " TEXT ,"
            + KEY_COLUMN_SITE_ID + " INTEGER "
            + ")";

    private int areaId;
    private String name;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
