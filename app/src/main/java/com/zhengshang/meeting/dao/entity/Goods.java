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
    public static final String KEY_COLUMN_ATTENTION_STATE = "attention_state";
    public static final String KEY_COLUMN_EXCHANGE_PRICE = "exchange_price";
    public static final String KEY_COLUMN_MARKET_PRICE = "market_price";
    public static final String KEY_COLUMN_COUNT = "count";
    public static final String KEY_COLUMN_VALID_TIME_STR = "valid_time_str";
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
            + KEY_COLUMN_REMARK + " TEXT ,"
            + KEY_COLUMN_ATTENTION_STATE + " INTEGER ,"
            + KEY_COLUMN_EXCHANGE_PRICE + " DOUBLE ,"
            + KEY_COLUMN_MARKET_PRICE + " DOUBLE ,"
            + KEY_COLUMN_COUNT + " INTEGER ,"
            + KEY_COLUMN_VALID_TIME_STR + " TEXT "
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
    private double exchangePrice;
    private double marketPrice;
    private int count;
    private int attentionState;
    private String validTimeStr;

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

    public double getExchangePrice() {
        return exchangePrice;
    }

    public void setExchangePrice(double exchangePrice) {
        this.exchangePrice = exchangePrice;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(int attentionState) {
        this.attentionState = attentionState;
    }

    public String getValidTimeStr() {
        return validTimeStr;
    }

    public void setValidTimeStr(String validTimeStr) {
        this.validTimeStr = validTimeStr;
    }
}
