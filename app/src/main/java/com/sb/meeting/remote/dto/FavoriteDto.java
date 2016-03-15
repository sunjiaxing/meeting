package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收藏 dto
 * Created by sun on 2016/1/21.
 */
public class FavoriteDto {
    private String newsId;
    private int favoriteType;
    private String title;
    private String summary;
    private String iconUrl;
    private long addTime;
    private String favoriteId;

    public String getNewsId() {
        return newsId;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public int getFavoriteType() {
        return favoriteType;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public long getAddTime() {
        return addTime;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.NEWS_ID)) {
            this.newsId = json.getString(IParam.NEWS_ID);
        }
        if (json.has(IParam.FAVORITE_ID)) {
            this.favoriteId = json.getString(IParam.FAVORITE_ID);
        }
        if (json.has(IParam.FAVORITE_TYPE)) {
            this.favoriteType = json.getInt(IParam.FAVORITE_TYPE);
        }
        if (json.has(IParam.TITLE)) {
            this.title = json.getString(IParam.TITLE);
        }
        if (json.has(IParam.SUMMARY)) {
            this.summary = json.getString(IParam.SUMMARY);
        }
        if (json.has(IParam.ICON_URL)) {
            this.iconUrl = json.getString(IParam.ICON_URL);
        }
        if (json.has(IParam.ADD_TIME)) {
            this.addTime = json.getLong(IParam.ADD_TIME);
        }
    }
}
