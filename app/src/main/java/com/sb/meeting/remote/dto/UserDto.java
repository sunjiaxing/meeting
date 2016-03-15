package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户 dto
 * Created by sun on 2016/1/11.
 */
public class UserDto {
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

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.USER_ID)) {
            this.userId = json.getString(IParam.USER_ID);
        }
        if (json.has(IParam.USER_NAME)) {
            this.userName = json.getString(IParam.USER_NAME);
        }
        if (json.has(IParam.NICK_NAME)) {
            this.nickName = json.getString(IParam.NICK_NAME);
        }
        if (json.has(IParam.EMAIL)) {
            this.email = json.getString(IParam.EMAIL);
        }
        if (json.has(IParam.MOBILE)) {
            this.mobile = json.getString(IParam.MOBILE);
        }
        if (json.has(IParam.REGISTER_TIME)) {
            this.registerTime = json.getLong(IParam.REGISTER_TIME);
        }
        if (json.has(IParam.LAST_LOGIN_TIME)) {
            this.lastLoginTime = json.getLong(IParam.LAST_LOGIN_TIME);
        }
    }
}
