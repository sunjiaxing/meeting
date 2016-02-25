package com.zhengshang.meeting.dao.entity;

/**
 * 用户 数据库存储实体
 * Created by sun on 2016/1/12.
 */
public class User {
    public static final String KEY_COLUMN_TABLE_ID = "table_id";
    public static final String KEY_COLUMN_USER_ID = "userId";
    public static final String KEY_COLUMN_USER_NAME = "userName";
    public static final String KEY_COLUMN_NICK_NAME = "nickName";
    public static final String KEY_COLUMN_EMAIL = "email";
    public static final String KEY_COLUMN_MOBILE = "mobile";
    public static final String KEY_COLUMN_REGISTER_TIME = "registerTime";
    public static final String KEY_COLUMN_LAST_LOGIN_TIME = "lastLoginTime";
    public static final String KEY_COLUMN_REMARK = "remark";

    public static final String KEY_TABLE_NAME = "user_info";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COLUMN_USER_ID + " TEXT,"
            + KEY_COLUMN_USER_NAME + " TEXT,"
            + KEY_COLUMN_NICK_NAME + " TEXT,"
            + KEY_COLUMN_EMAIL + " TEXT,"
            + KEY_COLUMN_MOBILE + " TEXT,"
            + KEY_COLUMN_REGISTER_TIME + " LONG,"
            + KEY_COLUMN_LAST_LOGIN_TIME + " LONG ,"
            + KEY_COLUMN_REMARK + " TEXT " + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + KEY_TABLE_NAME;
    public static final String DELETE_TABLE_DATA = "DELETE FROM "
            + KEY_TABLE_NAME;

    private String userId;
    private String userName;
    private String nickName;
    private String email;
    private String mobile;
    private long registerTime;
    private long lastLoginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
