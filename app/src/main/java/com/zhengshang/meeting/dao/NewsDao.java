package com.zhengshang.meeting.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.entity.News;
import com.zhengshang.meeting.dao.entity.NewsChannel;
import com.zhengshang.meeting.exeception.AppException;

import java.util.ArrayList;
import java.util.List;

/**
 * news dao
 */
public class NewsDao extends BaseDao {

    public NewsDao(Context context) {
        super(context);
    }

    /**
     * 更新 新闻栏目
     *
     * @param types 新闻栏目集合
     */
    public void updateNewsType(List<NewsChannel> types) {
        try {
            beginTransaction();
            for (NewsChannel tmp : types) {
                ContentValues values = new ContentValues();
                if (!Utils.isEmpty(tmp.getTypeId())) {
                    values.put(NewsChannel.KEY_COLUMN_TYPE_ID, tmp.getTypeId());
                }
                if (!Utils.isEmpty(tmp.getName())) {
                    values.put(NewsChannel.KEY_COLUMN_NAME, tmp.getName());
                }
                if (!Utils.isEmpty(tmp.getMasterId())) {
                    values.put(NewsChannel.KEY_COLUMN_MASTER_ID,
                            tmp.getMasterId());
                }
                if (tmp.getIsMine() > 0) {
                    values.put(NewsChannel.KEY_COLUMN_IS_MINE, tmp.getIsMine());
                }
                values.put(NewsChannel.KEY_COLUMN_IS_LOCK, tmp.getIsLock());
                if (isExistNewsType(tmp.getTypeId(), tmp.getMasterId())) {
                    db.update(NewsChannel.KEY_TABLE_NAME, values,
                            NewsChannel.KEY_COLUMN_TYPE_ID + "=? and "
                                    + NewsChannel.KEY_COLUMN_MASTER_ID + "=?",
                            new String[]{tmp.getTypeId(), tmp.getMasterId()});
                } else {
                    if (tmp.getPosition() > 0) {
                        values.put(NewsChannel.KEY_COLUMN_POSITION,
                                tmp.getPosition());
                    }
                    db.insert(NewsChannel.KEY_TABLE_NAME, null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 判断新闻类型是否存在
     *
     * @param typeId   栏目id
     * @param masterId 用户id
     * @return
     */
    private boolean isExistNewsType(String typeId, String masterId) {
        try {
            sql = "select * from " + NewsChannel.KEY_TABLE_NAME + " where "
                    + NewsChannel.KEY_COLUMN_TYPE_ID + "=? and "
                    + NewsChannel.KEY_COLUMN_MASTER_ID + "=?";
            cursor = db.rawQuery(sql, new String[]{typeId, masterId});
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return false;
    }

    /**
     * 读取对应类型的栏目信息
     *
     * @param isAll    是否获取所有
     * @param masterId 用户id
     * @return
     */
    public List<NewsChannel> getNewsType(boolean isAll, String masterId) {
        List<NewsChannel> typeList = null;
        try {
            sql = "select * from " + NewsChannel.KEY_TABLE_NAME
                    + " where " + NewsChannel.KEY_COLUMN_MASTER_ID + " = ? ";
            if (!isAll) {
                sql += " and " + NewsChannel.KEY_COLUMN_IS_MINE + " = 1 ";
            }
            sql += " order by " + NewsChannel.KEY_COLUMN_POSITION + " asc";
            cursor = db.rawQuery(sql, new String[]{masterId});
            NewsChannel newsType;
            if (cursor != null && cursor.getCount() > 0) {
                typeList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    newsType = new NewsChannel();
                    newsType.setTypeId(cursor.getString(cursor
                            .getColumnIndex(NewsChannel.KEY_COLUMN_TYPE_ID)));
                    newsType.setMasterId(masterId);
                    newsType.setName(cursor.getString(cursor
                            .getColumnIndex(NewsChannel.KEY_COLUMN_NAME)));
                    newsType.setIsLock(cursor.getInt(cursor
                            .getColumnIndex(NewsChannel.KEY_COLUMN_IS_LOCK)));
                    newsType.setIsMine(cursor.getInt(cursor
                            .getColumnIndex(NewsChannel.KEY_COLUMN_IS_MINE)));
                    newsType.setPosition(cursor.getInt(cursor
                            .getColumnIndex(NewsChannel.KEY_COLUMN_POSITION)));
                    typeList.add(newsType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return typeList;
    }

    /**
     * 更新为我的栏目
     *
     * @param newsTypes 新闻栏目集合
     * @param masterId  用户id
     */
    public void saveToMyChannel(List<NewsChannel> newsTypes, String masterId) {
        try {
            beginTransaction();
            sql = "update " + NewsChannel.KEY_TABLE_NAME + " set "
                    + NewsChannel.KEY_COLUMN_IS_MINE + " = 0 where "
                    + NewsChannel.KEY_COLUMN_MASTER_ID + " = ?";
            db.execSQL(sql, new String[]{masterId});

            sql = "update " + NewsChannel.KEY_TABLE_NAME + " set "
                    + NewsChannel.KEY_COLUMN_IS_MINE + " = 1 ,"
                    + NewsChannel.KEY_COLUMN_POSITION + " = ?  where "
                    + NewsChannel.KEY_COLUMN_MASTER_ID + " = ? and "
                    + NewsChannel.KEY_COLUMN_TYPE_ID + " = ? ";
            for (NewsChannel newsChannel : newsTypes) {
                db.execSQL(sql,
                        new Object[]{newsChannel.getPosition(), masterId, newsChannel.getTypeId()});
            }
            setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }
    }

    /**
     * 读取新闻
     *
     * @param catId 栏目id
     */
    public List<News> getOnlineNewsFromDB(String catId) {
        List<News> newsList = null;
        try {
            sql = "select * from " + News.KEY_TABLE_NAME + " where "
                    + News.KEY_COLUMN_CAT_ID + "=? order by "
                    + News.KEY_COLUMN_CREATE_TIME + " desc";
            cursor = db.rawQuery(sql, new String[]{catId});
            News lm;
            if (cursor != null && cursor.getCount() > 0) {
                newsList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    lm = new News();
                    lm.setIsAd(cursor.getInt(cursor.getColumnIndex(News.KEY_COLUMN_IS_AD)));
                    lm.setNewsId(cursor.getString(cursor
                            .getColumnIndex(News.KEY_COLUMN_ID)));
                    lm.setTitle(cursor.getString(cursor
                            .getColumnIndex(News.KEY_COLUMN_TITLE)));
                    lm.setIconPath(cursor.getString(cursor
                            .getColumnIndex(News.KEY_COLUMN_ICON_PATH)));
                    lm.setSummary(cursor.getString(cursor
                            .getColumnIndex(News.KEY_COLUMN_SUMMARY)));
                    lm.setIsOpenBlank(cursor.getInt(cursor
                            .getColumnIndex(News.KEY_COLUMN_IS_OPEN_BLANK)));
                    lm.setTop(cursor.getInt(cursor.getColumnIndex(News.KEY_COLUMN_TOP)));
                    lm.setIsRead(cursor.getInt(cursor
                            .getColumnIndex(News.KEY_COLUMN_IS_READ)));
                    lm.setCreateTime(cursor.getLong(cursor
                            .getColumnIndex(News.KEY_COLUMN_CREATE_TIME)));
                    lm.setSubject(cursor.getInt(cursor
                            .getColumnIndex(News.KEY_COLUMN_SUBJECT)));
                    lm.setSubjectId(cursor.getInt(cursor
                            .getColumnIndex(News.KEY_COLUMN_SUBJECT_ID)));
                    lm.setIconAdUrl(cursor.getString(cursor
                            .getColumnIndex(News.KEY_COLUMN_AD_URL)));

                    lm.setCatId(catId);
                    newsList.add(lm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return newsList;
    }

    /**
     * 删除新闻标志对应类型下的所有数据
     *
     * @param catId 栏目id
     */
    public void deleteAllByCatId(String catId) {
        sql = "delete from " + News.KEY_TABLE_NAME + " where "
                + News.KEY_COLUMN_CAT_ID + " = ?";
        db.beginTransaction();
        db.execSQL(sql, new String[]{catId});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 获取新闻阅读状态
     *
     * @param newsId 新闻id
     * @param catId  栏目id
     * @return
     */
    public int getReadState(String newsId, String catId) {
        int isRead = 0;
        try {
            sql = "select " + News.KEY_COLUMN_IS_READ + " from "
                    + News.KEY_TABLE_NAME + " where " + News.KEY_COLUMN_ID
                    + "= ?" + "and " + News.KEY_COLUMN_CAT_ID + " = ?";
            cursor = db.rawQuery(sql, new String[]{newsId, catId});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    isRead = cursor.getInt(cursor
                            .getColumnIndex(News.KEY_COLUMN_IS_READ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return isRead;
    }

    /**
     * 保存新闻
     *
     * @param n 新闻对象
     */

    public void saveNews(News n) {
        ContentValues values = new ContentValues();
        if (!Utils.isEmpty(n.getNewsId())) {
            values.put(News.KEY_COLUMN_ID, n.getNewsId());
        }
        if (!Utils.isEmpty(n.getCatId())) {
            values.put(News.KEY_COLUMN_CAT_ID, n.getCatId());
        }
        if (!Utils.isEmpty(n.getTitle())) {
            values.put(News.KEY_COLUMN_TITLE, n.getTitle());
        }
        if (!Utils.isEmpty(n.getIconPath())) {
            values.put(News.KEY_COLUMN_ICON_PATH, n.getIconPath());
        }
        if (!Utils.isEmpty(n.getSummary())) {
            values.put(News.KEY_COLUMN_SUMMARY, n.getSummary());
        }
        if (n.getTop() != 0) {
            values.put(News.KEY_COLUMN_TOP, n.getTop());
        }
        if (n.getIsRead() != 0) {
            values.put(News.KEY_COLUMN_IS_READ, n.getIsRead());
        }
        if (n.getSubject() != 0) {
            values.put(News.KEY_COLUMN_SUBJECT, n.getSubject());
        }
        if (n.getSubjectId() != 0) {
            values.put(News.KEY_COLUMN_SUBJECT_ID, n.getSubjectId());
        }
        values.put(News.KEY_COLUMN_IS_OPEN_BLANK, n.getIsOpenBlank());
        values.put(News.KEY_COLUMN_CREATE_TIME, n.getCreateTime());
        if (!Utils.isEmpty(n.getIconAdUrl())) {
            values.put(News.KEY_COLUMN_AD_URL, n.getIconAdUrl());
        }
        values.put(News.KEY_COLUMN_REMARK, n.getUpdateTime());
        if (n.getIsAd() != 0) {
            values.put(News.KEY_COLUMN_IS_AD, n.getIsAd());
        }
        db.insert(News.KEY_TABLE_NAME, null, values);
    }

    /**
     * 保存新闻
     *
     * @param data 新闻集合
     */
    public void saveNews(List<News> data) {
        if (!Utils.isEmpty(data)) {
            for (News news : data) {
                saveNews(news);
            }
        }
    }

    /**
     * 设置新闻阅读状态
     *
     * @param newsId    新闻id
     * @param catId     栏目id
     * @param readState 阅读状态
     */
    public void setReadState(String newsId, String catId, int readState) {
        try {
            sql = "update " + News.KEY_TABLE_NAME + " set "
                    + News.KEY_COLUMN_IS_READ + "= ?" + " where "
                    + News.KEY_COLUMN_ID + "= ? and " + News.KEY_COLUMN_CAT_ID
                    + " = ?";
            db.execSQL(sql, new String[]{String.valueOf(readState), newsId,
                    catId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据新闻id删除列表中的新闻
     *
     * @param newsId 新闻id
     */
    public void deleteNewsByID(String newsId) {
        try {
            sql = "delete from " + News.KEY_TABLE_NAME + " where " + News.KEY_COLUMN_ID + " = ?";
            db.beginTransaction();
            db.execSQL(sql, new String[]{newsId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 获取新闻栏目
     *
     * @param typeId   栏目id
     * @param masterId 用户id
     * @return
     */
    public NewsChannel getNewsTypeById(String typeId, String masterId) {
        try {
            sql = "select * from " + NewsChannel.KEY_TABLE_NAME + " where "
                    + NewsChannel.KEY_COLUMN_TYPE_ID + " = ? and "
                    + NewsChannel.KEY_COLUMN_MASTER_ID + " = ? ";
            cursor = db.rawQuery(sql, new String[]{typeId, masterId});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    NewsChannel newsChannel = new NewsChannel();
                    newsChannel.setTypeId(typeId);
                    newsChannel.setMasterId(masterId);
                    newsChannel.setIsMine(cursor.getInt(cursor.getColumnIndex(NewsChannel.KEY_COLUMN_IS_MINE)));
                    newsChannel.setPosition(cursor.getInt(cursor.getColumnIndex(NewsChannel.KEY_COLUMN_POSITION)));
                    newsChannel.setName(cursor.getString(cursor.getColumnIndex(NewsChannel.KEY_COLUMN_NAME)));
                    return newsChannel;
                }
            }
            return null;
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        } finally {
            releaseConnection();
        }
    }

    public void deleteAllChannel() {
        db.execSQL(NewsChannel.DELETE_TABLE_DATA);
    }
}
