package com.sb.meeting.dao;

import android.content.ContentValues;
import android.content.Context;

import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.entity.CheckingGoods;
import com.sb.meeting.dao.entity.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * 易物 dao
 * Created by sun on 2016/2/25.
 */
public class GoodsDao extends BaseDao {

    public GoodsDao(Context context) {
        super(context);
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        db.execSQL(Goods.DELETE_TABLE_DATA);
    }

    /**
     * 添加 物品
     *
     * @param goods
     */
    public void insertGoods(Goods goods) {
        ContentValues values = new ContentValues();
        values.put(Goods.KEY_COLUMN_ID, goods.getId());
        if (!Utils.isEmpty(goods.getGoodsName())) {
            values.put(Goods.KEY_COLUMN_GOODS_NAME, goods.getGoodsName());
        }
        if (!Utils.isEmpty(goods.getCoverUrl())) {
            values.put(Goods.KEY_COLUMN_COVER_URL, goods.getCoverUrl());
        }
        values.put(Goods.KEY_COLUMN_SCAN_NUM, goods.getScanNum());
        values.put(Goods.KEY_COLUMN_ATTENTION_NUM, goods.getAttentionNum());
        values.put(Goods.KEY_COLUMN_PUBLISH_TIME, goods.getPublishTime());
        values.put(Goods.KEY_COLUMN_EXCHANGE_PRICE, goods.getExchangePrice());
        values.put(Goods.KEY_COLUMN_MARKET_PRICE, goods.getMarketPrice());
        values.put(Goods.KEY_COLUMN_COUNT, goods.getCount());
        values.put(Goods.KEY_COLUMN_VALID_TIME_STR, goods.getValidTimeStr());
        values.put(Goods.KEY_COLUMN_ATTENTION_STATE, goods.getAttentionState());
        if (isExist(goods.getId())) {
            db.update(Goods.KEY_TABLE_NAME, values, Goods.KEY_COLUMN_ID + " = ?", new String[]{String.valueOf(goods.getId())});
        } else {
            db.insert(Goods.KEY_TABLE_NAME, null, values);
        }
    }

    /**
     * 判断 是否存在
     *
     * @param id
     * @return
     */
    private boolean isExist(int id) {
        try {
            sql = "select * from " + Goods.KEY_TABLE_NAME + " where " + Goods.KEY_COLUMN_ID + " = ? ";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            releaseConnection();
        }
        return false;
    }

    /**
     * 获取物品 列表
     *
     * @return
     */
    public List<Goods> getGoodsList() {
        try {
            sql = "select * from " + Goods.KEY_TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            List<Goods> list = null;
            if (cursor.getCount() > 0) {
                list = new ArrayList<>();
                Goods goods;
                while (cursor.moveToNext()) {
                    goods = new Goods();
                    goods.setId(cursor.getInt(cursor.getColumnIndex(Goods.KEY_COLUMN_ID)));
                    goods.setGoodsName(cursor.getString(cursor.getColumnIndex(Goods.KEY_COLUMN_GOODS_NAME)));
                    goods.setCoverUrl(cursor.getString(cursor.getColumnIndex(Goods.KEY_COLUMN_COVER_URL)));
                    goods.setScanNum(cursor.getInt(cursor.getColumnIndex(Goods.KEY_COLUMN_SCAN_NUM)));
                    goods.setAttentionNum(cursor.getInt(cursor.getColumnIndex(Goods.KEY_COLUMN_ATTENTION_NUM)));
                    goods.setPublishTime(cursor.getLong(cursor.getColumnIndex(Goods.KEY_COLUMN_PUBLISH_TIME)));
                    goods.setExchangePrice(cursor.getDouble(cursor.getColumnIndex(Goods.KEY_COLUMN_EXCHANGE_PRICE)));
                    goods.setMarketPrice(cursor.getDouble(cursor.getColumnIndex(Goods.KEY_COLUMN_MARKET_PRICE)));
                    goods.setCount(cursor.getInt(cursor.getColumnIndex(Goods.KEY_COLUMN_COUNT)));
                    goods.setValidTimeStr(cursor.getString(cursor.getColumnIndex(Goods.KEY_COLUMN_VALID_TIME_STR)));
                    goods.setAttentionState(cursor.getInt(cursor.getColumnIndex(Goods.KEY_COLUMN_ATTENTION_STATE)));
                    list.add(goods);
                }
            }
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseConnection();
        }
    }

    /**
     * 保存临时 发布的 审核中的数据
     *
     * @param goods
     */
    public void saveCheckingData(CheckingGoods goods) {
        ContentValues values = new ContentValues();
        values.put(CheckingGoods.KEY_COLUMN_ID, goods.getId());
        values.put(CheckingGoods.KEY_COLUMN_GOODS_NAME, goods.getGoodsName());
        values.put(CheckingGoods.KEY_COLUMN_COVER_URL, goods.getCoverUrl());
        values.put(CheckingGoods.KEY_COLUMN_EXCHANGE_PRICE, goods.getExchangePrice());
        values.put(CheckingGoods.KEY_COLUMN_MARKET_PRICE, goods.getMarketPrice());
        values.put(CheckingGoods.KEY_COLUMN_CATEGORY, goods.getCategory());
        values.put(CheckingGoods.KEY_COLUMN_NEED_CATEGORY, goods.getNeedCategory());
        values.put(CheckingGoods.KEY_COLUMN_COUNT, goods.getCount());
        values.put(CheckingGoods.KEY_COLUMN_CONTACT, goods.getContact());
        values.put(CheckingGoods.KEY_COLUMN_IMAGES, goods.getImages());
        values.put(CheckingGoods.KEY_COLUMN_VALID_TIME, goods.getValidTime());
        values.put(CheckingGoods.KEY_COLUMN_SEND_SUCCESS, goods.getSendSuccess());
        values.put(CheckingGoods.KEY_COLUMN_PUBLISH_TIME, goods.getPublishTime());
        values.put(CheckingGoods.KEY_COLUMN_UUID, goods.getUUID());
        db.insert(CheckingGoods.KEY_TABLE_NAME, null, values);
    }

