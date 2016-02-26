package com.zhengshang.meeting.dao;

import android.content.ContentValues;
import android.content.Context;

import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.entity.Goods;

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
}
