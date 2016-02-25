package com.zhengshang.meeting.dao.entity;

/**
 * 物品 数据库存储实体
 * Created by sun on 2016/2/25.
 */
public class Goods {
    public static final String KEY_COLUMN_TABLE_ID = "table_id";
    public static final String KEY_COLUMN_ID = "id";
    public static final String KEY_COLUMN_GOODS_NAME = "goods_name";
    public static final String KEY_COLUMN_COVER_URL = "cover_url";
    public static final String KEY_COLUMN_SCAN_NUM = "scan_num";
    public static final String KEY_COLUMN_ATTENTION_NUM = "attention_num";
    public static final String KEY_COLUMN_PUBLISH_TIME = "publish_time";
    public static final String KEY_COLUMN_REMARK = "remark";

    public static final String KEY_TABLE_NAME = "goods";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COLUMN_ID + " INTEGER,"
            + KEY_COLUMN_GOODS_NAME + " TEXT,"
            + KEY_COLUMN_COVER_URL + " TEXT,"
            + KEY_COLUMN_SCAN_NUM + " INTEGER,"
            + KEY_COLUMN_ATTENTION_NUM + " INTEGER,"
            + KEY_COLUMN_PUBLISH_TIME + " LONG,"
            + KEY_COLUMN_REMARK + " TEXT "
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + KEY_TABLE_NAME;
    public static final String DELETE_TABLE_DATA = "DELETE FROM "
            + KEY_TABLE_NAME;

    private int id;
    private String goodsName;
    private String coverUrl;
    private int scanNum;
    private int attentionNum;
    private long publishTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getScanNum() {
        return scanNum;
    }

    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