    /**
     * 添加 审核数据id
     *
     * @param id   id
     * @param uuid uuid
     */
    public void addCheckingDataId(int id, String uuid) {
        sql = "update " + CheckingGoods.KEY_TABLE_NAME + " set "
                + CheckingGoods.KEY_COLUMN_ID + " = ? , "
                + CheckingGoods.KEY_COLUMN_SEND_SUCCESS + " = 1 where "
                + CheckingGoods.KEY_COLUMN_UUID + " = ? ";
        db.execSQL(sql, new Object[]{id, uuid});
    }

    /**
     * 获取审核中的数据
     *
     * @return
     */
    public CheckingGoods getCheckingData() {
        sql = "select * from " + CheckingGoods.KEY_TABLE_NAME + " order by " + CheckingGoods.KEY_COLUMN_PUBLISH_TIME + " desc limit 1";
        cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                CheckingGoods checkingGoods = new CheckingGoods();
                checkingGoods.setId(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_ID)));
                checkingGoods.setGoodsName(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_GOODS_NAME)));
                checkingGoods.setCoverUrl(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COVER_URL)));
                checkingGoods.setExchangePrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_EXCHANGE_PRICE)));
                checkingGoods.setMarketPrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_MARKET_PRICE)));
                checkingGoods.setValidTime(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_VALID_TIME)));
                checkingGoods.setCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CATEGORY)));
                checkingGoods.setNeedCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_NEED_CATEGORY)));
                checkingGoods.setContact(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CONTACT)));
                checkingGoods.setCount(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COUNT)));
                checkingGoods.setImages(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_IMAGES)));
                checkingGoods.setPublishTime(cursor.getLong(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_PUBLISH_TIME)));
                checkingGoods.setSendSuccess(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_SEND_SUCCESS)));
                checkingGoods.setUUID(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_UUID)));
                return checkingGoods;
            }
        }
        return null;
    }

    /**
     * 获取所有 审核中的数据
     *
     * @return
     */
    public List<CheckingGoods> getCheckingGoods() {
        List<CheckingGoods> data = null;
        try {
            sql = "select * from " + CheckingGoods.KEY_TABLE_NAME + " order by " + CheckingGoods.KEY_COLUMN_TABLE_ID + " desc";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                data = new ArrayList<>();
                CheckingGoods item;
                while (cursor.moveToNext()) {
                    item = new CheckingGoods();
                    item.setId(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_ID)));
                    item.setGoodsName(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_GOODS_NAME)));
                    item.setCoverUrl(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COVER_URL)));
                    item.setCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CATEGORY)));
                    item.setNeedCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_NEED_CATEGORY)));
                    item.setExchangePrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_EXCHANGE_PRICE)));
                    item.setMarketPrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_MARKET_PRICE)));
                    item.setCount(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COUNT)));
                    item.setContact(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CONTACT)));
                    item.setImages(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_IMAGES)));
                    item.setValidTime(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_VALID_TIME)));
                    data.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return data;
    }

    /**
     * 通过 物品名称 获取 存储的数据
     *
     * @param goodsName 物品名称
     * @return
     */
    public CheckingGoods getCheckingGoodsByName(String goodsName) {
        CheckingGoods item = null;
        try {
            sql = "select * from " + CheckingGoods.KEY_TABLE_NAME + " where " + CheckingGoods.KEY_COLUMN_GOODS_NAME + " = ? ";
            cursor = db.rawQuery(sql, new String[]{goodsName});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    item = new CheckingGoods();
                    item.setId(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_ID)));
                    item.setGoodsName(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_GOODS_NAME)));
                    item.setCoverUrl(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COVER_URL)));
                    item.setCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CATEGORY)));
                    item.setNeedCategory(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_NEED_CATEGORY)));
                    item.setExchangePrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_EXCHANGE_PRICE)));
                    item.setMarketPrice(cursor.getDouble(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_MARKET_PRICE)));
                    item.setCount(cursor.getInt(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_COUNT)));
                    item.setContact(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_CONTACT)));
                    item.setImages(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_IMAGES)));
                    item.setValidTime(cursor.getString(cursor.getColumnIndex(CheckingGoods.KEY_COLUMN_VALID_TIME)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return item;
    }

    /**
     * 删除审核中的数据
     *
     * @param goodsId 物品id
     */
    public void deleteCheckingData(String goodsId) {
        sql = "delete from " + CheckingGoods.KEY_TABLE_NAME + " where " + CheckingGoods.KEY_COLUMN_ID + " = ?";
        db.execSQL(sql, new String[]{goodsId});
    }
}
