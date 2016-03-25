package com.sb.meeting.dao.entity;

import java.io.Serializable;

/**
 * 审核中 临时存储
 * Created by sun on 2016/3/22.
 */
public class CheckingGoods implements Serializable {
    public static final String KEY_COLUMN_TABLE_ID = "table_id";
    public static final String KEY_COLUMN_ID = "id";
    public static final String KEY_COLUMN_GOODS_NAME = "goods_name";
    public static final String KEY_COLUMN_COVER_URL = "cover_url";
    public static final String KEY_COLUMN_EXCHANGE_PRICE = "exchange_price";
    public static final String KEY_COLUMN_MARKET_PRICE = "market_price";
    public static final String KEY_COLUMN_COUNT = "count";
    public static final String KEY_COLUMN_CATEGORY = "category";
    public static final String KEY_COLUMN_NEED_CATEGORY = "needCategory";
    public static final String KEY_COLUMN_CONTACT = "contact";
    public static final String KEY_COLUMN_VALID_TIME = "valid_time";
    public static final String KEY_COLUMN_IMAGES = "images";
    public static final String KEY_COLUMN_SEND_SUCCESS = "send_success";
    public static final String KEY_COLUMN_PUBLISH_TIME = "publish_time";
    public static final String KEY_COLUMN_UUID = "uuid";

    public static final String KEY_TABLE_NAME = "checking_goods";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COLUMN_ID + " INTEGER,"
            + KEY_COLUMN_GOODS_NAME + " TEXT,"
            + KEY_COLUMN_COVER_URL + " TEXT,"
            + KEY_COLUMN_CATEGORY + " TEXT,"
            + KEY_COLUMN_NEED_CATEGORY + " TEXT,"
            + KEY_COLUMN_CONTACT + " TEXT,"
            + KEY_COLUMN_VALID_TIME + " TEXT ,"
            + KEY_COLUMN_IMAGES + " TEXT ,"
            + KEY_COLUMN_EXCHANGE_PRICE + " DOUBLE ,"
            + KEY_COLUMN_MARKET_PRICE + " DOUBLE ,"
            + KEY_COLUMN_COUNT + " INTEGER ,"
            + KEY_COLUMN_SEND_SUCCESS + " INTEGER ,"
            + KEY_COLUMN_PUBLISH_TIME + " LONG ,"
            + KEY_COLUMN_UUID + " TEXT "
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + KEY_TABLE_NAME;
    public static final String DELETE_TABLE_DATA = "DELETE FROM "
            + KEY_TABLE_NAME;


    private int id;
    private String goodsName;
    private String coverUrl;
    private double exchangePrice;
    private double marketPrice;
    private int count;
    private String category;
    private String needCategory;
    private String contact;
    private String validTime;
    private String images;
    private int sendSuccess;
    private long publishTime;
    private String uuid;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNeedCategory() {
        return needCategory;
    }

    public void setNeedCategory(String needCategory) {
        this.needCategory = needCategory;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(int sendSuccess) {
        this.sendSuccess = sendSuccess;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
