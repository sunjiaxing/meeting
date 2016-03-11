package com.zhengshang.meeting.remote.dto;

import com.zhengshang.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 物品 dto
 * Created by sun on 2016/2/26.
 */
public class GoodsDto {
    private int id;
    private String goodsName;
    private String coverUrl;
    private int scanNum;
    private int attentionNum;
    private long publishTime;
    private double marketPrice;
    private double exchangePrice;
    private int isAttention;
    private int count;
    private String validTimeStr;

    public int getId() {
        return id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getScanNum() {
        return scanNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public double getExchangePrice() {
        return exchangePrice;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public int getCount() {
        return count;
    }

    public String getValidTimeStr() {
        return validTimeStr;
    }

    /**
     * 解析 json
     *
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.GOODS_ID)) {
            this.id = json.getInt(IParam.GOODS_ID);
        }
        if (json.has(IParam.GOODS_NAME)) {
            this.goodsName = json.getString(IParam.GOODS_NAME);
        }
        if (json.has(IParam.COVER_URL)) {
            this.coverUrl = json.getString(IParam.COVER_URL);
        }
        if (json.has(IParam.SCAN_NUM)) {
            this.scanNum = json.getInt(IParam.SCAN_NUM);
        }
        if (json.has(IParam.ATTENTION_NUM)) {
            this.attentionNum = json.getInt(IParam.ATTENTION_NUM);
        }
        if (json.has(IParam.PUBLISH_TIME)) {
            this.publishTime = json.getLong(IParam.PUBLISH_TIME);
        }
        if (json.has(IParam.MARKET_PRICE)) {
            this.marketPrice = json.getDouble(IParam.MARKET_PRICE);
        }
        if (json.has(IParam.EXCHANGE_PRICE)) {
            this.exchangePrice = json.getDouble(IParam.EXCHANGE_PRICE);
        }
        if (json.has(IParam.ATTENTION_STATUS)) {
            this.isAttention = json.getInt(IParam.ATTENTION_STATUS);
        }
        if (json.has(IParam.COUNT)) {
            this.count = json.getInt(IParam.COUNT);
        }
        if (json.has(IParam.VALID_TIME)) {
            this.validTimeStr = json.getString(IParam.VALID_TIME);
        }
    }
}
