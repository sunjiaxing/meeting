package com.zhengshang.meeting.dao;

import android.content.ContentValues;
import android.content.Context;

import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.entity.User;

/**
 * 用户 dao
 * Created by sun on 2016/1/12.
 */
public class UserDao extends BaseDao {

    public UserDao(Context context) {
        super(context);
    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    public void saveUser(User user) {
        ContentValues values = new ContentValues();
        if (!Utils.isEmpty(user.getUserId())) {
            values.put(User.KEY_COLUMN_USER_ID, user.getUserId());
        }
        if (!Utils.isEmpty(user.getUserName())) {
            values.put(User.KEY_COLUMN_USER_NAME, user.getUserName());
        }
        if (!Utils.isEmpty(user.getNickName())) {
            values.put(User.KEY_COLUMN_NICK_NAME, user.getNickName());
        }
        if (!Utils.isEmpty(user.getEmail())) {
            values.put(User.KEY_COLUMN_EMAIL, user.getEmail());
        }
        if (!Utils.isEmpty(user.getMobile())) {
            values.put(User.KEY_COLUMN_MOBILE, user.getMobile());
        }
        if (user.getRegisterTime() > 0) {
            values.put(User.KEY_COLUMN_REGISTER_TIME, user.getRegisterTime());
        }
        if (user.getLastLoginTime() > 0) {
            values.put(User.KEY_COLUMN_LAST_LOGIN_TIME, user.getLastLoginTime());
        }
        if (getUserById(user.getUserId()) != null) {
            db.update(User.KEY_TABLE_NAME, values, User.KEY_COLUMN_USER_ID + " = ? ", new String[]{user.getUserId()});
        } else {
            db.insert(User.KEY_TABLE_NAME, null, values);
        }
    }

    /**
     * 根据id获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    public User getUserById(String userId) {
        try {
            sql = "select * from " + User.KEY_TABLE_NAME + " where " + User.KEY_COLUMN_USER_ID + " = ? ";
            cursor = db.rawQuery(sql, new String[]{userId});
            if (cursor != null && cursor.getCount() > 0) {
                User user;
                if (cursor.moveToFirst()) {
                    user = new User();
                    user.setUserId(userId);
                    user.setUserName(cursor.getString(cursor.getColumnIndex(User.KEY_COLUMN_USER_NAME)));
                    user.setNickName(cursor.getString(cursor.getColumnIndex(User.KEY_COLUMN_NICK_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(User.KEY_COLUMN_EMAIL)));
                    user.setMobile(cursor.getString(cursor.getColumnIndex(User.KEY_COLUMN_MOBILE)));
                    user.setRegisterTime(cursor.getLong(cursor.getColumnIndex(User.KEY_COLUMN_REGISTER_TIME)));
                    user.setLastLoginTime(cursor.getLong(cursor.getColumnIndex(User.KEY_COLUMN_LAST_LOGIN_TIME)));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnection();
        }
        return null;
    }
}
